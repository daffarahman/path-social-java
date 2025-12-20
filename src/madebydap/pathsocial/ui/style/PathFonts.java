package madebydap.pathsocial.ui.style;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * Font definitions for Path clone.
 */
public class PathFonts {
    private static final String FONT_FAMILY = getAvailableFont();

    public static final Font TITLE_LARGE = new Font(FONT_FAMILY, Font.BOLD, 28);
    public static final Font TITLE = new Font(FONT_FAMILY, Font.BOLD, 22);
    public static final Font SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, 16);
    public static final Font BODY = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font SMALL_BOLD = new Font(FONT_FAMILY, Font.BOLD, 12);
    public static final Font ICON = new Font("Segoe UI Emoji", Font.PLAIN, 20);
    public static final Font ICON_LARGE = new Font("Segoe UI Emoji", Font.PLAIN, 32);

    private PathFonts() {}

    private static String getAvailableFont() {
        String[] preferredFonts = {"Segoe UI", "Roboto", "Arial", "SansSerif"};
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();

        for (String preferred : preferredFonts) {
            for (String available : availableFonts) {
                if (available.equalsIgnoreCase(preferred)) {
                    return available;
                }
            }
        }
        return "SansSerif";
    }
}
