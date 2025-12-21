package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.User;
import madebydap.pathsocial.ui.components.MomentCard;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Profile panel with horizontal layout: avatar left, info right.
 */
public class ProfilePanel extends JPanel {
    private final MainFrame mainFrame;
    private JPanel contentPanel;

    public ProfilePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(PathColors.BACKGROUND);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        add(createHeader(), BorderLayout.NORTH);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PathColors.BACKGROUND);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PathColors.BACKGROUND);
        scrollPane.getViewport().setBackground(PathColors.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PathColors.BACKGROUND_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        JLabel logoLabel = new JLabel("path");
        logoLabel.setFont(new Font("Georgia", Font.ITALIC, 32));
        logoLabel.setForeground(PathColors.PRIMARY);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(logoLabel, BorderLayout.CENTER);

        return header;
    }

    public void refresh() {
        contentPanel.removeAll();

        User user = DataStore.getInstance().getCurrentUser();
        if (user == null) return;

        // Profile card - horizontal layout
        JPanel profileCard = new JPanel(new BorderLayout(20, 0));
        profileCard.setBackground(PathColors.BACKGROUND_WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        // Left: Avatar
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(PathColors.PRIMARY);
                g2.fillOval(0, 0, 80, 80);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 28));
                String initials = user.getInitials();
                FontMetrics fm = g2.getFontMetrics();
                int x = (80 - fm.stringWidth(initials)) / 2;
                int y = (80 - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(initials, x, y);

                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(80, 80));
        profileCard.add(avatar, BorderLayout.WEST);

        // Right: Info (name, username, friends count)
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel nameLabel = new JLabel(user.getDisplayName());
        nameLabel.setFont(PathFonts.TITLE);
        nameLabel.setForeground(PathColors.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);

        infoPanel.add(Box.createVerticalStrut(4));

        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setFont(PathFonts.BODY);
        usernameLabel.setForeground(PathColors.TEXT_MUTED);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(usernameLabel);

        infoPanel.add(Box.createVerticalStrut(8));

        JLabel friendsLabel = new JLabel(user.getFriendCount() + " of " + User.MAX_FRIENDS + " friends");
        friendsLabel.setFont(PathFonts.SMALL);
        friendsLabel.setForeground(PathColors.PRIMARY);
        friendsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(friendsLabel);

        profileCard.add(infoPanel, BorderLayout.CENTER);
        contentPanel.add(profileCard);

        // Logout button section
        JPanel logoutSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 16));
        logoutSection.setBackground(PathColors.BACKGROUND);
        logoutSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JButton logoutBtn = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(PathColors.ERROR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(PathColors.ERROR.brighter());
                } else {
                    g2.setColor(PathColors.ERROR);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(PathFonts.BODY_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        logoutBtn.setPreferredSize(new Dimension(100, 38));
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            DataStore.getInstance().logout();
            mainFrame.showPanel("login");
        });
        logoutSection.add(logoutBtn);
        contentPanel.add(logoutSection);

        // Moments section
        JPanel sectionHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 12));
        sectionHeader.setBackground(PathColors.BACKGROUND);
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        
        JLabel momentsTitle = new JLabel("My Moments");
        momentsTitle.setFont(PathFonts.SMALL_BOLD);
        momentsTitle.setForeground(PathColors.TEXT_MUTED);
        sectionHeader.add(momentsTitle);
        contentPanel.add(sectionHeader);

        List<Moment> userMoments = DataStore.getInstance().getUserMoments(user.getId());
        if (userMoments.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emptyPanel.setBackground(PathColors.BACKGROUND_WHITE);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
            
            JLabel emptyLabel = new JLabel("No moments yet");
            emptyLabel.setFont(PathFonts.BODY);
            emptyLabel.setForeground(PathColors.TEXT_MUTED);
            emptyPanel.add(emptyLabel);
            contentPanel.add(emptyPanel);
        } else {
            for (Moment moment : userMoments) {
                MomentCard card = new MomentCard(moment);
                contentPanel.add(card);
            }
        }

        // Bottom padding
        contentPanel.add(Box.createVerticalStrut(80));

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
