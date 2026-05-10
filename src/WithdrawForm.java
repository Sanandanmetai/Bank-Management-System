import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * WithdrawForm.java
 * -----------------
 * Allows a customer to withdraw money from their account.
 * Validates sufficient balance before processing.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class WithdrawForm extends JFrame {

    private final Account           account;
    private final CustomerDashboard dashboard;
    private JTextField              amountField;

    public WithdrawForm(Account account, CustomerDashboard dashboard) {
        this.account   = account;
        this.dashboard = dashboard;
        UITheme.setupFrame(this, "NexaBank – Withdraw", 420, 400);
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

        // ── Icon + Title ───────────────────────────────────────────────────
        JLabel icon = new JLabel("💸", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 8, 0);
        root.add(icon, gbc);

        JLabel title = new JLabel("Withdraw Money", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 8, 0);
        root.add(title, gbc);

        // ── Current balance ────────────────────────────────────────────────
        JLabel balLbl = new JLabel(
            "Available Balance: PKR " + String.format("%,.2f", account.getBalance()),
            SwingConstants.CENTER);
        balLbl.setFont(UITheme.FONT_BODY);
        balLbl.setForeground(UITheme.ACCENT);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(balLbl, gbc);

        // ── Amount field ───────────────────────────────────────────────────
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 6, 0);
        root.add(UITheme.label("WITHDRAWAL AMOUNT (PKR)"), gbc);

        amountField = UITheme.styledField();
        amountField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(amountField, gbc);

        // ── Withdraw Button ────────────────────────────────────────────────
        JButton withdrawBtn = UITheme.dangerButton("Confirm Withdrawal");
        withdrawBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        withdrawBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        root.add(withdrawBtn, gbc);

        add(root);

        withdrawBtn.addActionListener(e -> handleWithdraw());
        amountField.addActionListener(e -> handleWithdraw());
    }

    private void handleWithdraw() {
        String amtText = amountField.getText().trim();

        if (amtText.isEmpty()) {
            showError("Please enter an amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amtText);
        } catch (NumberFormatException ex) {
            showError("Invalid amount. Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            showError("Amount must be greater than zero.");
            return;
        }

        // ── Balance validation ─────────────────────────────────────────────
        if (amount > account.getBalance()) {
            showError(String.format(
                "Insufficient balance!\nAvailable: PKR %,.2f\nRequested: PKR %,.2f",
                account.getBalance(), amount));
            return;
        }

        // ── Confirm ────────────────────────────────────────────────────────
        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Withdraw PKR %,.2f from your account?", amount),
            "Confirm Withdrawal", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        // ── Update DB ──────────────────────────────────────────────────────
        double newBalance  = account.getBalance() - amount;
        AccountDAO     accDAO = new AccountDAO();
        TransactionDAO txDAO  = new TransactionDAO();

        boolean balUpdated = accDAO.updateBalance(account.getAccountId(), newBalance);
        boolean txSaved    = txDAO.addTransaction(account.getAccountId(), "WITHDRAW", amount);

        if (balUpdated && txSaved) {
            account.setBalance(newBalance);
            JOptionPane.showMessageDialog(this,
                String.format("Successfully withdrew PKR %,.2f\nNew Balance: PKR %,.2f",
                    amount, newBalance),
                "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);

            if (dashboard != null) dashboard.refreshBalance();
            dispose();
        } else {
            showError("Transaction failed. Please try again.");
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
