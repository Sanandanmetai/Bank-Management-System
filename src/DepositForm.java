import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * DepositForm.java
 * ----------------
 * Allows a customer to deposit money into their account.
 * Updates balance in DB and records a DEPOSIT transaction.
 *
 * OOP Concepts: Inheritance (extends JFrame), Encapsulation
 */
public class DepositForm extends JFrame {

    private final Account           account;
    private final CustomerDashboard dashboard;
    private JTextField              amountField;

    public DepositForm(Account account, CustomerDashboard dashboard) {
        this.account   = account;
        this.dashboard = dashboard;
        UITheme.setupFrame(this, "NexaBank – Deposit", 420, 400);
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
        JLabel icon = new JLabel("💰", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 8, 0);
        root.add(icon, gbc);

        JLabel title = new JLabel("Deposit Money", SwingConstants.CENTER);
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 8, 0);
        root.add(title, gbc);

        // ── Current balance ────────────────────────────────────────────────
        JLabel balLbl = new JLabel(
            "Current Balance: PKR " + String.format("%,.2f", account.getBalance()),
            SwingConstants.CENTER);
        balLbl.setFont(UITheme.FONT_BODY);
        balLbl.setForeground(UITheme.ACCENT);
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(balLbl, gbc);

        // ── Amount field ───────────────────────────────────────────────────
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 6, 0);
        root.add(UITheme.label("DEPOSIT AMOUNT (PKR)"), gbc);

        amountField = UITheme.styledField();
        amountField.setPreferredSize(new Dimension(0, 42));
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 28, 0);
        root.add(amountField, gbc);

        // ── Deposit Button ─────────────────────────────────────────────────
        JButton depositBtn = UITheme.primaryButton("Confirm Deposit");
        depositBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        depositBtn.setBorder(new EmptyBorder(14, 24, 14, 24));
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 0, 0);
        root.add(depositBtn, gbc);

        add(root);

        depositBtn.addActionListener(e -> handleDeposit());
        amountField.addActionListener(e -> handleDeposit());
    }

    private void handleDeposit() {
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

        // ── Update balance in DB ───────────────────────────────────────────
        double newBalance = account.getBalance() + amount;
        AccountDAO     accDAO = new AccountDAO();
        TransactionDAO txDAO  = new TransactionDAO();

        boolean balUpdated = accDAO.updateBalance(account.getAccountId(), newBalance);
        boolean txSaved    = txDAO.addTransaction(account.getAccountId(), "DEPOSIT", amount);

        if (balUpdated && txSaved) {
            account.setBalance(newBalance);
            JOptionPane.showMessageDialog(this,
                String.format("Successfully deposited PKR %,.2f\nNew Balance: PKR %,.2f",
                    amount, newBalance),
                "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);

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
