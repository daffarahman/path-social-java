package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.ui.components.FloatingActionButton;
import madebydap.pathsocial.ui.components.MomentCard;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Timeline panel with connected moments.
 */
public class TimelinePanel extends JPanel {
    private final MainFrame mainFrame;
    private JPanel momentsContainer;
    private JScrollPane scrollPane;

    public TimelinePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(PathColors.BACKGROUND);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // Header
        add(createHeader(), BorderLayout.NORTH);

        // Content area with FAB
        JPanel contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(PathColors.BACKGROUND);

        // Timeline content
        momentsContainer = new JPanel();
        momentsContainer.setLayout(new BoxLayout(momentsContainer, BoxLayout.Y_AXIS));
        momentsContainer.setBackground(PathColors.CARD);

        scrollPane = new JScrollPane(momentsContainer);
        scrollPane.setBorder(null);
        scrollPane.setBackground(PathColors.BACKGROUND);
        scrollPane.getViewport().setBackground(PathColors.BACKGROUND);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contentArea.add(scrollPane, BorderLayout.CENTER);

        // FAB panel
        JPanel fabPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        fabPanel.setOpaque(false);
        
        FloatingActionButton fab = new FloatingActionButton();
        fab.setOnMomentTypeSelected(this::showAddMomentDialog);
        fabPanel.add(fab);
        
        contentArea.add(fabPanel, BorderLayout.SOUTH);

        add(contentArea, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PathColors.BACKGROUND_WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.DIVIDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        // Left: Logo
        JLabel logoLabel = new JLabel("path");
        logoLabel.setFont(new Font("Georgia", Font.ITALIC, 24));
        logoLabel.setForeground(PathColors.PRIMARY);
        header.add(logoLabel, BorderLayout.WEST);

        // Right: Navigation
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        navPanel.setOpaque(false);

        navPanel.add(createNavLink("Profile", () -> mainFrame.showPanel("profile")));
        navPanel.add(createNavLink("Friends", () -> mainFrame.showPanel("friends")));
        navPanel.add(createNavLink("Logout", () -> {
            DataStore.getInstance().logout();
            mainFrame.showPanel("login");
        }));

        header.add(navPanel, BorderLayout.EAST);

        return header;
    }

    private JLabel createNavLink(String text, Runnable action) {
        JLabel label = new JLabel(text);
        label.setFont(PathFonts.SMALL);
        label.setForeground(PathColors.TEXT_SECONDARY);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                label.setForeground(PathColors.PRIMARY);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                label.setForeground(PathColors.TEXT_SECONDARY);
            }
        });
        return label;
    }

    private void showAddMomentDialog(MomentType type) {
        AddMomentDialog dialog = new AddMomentDialog(mainFrame, type);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            refresh();
        }
    }

    public void refresh() {
        momentsContainer.removeAll();

        List<Moment> moments = DataStore.getInstance().getTimelineMoments();

        if (moments.isEmpty()) {
            JPanel emptyPanel = new JPanel(new GridBagLayout());
            emptyPanel.setBackground(PathColors.CARD);
            emptyPanel.setPreferredSize(new Dimension(400, 200));
            
            JPanel emptyContent = new JPanel();
            emptyContent.setOpaque(false);
            emptyContent.setLayout(new BoxLayout(emptyContent, BoxLayout.Y_AXIS));
            
            JLabel emptyIcon = new JLabel("+");
            emptyIcon.setFont(new Font("SansSerif", Font.PLAIN, 48));
            emptyIcon.setForeground(PathColors.BORDER);
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyContent.add(emptyIcon);
            
            emptyContent.add(Box.createVerticalStrut(12));
            
            JLabel emptyLabel = new JLabel("Share your first moment");
            emptyLabel.setFont(PathFonts.BODY);
            emptyLabel.setForeground(PathColors.TEXT_MUTED);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyContent.add(emptyLabel);
            
            emptyPanel.add(emptyContent);
            momentsContainer.add(emptyPanel);
        } else {
            for (int i = 0; i < moments.size(); i++) {
                Moment moment = moments.get(i);
                MomentCard card = new MomentCard(moment);
                
                // Set first/last flags for timeline line
                card.setFirstInTimeline(i == 0);
                card.setLastInTimeline(i == moments.size() - 1);
                
                momentsContainer.add(card);
            }
        }

        momentsContainer.revalidate();
        momentsContainer.repaint();

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }
}
