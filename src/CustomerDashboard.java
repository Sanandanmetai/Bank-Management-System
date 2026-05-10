import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * CustomerDashboard.java
 * ----------------------
 * The main dashboard shown after a successful customer login.
 * Contains navigation cards for all customer operations.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation, Composition
 */
public class CustomerDashboard extends JFrame {

    private Account account;    // Logged-in customer
    private JLabel  balanceLabel;

    public CustomerDashboard(Account account) {
        this.account = account;
        UITheme.setupFrame(this, "NexaBank – Dashboard", 800, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UITheme.BG_DARK);

        add(buildSidebar(),     BorderLayout.WEST);
        add(buildMainContent(), BorderLayout.CENTER);
    }

    // ── Left Sidebar ──────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UITheme.BG_CARD);
        sidebar.setBorder(new EmptyBorder(30, 20, 30, 20));
        sidebar.setPreferredSize(new Dimension(200, 0));

        // Bank logo
        JLabel logo = new JLabel("🏦 NexaBank");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        logo.setForeground(UITheme.ACCENT);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);

        sidebar.add(Box.createVerticalStrut(30));

        // Customer name
        JLabel greeting = new JLabel("Hello,");
        greeting.setFont(UITheme.FONT_SMALL);
        greeting.setForeground(UITheme.TEXT_MUTED);
        greeting.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(greeting);

        JLabel nameLabel = new JLabel(account.getName().split(" ")[0]);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(UITheme.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(nameLabel);

        sidebar.add(Box.createVerticalStrut(30));

        // Nav items
        String[][] navItems = {
            {"💰", "Deposit"},
            {"💸", "Withdraw"},
            {"📋", "Transactions"},
            {"🏛️", "Apply Loan"},
            {"👤", "My Account"},
            {"✏️", "Update Info"}
        };

        for (String[] item : navItems) {
            JButton btn = makeNavButton(item[0] + "  " + item[1]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(6));
            final String label = item[1];
            btn.addActionListener(e -> handleNav(label));
        }

        sidebar.add(Box.createVerticalGlue());

        // Logout button
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
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new WelcomeScreen();
                dispose();
            }
        });

        return sidebar;
    }

    // ── Main Content Area ─────────────────────────────────────────────────────
    private JPanel buildMainContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // ── Top greeting ───────────────────────────────────────────────────
        JLabel pageTitle = new JLabel("Account Overview");
        pageTitle.setFont(UITheme.FONT_TITLE);
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);

        JLabel dateLabel = new JLabel(new java.util.Date().toString());
        dateLabel.setFont(UITheme.FONT_SMALL);
        dateLabel.setForeground(UITheme.TEXT_MUTED);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UITheme.BG_DARK);
        topBar.add(pageTitle, BorderLayout.WEST);
        topBar.add(dateLabel, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // ── Card grid ──────────────────────────────────────────────────────
        JPanel cardGrid = new JPanel(new GridLayout(2, 3, 16, 16));
        cardGrid.setBackground(UITheme.BG_DARK);
        cardGrid.setBorder(new EmptyBorder(24, 0, 0, 0));

        // Balance card (prominent) — capture=true stores the JLabel in balanceLabel directly
        JPanel balanceCard = makeInfoCard("💳 Current Balance",
            String.format("PKR %,.2f", account.getBalance()), UITheme.ACCENT, true);
        cardGrid.add(balanceCard);

        // Account ID card
        cardGrid.add(makeInfoCard("🆔 Account Number",
            "#" + String.format("%06d", account.getAccountId()), UITheme.TEXT_PRIMARY));

        // Email card
        cardGrid.add(makeInfoCard("📧 Email", account.getEmail(), UITheme.TEXT_PRIMARY));

        // Phone card
        cardGrid.add(makeInfoCard("📞 Phone", account.getPhone(), UITheme.TEXT_PRIMARY));

        // Quick deposit button-card
        cardGrid.add(makeActionCard("💰 Quick Deposit", "Add funds to your account",
            e -> new DepositForm(account, this)));

        // Quick withdraw button-card
        cardGrid.add(makeActionCard("💸 Quick Withdraw", "Withdraw from your account",
            e -> new WithdrawForm(account, this)));

        panel.add(cardGrid, BorderLayout.CENTER);

        // ── Bottom tip ─────────────────────────────────────────────────────
        JLabel tip = new JLabel("Tip: Use the sidebar to access all features.");
        tip.setFont(UITheme.FONT_SMALL);
        tip.setForeground(UITheme.TEXT_MUTED);
        tip.setBorder(new EmptyBorder(16, 0, 0, 0));
        panel.add(tip, BorderLayout.SOUTH);

        return panel;
    }

    // ── Navigation handler ────────────────────────────────────────────────────
    private void handleNav(String label) {
        // Refresh account before each action
        AccountDAO dao = new AccountDAO();
        account = dao.getAccountById(account.getAccountId());

        switch (label) {
            case "Deposit":      new DepositForm(account, this);        break;
            case "Withdraw":     new WithdrawForm(account, this);       break;
            case "Transactions": new TransactionHistory(account);       break;
            case "Apply Loan":   new LoanForm(account);                 break;
            case "My Account":   new AccountDetails(account, this);     break;
            case "Update Info":  new UpdateInfoForm(account, this);     break;
        }
    }

    /** Refreshes balance display after a transaction. */
    public void refreshBalance() {
        AccountDAO dao = new AccountDAO();
        account = dao.getAccountById(account.getAccountId());
        if (balanceLabel != null && account != null) {
            balanceLabel.setText(String.format("PKR %,.2f", account.getBalance()));
        }
    }

    public Account getAccount() { return account; }

    // ── UI Helpers ────────────────────────────────────────────────────────────

    private JButton makeNavButton(String text) {
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

    /**
     * Creates an info display card.
     * If captureValueLabel is true, saves the value JLabel into balanceLabel field.
     */
    private JPanel makeInfoCard(String label, String value, Color valueColor) {
        return makeInfoCard(label, value, valueColor, false);
    }

    private JPanel makeInfoCard(String label, String value, Color valueColor, boolean capture) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(UITheme.cardBorder());

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_SMALL);
        lbl.setForeground(UITheme.TEXT_MUTED);
        card.add(lbl, BorderLayout.NORTH);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 16));
        val.setForeground(valueColor);
        JPanel valPanel = new JPanel(new BorderLayout());
        valPanel.setBackground(UITheme.BG_CARD);
        valPanel.add(val, BorderLayout.WEST);
        card.add(valPanel, BorderLayout.CENTER);

        if (capture) balanceLabel = val;  // Direct reference — no fragile casting

        return card;
    }

    /** Creates a clickable action card. */
    private JPanel makeActionCard(String title, String subtitle, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(UITheme.cardBorder());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 14));
        t.setForeground(UITheme.ACCENT);
        card.add(t, BorderLayout.NORTH);

        JLabel s = new JLabel(subtitle);
        s.setFont(UITheme.FONT_SMALL);
        s.setForeground(UITheme.TEXT_MUTED);
        card.add(s, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { action.actionPerformed(null); }
            public void mouseEntered(MouseEvent e) { card.setBackground(UITheme.BG_INPUT); }
            public void mouseExited(MouseEvent e)  { card.setBackground(UITheme.BG_CARD);  }
        });

        return card;
    }
}
