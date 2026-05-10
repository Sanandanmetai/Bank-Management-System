import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AdminDashboard.java
 * -------------------
 * Admin control panel — shows bank-wide statistics and navigation
 * to manage customers, transactions, and loans.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation, Composition
 */
public class AdminDashboard extends JFrame {

    private final Admin admin;

    public AdminDashboard(Admin admin) {
        this.admin = admin;
        UITheme.setupFrame(this, "NexaBank – Admin Panel", 860, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_DARK);
        add(buildSidebar(),  BorderLayout.WEST);
        add(buildContent(),  BorderLayout.CENTER);
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.BG_CARD);
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));
        sidebar.setPreferredSize(new Dimension(210, 0));

        JLabel logo = new JLabel("🏦 NexaBank");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        logo.setForeground(UITheme.ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        JLabel adminTag = new JLabel("  ADMIN PANEL");
        adminTag.setFont(new Font("SansSerif", Font.BOLD, 9));
        adminTag.setForeground(UITheme.WARNING);
        adminTag.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(adminTag);

        sidebar.add(Box.createVerticalStrut(28));

        JLabel greet = new JLabel("Welcome,");
        greet.setFont(UITheme.FONT_SMALL);
        greet.setForeground(UITheme.TEXT_MUTED);
        greet.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(greet);

        JLabel adminName = new JLabel(admin.getUsername());
        adminName.setFont(new Font("SansSerif", Font.BOLD, 18));
        adminName.setForeground(UITheme.TEXT_PRIMARY);
        adminName.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(adminName);

        sidebar.add(Box.createVerticalStrut(28));

        String[][] navItems = {
            {"👥", "All Customers"},
            {"📊", "All Transactions"},
            {"🏛️", "Loan Management"},
            {"🔍", "Search Customer"}
        };

        for (String[] item : navItems) {
            JButton btn = makeNavBtn(item[0] + "  " + item[1]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
            final String label = item[1];
            btn.addActionListener(e -> handleNav(label));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = new JButton("⏻  Logout");
        logoutBtn.setBackground(new Color(60, 20, 20));
        logoutBtn.setForeground(UITheme.DANGER);
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        logoutBtn.setBorder(new EmptyBorder(10, 16, 10, 16));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.setOpaque(true);
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        sidebar.add(logoutBtn);

        logoutBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this, "Logout from admin panel?",
                "Logout", JOptionPane.YES_NO_OPTION);
            if (c == JOptionPane.YES_OPTION) {
                new WelcomeScreen();
                dispose();
            }
        });

        return sidebar;
    }

    // ── Main Content ──────────────────────────────────────────────────────────
    private JPanel buildContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(28, 28, 28, 28));

        // Title bar
        JLabel title = new JLabel("Bank Overview");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        panel.add(title, BorderLayout.NORTH);

        // Stat cards
        JPanel stats = new JPanel(new GridLayout(2, 3, 16, 16));
        stats.setBackground(UITheme.BG_DARK);
        stats.setBorder(new EmptyBorder(22, 0, 0, 0));

        // Fetch live stats
        AccountDAO     accDAO = new AccountDAO();
        TransactionDAO txDAO  = new TransactionDAO();
        LoanDAO        lnDAO  = new LoanDAO();

        int    totalAccounts    = accDAO.getTotalAccounts();
        double totalBalance     = accDAO.getTotalBalance();
        int    totalTx          = txDAO.getTotalTransactions();
        int    pendingLoans     = lnDAO.getPendingLoanCount();

        stats.add(makeStatCard("👥 Total Customers", String.valueOf(totalAccounts), UITheme.ACCENT));
        stats.add(makeStatCard("💰 Total Deposits",
            String.format("PKR %,.0f", totalBalance), UITheme.ACCENT));
        stats.add(makeStatCard("📊 Transactions", String.valueOf(totalTx), UITheme.TEXT_PRIMARY));
        stats.add(makeStatCard("⏳ Pending Loans", String.valueOf(pendingLoans), UITheme.WARNING));
        stats.add(makeActionCard("👥 Manage Customers",
            "View, search, delete accounts", e -> new ViewAllCustomers(this)));
        stats.add(makeActionCard("🏛️ Manage Loans",
            "Approve or reject applications", e -> new LoanManagement(this)));

        panel.add(stats, BorderLayout.CENTER);

        JLabel footer = new JLabel("NexaBank Admin Panel  ·  All activities are monitored.");
        footer.setFont(UITheme.FONT_SMALL);
        footer.setForeground(UITheme.TEXT_MUTED);
        footer.setBorder(new EmptyBorder(16, 0, 0, 0));
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void handleNav(String label) {
        switch (label) {
            case "All Customers":    new ViewAllCustomers(this);  break;
            case "All Transactions": new ViewAllTransactions();   break;
            case "Loan Management":  new LoanManagement(this);   break;
            case "Search Customer":  new SearchCustomer();        break;
        }
    }

    // ── UI Helpers ────────────────────────────────────────────────────────────

    private JPanel makeStatCard(String label, String value, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(UITheme.cardBorder());

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_SMALL);
        lbl.setForeground(UITheme.TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 20));
        val.setForeground(valueColor);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    private JPanel makeActionCard(String title, String sub, ActionListener al) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(UITheme.cardBorder());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 14));
        t.setForeground(UITheme.ACCENT);

        JLabel s = new JLabel(sub);
        s.setFont(UITheme.FONT_SMALL);
        s.setForeground(UITheme.TEXT_MUTED);

        card.add(t, BorderLayout.NORTH);
        card.add(s, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { al.actionPerformed(null); }
            public void mouseEntered(MouseEvent e) { card.setBackground(UITheme.BG_INPUT); }
            public void mouseExited(MouseEvent e)  { card.setBackground(UITheme.BG_CARD);  }
        });
        return card;
    }

    private JButton makeNavBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(UITheme.BG_DARK);
        btn.setForeground(UITheme.TEXT_PRIMARY);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btn.setBorder(new EmptyBorder(10, 12, 10, 12));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(UITheme.BG_INPUT); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(UITheme.BG_DARK);  }
        });
        return btn;
    }
}
