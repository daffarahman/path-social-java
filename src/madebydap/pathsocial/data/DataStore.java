package madebydap.pathsocial.data;

import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.model.User;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Singleton data store with persistence and real-time sync.
 * Uses FileWatcher thread for multi-instance synchronization.
 */
public class DataStore {
    private static DataStore instance;

    private Map<String, User> users;
    private List<Moment> moments;
    private User currentUser;
    
    private final PersistenceManager persistence;
    private ScheduledExecutorService fileWatcher;
    private final List<Runnable> changeListeners = new ArrayList<>();
    
    private static final int SYNC_INTERVAL_MS = 2000;

    private DataStore() {
        users = new HashMap<>();
        moments = new ArrayList<>();
        persistence = new PersistenceManager();
        
        // Load existing data
        loadData();
        
        // Start file watcher for real-time sync
        startFileWatcher();
    }

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Add listener to be notified when external changes are detected.
     */
    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    private void notifyChangeListeners() {
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : changeListeners) {
                listener.run();
            }
        });
    }

    /**
     * Start the file watcher thread for real-time sync.
     */
    private void startFileWatcher() {
        fileWatcher = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "FileWatcher");
            t.setDaemon(true);
            return t;
        });

        fileWatcher.scheduleAtFixedRate(() -> {
            if (persistence.hasExternalChanges()) {
                System.out.println("[FileWatcher] External changes detected, reloading...");
                loadDataFromFile();
                notifyChangeListeners();
            }
        }, SYNC_INTERVAL_MS, SYNC_INTERVAL_MS, TimeUnit.MILLISECONDS);

        System.out.println("[FileWatcher] Started - checking every " + SYNC_INTERVAL_MS + "ms");
    }

    public void stopFileWatcher() {
        if (fileWatcher != null && !fileWatcher.isShutdown()) {
            fileWatcher.shutdown();
            System.out.println("[FileWatcher] Stopped");
        }
    }

    private void loadData() {
        PersistenceManager.LoadResult result = persistence.load();
        
        if (result.users.isEmpty()) {
            // Create sample users only if no data exists
            createSampleData();
            saveData();
        } else {
            this.users = result.users;
            this.moments = result.moments;
        }
    }

    private void loadDataFromFile() {
        PersistenceManager.LoadResult result = persistence.load();
        
        // Preserve current user reference
        String currentUserId = currentUser != null ? currentUser.getId() : null;
        
        this.users = result.users;
        this.moments = result.moments;
        
        // Restore current user
        if (currentUserId != null && users.containsKey(currentUserId)) {
            this.currentUser = users.get(currentUserId);
        }
    }

    private void saveData() {
        persistence.save(users, moments);
    }

    private void createSampleData() {
        // Sample users
        User alice = new User("alice", "password", "Alice Johnson");
        User bob = new User("bob", "password", "Bob Smith");
        User charlie = new User("charlie", "password", "Charlie Brown");

        users.put(alice.getId(), alice);
        users.put(bob.getId(), bob);
        users.put(charlie.getId(), charlie);

        // Make them friends
        alice.addFriend(bob.getId());
        bob.addFriend(alice.getId());

        // Sample moments
        moments.add(new Moment(alice.getId(), MomentType.AWAKE, "Jakarta"));
        moments.add(new Moment(bob.getId(), MomentType.MUSIC, "Smooth Criminal"));
    }

    // ==================== User Operations ====================

    public User register(String username, String password, String displayName) {
        // Check if username exists
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return null;
            }
        }

        User newUser = new User(username, password, displayName);
        users.put(newUser.getId(), newUser);
        saveData();
        return newUser;
    }

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

    public User getUserById(String id) {
        return users.get(id);
    }

    public List<User> searchUsers(String query) {
        List<User> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (User user : users.values()) {
            if (currentUser != null && user.getId().equals(currentUser.getId())) {
                continue;
            }
            if (user.getUsername().toLowerCase().contains(lowerQuery) ||
                user.getDisplayName().toLowerCase().contains(lowerQuery)) {
                results.add(user);
            }
        }
        return results;
    }

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
        
        saveData();
        return true;
    }

    // ==================== Moment Operations ====================

    public void addMoment(Moment moment) {
        // Copy image to app directory if present
        if (moment.hasImage()) {
            String newPath = persistence.copyImage(moment.getImagePath());
            moment.setImagePath(newPath);
        }
        
        moments.add(0, moment);
        saveData();
    }

    public List<Moment> getTimelineMoments() {
        if (currentUser == null) return new ArrayList<>();

        List<Moment> timeline = new ArrayList<>();
        Set<String> visibleUserIds = new HashSet<>();
        visibleUserIds.add(currentUser.getId());
        visibleUserIds.addAll(currentUser.getFriendIds());

        for (Moment moment : moments) {
            if (visibleUserIds.contains(moment.getUserId())) {
                timeline.add(moment);
            }
        }

        // Sort by timestamp (newest first)
        timeline.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return timeline;
    }

    public List<Moment> getUserMoments(String userId) {
        List<Moment> userMoments = new ArrayList<>();
        for (Moment moment : moments) {
            if (moment.getUserId().equals(userId)) {
                userMoments.add(moment);
            }
        }
        userMoments.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return userMoments;
    }
}
