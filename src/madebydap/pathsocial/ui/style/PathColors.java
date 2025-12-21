package madebydap.pathsocial.ui.style;

import java.awt.Color;

/**
 * Kelas yang mendefinisikan palet warna untuk aplikasi Path Social.
 * Mengikuti tema terang original Path dengan warna merah sebagai aksen utama.
 * 
 * @author madebydap
 * @version 1.0
 */
public class PathColors {
    
    /** Warna background utama (abu-abu terang) */
    public static final Color BACKGROUND = new Color(245, 245, 245);
    
    /** Warna background putih */
    public static final Color BACKGROUND_WHITE = Color.WHITE;
    
    /** Warna card */
    public static final Color CARD = Color.WHITE;
    
    /** Warna card saat hover */
    public static final Color CARD_HOVER = new Color(250, 250, 250);

    /** Warna aksen utama (merah khas Path) */
    public static final Color PRIMARY = new Color(227, 38, 54);
    
    /** Warna primary gelap */
    public static final Color PRIMARY_DARK = new Color(180, 30, 45);
    
    /** Warna primary terang */
    public static final Color PRIMARY_LIGHT = new Color(240, 80, 95);

    /** Warna teks utama (hitam) */
    public static final Color TEXT_PRIMARY = new Color(33, 33, 33);
    
    /** Warna teks sekunder (abu-abu gelap) */
    public static final Color TEXT_SECONDARY = new Color(100, 100, 100);
    
    /** Warna teks muted (abu-abu terang) */
    public static final Color TEXT_MUTED = new Color(160, 160, 160);

    /** Warna border */
    public static final Color BORDER = new Color(230, 230, 230);
    
    /** Warna divider */
    public static final Color DIVIDER = new Color(238, 238, 238);

    /** Warna status sukses (hijau) */
    public static final Color SUCCESS = new Color(76, 175, 80);
    
    /** Warna status warning (oranye) */
    public static final Color WARNING = new Color(255, 152, 0);
    
    /** Warna status error (merah) */
    public static final Color ERROR = new Color(244, 67, 54);

    /** Warna untuk moment AWAKE (oranye keemasan) */
    public static final Color AWAKE_COLOR = new Color(255, 183, 77);
    
    /** Warna untuk moment ASLEEP (ungu) */
    public static final Color ASLEEP_COLOR = new Color(126, 87, 194);
    
    /** Warna untuk moment MUSIC (pink) */
    public static final Color MUSIC_COLOR = new Color(236, 64, 122);
    
    /** Warna untuk moment PHOTO (biru) */
    public static final Color PHOTO_COLOR = new Color(66, 165, 245);
    
    /** Warna untuk moment LOCATION (hijau) */
    public static final Color LOCATION_COLOR = new Color(102, 187, 106);
    
    /** Warna untuk moment THOUGHT (cyan) */
    public static final Color THOUGHT_COLOR = new Color(38, 198, 218);
    
    /** Warna untuk moment FRIENDSHIP (ungu muda) */
    public static final Color FRIENDSHIP_COLOR = new Color(171, 71, 188);

    /**
     * Konstruktor private untuk mencegah instansiasi.
     */
    private PathColors() {}

    /**
     * Mengambil warna berdasarkan tipe moment.
     * 
     * @param type tipe moment
     * @return warna yang sesuai dengan tipe moment
     */
    public static Color getMomentTypeColor(madebydap.pathsocial.model.MomentType type) {
        switch (type) {
            case AWAKE: return AWAKE_COLOR;
            case ASLEEP: return ASLEEP_COLOR;
            case MUSIC: return MUSIC_COLOR;
            case PHOTO: return PHOTO_COLOR;
            case LOCATION: return LOCATION_COLOR;
            case THOUGHT: return THOUGHT_COLOR;
            case FRIENDSHIP: return FRIENDSHIP_COLOR;
            default: return PRIMARY;
        }
    }
}
