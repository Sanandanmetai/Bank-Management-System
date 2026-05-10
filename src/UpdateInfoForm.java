import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * UpdateInfoForm.java
 * -------------------
 * Allows a customer to update their name, email, and phone number.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class UpdateInfoForm extends JFrame {

    private Account           account;
    private CustomerDashboard dashboard;
    private JTextField        nameField, emailField, phoneField;

    public UpdateInfoForm(Account account, CustomerDashboard dashboard) {
        this.account   = account;
        this.dashboard = dashboard;
        UITheme.setupFrame(this, "NexaBank – Update Information", 460, 480);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(new EmptyBorder(36, 50, 36, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;

        // ── Title ──────────────────────────────────────────────────────────
        JLabel title = new JLabel("✏️  Update Info", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(title, gbc);

        // ── Name ───────────────────────────────────────────────────────────
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 6, 0);
        root.add(UITheme.label("FULL NAME"), gbc);
        nameField = UITheme.styledField();
        nameField.setText(account.getName());
        nameField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 16, 0);
        root.add(nameField, gbc);

        // ── Email ──────────────────────────────────────────────────────────
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 6, 0);
        root.add(UITheme.label("EMAIL ADDRESS"), gbc);
        emailField = UITheme.styledField();
        emailField.setText(account.getEmail());
        emailField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 16, 0);
        root.add(emailField, gbc);

        // ── Phone ──────────────────────────────────────────────────────────
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 6, 0);
        root.add(UITheme.label("PHONE NUMBER"), gbc);
        phoneField = UITheme.styledField();
        phoneField.setText(account.getPhone());
        phoneField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(phoneField, gbc);

        // ── Save Button ────────────────────────────────────────────────────
        JButton saveBtn = UITheme.primaryButton("Save Changes");
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        saveBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 12, 0);
        root.add(saveBtn, gbc);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(UITheme.BG_INPUT);
        cancelBtn.setForeground(UITheme.TEXT_MUTED);
        cancelBtn.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cancelBtn.setBorder(new EmptyBorder(10, 24, 10, 24));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelBtn.setOpaque(true);
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 0, 0);
        root.add(cancelBtn, gbc);

        add(root);

        saveBtn.addActionListener(e -> handleSave());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void handleSave() {
        String name  = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Invalid email format.",
                "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        account.setName(name);
        account.setEmail(email);
        account.setPhone(phone);

        AccountDAO dao  = new AccountDAO();
        boolean success = dao.updateAccount(account);

        if (success) {
            JOptionPane.showMessageDialog(this,
                "Information updated successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
            if (dashboard != null) dashboard.refreshBalance();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Update failed. Email may already be in use.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
