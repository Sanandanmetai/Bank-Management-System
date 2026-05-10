import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * ViewAllCustomers.java
 * ---------------------
 * Admin window to view all customer accounts with delete functionality.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class ViewAllCustomers extends JFrame {

    private final AdminDashboard    adminDashboard;
    private DefaultTableModel       model;
    private JTable                  table;

    public ViewAllCustomers(AdminDashboard adminDashboard) {
        this.adminDashboard = adminDashboard;
        UITheme.setupFrame(this, "NexaBank – All Customers", 820, 540);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ── Header ─────────────────────────────────────────────────────────
        JLabel title = new JLabel("👥  All Customer Accounts");
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
        String[] cols = {"ID", "Name", "Email", "Phone", "Balance (PKR)"};
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
        table.setSelectionBackground(new Color(0, 100, 60));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(UITheme.FONT_LABEL);
        table.getTableHeader().setBackground(UITheme.BG_INPUT);
        table.getTableHeader().setForeground(UITheme.TEXT_MUTED);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? new Color(0, 100, 60) : UITheme.BG_CARD);
                setForeground(sel ? Color.WHITE : UITheme.TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                return this;
            }
        });

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(130);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        root.add(scroll, BorderLayout.CENTER);

        // ── Action bar ─────────────────────────────────────────────────────
        JButton deleteBtn = UITheme.dangerButton("🗑  Delete Selected Account");
        deleteBtn.setBorder(new EmptyBorder(10, 20, 10, 20));

        JButton viewTxBtn = UITheme.primaryButton("📋 View Transactions");
        viewTxBtn.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bottom.setBackground(UITheme.BG_DARK);
        bottom.add(deleteBtn);
        bottom.add(viewTxBtn);
        root.add(bottom, BorderLayout.SOUTH);

        add(root);

        // ── Listeners ──────────────────────────────────────────────────────
        refreshBtn.addActionListener(e -> { model.setRowCount(0); loadData(); });

        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select an account to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id   = (int) model.getValueAt(row, 0);
            String name = (String) model.getValueAt(row, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                "Delete account for " + name + "?\nThis will also remove all their transactions and loans.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                AccountDAO dao = new AccountDAO();
                if (dao.deleteAccount(id)) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this, "Account deleted successfully.",
                        "Deleted", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete account.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        viewTxBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a customer.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int    id   = (int)    model.getValueAt(row, 0);
            String name = (String) model.getValueAt(row, 1);
            AccountDAO dao = new AccountDAO();
            Account acc = dao.getAccountById(id);
            if (acc != null) new TransactionHistory(acc);
        });
    }

    private void loadData() {
        AccountDAO dao = new AccountDAO();
        List<Account> accounts = dao.getAllAccounts();
        for (Account a : accounts) {
            model.addRow(new Object[]{
                a.getAccountId(),
                a.getName(),
                a.getEmail(),
                a.getPhone(),
                String.format("%,.2f", a.getBalance())
            });
        }
    }
}
