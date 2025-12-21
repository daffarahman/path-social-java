package madebydap.pathsocial.data;

import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.model.User;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Kelas singleton untuk menyimpan dan mengelola data aplikasi.
 * Menggunakan PersistenceManager untuk menyimpan data ke file JSON.
 * Mendukung sinkronisasi real-time antar instance menggunakan FileWatcher.
 * 
 * @author madebydap
 * @version 1.0
 */
public class DataStore {
    
    /** Instance singleton */
    private static DataStore instance;

    /** Map pengguna dengan ID sebagai key */
    private Map<String, User> users;
    
    /** Daftar semua moment */
    private List<Moment> moments;
    
    /** Pengguna yang sedang login */
    private User currentUser;
    
    /** Manager untuk persistensi data ke file */
    private final PersistenceManager persistence;
    
    /** Executor untuk thread file watcher */
    private ScheduledExecutorService fileWatcher;
    
    /** Daftar listener yang dipanggil saat ada perubahan eksternal */
    private final List<Runnable> changeListeners = new ArrayList<>();
    
    /** Interval pengecekan perubahan file dalam milidetik */
    private static final int SYNC_INTERVAL_MS = 2000;

    /**
     * Konstruktor private untuk singleton pattern.
     * Memuat data yang ada dan memulai file watcher.
     */
    private DataStore() {
        users = new HashMap<>();
        moments = new ArrayList<>();
        persistence = new PersistenceManager();
        
        loadData();
        startFileWatcher();
    }

    /**
     * Mengambil instance singleton DataStore.
     * Thread-safe dengan synchronized.
     * 
     * @return instance DataStore
     */
    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    /**
     * Menghapus semua data tersimpan dan reset ke kondisi awal.
     * Membuat ulang sample users setelah penghapusan.
     */
    public void clearAllData() {
        persistence.clearAllData();
        users = new HashMap<>();
        moments = new ArrayList<>();
        currentUser = null;
        createSampleData();
        saveData();
    }

    /**
     * Menambahkan listener yang dipanggil saat ada perubahan data eksternal.
     * Digunakan untuk refresh UI saat instance lain mengubah data.
     * 
     * @param listener callback yang akan dipanggil
     */
    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    /**
     * Menghapus listener dari daftar.
     * 
     * @param listener listener yang akan dihapus
     */
    public void removeChangeListener(Runnable listener) {
        changeListeners.remove(listener);
    }

    /**
     * Memanggil semua listener yang terdaftar di EDT (Event Dispatch Thread).
     */
    private void notifyChangeListeners() {
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : changeListeners) {
                listener.run();
            }
        });
    }

    /**
     * Memulai thread file watcher untuk mendeteksi perubahan eksternal.
     * Thread berjalan sebagai daemon dan mengecek setiap SYNC_INTERVAL_MS.
     */
    private void startFileWatcher() {
        fileWatcher = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "FileWatcher");
            t.setDaemon(true);
            return t;
        });

        fileWatcher.scheduleAtFixedRate(() -> {
            if (persistence.hasExternalChanges()) {
                loadDataFromFile();
                notifyChangeListeners();
            }
        }, SYNC_INTERVAL_MS, SYNC_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Menghentikan thread file watcher.
     */
    public void stopFileWatcher() {
        if (fileWatcher != null && !fileWatcher.isShutdown()) {
            fileWatcher.shutdown();
        }
    }

    /**
     * Memuat data dari file. Jika tidak ada, buat sample data.
     */
    private void loadData() {
        PersistenceManager.LoadResult result = persistence.load();
        
        if (result.users.isEmpty()) {
            createSampleData();
            saveData();
        } else {
            this.users = result.users;
            this.moments = result.moments;
        }
    }

    /**
     * Memuat ulang data dari file tanpa menghilangkan current user.
     */
    private void loadDataFromFile() {
        PersistenceManager.LoadResult result = persistence.load();
        
        String currentUserId = currentUser != null ? currentUser.getId() : null;
        
        this.users = result.users;
        this.moments = result.moments;
        
        if (currentUserId != null && users.containsKey(currentUserId)) {
            this.currentUser = users.get(currentUserId);
        }
    }

    /**
     * Menyimpan data ke file.
     */
    private void saveData() {
        persistence.save(users, moments);
    }

    /**
     * Membuat data sample untuk testing.
     * Membuat 3 user (alice, bob, charlie) dengan password "password".
     */
    private void createSampleData() {
        User alice = new User("alice", "password", "Alice Johnson");
        User bob = new User("bob", "password", "Bob Smith");
        User charlie = new User("charlie", "password", "Charlie Brown");

        users.put(alice.getId(), alice);
        users.put(bob.getId(), bob);
        users.put(charlie.getId(), charlie);

        alice.addFriend(bob.getId());
        bob.addFriend(alice.getId());

        moments.add(new Moment(alice.getId(), MomentType.AWAKE, "Jakarta"));
        moments.add(new Moment(bob.getId(), MomentType.MUSIC, "Smooth Criminal"));
    }

    // ==================== Operasi Pengguna ====================

    /**
     * Mendaftarkan pengguna baru.
     * 
     * @param username username untuk login (harus unik)
     * @param password password pengguna
     * @param displayName nama tampilan
     * @return User yang baru dibuat, atau null jika username sudah ada
     */
    public User register(String username, String password, String displayName) {
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

    /**
     * Melakukan login pengguna.
     * 
     * @param username username pengguna
     * @param password password pengguna
     * @return User jika login berhasil, null jika gagal
     */
    public User login(String username, String password) {
        for (User user : users.values()) {
            if (user.authenticate(username, password)) {
                currentUser = user;
                return user;
            }
        }
        return null;
    }

    /**
     * Logout pengguna saat ini.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Mengambil pengguna yang sedang login.
     * 
     * @return User yang sedang login, atau null jika tidak ada
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Mengambil pengguna berdasarkan ID.
     * 
     * @param id ID pengguna
     * @return User dengan ID tersebut, atau null jika tidak ditemukan
     */
    public User getUserById(String id) {
        return users.get(id);
    }

    /**
     * Mencari pengguna berdasarkan username atau display name.
     * Tidak termasuk pengguna yang sedang login.
     * 
     * @param query kata kunci pencarian
     * @return daftar pengguna yang cocok
     */
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

    /**
     * Menambahkan pertemanan antara dua pengguna (mutual).
     * Otomatis membuat moment FRIENDSHIP.
     * 
     * @param userId ID pengguna pertama
     * @param friendId ID pengguna kedua
     * @return true jika berhasil, false jika gagal
     */
    public boolean addFriend(String userId, String friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        
        if (user == null || friend == null) return false;
        if (user.isFriend(friendId)) return false;
        if (!user.canAddFriend() || !friend.canAddFriend()) return false;
        
        user.addFriend(friendId);
        friend.addFriend(userId);
        
        String content = friend.getDisplayName();
        Moment friendshipMoment = new Moment(userId, MomentType.FRIENDSHIP, content);
        moments.add(0, friendshipMoment);
        
        saveData();
        return true;
    }

    // ==================== Operasi Moment ====================

    /**
     * Menambahkan moment baru.
     * Jika moment memiliki gambar, gambar akan dicopy ke folder aplikasi.
     * 
     * @param moment moment yang akan ditambahkan
     */
    public void addMoment(Moment moment) {
        if (moment.hasImage()) {
            String newPath = persistence.copyImage(moment.getImagePath());
            moment.setImagePath(newPath);
        }
        
        moments.add(0, moment);
        saveData();
    }

    /**
     * Mengambil daftar moment untuk timeline.
     * Hanya menampilkan moment dari pengguna sendiri dan teman-teman.
     * 
     * @return daftar moment yang relevan, diurutkan dari terbaru
     */
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

        timeline.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return timeline;
    }

    /**
     * Mengambil daftar moment milik pengguna tertentu.
     * 
     * @param userId ID pengguna
     * @return daftar moment pengguna tersebut, diurutkan dari terbaru
     */
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
