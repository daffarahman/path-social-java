package madebydap.pathsocial.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Represents a moment shared by a user on Path.
 * Supports optional image attachment and serialization.
 */
public class Moment {
    private final String id;
    private final String userId;
    private final MomentType type;
    private final String content;
    private final LocalDateTime timestamp;
    private String imagePath;

    // Standard constructor for new moments
    public Moment(String userId, MomentType type, String content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.imagePath = null;
    }

    public Moment(String userId, MomentType type, String content, String imagePath) {
        this(userId, type, content);
        this.imagePath = imagePath;
    }

    // Constructor for loading from persistence
    public Moment(String id, String userId, MomentType type, String content, String imagePath, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.imagePath = imagePath;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public MomentType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean hasImage() {
        return imagePath != null && !imagePath.isEmpty();
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timestamp.format(formatter);
    }

    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return timestamp.format(formatter);
    }

    public String getRelativeTime() {
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(timestamp, now).toMinutes();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + "m ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        
        long days = hours / 24;
        if (days < 7) return days + "d ago";
        
        return getFormattedDate();
    }
}
