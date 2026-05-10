import javax.swing.SwingUtilities;

/**
 * Main.java
 * ---------
 * Entry point for the NexaBank Management System.
 * Applies the global UI theme and launches the Welcome Screen.
 *
 * OOP Concepts: Static main method, SwingUtilities for thread safety
 */
public class Main {

    public static void main(String[] args) {
        // Run GUI on the Event Dispatch Thread (EDT) — Swing best practice
        SwingUtilities.invokeLater(() -> {
            // Apply the global dark theme settings
            UITheme.applyGlobalTheme();

            // Launch the Welcome Screen
            new WelcomeScreen();

            System.out.println("========================================");
            System.out.println("  NexaBank Management System Started    ");
            System.out.println("  Default Admin → admin / admin123       ");
            System.out.println("========================================");
        });
    }
}
