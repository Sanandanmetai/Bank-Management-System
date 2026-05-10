import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginForm.java
 * --------------
 * A single form that handles both Customer and Admin login,
 * determined by the `isAdmin` flag passed to the constructor.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation, Constructors
 */
public class LoginForm extends JFrame {

    private final boolean  isAdmin;
    private JTextField     emailField;
    private JPasswordField passwordField;

    /**
     * @param isAdmin true = Admin login, false = Customer login
     */
    public LoginForm(boolean isAdmin) {
        this.isAdmin = isAdmin;
        UITheme.setupFrame(this,
            isAdmin ? "NexaBank – Admin Login" : "NexaBank – Customer Login",
            460, 520);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;

        // ── Title ──────────────────────────────────────────────────────────
        JLabel icon = new JLabel(isAdmin ? "🔐" : "👤", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 44));
        gbc.gridy  = 0;
        gbc.insets = new Insets(0, 0, 8, 0);
        root.add(icon, gbc);

        JLabel title = new JLabel(isAdmin ? "Admin Login" : "Customer Login", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy  = 1;
        gbc.insets = new Insets(0, 0, 6, 0);
        root.add(title, gbc);

        JLabel sub = new JLabel(
            isAdmin ? "Access the admin control panel" : "Sign in to your account",
            SwingConstants.CENTER);
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_MUTED);
        gbc.gridy  = 2;
        gbc.insets = new Insets(0, 0, 32, 0);
        root.add(sub, gbc);

        // ── Email/Username field ───────────────────────────────────────────
        JLabel emailLabel = UITheme.label(isAdmin ? "USERNAME" : "EMAIL ADDRESS");
        gbc.gridy  = 3;
        gbc.insets = new Insets(0, 0, 6, 0);
        root.add(emailLabel, gbc);

        emailField = UITheme.styledField();
        emailField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy  = 4;
        gbc.insets = new Insets(0, 0, 18, 0);
        root.add(emailField, gbc);

        // ── Password field ─────────────────────────────────────────────────
        JLabel passLabel = UITheme.label("PASSWORD");
        gbc.gridy  = 5;
        gbc.insets = new Insets(0, 0, 6, 0);
        root.add(passLabel, gbc);

        passwordField = UITheme.styledPasswordField();
        passwordField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy  = 6;
        gbc.insets = new Insets(0, 0, 28, 0);
        root.add(passwordField, gbc);

        // ── Login button ───────────────────────────────────────────────────
        JButton loginBtn = UITheme.primaryButton(isAdmin ? "Login as Admin" : "Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        loginBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        gbc.gridy  = 7;
        gbc.insets = new Insets(0, 0, 20, 0);
        root.add(loginBtn, gbc);

        // ── Back link ──────────────────────────────────────────────────────
        JLabel backLbl = new JLabel("← Back to Welcome", SwingConstants.CENTER);
        backLbl.setForeground(UITheme.TEXT_MUTED);
        backLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        backLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy  = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        root.add(backLbl, gbc);

        add(root);

        // ── Action Listeners ───────────────────────────────────────────────
        loginBtn.addActionListener(e -> handleLogin());

        // Allow pressing Enter to login
        passwordField.addActionListener(e -> handleLogin());
        emailField.addActionListener(e -> handleLogin());

        backLbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                new WelcomeScreen();
                dispose();
            }
            public void mouseEntered(MouseEvent e) {
                backLbl.setForeground(UITheme.ACCENT);
            }
            public void mouseExited(MouseEvent e) {
                backLbl.setForeground(UITheme.TEXT_MUTED);
            }
        });
    }

    /** Validates inputs and calls appropriate DAO. */
    private void handleLogin() {
        String identifier = emailField.getText().trim();
        String password   = new String(passwordField.getPassword()).trim();

        // ── Validation ─────────────────────────────────────────────────────
        if (identifier.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields.", "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (isAdmin) {
            // Admin authentication
            AdminDAO adminDAO = new AdminDAO();
            Admin admin = adminDAO.login(identifier, password);

            if (admin != null) {
                JOptionPane.showMessageDialog(this,
                    "Welcome, " + admin.getUsername() + "!", "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                new AdminDashboard(admin);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } else {
            // Customer authentication
            AccountDAO accountDAO = new AccountDAO();
            Account account = accountDAO.login(identifier, password);

            if (account != null) {
                new CustomerDashboard(account);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid email or password.", "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        }
    }
}
