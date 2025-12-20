package madebydap.pathsocial.model;

/**
 * Enum representing the types of moments that can be shared on Path.
 * Each moment type has an icon symbol and a display name.
 */
public enum MomentType {
    AWAKE("☀", "Awake", "Just woke up"),
    ASLEEP("🌙", "Asleep", "Going to sleep"),
    MUSIC("♪", "Music", "Listening to"),
    PHOTO("📷", "Photo", "Shared a photo"),
    LOCATION("📍", "Location", "Checked in at"),
    THOUGHT("💭", "Thought", "Thinking about");

    private final String icon;
    private final String displayName;
    private final String prefix;

    MomentType(String icon, String displayName, String prefix) {
        this.icon = icon;
        this.displayName = displayName;
        this.prefix = prefix;
    }

    public String getIcon() {
        return icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }
}
