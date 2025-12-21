package madebydap.pathsocial.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user on the Path social network with serialization support.
 */
public class User {
    public static final int MAX_FRIENDS = 50;

    private final String id;
    private final String username;
    private final String password;
    private String displayName;
    private final List<String> friendIds;

    // Standard constructor for new users
    public User(String username, String password, String displayName) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.friendIds = new ArrayList<>();
    }

    // Constructor for loading from persistence
    public User(String id, String username, String password, String displayName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.friendIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInitials() {
        if (displayName == null || displayName.isEmpty()) {
            return username.substring(0, 1).toUpperCase();
        }
        String[] parts = displayName.trim().split("\\s+");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
        }
        return displayName.substring(0, 1).toUpperCase();
    }

    public List<String> getFriendIds() {
        return new ArrayList<>(friendIds);
    }

    public int getFriendCount() {
        return friendIds.size();
    }

    public boolean canAddFriend() {
        return friendIds.size() < MAX_FRIENDS;
    }

    public boolean isFriend(String userId) {
        return friendIds.contains(userId);
    }

    public boolean addFriend(String userId) {
        if (canAddFriend() && !isFriend(userId) && !userId.equals(this.id)) {
            friendIds.add(userId);
            return true;
        }
        return false;
    }

    public void removeFriend(String userId) {
        friendIds.remove(userId);
    }

    public boolean authenticate(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
