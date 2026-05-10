import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * LoanManagement.java
 * -------------------
 * Admin window to view all loan applications and approve or reject them.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class LoanManagement extends JFrame {

    private final AdminDashboard adminDashboard;
    private DefaultTableModel    model;
    private JTable               table;

    public LoanManagement(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        UITheme.setupFrame(this, "NexaBank – Loan Management", 700, 520);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ── Header ─────────────────────────────────────────────────────────
        JLabel title = new JLabel("🏛️  Loan Applications");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JButton refreshBtn = UITheme.primaryButton("⟳ Refresh");
        refreshBtn.setBorder(new EmptyBorder(8, 18, 8, 18));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_DARK);
        header.add(title,      BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────────────────
        String[] cols = {"Loan ID", "Account ID", "Amount (PKR)", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        loadData();

        table = new JTable(model);
        table.setFont(UITheme.FONT_BODY);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setBackground(UITheme.BG_CARD);
        table.setGridColor(UITheme.BORDER);
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(UITheme.BG_INPUT);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? UITheme.BG_INPUT : UITheme.BG_CARD);
                setForeground(UITheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                if (col == 3) {
                    String s = val == null ? "" : val.toString();
                    if      ("APPROVED".equals(s)) setForeground(UITheme.ACCENT);
                    else if ("REJECTED".equals(s)) setForeground(UITheme.DANGER);
                    else                           setForeground(UITheme.WARNING);
                    setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        root.add(scroll, BorderLayout.CENTER);

        // ── Action buttons ─────────────────────────────────────────────────
        JButton approveBtn = UITheme.primaryButton("✔  Approve");
        approveBtn.setBorder(new EmptyBorder(10, 22, 10, 22));

        JButton rejectBtn = UITheme.dangerButton("✘  Reject");
        rejectBtn.setBorder(new EmptyBorder(10, 22, 10, 22));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bottom.setBackground(UITheme.BG_DARK);
        bottom.add(approveBtn);
        bottom.add(rejectBtn);
        root.add(bottom, BorderLayout.SOUTH);

        add(root);

        // ── Listeners ──────────────────────────────────────────────────────
        refreshBtn.addActionListener(e -> { model.setRowCount(0); loadData(); });

        approveBtn.addActionListener(e -> handleStatusUpdate("APPROVED"));
        rejectBtn.addActionListener(e  -> handleStatusUpdate("REJECTED"));
    }

    private void handleStatusUpdate(String newStatus) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Please select a loan application.", "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int    loanId     = (int)    model.getValueAt(row, 0);
        String curStatus  = (String) model.getValueAt(row, 3);

        if (!"PENDING".equals(curStatus)) {
            JOptionPane.showMessageDialog(this,
                "This loan has already been " + curStatus + ".",
                "Already Processed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Mark this loan as " + newStatus + "?",
            "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            LoanDAO dao = new LoanDAO();
            if (dao.updateLoanStatus(loanId, newStatus)) {
                model.setValueAt(newStatus, row, 3);
                JOptionPane.showMessageDialog(this,
                    "Loan " + newStatus.toLowerCase() + " successfully.",
                    "Done", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to update loan status.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadData() {
        LoanDAO dao = new LoanDAO();
        List<Loan> loans = dao.getAllLoans();
        for (Loan loan : loans) {
            model.addRow(new Object[]{
                loan.getLoanId(),
                loan.getAccountId(),
                String.format("%,.2f", loan.getAmount()),
                loan.getStatus()
            });
        }
    }
}
