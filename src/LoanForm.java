import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * LoanForm.java
 * -------------
 * Allows a customer to apply for a loan and view their loan history.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class LoanForm extends JFrame {

    private final Account account;
    private JTextField    amountField;

    public LoanForm(Account account) {
        this.account = account;
        UITheme.setupFrame(this, "NexaBank – Loan Application", 640, 580);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 20));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ── Header ─────────────────────────────────────────────────────────
        JLabel title = new JLabel("🏛️  Loan Application");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        root.add(title, BorderLayout.NORTH);

        // ── Application form (card) ────────────────────────────────────────
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(UITheme.BG_CARD);
        formCard.setBorder(UITheme.cardBorder());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel formTitle = new JLabel("Apply for a New Loan");
        formTitle.setFont(UITheme.FONT_HEADING);
        formTitle.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 16, 0);
        formCard.add(formTitle, gbc);

        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 6, 0);
        formCard.add(UITheme.label("LOAN AMOUNT (PKR)"), gbc);

        amountField = UITheme.styledField();
        amountField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 20, 0);
        formCard.add(amountField, gbc);

        JButton applyBtn = UITheme.primaryButton("Submit Loan Application");
        applyBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        applyBtn.setBorder(new EmptyBorder(12, 24, 12, 24));
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 0, 0);
        formCard.add(applyBtn, gbc);

        // ── Loan history table ─────────────────────────────────────────────
        JLabel histTitle = new JLabel("My Loan Applications");
        histTitle.setFont(UITheme.FONT_HEADING);
        histTitle.setForeground(UITheme.TEXT_PRIMARY);

        String[] cols = {"Loan ID", "Amount (PKR)", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        loadLoans(model);

        JTable table = buildTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);

        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setBackground(UITheme.BG_DARK);
        center.add(formCard,  BorderLayout.NORTH);
        center.add(histTitle, BorderLayout.CENTER);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UITheme.BG_DARK);
        tablePanel.setBorder(new EmptyBorder(8, 0, 0, 0));
        tablePanel.add(scroll);

        center.add(tablePanel, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);

        add(root);

        applyBtn.addActionListener(e -> handleApply(model));
    }

    private void handleApply(DefaultTableModel model) {
        String amtText = amountField.getText().trim();

        if (amtText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a loan amount.",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Loan amount must be greater than zero.",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoanDAO dao   = new LoanDAO();
        boolean saved = dao.applyLoan(account.getAccountId(), amount);

        if (saved) {
            JOptionPane.showMessageDialog(this,
                "Loan application submitted!\nStatus: PENDING\nAn admin will review your application.",
                "Application Submitted", JOptionPane.INFORMATION_MESSAGE);
            amountField.setText("");
            model.setRowCount(0);
            loadLoans(model);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit loan application.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLoans(DefaultTableModel model) {
        LoanDAO dao = new LoanDAO();
        List<Loan> loans = dao.getLoansByAccount(account.getAccountId());
        for (Loan loan : loans) {
            model.addRow(new Object[]{
                loan.getLoanId(),
                String.format("%,.2f", loan.getAmount()),
                loan.getStatus()
            });
        }
    }

    private JTable buildTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(UITheme.FONT_BODY);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setBackground(UITheme.BG_CARD);
        table.setGridColor(UITheme.BORDER);
        table.setRowHeight(34);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);

        // Color status column
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean selected, boolean focused, int row, int col) {
                super.getTableCellRendererComponent(t, val, selected, focused, row, col);
                setBackground(selected ? UITheme.BG_INPUT : UITheme.BG_CARD);
                setForeground(UITheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));

                if (col == 2) {
                    String s = val == null ? "" : val.toString();
                    if      ("APPROVED".equals(s)) setForeground(UITheme.ACCENT);
                    else if ("REJECTED".equals(s)) setForeground(UITheme.DANGER);
                    else                           setForeground(UITheme.WARNING);
                    setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                return this;
            }
        });
        return table;
    }
}
