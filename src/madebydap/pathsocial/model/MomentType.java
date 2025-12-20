package madebydap.pathsocial.model;

/**
 * Enum representing the types of moments that can be shared on Path.
 */
public enum MomentType {
    AWAKE("sun", "Awake", "Awake in"),
    ASLEEP("moon", "Asleep", "Sleeping in"),
    MUSIC("music", "Music", "Listening to"),
    PHOTO("camera", "Photo", "Shared a photo"),
    LOCATION("location", "Location", "At"),
    THOUGHT("chat", "Thought", "Thinking about"),
    FRIENDSHIP("friends", "Friendship", "Is now friends with");

    private final String iconName;
    private final String displayName;
    private final String prefix;

    MomentType(String iconName, String displayName, String prefix) {
        this.iconName = iconName;
        this.displayName = displayName;
        this.prefix = prefix;
    }

    public String getIconName() {
        return iconName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPrefix() {
        return prefix;
    }
    
    public boolean isUserCreatable() {
        return this != FRIENDSHIP;
    }
}
