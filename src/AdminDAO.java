import java.sql.*;

/**
 * AdminDAO.java
 * -------------
 * Handles Admin authentication database operations.
 *
 * OOP Concepts: Encapsulation, Abstraction
 */
public class AdminDAO {

    /**
     * Authenticates an admin by username and password.
     * Returns Admin object on success, null on failure.
     */
    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Admin(
                    rs.getInt("admin_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
