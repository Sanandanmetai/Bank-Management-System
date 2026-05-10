import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * UITheme.java
 * ------------
 * Centralized design constants for a modern, minimalistic banking UI.
 * All colors, fonts, and shared styling helpers live here.
 *
 * OOP Concepts: Static constants, Utility class
 */
public class UITheme {

    // ── Color Palette ─────────────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(13,  17,  23);   // near-black
    public static final Color BG_CARD      = new Color(22,  27,  34);   // card background
    public static final Color BG_INPUT     = new Color(33,  38,  45);   // input fields
    public static final Color ACCENT       = new Color(0,  168, 107);   // emerald green
    public static final Color ACCENT_HOVER = new Color(0,  140,  88);
    public static final Color TEXT_PRIMARY = new Color(230, 237, 243);  // almost white
    public static final Color TEXT_MUTED   = new Color(139, 148, 158);  // grey
    public static final Color BORDER       = new Color(48,  54,  61);
    public static final Color DANGER       = new Color(218,  54,  51);  // red for errors
    public static final Color WARNING      = new Color(210, 153,  34);  // amber for pending

    // ── Fonts ─────────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD,  26);
    public static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD,  18);
    public static final Font FONT_BODY    = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_MONO    = new Font("Monospaced", Font.PLAIN, 13);
    public static final Font FONT_LABEL   = new Font("SansSerif", Font.BOLD,  12);

    // ── Border radius emulation via padding ───────────────────────────────────
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(20, 24, 20, 24)
        );
    }

    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(8, 12, 8, 12)
        );
    }

    // ── Styled Components ─────────────────────────────────────────────────────

    /** Returns a styled primary button (green accent). */
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT);
            }
        });
        return btn;
    }

    /** Returns a styled danger (red) button. */
    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(DANGER);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(10, 24, 10, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    /** Returns a styled text field. */
    public static JTextField styledField() {
        JTextField field = new JTextField();
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(FONT_BODY);
        field.setBorder(inputBorder());
        return field;
    }

    /** Returns a styled password field. */
    public static JPasswordField styledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(FONT_BODY);
        field.setBorder(inputBorder());
        return field;
    }

    /** Returns a styled label (muted). */
    public static JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(FONT_LABEL);
        return lbl;
    }

    /** Sets up a dark JFrame with standard settings. */
    public static void setupFrame(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG_DARK);
    }

    /** Sets up the global Swing look-and-feel defaults. */
    public static void applyGlobalTheme() {
        UIManager.put("Panel.background",          BG_DARK);
        UIManager.put("Label.foreground",          TEXT_PRIMARY);
        UIManager.put("TextField.background",      BG_INPUT);
        UIManager.put("TextField.foreground",      TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
        UIManager.put("PasswordField.background",  BG_INPUT);
        UIManager.put("PasswordField.foreground",  TEXT_PRIMARY);
        UIManager.put("Table.background",          BG_CARD);
        UIManager.put("Table.foreground",          TEXT_PRIMARY);
        UIManager.put("Table.gridColor",           BORDER);
        UIManager.put("TableHeader.background",    BG_INPUT);
        UIManager.put("TableHeader.foreground",    TEXT_MUTED);
        UIManager.put("ScrollPane.background",     BG_DARK);
        UIManager.put("Viewport.background",       BG_DARK);
        UIManager.put("OptionPane.background",     BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
    }
}
