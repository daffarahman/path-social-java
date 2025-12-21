package madebydap.pathsocial.model;

/**
 * Enum yang mendefinisikan tipe-tipe moment yang tersedia di Path Social.
 * Setiap tipe memiliki icon, nama tampilan, dan prefix untuk ditampilkan di UI.
 * 
 * @author madebydap
 * @version 1.0
 */
public enum MomentType {
    
    /** Moment foto dengan kamera */
    PHOTO("camera", "Photo", "Shared a photo"),
    
    /** Moment musik yang sedang didengarkan */
    MUSIC("music", "Music", "Listening to"),
    
    /** Moment pemikiran atau status */
    THOUGHT("thought", "Thought", "Thinking"),
    
    /** Moment lokasi atau check-in */
    LOCATION("location", "Location", "At"),
    
    /** Moment bangun tidur */
    AWAKE("awake", "Awake", "Awake in"),
    
    /** Moment tidur */
    ASLEEP("asleep", "Asleep", "Sleeping in"),
    
    /** Moment pertemanan baru (tidak bisa dibuat manual) */
    FRIENDSHIP("friends", "Friendship", "Is now friends with");

    /** ID icon untuk tipe ini */
    private final String iconId;
    
    /** Nama tampilan tipe */
    private final String displayName;
    
    /** Prefix yang ditampilkan sebelum konten */
    private final String prefix;

    /**
     * Konstruktor untuk MomentType.
     * 
     * @param iconId ID icon yang digunakan
     * @param displayName nama yang ditampilkan di UI
     * @param prefix teks yang muncul sebelum konten moment
     */
    MomentType(String iconId, String displayName, String prefix) {
        this.iconId = iconId;
        this.displayName = displayName;
        this.prefix = prefix;
    }

    /**
     * Mengambil ID icon.
     * @return ID icon untuk tipe ini
     */
    public String getIconId() {
        return iconId;
    }

    /**
     * Mengambil nama tampilan.
     * @return nama yang ditampilkan di UI
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Mengambil prefix teks.
     * @return prefix yang ditampilkan sebelum konten
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Memeriksa apakah tipe ini bisa dibuat oleh pengguna.
     * FRIENDSHIP tidak bisa dibuat manual karena digenerate otomatis saat berteman.
     * 
     * @return true jika pengguna bisa membuat moment tipe ini
     */
    public boolean isUserCreatable() {
        return this != FRIENDSHIP;
    }
}
