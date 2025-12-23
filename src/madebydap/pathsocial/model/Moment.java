package madebydap.pathsocial.model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Kelas yang merepresentasikan sebuah moment/postingan di Path Social.
 * Moment adalah konten yang dibagikan pengguna seperti foto, musik, lokasi, dll.
 * 
 * @author madebydap
 * @version 1.0
 */
public class Moment {
    
    /** ID unik moment */
    private final String id;
    
    /** ID pengguna yang membuat moment */
    private final String userId;
    
    /** Tipe moment (PHOTO, MUSIC, THOUGHT, dll) */
    private final MomentType type;
    
    /** Konten teks dari moment */
    private final String content;
    
    /** Waktu pembuatan moment */
    private final LocalDateTime timestamp;
    
    /** Path ke file gambar (untuk moment tipe PHOTO) */
    private String imagePath;

    /**
     * Konstruktor untuk membuat moment baru tanpa gambar.
     * 
     * @param userId ID pengguna yang membuat moment
     * @param type tipe moment
     * @param content konten teks moment
     */
    public Moment(String userId, MomentType type, String content) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.imagePath = null;
    }

    /**
     * Konstruktor untuk membuat moment baru dengan gambar.
     * 
     * @param userId ID pengguna yang membuat moment
     * @param type tipe moment
     * @param content konten teks moment
     * @param imagePath path ke file gambar
     */
    public Moment(String userId, MomentType type, String content, String imagePath) {
        this(userId, type, content);
        this.imagePath = imagePath;
    }

    /**
     * Konstruktor untuk memuat moment dari penyimpanan.
     * Digunakan saat load data dari file JSON.
     * 
     * @param id ID moment yang sudah ada
     * @param userId ID pengguna pembuat
     * @param type tipe moment
     * @param content konten teks
     * @param imagePath path gambar (bisa null)
     * @param timestamp waktu pembuatan
     */
    public Moment(String id, String userId, MomentType type, String content, String imagePath, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.imagePath = imagePath;
        this.timestamp = timestamp;
    }

    /**
     * Mengambil ID moment.
     * @return ID unik moment
     */
    public String getId() {
        return id;
    }

    /**
     * Mengambil ID pengguna pembuat moment.
     * @return ID pengguna
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Mengambil tipe moment.
     * @return tipe moment
     */
    public MomentType getType() {
        return type;
    }

    /**
     * Mengambil konten teks moment.
     * @return konten moment
     */
    public String getContent() {
        return content;
    }

    /**
     * Mengambil waktu pembuatan moment.
     * @return timestamp pembuatan
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Mengambil path file gambar.
     * @return path gambar atau null jika tidak ada
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Mengatur path file gambar.
     * @param imagePath path gambar baru
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Memeriksa apakah moment memiliki gambar.
     * @return true jika moment memiliki gambar
     */
    public boolean hasImage() {
        return imagePath != null && !imagePath.isEmpty();
    }

    /**
     * Mengambil waktu dalam format jam:menit.
     * @return waktu terformat (contoh: "14:30")
     */
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return timestamp.format(formatter);
    }

    /**
     * Mengambil tanggal dalam format lengkap.
     * @return tanggal terformat (contoh: "21 Dec 2024")
     */
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        return timestamp.format(formatter);
    }

    /**
     * Mengambil waktu relatif dari sekarang.
     * Contoh: "5m ago", "2h ago", "3d ago"
     * 
     * @return waktu relatif dalam format yang mudah dibaca
     */
    public String getRelativeTime() {
        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(timestamp, now).toMinutes();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + "m ago";
        
        long hours = minutes / 60;
        if (hours < 24) return hours + "h ago";
        
        long days = hours / 24;
        if (days < 7) return days + "d ago";
        
        return getFormattedDate();
    }
}
