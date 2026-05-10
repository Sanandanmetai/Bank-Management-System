import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TransactionDAO.java
 * -------------------
 * Handles all Transaction database operations.
 *
 * OOP Concepts: Encapsulation, Abstraction, DAO Pattern
 */
public class TransactionDAO {

    /**
     * Records a transaction (DEPOSIT or WITHDRAW) in the database.
     */
    public boolean addTransaction(int accountId, String type, double amount) {
        String sql = "INSERT INTO transaction (account_id, type, amount) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all transactions for a specific account.
     */
    public List<Transaction> getTransactionsByAccount(int accountId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction WHERE account_id = ? ORDER BY date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Returns ALL transactions (Admin view).
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transaction ORDER BY date DESC";
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
     * Returns total number of transactions.
     */
    public int getTotalTransactions() {
        String sql = "SELECT COUNT(*) FROM transaction";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ── Helper ────────────────────────────────────────────────────────────────
    private Transaction mapRow(ResultSet rs) throws SQLException {
        return new Transaction(
            rs.getInt("transaction_id"),
            rs.getInt("account_id"),
            rs.getString("type"),
            rs.getDouble("amount"),
            rs.getString("date")
        );
    }
}
