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
 * Profile panel with logout button.
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
        header.setBackground(PathColors.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        // Same header as all screens - just "Path" logo
        JLabel logoLabel = new JLabel("Path");
        logoLabel.setFont(new Font("Georgia", Font.ITALIC | Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(logoLabel, BorderLayout.CENTER);

        return header;
    }

    public void refresh() {
        contentPanel.removeAll();

        User user = DataStore.getInstance().getCurrentUser();
        if (user == null) return;

        // Profile card
        JPanel profileCard = new JPanel(new GridBagLayout());
        profileCard.setBackground(PathColors.BACKGROUND_WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        JPanel centerContent = new JPanel();
        centerContent.setOpaque(false);
        centerContent.setLayout(new BoxLayout(centerContent, BoxLayout.Y_AXIS));

        // Avatar
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
        avatar.setMaximumSize(new Dimension(80, 80));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(avatar);

        centerContent.add(Box.createVerticalStrut(16));

        JLabel nameLabel = new JLabel(user.getDisplayName());
        nameLabel.setFont(PathFonts.TITLE);
        nameLabel.setForeground(PathColors.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(nameLabel);

        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setFont(PathFonts.BODY);
        usernameLabel.setForeground(PathColors.TEXT_MUTED);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(usernameLabel);

        centerContent.add(Box.createVerticalStrut(12));

        JLabel friendsLabel = new JLabel(user.getFriendCount() + " of " + User.MAX_FRIENDS + " friends");
        friendsLabel.setFont(PathFonts.SMALL);
        friendsLabel.setForeground(PathColors.PRIMARY);
        friendsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerContent.add(friendsLabel);

        profileCard.add(centerContent);
        contentPanel.add(profileCard);

        // Logout button section
        JPanel logoutSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        logoutSection.setBackground(PathColors.BACKGROUND);
        logoutSection.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

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
        logoutBtn.setPreferredSize(new Dimension(200, 44));
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
        JPanel sectionHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
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

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
