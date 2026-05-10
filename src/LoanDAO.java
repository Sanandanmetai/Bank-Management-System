import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * LoanDAO.java
 * ------------
 * Handles all Loan database operations.
 *
 * OOP Concepts: Encapsulation, Abstraction, DAO Pattern
 */
public class LoanDAO {

    /**
     * Submits a new loan application.
     */
    public boolean applyLoan(int accountId, double amount) {
        String sql = "INSERT INTO loan (account_id, amount, status) VALUES (?, ?, 'PENDING')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ps.setDouble(2, amount);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns all loans for a specific account.
     */
    public List<Loan> getLoansByAccount(int accountId) {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loan WHERE account_id = ? ORDER BY loan_id DESC";
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
     * Returns ALL loan applications (Admin view).
     */
    public List<Loan> getAllLoans() {
        List<Loan> list = new ArrayList<>();
        String sql = "SELECT * FROM loan ORDER BY loan_id DESC";
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
     * Updates a loan status (APPROVED or REJECTED) — Admin action.
     */
    public boolean updateLoanStatus(int loanId, String status) {
        String sql = "UPDATE loan SET status = ? WHERE loan_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, loanId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns count of PENDING loans.
     */
    public int getPendingLoanCount() {
        String sql = "SELECT COUNT(*) FROM loan WHERE status = 'PENDING'";
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
    private Loan mapRow(ResultSet rs) throws SQLException {
        return new Loan(
            rs.getInt("loan_id"),
            rs.getInt("account_id"),
            rs.getDouble("amount"),
            rs.getString("status")
        );
    }
}
