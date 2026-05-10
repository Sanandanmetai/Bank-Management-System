import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * ViewAllTransactions.java
 * ------------------------
 * Admin view to monitor all bank transactions across all accounts.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class ViewAllTransactions extends JFrame {

    public ViewAllTransactions() {
        UITheme.setupFrame(this, "NexaBank – All Transactions", 760, 520);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ── Header ─────────────────────────────────────────────────────────
        JLabel title = new JLabel("📊  All Bank Transactions");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        root.add(title, BorderLayout.NORTH);

        // ── Table ──────────────────────────────────────────────────────────
        String[] cols = {"TX ID", "Account ID", "Type", "Amount (PKR)", "Date"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        TransactionDAO dao = new TransactionDAO();
        List<Transaction> txList = dao.getAllTransactions();
        for (Transaction tx : txList) {
            model.addRow(new Object[]{
                tx.getTransactionId(),
                tx.getAccountId(),
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
                if (col == 2) {
                    String type = val == null ? "" : val.toString();
                    setForeground("DEPOSIT".equals(type) ? UITheme.ACCENT : UITheme.DANGER);
                    setFont(new Font("SansSerif", Font.BOLD, 12));
                }
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        root.add(scroll, BorderLayout.CENTER);

        // ── Footer ─────────────────────────────────────────────────────────
        JLabel count = new JLabel("Total records: " + txList.size());
        count.setFont(UITheme.FONT_SMALL);
        count.setForeground(UITheme.TEXT_MUTED);
        root.add(count, BorderLayout.SOUTH);

        add(root);
    }
}
