package madebydap.pathsocial.data;

import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.model.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Singleton data store for the application.
 * Stores users and moments in memory.
 */
public class DataStore {
    private static DataStore instance;

    private final Map<String, User> users;
    private final List<Moment> moments;
    private User currentUser;

    private DataStore() {
        this.users = new HashMap<>();
        this.moments = new ArrayList<>();
        initializeSampleData();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private void initializeSampleData() {
        // Create sample users
        User alice = new User("alice", "password", "Alice Johnson");
        alice.setBio("Living my best life ✨");
        users.put(alice.getId(), alice);

        User bob = new User("bob", "password", "Bob Smith");
        bob.setBio("Coffee enthusiast ☕");
        users.put(bob.getId(), bob);

        User charlie = new User("charlie", "password", "Charlie Brown");
        charlie.setBio("Music lover 🎵");
        users.put(charlie.getId(), charlie);

        // Make them friends
        alice.addFriend(bob.getId());
        alice.addFriend(charlie.getId());
        bob.addFriend(alice.getId());
        charlie.addFriend(alice.getId());

        // Create sample moments
        moments.add(new Moment(alice.getId(), MomentType.AWAKE, "Good morning everyone!"));
        moments.add(new Moment(bob.getId(), MomentType.MUSIC, "Bohemian Rhapsody - Queen"));
        moments.add(new Moment(charlie.getId(), MomentType.LOCATION, "Central Park, NYC"));
        moments.add(new Moment(alice.getId(), MomentType.THOUGHT, "What a beautiful day!"));
        moments.add(new Moment(bob.getId(), MomentType.PHOTO, "Sunset at the beach"));
    }

    // User operations
    public User registerUser(String username, String password, String displayName) {
        // Check if username exists
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return null;
            }
        }
        User newUser = new User(username, password, displayName);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    public User login(String username, String password) {
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username) && user.checkPassword(password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    public User getUserByUsername(String username) {
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<User> searchUsers(String query) {
        String lowerQuery = query.toLowerCase();
        return users.values().stream()
                .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) ||
                        u.getDisplayName().toLowerCase().contains(lowerQuery))
                .filter(u -> !u.getId().equals(currentUser != null ? currentUser.getId() : ""))
                .collect(Collectors.toList());
    }

    // Moment operations
    public void addMoment(Moment moment) {
        moments.add(0, moment);
    }

    public List<Moment> getTimelineMoments() {
        if (currentUser == null) return new ArrayList<>();

        List<String> visibleUserIds = new ArrayList<>(currentUser.getFriendIds());
        visibleUserIds.add(currentUser.getId());

        return moments.stream()
                .filter(m -> visibleUserIds.contains(m.getUserId()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .collect(Collectors.toList());
    }

    public List<Moment> getUserMoments(String userId) {
        return moments.stream()
                .filter(m -> m.getUserId().equals(userId))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .collect(Collectors.toList());
    }
}
