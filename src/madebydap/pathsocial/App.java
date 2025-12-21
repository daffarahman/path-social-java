package madebydap.pathsocial;

import com.formdev.flatlaf.FlatLightLaf;
import madebydap.pathsocial.ui.MainFrame;

import javax.swing.*;

/**
 * Kelas utama aplikasi Path Social.
 * Berfungsi sebagai entry point untuk menjalankan aplikasi.
 * 
 * @author madebydap
 * @version 1.0
 */
public class App {
    
    /**
     * Method main untuk memulai aplikasi.
     * Mengatur Look and Feel menggunakan FlatLaf dan menampilkan MainFrame.
     * 
     * @param args argumen command line (tidak digunakan)
     */
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception e) {
            System.err.println("Gagal mengatur FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}