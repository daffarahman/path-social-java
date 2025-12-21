package madebydap.pathsocial.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Kelas yang merepresentasikan pengguna di aplikasi Path Social.
 * Menyimpan informasi profil, kredensial login, dan daftar teman.
 * 
 * @author madebydap
 * @version 1.0
 */
public class User {
    
    /** Batas maksimal jumlah teman yang dapat dimiliki pengguna */
    public static final int MAX_FRIENDS = 50;

    /** ID unik pengguna */
    private final String id;
    
    /** Username untuk login */
    private final String username;
    
    /** Password pengguna */
    private final String password;
    
    /** Nama tampilan pengguna */
    private String displayName;
    
    /** Daftar ID teman pengguna */
    private final List<String> friendIds;

    /**
     * Konstruktor untuk membuat pengguna baru.
     * ID akan digenerate secara otomatis menggunakan UUID.
     * 
     * @param username username untuk login
     * @param password password pengguna
     * @param displayName nama yang ditampilkan di profil
     */
    public User(String username, String password, String displayName) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.friendIds = new ArrayList<>();
    }

    /**
     * Konstruktor untuk memuat pengguna dari penyimpanan.
     * Digunakan saat load data dari file JSON.
     * 
     * @param id ID pengguna yang sudah ada
     * @param username username pengguna
     * @param password password pengguna
     * @param displayName nama tampilan pengguna
     */
    public User(String id, String username, String password, String displayName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.friendIds = new ArrayList<>();
    }

    /**
     * Mengambil ID pengguna.
     * @return ID unik pengguna
     */
    public String getId() {
        return id;
    }

    /**
     * Mengambil username pengguna.
     * @return username untuk login
     */
    public String getUsername() {
        return username;
    }

    /**
     * Mengambil password pengguna.
     * @return password pengguna
     */
    public String getPassword() {
        return password;
    }

    /**
     * Mengambil nama tampilan pengguna.
     * @return nama yang ditampilkan di profil
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Mengubah nama tampilan pengguna.
     * @param displayName nama tampilan baru
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Mengambil inisial dari nama tampilan pengguna.
     * Digunakan untuk menampilkan avatar.
     * 
     * @return inisial pengguna (1-2 karakter)
     */
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

    /**
     * Mengambil daftar ID teman pengguna.
     * @return salinan daftar ID teman
     */
    public List<String> getFriendIds() {
        return new ArrayList<>(friendIds);
    }

    /**
     * Mengambil jumlah teman pengguna.
     * @return jumlah teman saat ini
     */
    public int getFriendCount() {
        return friendIds.size();
    }

    /**
     * Memeriksa apakah pengguna masih bisa menambah teman.
     * @return true jika jumlah teman belum mencapai batas maksimal
     */
    public boolean canAddFriend() {
        return friendIds.size() < MAX_FRIENDS;
    }

    /**
     * Memeriksa apakah pengguna tertentu adalah teman.
     * @param userId ID pengguna yang dicek
     * @return true jika pengguna tersebut adalah teman
     */
    public boolean isFriend(String userId) {
        return friendIds.contains(userId);
    }

    /**
     * Menambahkan teman baru.
     * 
     * @param userId ID pengguna yang akan ditambahkan sebagai teman
     * @return true jika berhasil ditambahkan, false jika gagal
     */
    public boolean addFriend(String userId) {
        if (canAddFriend() && !isFriend(userId) && !userId.equals(this.id)) {
            friendIds.add(userId);
            return true;
        }
        return false;
    }

    /**
     * Menghapus teman dari daftar.
     * @param userId ID teman yang akan dihapus
     */
    public void removeFriend(String userId) {
        friendIds.remove(userId);
    }

    /**
     * Memvalidasi kredensial login pengguna.
     * 
     * @param username username yang dimasukkan
     * @param password password yang dimasukkan
     * @return true jika kredensial valid
     */
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
