package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.User;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Friends panel - clean list design with add functionality.
 */
public class FriendsPanel extends JPanel {
    private final MainFrame mainFrame;
    private JPanel friendsContainer;
    private JTextField searchField;
    private JPanel searchResultsPanel;

    public FriendsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(PathColors.BACKGROUND);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(PathColors.BACKGROUND);

        // Search section
        content.add(createSearchSection());

        // Friends list
        JPanel sectionHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        sectionHeader.setBackground(PathColors.BACKGROUND);
        sectionHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        
        JLabel friendsTitle = new JLabel("Friends");
        friendsTitle.setFont(PathFonts.SMALL_BOLD);
        friendsTitle.setForeground(PathColors.TEXT_MUTED);
        sectionHeader.add(friendsTitle);
        content.add(sectionHeader);

        friendsContainer = new JPanel();
        friendsContainer.setLayout(new BoxLayout(friendsContainer, BoxLayout.Y_AXIS));
        friendsContainer.setBackground(PathColors.BACKGROUND_WHITE);
        content.add(friendsContainer);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PathColors.BACKGROUND_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel backLabel = new JLabel("← Back");
        backLabel.setFont(PathFonts.BODY);
        backLabel.setForeground(PathColors.PRIMARY);
        backLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.showPanel("timeline");
            }
        });
        header.add(backLabel, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Friends");
        titleLabel.setFont(PathFonts.BODY_BOLD);
        titleLabel.setForeground(PathColors.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titleLabel, BorderLayout.CENTER);

        User currentUser = DataStore.getInstance().getCurrentUser();
        String countText = currentUser != null
                ? currentUser.getFriendCount() + "/" + User.MAX_FRIENDS
                : "0/" + User.MAX_FRIENDS;
        JLabel countLabel = new JLabel(countText);
        countLabel.setFont(PathFonts.SMALL);
        countLabel.setForeground(PathColors.TEXT_MUTED);
        header.add(countLabel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSearchSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(PathColors.BACKGROUND_WHITE);
        section.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        section.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JLabel searchLabel = new JLabel("Add Friend");
        searchLabel.setFont(PathFonts.SMALL_BOLD);
        searchLabel.setForeground(PathColors.TEXT_MUTED);
        searchLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(searchLabel);
        section.add(Box.createVerticalStrut(10));

        // Search field
        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setOpaque(false);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        searchField = new JTextField();
        searchField.setFont(PathFonts.BODY);
        searchField.setForeground(PathColors.TEXT_PRIMARY);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PathColors.BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.addActionListener(e -> performSearch());
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchBtn = new JButton("Search") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? PathColors.PRIMARY_DARK : PathColors.PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(Color.WHITE);
                g2.setFont(PathFonts.SMALL);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        searchBtn.setPreferredSize(new Dimension(70, 36));
        searchBtn.setContentAreaFilled(false);
        searchBtn.setBorderPainted(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.addActionListener(e -> performSearch());
        searchPanel.add(searchBtn, BorderLayout.EAST);

        section.add(searchPanel);
        section.add(Box.createVerticalStrut(10));

        searchResultsPanel = new JPanel();
        searchResultsPanel.setLayout(new BoxLayout(searchResultsPanel, BoxLayout.Y_AXIS));
        searchResultsPanel.setOpaque(false);
        searchResultsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        section.add(searchResultsPanel);

        return section;
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        searchResultsPanel.removeAll();

        if (query.isEmpty()) {
            searchResultsPanel.revalidate();
            searchResultsPanel.repaint();
            return;
        }

        List<User> results = DataStore.getInstance().searchUsers(query);
        User currentUser = DataStore.getInstance().getCurrentUser();

        if (results.isEmpty()) {
            JLabel noResults = new JLabel("No users found");
            noResults.setFont(PathFonts.SMALL);
            noResults.setForeground(PathColors.TEXT_MUTED);
            searchResultsPanel.add(noResults);
        } else {
            for (User user : results) {
                boolean isFriend = currentUser.isFriend(user.getId());
                searchResultsPanel.add(createUserRow(user, isFriend, true));
            }
        }

        searchResultsPanel.revalidate();
        searchResultsPanel.repaint();
    }

    private JPanel createUserRow(User user, boolean isFriend, boolean showAction) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        // Avatar
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(PathColors.PRIMARY);
                g2.fillOval(0, 0, 32, 32);
                g2.setColor(Color.WHITE);
                g2.setFont(PathFonts.SMALL_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                String initials = user.getInitials();
                g2.drawString(initials, (32 - fm.stringWidth(initials)) / 2,
                    (32 - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(32, 32));
        row.add(avatar, BorderLayout.WEST);

        // Name
        JPanel namePanel = new JPanel();
        namePanel.setOpaque(false);
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(user.getDisplayName());
        nameLabel.setFont(PathFonts.BODY);
        nameLabel.setForeground(PathColors.TEXT_PRIMARY);
        namePanel.add(nameLabel);

        JLabel usernameLabel = new JLabel("@" + user.getUsername());
        usernameLabel.setFont(PathFonts.SMALL);
        usernameLabel.setForeground(PathColors.TEXT_MUTED);
        namePanel.add(usernameLabel);

        row.add(namePanel, BorderLayout.CENTER);

        // Action
        if (showAction) {
            if (isFriend) {
                JLabel friendLabel = new JLabel("Friend ✓");
                friendLabel.setFont(PathFonts.SMALL);
                friendLabel.setForeground(PathColors.SUCCESS);
                row.add(friendLabel, BorderLayout.EAST);
            } else {
                JLabel addLabel = new JLabel("+ Add");
                addLabel.setFont(PathFonts.SMALL);
                addLabel.setForeground(PathColors.PRIMARY);
                addLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                addLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        User currentUser = DataStore.getInstance().getCurrentUser();
                        if (!currentUser.canAddFriend()) {
                            JOptionPane.showMessageDialog(FriendsPanel.this,
                                "You've reached the limit of " + User.MAX_FRIENDS + " friends.",
                                "Limit Reached",
                                JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                        currentUser.addFriend(user.getId());
                        refresh();
                        performSearch();
                    }
                });
                row.add(addLabel, BorderLayout.EAST);
            }
        }

        return row;
    }

    public void refresh() {
        removeAll();
        initComponents();
        
        friendsContainer.removeAll();

        User currentUser = DataStore.getInstance().getCurrentUser();
        if (currentUser == null) return;

        List<String> friendIds = currentUser.getFriendIds();

        if (friendIds.isEmpty()) {
            JPanel emptyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            emptyPanel.setOpaque(false);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
            
            JLabel emptyLabel = new JLabel("No friends yet");
            emptyLabel.setFont(PathFonts.BODY);
            emptyLabel.setForeground(PathColors.TEXT_MUTED);
            emptyPanel.add(emptyLabel);
            friendsContainer.add(emptyPanel);
        } else {
            for (String friendId : friendIds) {
                User friend = DataStore.getInstance().getUserById(friendId);
                if (friend != null) {
                    JPanel row = createUserRow(friend, true, false);
                    row.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
                        BorderFactory.createEmptyBorder(12, 16, 12, 16)
                    ));
                    friendsContainer.add(row);
                }
            }
        }

        friendsContainer.revalidate();
        friendsContainer.repaint();
        revalidate();
        repaint();
    }
}
