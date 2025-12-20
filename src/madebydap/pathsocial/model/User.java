package madebydap.pathsocial.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user on Path.
 * Implements Dunbar's Number by limiting friends to 50.
 */
public class User {
    public static final int MAX_FRIENDS = 50;

    private final String id;
    private final String username;
    private String password;
    private String displayName;
    private String bio;
    private final List<String> friendIds;

    public User(String username, String password, String displayName) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.bio = "";
        this.friendIds = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public boolean authenticate(String username, String password) {
        return this.username.equalsIgnoreCase(username) && this.password.equals(password);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public boolean addFriend(String friendId) {
        if (!canAddFriend()) {
            return false;
        }
        if (friendIds.contains(friendId)) {
            return false;
        }
        if (friendId.equals(this.id)) {
            return false;
        }
        friendIds.add(friendId);
        return true;
    }

    public boolean removeFriend(String friendId) {
        return friendIds.remove(friendId);
    }

    public boolean isFriend(String userId) {
        return friendIds.contains(userId);
    }

    public String getInitials() {
        if (displayName == null || displayName.isEmpty()) {
            return username.substring(0, 1).toUpperCase();
        }
        String[] parts = displayName.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
        }
        return displayName.substring(0, 1).toUpperCase();
    }
}
