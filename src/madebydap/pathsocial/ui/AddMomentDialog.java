package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.Moment;
import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog for adding a new moment - minimal design.
 */
public class AddMomentDialog extends JDialog {
    private final MomentType momentType;
    private JTextArea contentArea;
    private boolean confirmed = false;

    public AddMomentDialog(JFrame parent, MomentType momentType) {
        super(parent, true);
        this.momentType = momentType;

        setTitle("New " + momentType.getDisplayName());
        setSize(360, 280);
        setLocationRelativeTo(parent);
        setUndecorated(false);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(PathColors.BACKGROUND_WHITE);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel typeLabel = new JLabel(momentType.getIcon() + " " + momentType.getDisplayName());
        typeLabel.setFont(PathFonts.SUBTITLE);
        typeLabel.setForeground(PathColors.getMomentTypeColor(momentType));
        header.add(typeLabel, BorderLayout.WEST);

        main.add(header, BorderLayout.NORTH);

        // Content input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);

        JLabel promptLabel = new JLabel(momentType.getPrefix() + "...");
        promptLabel.setFont(PathFonts.SMALL);
        promptLabel.setForeground(PathColors.TEXT_MUTED);
        promptLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        inputPanel.add(promptLabel, BorderLayout.NORTH);

        contentArea = new JTextArea(5, 25);
        contentArea.setFont(PathFonts.BODY);
        contentArea.setForeground(PathColors.TEXT_PRIMARY);
        contentArea.setBackground(PathColors.BACKGROUND);
        contentArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(PathColors.BORDER));
        inputPanel.add(scrollPane, BorderLayout.CENTER);

        main.add(inputPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        JButton cancelBtn = createButton("Cancel", PathColors.BACKGROUND, PathColors.TEXT_SECONDARY);
        cancelBtn.addActionListener(e -> dispose());
        buttonPanel.add(cancelBtn);

        JButton shareBtn = createButton("Share", PathColors.PRIMARY, Color.WHITE);
        shareBtn.addActionListener(e -> shareMoment());
        buttonPanel.add(shareBtn);

        main.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private JButton createButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color bg = bgColor;
                if (getModel().isPressed()) {
                    bg = bg.darker();
                }
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                if (bgColor.equals(PathColors.BACKGROUND)) {
                    g2.setColor(PathColors.BORDER);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 6, 6);
                }

                g2.setColor(fgColor);
                g2.setFont(PathFonts.BODY);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(80, 34));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void shareMoment() {
        String content = contentArea.getText().trim();
        if (content.isEmpty()) {
            contentArea.requestFocus();
            return;
        }

        DataStore dataStore = DataStore.getInstance();
        Moment moment = new Moment(dataStore.getCurrentUser().getId(), momentType, content);
        dataStore.addMoment(moment);

        confirmed = true;
        dispose();
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
