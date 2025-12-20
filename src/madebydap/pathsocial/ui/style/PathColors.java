package madebydap.pathsocial.ui.style;

import java.awt.Color;

/**
 * Color palette for Path clone - matching original Path's light theme.
 */
public class PathColors {
    // Background colors (Light theme like original Path)
    public static final Color BACKGROUND = new Color(245, 245, 245);
    public static final Color BACKGROUND_WHITE = Color.WHITE;
    public static final Color CARD = Color.WHITE;
    public static final Color CARD_HOVER = new Color(250, 250, 250);

    // Primary accent - Path's signature red
    public static final Color PRIMARY = new Color(227, 38, 54);
    public static final Color PRIMARY_DARK = new Color(180, 30, 45);
    public static final Color PRIMARY_LIGHT = new Color(240, 80, 95);

    // Text colors
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    public static final Color TEXT_SECONDARY = new Color(100, 100, 100);
    public static final Color TEXT_MUTED = new Color(160, 160, 160);

    // Border and divider
    public static final Color BORDER = new Color(230, 230, 230);
    public static final Color DIVIDER = new Color(238, 238, 238);

    // Status colors
    public static final Color SUCCESS = new Color(76, 175, 80);
    public static final Color WARNING = new Color(255, 152, 0);
    public static final Color ERROR = new Color(244, 67, 54);

    // Moment type colors (softer, more natural)
    public static final Color AWAKE_COLOR = new Color(255, 183, 77);
    public static final Color ASLEEP_COLOR = new Color(126, 87, 194);
    public static final Color MUSIC_COLOR = new Color(236, 64, 122);
    public static final Color PHOTO_COLOR = new Color(66, 165, 245);
    public static final Color LOCATION_COLOR = new Color(102, 187, 106);
    public static final Color THOUGHT_COLOR = new Color(38, 198, 218);

    private PathColors() {}

    public static Color getMomentTypeColor(madebydap.pathsocial.model.MomentType type) {
        switch (type) {
            case AWAKE: return AWAKE_COLOR;
            case ASLEEP: return ASLEEP_COLOR;
            case MUSIC: return MUSIC_COLOR;
            case PHOTO: return PHOTO_COLOR;
            case LOCATION: return LOCATION_COLOR;
            case THOUGHT: return THOUGHT_COLOR;
            default: return PRIMARY;
        }
    }
}
