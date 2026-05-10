import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 * -----------------
 * Handles the MySQL database connection using JDBC.
 * Compatible with MySQL Connector 9.x
 *
 * OOP Concept: Encapsulation, Static methods
 */
public class DBConnection {

    // ── Database credentials ─────────────────────────────────────────────────
    private static final String URL      = "jdbc:mysql://localhost:3306/bank_management_system";
    private static final String USER     = "root";
    private static final String PASSWORD = "radhna@iba#01";  // ← PUT YOUR MYSQL PASSWORD HERE

    private static Connection connection = null;

    private DBConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Manually register the MySQL driver — works for all versions
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
