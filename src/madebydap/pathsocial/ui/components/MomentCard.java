package madebydap.pathsocial.ui.components;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.User;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Moment card with timeline line connecting through avatars.
 */
public class MomentCard extends JPanel {
    private final Moment moment;
    private final User author;
    private boolean isFirst = false;
    private boolean isLast = false;

    public MomentCard(Moment moment) {
        this.moment = moment;
        this.author = DataStore.getInstance().getUserById(moment.getUserId());
        
        setBackground(PathColors.CARD);
        setLayout(new BorderLayout(0, 0));
        setOpaque(true);
        
        initComponents();
    }

    public void setFirstInTimeline(boolean first) {
        this.isFirst = first;
        repaint();
    }

    public void setLastInTimeline(boolean last) {
        this.isLast = last;
        repaint();
    }

    private void initComponents() {
        JPanel content = new JPanel(new BorderLayout(12, 0));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        // Left: Timeline line + Avatar
        content.add(createTimelineAvatar(), BorderLayout.WEST);

        // Center: Content
        content.add(createContentPanel(), BorderLayout.CENTER);

        // Right: Time
        content.add(createTimePanel(), BorderLayout.EAST);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createTimelineAvatar() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = 20;
                int avatarY = 16;
                int avatarSize = 40;
                int avatarCenterY = avatarY + avatarSize / 2;

                // Draw timeline line
                g2.setColor(PathColors.BORDER);
                g2.setStroke(new BasicStroke(2));

                // Line from top to avatar (if not first)
                if (!isFirst) {
                    g2.drawLine(centerX, 0, centerX, avatarY);
                }

                // Line from avatar to bottom (if not last)
                if (!isLast) {
                    g2.drawLine(centerX, avatarY + avatarSize, centerX, getHeight());
                }

                // Draw avatar circle (on top of line)
                g2.setColor(PathColors.getMomentTypeColor(moment.getType()));
                g2.fillOval(centerX - avatarSize / 2, avatarY, avatarSize, avatarSize);

                // Draw initials
                g2.setColor(Color.WHITE);
                g2.setFont(PathFonts.BODY_BOLD);
                String initials = author != null ? author.getInitials() : "?";
                FontMetrics fm = g2.getFontMetrics();
                int textX = centerX - fm.stringWidth(initials) / 2;
                int textY = avatarCenterY + fm.getAscent() / 2 - 2;
                g2.drawString(initials, textX, textY);

                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(50, 0));
        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 16, 0));

        // Author name
        JLabel nameLabel = new JLabel(author != null ? author.getDisplayName() : "Unknown");
        nameLabel.setFont(PathFonts.BODY_BOLD);
        nameLabel.setForeground(PathColors.TEXT_PRIMARY);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(nameLabel);

        panel.add(Box.createVerticalStrut(2));

        // Action text
        String actionText = moment.getType().getIcon() + " " + moment.getType().getPrefix();
        JLabel actionLabel = new JLabel(actionText);
        actionLabel.setFont(PathFonts.SMALL);
        actionLabel.setForeground(PathColors.TEXT_MUTED);
        actionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(actionLabel);

        panel.add(Box.createVerticalStrut(6));

        // Content
        JLabel contentLabel = new JLabel("<html><body style='width: 180px'>" + moment.getContent() + "</body></html>");
        contentLabel.setFont(PathFonts.BODY);
        contentLabel.setForeground(PathColors.TEXT_SECONDARY);
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(contentLabel);

        return panel;
    }

    private JPanel createTimePanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        panel.setPreferredSize(new Dimension(50, 50));

        JLabel timeLabel = new JLabel(moment.getFormattedTime());
        timeLabel.setFont(PathFonts.SMALL);
        timeLabel.setForeground(PathColors.TEXT_MUTED);
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panel.add(timeLabel);

        return panel;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        return new Dimension(d.width, Math.max(d.height, 90));
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
