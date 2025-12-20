package madebydap.pathsocial.ui;

import madebydap.pathsocial.ui.style.PathColors;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame with CardLayout for navigation.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private LoginPanel loginPanel;
    private TimelinePanel timelinePanel;
    private ProfilePanel profilePanel;
    private FriendsPanel friendsPanel;

    public MainFrame() {
        setTitle("Path - Share Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 700);
        setMinimumSize(new Dimension(380, 600));
        setLocationRelativeTo(null);

        // Set background
        getContentPane().setBackground(PathColors.BACKGROUND);

        initComponents();
    }

    private void initComponents() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(PathColors.BACKGROUND);

        // Initialize panels
        loginPanel = new LoginPanel(this);
        timelinePanel = new TimelinePanel(this);
        profilePanel = new ProfilePanel(this);
        friendsPanel = new FriendsPanel(this);

        // Add panels to card layout
        mainPanel.add(loginPanel, "login");
        mainPanel.add(timelinePanel, "timeline");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(friendsPanel, "friends");

        add(mainPanel);

        // Show login panel initially
        cardLayout.show(mainPanel, "login");
    }

    public void showPanel(String panelName) {
        switch (panelName) {
            case "timeline":
                timelinePanel.refresh();
                break;
            case "profile":
                profilePanel.refresh();
                break;
            case "friends":
                friendsPanel.refresh();
                break;
            case "login":
                loginPanel.reset();
                break;
        }
        cardLayout.show(mainPanel, panelName);
    }
}
