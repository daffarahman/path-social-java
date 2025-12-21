package madebydap.pathsocial.ui.style;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * Kelas yang mendefinisikan font untuk aplikasi Path Social.
 * Mencari font yang tersedia di sistem dan menggunakan fallback jika tidak ditemukan.
 * 
 * @author madebydap
 * @version 1.0
 */
public class PathFonts {
    
    /** Font family yang digunakan */
    private static final String FONT_FAMILY = getAvailableFont();

    /** Font untuk judul besar (28pt bold) */
    public static final Font TITLE_LARGE = new Font(FONT_FAMILY, Font.BOLD, 28);
    
    /** Font untuk judul (22pt bold) */
    public static final Font TITLE = new Font(FONT_FAMILY, Font.BOLD, 22);
    
    /** Font untuk subjudul (16pt bold) */
    public static final Font SUBTITLE = new Font(FONT_FAMILY, Font.BOLD, 16);
    
    /** Font untuk body text (14pt regular) */
    public static final Font BODY = new Font(FONT_FAMILY, Font.PLAIN, 14);
    
    /** Font untuk body text bold (14pt bold) */
    public static final Font BODY_BOLD = new Font(FONT_FAMILY, Font.BOLD, 14);
    
    /** Font untuk teks kecil (12pt regular) */
    public static final Font SMALL = new Font(FONT_FAMILY, Font.PLAIN, 12);
    
    /** Font untuk teks kecil bold (12pt bold) */
    public static final Font SMALL_BOLD = new Font(FONT_FAMILY, Font.BOLD, 12);
    
    /** Font untuk emoji/icon (20pt) */
    public static final Font ICON = new Font("Segoe UI Emoji", Font.PLAIN, 20);
    
    /** Font untuk emoji/icon besar (32pt) */
    public static final Font ICON_LARGE = new Font("Segoe UI Emoji", Font.PLAIN, 32);

    /**
     * Konstruktor private untuk mencegah instansiasi.
     */
    private PathFonts() {}

    /**
     * Mencari font yang tersedia di sistem berdasarkan prioritas.
     * Urutan prioritas: Segoe UI, Roboto, Arial, SansSerif.
     * 
     * @return nama font yang tersedia
     */
    private static String getAvailableFont() {
        String[] preferredFonts = {"Segoe UI", "Roboto", "Arial", "SansSerif"};
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] availableFonts = ge.getAvailableFontFamilyNames();

        for (String preferred : preferredFonts) {
            for (String available : availableFonts) {
                if (available.equalsIgnoreCase(preferred)) {
                    return available;
                }
            }
        }
        return "SansSerif";
    }
}
