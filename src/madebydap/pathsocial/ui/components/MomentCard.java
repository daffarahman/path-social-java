package madebydap.pathsocial.ui.components;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.model.User;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;
import madebydap.pathsocial.ui.style.PathIcons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Moment card with timeline line, custom icons, and image support.
 */
public class MomentCard extends JPanel {
    private final Moment moment;
    private final User author;
    private boolean isFirst = false;
    private boolean isLast = false;
    private BufferedImage momentImage;

    public MomentCard(Moment moment) {
        this.moment = moment;
        this.author = DataStore.getInstance().getUserById(moment.getUserId());
        
        // Load image if exists
        if (moment.hasImage()) {
            try {
                momentImage = ImageIO.read(new File(moment.getImagePath()));
            } catch (Exception e) {
                momentImage = null;
            }
        }
        
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

        // Left: Timeline line + Avatar with icon
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

                // Draw timeline line
                g2.setColor(PathColors.BORDER);
                g2.setStroke(new BasicStroke(2));

                if (!isFirst) {
                    g2.drawLine(centerX, 0, centerX, avatarY);
                }

                if (!isLast) {
                    g2.drawLine(centerX, avatarY + avatarSize, centerX, getHeight());
                }

                // Draw avatar circle
                g2.setColor(PathColors.getMomentTypeColor(moment.getType()));
                g2.fillOval(centerX - avatarSize / 2, avatarY, avatarSize, avatarSize);

                // Draw icon in center of avatar
                Icon icon = PathIcons.getMomentIcon(moment.getType(), 20, Color.WHITE);
                icon.paintIcon(this, g2, centerX - 10, avatarY + 10);

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

        panel.add(Box.createVerticalStrut(4));

        // Action + Content in one line with different colors
        JPanel actionContentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        actionContentPanel.setOpaque(false);
        actionContentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel prefixLabel = new JLabel(moment.getType().getPrefix() + " ");
        prefixLabel.setFont(PathFonts.BODY);
        prefixLabel.setForeground(PathColors.TEXT_MUTED);
        actionContentPanel.add(prefixLabel);
        
        JLabel contentLabel = new JLabel(moment.getContent());
        contentLabel.setFont(PathFonts.BODY);
        contentLabel.setForeground(PathColors.TEXT_PRIMARY);
        actionContentPanel.add(contentLabel);
        
        panel.add(actionContentPanel);

        // Image for PHOTO moments
        if (moment.getType() == MomentType.PHOTO && momentImage != null) {
            panel.add(Box.createVerticalStrut(8));
            
            JPanel imagePanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    int maxWidth = getWidth();
                    int maxHeight = 200;
                    
                    double scale = Math.min((double) maxWidth / momentImage.getWidth(), 
                                           (double) maxHeight / momentImage.getHeight());
                    int imgWidth = (int) (momentImage.getWidth() * scale);
                    int imgHeight = (int) (momentImage.getHeight() * scale);

                    // Draw rounded rectangle clip
                    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, imgWidth, imgHeight, 12, 12));
                    g2.drawImage(momentImage, 0, 0, imgWidth, imgHeight, null);

                    g2.dispose();
                }

                @Override
                public Dimension getPreferredSize() {
                    if (momentImage != null) {
                        int maxWidth = 250;
                        int maxHeight = 200;
                        double scale = Math.min((double) maxWidth / momentImage.getWidth(), 
                                               (double) maxHeight / momentImage.getHeight());
                        return new Dimension((int)(momentImage.getWidth() * scale), 
                                           (int)(momentImage.getHeight() * scale));
                    }
                    return new Dimension(200, 150);
                }
            };
            imagePanel.setOpaque(false);
            imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imagePanel.setMaximumSize(new Dimension(250, 200));
            panel.add(imagePanel);
        }

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
        int minHeight = 80;
        if (moment.getType() == MomentType.PHOTO && momentImage != null) {
            minHeight = 280; // More space for image
        }
        return new Dimension(d.width, Math.max(d.height, minHeight));
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
    }
}
