import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * RegisterForm.java
 * -----------------
 * Allows a new customer to register a bank account.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class RegisterForm extends JFrame {

    private JTextField     nameField, emailField, phoneField;
    private JPasswordField passField, confirmPassField;

    public RegisterForm() {
        UITheme.setupFrame(this, "NexaBank – Create Account", 500, 660);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;

        // ── Header ─────────────────────────────────────────────────────────
        addRow(root, gbc, 0, makeTitle("✨", "Create Account", "Join NexaBank today"), new Insets(0, 0, 28, 0));

        // ── Full Name ──────────────────────────────────────────────────────
        addRow(root, gbc, 1, UITheme.label("FULL NAME"), new Insets(0, 0, 6, 0));
        nameField = UITheme.styledField();
        nameField.setPreferredSize(new Dimension(0, 42));
        addRow(root, gbc, 2, nameField, new Insets(0, 0, 16, 0));

        // ── Email ──────────────────────────────────────────────────────────
        addRow(root, gbc, 3, UITheme.label("EMAIL ADDRESS"), new Insets(0, 0, 6, 0));
        emailField = UITheme.styledField();
        emailField.setPreferredSize(new Dimension(0, 42));
        addRow(root, gbc, 4, emailField, new Insets(0, 0, 16, 0));

        // ── Phone ──────────────────────────────────────────────────────────
        addRow(root, gbc, 5, UITheme.label("PHONE NUMBER"), new Insets(0, 0, 6, 0));
        phoneField = UITheme.styledField();
        phoneField.setPreferredSize(new Dimension(0, 42));
        addRow(root, gbc, 6, phoneField, new Insets(0, 0, 16, 0));

        // ── Password ───────────────────────────────────────────────────────
        addRow(root, gbc, 7, UITheme.label("PASSWORD"), new Insets(0, 0, 6, 0));
        passField = UITheme.styledPasswordField();
        passField.setPreferredSize(new Dimension(0, 42));
        addRow(root, gbc, 8, passField, new Insets(0, 0, 16, 0));

        // ── Confirm Password ───────────────────────────────────────────────
        addRow(root, gbc, 9, UITheme.label("CONFIRM PASSWORD"), new Insets(0, 0, 6, 0));
        confirmPassField = UITheme.styledPasswordField();
        confirmPassField.setPreferredSize(new Dimension(0, 42));
        addRow(root, gbc, 10, confirmPassField, new Insets(0, 0, 28, 0));

        // ── Register Button ────────────────────────────────────────────────
        JButton registerBtn = UITheme.primaryButton("Create Account");
        registerBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        registerBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        addRow(root, gbc, 11, registerBtn, new Insets(0, 0, 20, 0));

        // ── Back link ──────────────────────────────────────────────────────
        JLabel backLbl = new JLabel("← Already have an account? Login", SwingConstants.CENTER);
        backLbl.setForeground(UITheme.TEXT_MUTED);
        backLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addRow(root, gbc, 12, backLbl, new Insets(0, 0, 0, 0));

        add(root);

        // ── Listeners ──────────────────────────────────────────────────────
        registerBtn.addActionListener(e -> handleRegister());

        backLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new WelcomeScreen();
                dispose();
            }
            public void mouseEntered(MouseEvent e) { backLbl.setForeground(UITheme.ACCENT); }
            public void mouseExited(MouseEvent e)  { backLbl.setForeground(UITheme.TEXT_MUTED); }
        });
    }

    private void handleRegister() {
        String name        = nameField.getText().trim();
        String email       = emailField.getText().trim();
        String phone       = phoneField.getText().trim();
        String pass        = new String(passField.getPassword()).trim();
        String confirmPass = new String(confirmPassField.getPassword()).trim();

        // ── Validations ─────────────────────────────────────────────────
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || pass.isEmpty() || confirmPass.isEmpty()) {
            showError("All fields are required.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (phone.length() < 10) {
            showError("Phone number must be at least 10 digits.");
            return;
        }

        if (pass.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showError("Passwords do not match.");
            confirmPassField.setText("");
            return;
        }

        // ── Create account ──────────────────────────────────────────────
        Account account = new Account(0, name, email, phone, 0.0, pass);
        AccountDAO dao  = new AccountDAO();
        boolean success = dao.createAccount(account);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Account created successfully!\nYou can now log in.",
                "Success", JOptionPane.INFORMATION_MESSAGE);
            new WelcomeScreen();
            dispose();
        } else {
            showError("Email is already registered. Please use a different email.");
        }
    }

    // ── Helper methods ────────────────────────────────────────────────────────

    private JPanel makeTitle(String iconText, String title, String sub) {
        JPanel p = new JPanel(new GridLayout(3, 1, 0, 4));
        p.setBackground(UITheme.BG_DARK);
        JLabel icon = new JLabel(iconText, SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 36));
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setFont(UITheme.FONT_TITLE);
        t.setForeground(UITheme.TEXT_PRIMARY);
        JLabel s = new JLabel(sub, SwingConstants.CENTER);
        s.setFont(UITheme.FONT_BODY);
        s.setForeground(UITheme.TEXT_MUTED);
        p.add(icon); p.add(t); p.add(s);
        return p;
    }

    private void addRow(JPanel parent, GridBagConstraints gbc,
                        int row, Component comp, Insets insets) {
        gbc.gridy  = row;
        gbc.insets = insets;
        parent.add(comp, gbc);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }
}
