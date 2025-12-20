package madebydap.pathsocial.data;

import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.model.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory data store for Path clone.
 * Manages users, moments, and authentication.
 */
public class DataStore {
    private static DataStore instance;
    
    private final Map<String, User> users = new HashMap<>();
    private final List<Moment> moments = new ArrayList<>();
    private User currentUser;

    private DataStore() {
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
        bob.setBio("Coffee enthusiast & developer");
        users.put(bob.getId(), bob);

        User charlie = new User("charlie", "password", "Charlie Brown");
        charlie.setBio("Music lover");
        users.put(charlie.getId(), charlie);

        // Make them friends (mutual)
        alice.addFriend(bob.getId());
        bob.addFriend(alice.getId());
        alice.addFriend(charlie.getId());
        charlie.addFriend(alice.getId());

        // Add sample moments
        moments.add(new Moment(alice.getId(), MomentType.AWAKE, "Ready for a productive day!"));
        moments.add(new Moment(bob.getId(), MomentType.MUSIC, "Bohemian Rhapsody - Queen"));
        moments.add(new Moment(charlie.getId(), MomentType.LOCATION, "Central Park, NYC"));
        moments.add(new Moment(alice.getId(), MomentType.THOUGHT, "The weather is beautiful today"));
    }

    // Authentication
    public User login(String username, String password) {
        for (User user : users.values()) {
            if (user.authenticate(username, password)) {
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

    // User queries
    public User getUserById(String id) {
        return users.get(id);
    }

    public List<User> searchUsers(String query) {
        String lowerQuery = query.toLowerCase();
        return users.values().stream()
                .filter(u -> !u.getId().equals(currentUser.getId()))
                .filter(u -> u.getUsername().toLowerCase().contains(lowerQuery) ||
                           u.getDisplayName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    // Friendship with mutual logic
    public boolean addFriend(String userId, String friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        
        if (user == null || friend == null) return false;
        if (user.isFriend(friendId)) return false;
        if (!user.canAddFriend() || !friend.canAddFriend()) return false;
        
        // Add mutual friendship
        user.addFriend(friendId);
        friend.addFriend(userId);
        
        // Create friendship moment
        String content = friend.getDisplayName();
        Moment friendshipMoment = new Moment(userId, MomentType.FRIENDSHIP, content);
        moments.add(0, friendshipMoment);
        
        return true;
    }

    // Moments
    public void addMoment(Moment moment) {
        moments.add(0, moment);
    }

    public List<Moment> getTimelineMoments() {
        if (currentUser == null) return Collections.emptyList();

        Set<String> visibleUsers = new HashSet<>();
        visibleUsers.add(currentUser.getId());
        visibleUsers.addAll(currentUser.getFriendIds());

        return moments.stream()
                .filter(m -> visibleUsers.contains(m.getUserId()))
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
