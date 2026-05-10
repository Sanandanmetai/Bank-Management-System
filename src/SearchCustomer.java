import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * SearchCustomer.java
 * -------------------
 * Admin window to search customers by name.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class SearchCustomer extends JFrame {

    private JTextField    searchField;
    private DefaultTableModel model;

    public SearchCustomer() {
        UITheme.setupFrame(this, "NexaBank – Search Customer", 700, 480);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(24, 28, 24, 28));

        // ── Title ──────────────────────────────────────────────────────────
        JLabel title = new JLabel("🔍  Search Customer");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        root.add(title, BorderLayout.NORTH);

        // ── Search bar ─────────────────────────────────────────────────────
        JPanel searchBar = new JPanel(new BorderLayout(12, 0));
        searchBar.setBackground(UITheme.BG_DARK);

        searchField = UITheme.styledField();
        searchField.setPreferredSize(new Dimension(0, 42));

        JButton searchBtn = UITheme.primaryButton("Search");
        searchBtn.setBorder(new EmptyBorder(10, 24, 10, 24));

        JButton allBtn = new JButton("Show All");
        allBtn.setBackground(UITheme.BG_INPUT);
        allBtn.setForeground(UITheme.TEXT_PRIMARY);
        allBtn.setFont(UITheme.FONT_BODY);
        allBtn.setBorder(new EmptyBorder(10, 18, 10, 18));
        allBtn.setFocusPainted(false);
        allBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        allBtn.setOpaque(true);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setBackground(UITheme.BG_DARK);
        btns.add(searchBtn);
        btns.add(allBtn);

        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(btns, BorderLayout.EAST);
        root.add(searchBar, BorderLayout.BEFORE_FIRST_LINE);

        // ── Table ──────────────────────────────────────────────────────────
        String[] cols = {"ID", "Name", "Email", "Phone", "Balance (PKR)"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        loadAll();

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
                return this;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(UITheme.BORDER, 1));
        scroll.getViewport().setBackground(UITheme.BG_CARD);
        root.add(scroll, BorderLayout.CENTER);

        add(root);

        // ── Listeners ──────────────────────────────────────────────────────
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) { loadAll(); return; }
            model.setRowCount(0);
            AccountDAO dao = new AccountDAO();
            List<Account> results = dao.searchByName(keyword);
            for (Account a : results) {
                model.addRow(new Object[]{
                    a.getAccountId(), a.getName(), a.getEmail(),
                    a.getPhone(), String.format("%,.2f", a.getBalance())
                });
            }
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No customers found.",
                    "No Results", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        allBtn.addActionListener(e -> { searchField.setText(""); loadAll(); });

        searchField.addActionListener(e -> searchBtn.doClick());
    }

    private void loadAll() {
        model.setRowCount(0);
        AccountDAO dao = new AccountDAO();
        for (Account a : dao.getAllAccounts()) {
            model.addRow(new Object[]{
                a.getAccountId(), a.getName(), a.getEmail(),
                a.getPhone(), String.format("%,.2f", a.getBalance())
            });
        }
    }
}
