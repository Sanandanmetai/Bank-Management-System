import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * TransactionHistory.java
 * -----------------------
 * Displays a table of all transactions for the logged-in customer.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation, JTable usage
 */
public class TransactionHistory extends JFrame {

    private final Account account;

    public TransactionHistory(Account account) {
        this.account = account;
        UITheme.setupFrame(this, "NexaBank – Transaction History", 680, 500);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ── Header ─────────────────────────────────────────────────────────
        JLabel title = new JLabel("📋  Transaction History");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);

        JLabel sub = new JLabel("Account #" + String.format("%06d", account.getAccountId())
            + "  —  " + account.getName());
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_MUTED);

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(UITheme.BG_DARK);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        header.add(title);
        header.add(sub);
        root.add(header, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────────────────
        String[] columns = {"#", "Type", "Amount (PKR)", "Date & Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        TransactionDAO dao = new TransactionDAO();
        List<Transaction> txList = dao.getTransactionsByAccount(account.getAccountId());

        for (Transaction tx : txList) {
            model.addRow(new Object[]{
                tx.getTransactionId(),
                tx.getType(),
                String.format("%,.2f", tx.getAmount()),
                tx.getDate()
            });
        }

        JTable table = new JTable(model);
        table.setFont(UITheme.FONT_BODY);
        table.setForeground(UITheme.TEXT_PRIMARY);
        table.setBackground(UITheme.BG_CARD);
        table.setGridColor(UITheme.BORDER);
        table.setRowHeight(36);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(UITheme.BG_INPUT);
        table.setSelectionForeground(UITheme.TEXT_PRIMARY);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);

        // Color DEPOSIT green, WITHDRAW red
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean selected, boolean focused, int row, int col) {
                super.getTableCellRendererComponent(t, val, selected, focused, row, col);
                setBackground(selected ? UITheme.BG_INPUT : UITheme.BG_CARD);
                setForeground(UITheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));

                String type = (String) t.getValueAt(row, 1);
                if (col == 1) {
                    setForeground("DEPOSIT".equals(type) ? UITheme.ACCENT : UITheme.DANGER);
                    setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                return this;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(300);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(UITheme.BG_DARK);
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        scroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        root.add(scroll, BorderLayout.CENTER);

        // ── Summary bar ────────────────────────────────────────────────────
        JLabel countLbl = new JLabel("Total transactions: " + txList.size());
        countLbl.setFont(UITheme.FONT_SMALL);
        countLbl.setForeground(UITheme.TEXT_MUTED);
        countLbl.setBorder(new EmptyBorder(12, 0, 0, 0));
        root.add(countLbl, BorderLayout.SOUTH);

        if (txList.isEmpty()) {
            JLabel empty = new JLabel("No transactions found.", SwingConstants.CENTER);
            empty.setFont(UITheme.FONT_BODY);
            empty.setForeground(UITheme.TEXT_MUTED);
            root.add(empty, BorderLayout.CENTER);
        }

        add(root);
    }
}
