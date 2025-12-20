package madebydap.pathsocial;

import com.formdev.flatlaf.FlatLightLaf;
import madebydap.pathsocial.ui.MainFrame;

import javax.swing.*;

/**
 * Path Clone MVP - A social network focused on privacy and close friends.
 * 
 * Features:
 * - Share moments (Awake, Asleep, Music, Photo, Location, Thought)
 * - Friend limit of 50 (Dunbar's Number)
 * - Clean light theme UI with FlatLaf
 * - Iconic floating action button
 * 
 * @author madebydap
 */
public class App {
    public static void main(String[] args) {
        // Set FlatLaf Look and Feel
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Launch the application
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}