package madebydap.pathsocial;

import madebydap.pathsocial.ui.MainFrame;

import javax.swing.*;

/**
 * Path Clone MVP - A social network focused on privacy and close friends.
 * 
 * Features:
 * - Share moments (Awake, Asleep, Music, Photo, Location, Thought)
 * - Friend limit of 50 (Dunbar's Number)
 * - Premium dark theme UI
 * - Iconic floating action button
 * 
 * @author madebydap
 */
public class App {
    public static void main(String[] args) {
        // Set system look and feel properties
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default
        }

        // Launch the application
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}