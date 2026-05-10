import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AccountDAO.java
 * ---------------
 * Data Access Object (DAO) for all Account-related database operations.
 * Uses PreparedStatement for safe SQL execution (prevents SQL injection).
 *
 * OOP Concepts: Encapsulation, Abstraction, Separation of concerns
 */
public class AccountDAO {

    /**
     * Registers a new customer account in the database.
     * Returns true if successful.
     */
    public boolean createAccount(Account acc) {
        String sql = "INSERT INTO account (name, email, phone, balance, password) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, acc.getName());
            ps.setString(2, acc.getEmail());
            ps.setString(3, acc.getPhone());
            ps.setDouble(4, acc.getBalance());
            ps.setString(5, acc.getPassword());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Email already exists
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Authenticates a customer by email and password.
     * Returns the Account object on success, null on failure.
     */
    public Account login(String email, String password) {
        String sql = "SELECT * FROM account WHERE email = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns an Account by its ID.
     */
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns all accounts (for Admin view).
     */
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account ORDER BY account_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Searches accounts by name (partial match).
     */
    public List<Account> searchByName(String keyword) {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE name LIKE ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Updates customer balance (after deposit/withdraw).
     */
    public boolean updateBalance(int accountId, double newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, newBalance);
            ps.setInt(2, accountId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates customer personal information.
     */
    public boolean updateAccount(Account acc) {
        String sql = "UPDATE account SET name=?, email=?, phone=? WHERE account_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, acc.getName());
            ps.setString(2, acc.getEmail());
            ps.setString(3, acc.getPhone());
            ps.setInt(4, acc.getAccountId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes an account (Admin only).
     */
    public boolean deleteAccount(int accountId) {
        String sql = "DELETE FROM account WHERE account_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns total number of accounts.
     */
    public int getTotalAccounts() {
        String sql = "SELECT COUNT(*) FROM account";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Returns total balance across all accounts.
     */
    public double getTotalBalance() {
        String sql = "SELECT SUM(balance) FROM account";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ── Helper: map a ResultSet row to an Account object ─────────────────────
    private Account mapRow(ResultSet rs) throws SQLException {
        return new Account(
            rs.getInt("account_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getDouble("balance"),
            rs.getString("password")
        );
    }
}
