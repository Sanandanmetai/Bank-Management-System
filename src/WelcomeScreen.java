import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * WelcomeScreen.java
 * ------------------
 * The first screen displayed when the application launches.
 * Shows the bank name and offers Customer / Admin login.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class WelcomeScreen extends JFrame {

    public WelcomeScreen() {
        UITheme.setupFrame(this, "NexaBank – Welcome", 520, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(40, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // ── Logo area ──────────────────────────────────────────────────────
        JLabel logoIcon = new JLabel("🏦", SwingConstants.CENTER);
        logoIcon.setFont(new Font("SansSerif", Font.PLAIN, 64));
        gbc.gridy = 0;
        root.add(logoIcon, gbc);

        // ── Bank name ──────────────────────────────────────────────────────
        JLabel titleLabel = new JLabel("NexaBank", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 34));
        titleLabel.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 0, 0);
        root.add(titleLabel, gbc);

        // ── Tagline ────────────────────────────────────────────────────────
        JLabel tagLabel = new JLabel("Secure · Modern · Reliable", SwingConstants.CENTER);
        tagLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tagLabel.setForeground(UITheme.TEXT_MUTED);
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 0, 40, 0);
        root.add(tagLabel, gbc);

        // ── Divider ────────────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 30, 0);
        root.add(sep, gbc);

        // ── Customer Login button ──────────────────────────────────────────
        JButton customerBtn = UITheme.primaryButton("Customer Login");
        customerBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        customerBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 14, 0);
        root.add(customerBtn, gbc);

        // ── Register button ────────────────────────────────────────────────
        JButton registerBtn = new JButton("Create New Account");
        registerBtn.setBackground(UITheme.BG_INPUT);
        registerBtn.setForeground(UITheme.TEXT_PRIMARY);
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        registerBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        registerBtn.setFocusPainted(false);
        registerBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerBtn.setOpaque(true);
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 30, 0);
        root.add(registerBtn, gbc);

        // ── Admin Login link ───────────────────────────────────────────────
        JLabel adminLbl = new JLabel("Admin Login →", SwingConstants.CENTER);
        adminLbl.setForeground(UITheme.ACCENT);
        adminLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        adminLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        root.add(adminLbl, gbc);

        // ── Footer ─────────────────────────────────────────────────────────
        JLabel footer = new JLabel("© 2025 NexaBank. University DBMS Project.", SwingConstants.CENTER);
        footer.setForeground(UITheme.TEXT_MUTED);
        footer.setFont(UITheme.FONT_SMALL);
        gbc.gridy = 7;
        gbc.insets = new Insets(40, 0, 0, 0);
        root.add(footer, gbc);

        add(root);

        // ── Action Listeners ───────────────────────────────────────────────
        customerBtn.addActionListener(e -> {
            new LoginForm(false);
            dispose();
        });

        registerBtn.addActionListener(e -> {
            new RegisterForm();
            dispose();
        });

        adminLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new LoginForm(true);
                dispose();
            }
            public void mouseEntered(MouseEvent e) {
                adminLbl.setForeground(UITheme.ACCENT_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                adminLbl.setForeground(UITheme.ACCENT);
            }
        });
    }
}
