package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.ui.components.BottomNavBar;
import madebydap.pathsocial.ui.components.FloatingActionButton;
import madebydap.pathsocial.ui.style.PathColors;

import javax.swing.*;
import java.awt.*;

/**
 * Main application frame with bottom navigation and FAB.
 */
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BottomNavBar bottomNav;
    private FloatingActionButton fab;
    
    private LoginPanel loginPanel;
    private TimelinePanel timelinePanel;
    private ProfilePanel profilePanel;
    private FriendsPanel friendsPanel;

    public MainFrame() {
        setTitle("Path - Share Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(360, 600));

        initComponents();
    }

    private void initComponents() {
        // Use layered pane for FAB overlay
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(PathColors.BACKGROUND);

        // Main content area
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(PathColors.BACKGROUND);

        loginPanel = new LoginPanel(this);
        timelinePanel = new TimelinePanel(this);
        profilePanel = new ProfilePanel(this);
        friendsPanel = new FriendsPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(timelinePanel, "timeline");
        mainPanel.add(profilePanel, "profile");
        mainPanel.add(friendsPanel, "friends");

        container.add(mainPanel, BorderLayout.CENTER);

        // Bottom navigation bar
        bottomNav = new BottomNavBar();
        bottomNav.setOnNavigate(this::showPanel);
        bottomNav.setVisible(false);
        container.add(bottomNav, BorderLayout.SOUTH);

        // Make container fill the layered pane
        container.setBounds(0, 0, 400, 700);
        layeredPane.add(container, JLayeredPane.DEFAULT_LAYER);

        // FAB on top layer
        fab = new FloatingActionButton();
        fab.setOnMomentTypeSelected(type -> {
            AddMomentDialog dialog = new AddMomentDialog(this, type);
            dialog.setVisible(true);
            if (dialog.isConfirmed()) {
                timelinePanel.refresh();
            }
        });
        fab.setBounds(400 - 72, 700 - 130, 56, 56);
        fab.setVisible(false);
        layeredPane.add(fab, JLayeredPane.PALETTE_LAYER);

        // Handle resize
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                container.setBounds(0, 0, w, h);
                fab.setBounds(w - 72, h - 130, 56, 56);
            }
        });

        setContentPane(layeredPane);
        showPanel("login");
    }

    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
        
        boolean showNav = !panelName.equals("login");
        bottomNav.setVisible(showNav);
        fab.setVisible(showNav);
        
        if (showNav) {
            bottomNav.setCurrentPanel(panelName);
        }

        if (panelName.equals("timeline")) {
            timelinePanel.refresh();
        } else if (panelName.equals("profile")) {
            profilePanel.refresh();
        } else if (panelName.equals("friends")) {
            friendsPanel.refresh();
        } else if (panelName.equals("login")) {
            loginPanel.reset();
            timelinePanel.stopAutoRefresh();
        }
    }
}
