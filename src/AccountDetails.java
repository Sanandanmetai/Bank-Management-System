import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * AccountDetails.java
 * -------------------
 * Shows full account information for the logged-in customer.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class AccountDetails extends JFrame {

    private final Account           account;
    private final CustomerDashboard dashboard;

    public AccountDetails(Account account, CustomerDashboard dashboard) {
        this.account   = account;
        this.dashboard = dashboard;
        UITheme.setupFrame(this, "NexaBank – Account Details", 480, 520);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ── Header ─────────────────────────────────────────────────────────
        JLabel icon = new JLabel("👤", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 52));
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 8, 0);
        root.add(icon, gbc);

        JLabel title = new JLabel("Account Details", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(title, gbc);

        // ── Detail card ────────────────────────────────────────────────────
        JPanel card = new JPanel(new GridLayout(5, 1, 0, 16));
        card.setBackground(UITheme.BG_CARD);
        card.setBorder(UITheme.cardBorder());

        card.add(makeRow("Account Number",
            "#" + String.format("%06d", account.getAccountId())));
        card.add(makeRow("Full Name",     account.getName()));
        card.add(makeRow("Email Address", account.getEmail()));
        card.add(makeRow("Phone Number",  account.getPhone()));
        card.add(makeRow("Balance",
            "PKR " + String.format("%,.2f", account.getBalance()), UITheme.ACCENT));

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 20, 0);
        root.add(card, gbc);

        // ── Buttons ────────────────────────────────────────────────────────
        JButton updateBtn = UITheme.primaryButton("Update Personal Info");
        updateBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateBtn.setBorder(new EmptyBorder(13, 24, 13, 24));
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 12, 0);
        root.add(updateBtn, gbc);

        JButton closeBtn = new JButton("Close");
        closeBtn.setBackground(UITheme.BG_INPUT);
        closeBtn.setForeground(UITheme.TEXT_MUTED);
        closeBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        closeBtn.setBorder(new EmptyBorder(10, 24, 10, 24));
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.setOpaque(true);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 0, 0);
        root.add(closeBtn, gbc);

        add(root);

        updateBtn.addActionListener(e -> {
            new UpdateInfoForm(account, dashboard);
            dispose();
        });
        closeBtn.addActionListener(e -> dispose());
    }

    private JPanel makeRow(String label, String value) {
        return makeRow(label, value, UITheme.TEXT_PRIMARY);
    }

    private JPanel makeRow(String label, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout(0, 4));
        row.setBackground(UITheme.BG_CARD);

        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_SMALL);
        lbl.setForeground(UITheme.TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 14));
        val.setForeground(valueColor);

        row.add(lbl, BorderLayout.NORTH);
        row.add(val, BorderLayout.CENTER);
        return row;
    }
}
