package madebydap.pathsocial.ui.components;

import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

/**
 * Path's signature red floating action button.
 */
public class FloatingActionButton extends JButton {
    private Consumer<MomentType> onMomentTypeSelected;
    private JWindow popupWindow;
    private Timer animationTimer;
    private float animationProgress = 0f;
    private boolean expanded = false;

    private static final int BUTTON_SIZE = 52;
    private static final int MINI_BUTTON_SIZE = 40;
    private static final int EXPAND_RADIUS = 72;
    private static final int POPUP_SIZE = 240;

    public FloatingActionButton() {
        setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addActionListener(e -> toggleExpanded());
    }

    public void setOnMomentTypeSelected(Consumer<MomentType> listener) {
        this.onMomentTypeSelected = listener;
    }

    private void toggleExpanded() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }

    private void expand() {
        expanded = true;
        
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow == null) return;

        popupWindow = new JWindow(parentWindow);
        popupWindow.setBackground(new Color(0, 0, 0, 0));
        
        JPanel popupContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRadialMenu((Graphics2D) g);
            }
        };
        popupContent.setOpaque(false);
        popupContent.setPreferredSize(new Dimension(POPUP_SIZE, POPUP_SIZE));
        
        popupContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MomentType clickedType = getMomentTypeAtPoint(e.getPoint());
                if (clickedType != null && onMomentTypeSelected != null) {
                    collapse();
                    onMomentTypeSelected.accept(clickedType);
                } else {
                    collapse();
                }
            }
        });

        popupWindow.setContentPane(popupContent);
        popupWindow.pack();

        Point buttonLocation = getLocationOnScreen();
        int popupX = buttonLocation.x + BUTTON_SIZE / 2 - POPUP_SIZE / 2;
        int popupY = buttonLocation.y + BUTTON_SIZE / 2 - POPUP_SIZE / 2;
        popupWindow.setLocation(popupX, popupY);
        popupWindow.setVisible(true);

        animationProgress = 0f;
        if (animationTimer != null) animationTimer.stop();
        animationTimer = new Timer(16, evt -> {
            animationProgress += 0.15f;
            if (animationProgress >= 1f) {
                animationProgress = 1f;
                animationTimer.stop();
            }
            if (popupWindow != null) popupWindow.repaint();
            repaint();
        });
        animationTimer.start();
    }

    private void collapse() {
        if (animationTimer != null) animationTimer.stop();
        
        animationTimer = new Timer(16, evt -> {
            animationProgress -= 0.18f;
            if (animationProgress <= 0f) {
                animationProgress = 0f;
                expanded = false;
                animationTimer.stop();
                if (popupWindow != null) {
                    popupWindow.dispose();
                    popupWindow = null;
                }
            }
            if (popupWindow != null) popupWindow.repaint();
            repaint();
        });
        animationTimer.start();
    }

    private MomentType getMomentTypeAtPoint(Point point) {
        if (animationProgress < 0.8f) return null;

        Point center = new Point(POPUP_SIZE / 2, POPUP_SIZE / 2);
        MomentType[] types = MomentType.values();
        int count = types.length;
        double angleStep = 2 * Math.PI / count;
        double startAngle = -Math.PI / 2;

        for (int i = 0; i < count; i++) {
            double angle = startAngle + i * angleStep;
            int x = (int) (center.x + EXPAND_RADIUS * Math.cos(angle));
            int y = (int) (center.y + EXPAND_RADIUS * Math.sin(angle));

            double dist = Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
            if (dist <= MINI_BUTTON_SIZE / 2 + 5) {
                return types[i];
            }
        }
        return null;
    }

    private void drawRadialMenu(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point center = new Point(POPUP_SIZE / 2, POPUP_SIZE / 2);

        // Light overlay
        if (animationProgress > 0) {
            int size = (int)(EXPAND_RADIUS * 2.5f * animationProgress);
            g2.setColor(new Color(255, 255, 255, (int)(220 * animationProgress)));
            g2.fillOval(center.x - size/2, center.y - size/2, size, size);
        }

        // Mini buttons
        MomentType[] types = MomentType.values();
        int count = types.length;
        double angleStep = 2 * Math.PI / count;
        double startAngle = -Math.PI / 2;

        float currentRadius = EXPAND_RADIUS * animationProgress;

        for (int i = 0; i < count; i++) {
            MomentType type = types[i];
            double angle = startAngle + i * angleStep;
            int x = (int) (center.x + currentRadius * Math.cos(angle));
            int y = (int) (center.y + currentRadius * Math.sin(angle));

            // Shadow
            g2.setColor(new Color(0, 0, 0, (int) (30 * animationProgress)));
            g2.fillOval(x - MINI_BUTTON_SIZE / 2 + 2, y - MINI_BUTTON_SIZE / 2 + 2,
                    MINI_BUTTON_SIZE, MINI_BUTTON_SIZE);

            // Button
            g2.setColor(PathColors.getMomentTypeColor(type));
            g2.fillOval(x - MINI_BUTTON_SIZE / 2, y - MINI_BUTTON_SIZE / 2,
                    MINI_BUTTON_SIZE, MINI_BUTTON_SIZE);

            // Icon
            g2.setColor(Color.WHITE);
            g2.setFont(PathFonts.ICON);
            FontMetrics fm = g2.getFontMetrics();
            String icon = type.getIcon();
            g2.drawString(icon, x - fm.stringWidth(icon) / 2, y + fm.getAscent() / 2 - 2);
        }

        // Center button
        drawMainButtonAt(g2, center.x, center.y);
    }

    private void drawMainButtonAt(Graphics2D g2, int cx, int cy) {
        // Shadow
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(cx - BUTTON_SIZE / 2 + 2, cy - BUTTON_SIZE / 2 + 2, BUTTON_SIZE, BUTTON_SIZE);

        // Button
        g2.setColor(PathColors.PRIMARY);
        g2.fillOval(cx - BUTTON_SIZE / 2, cy - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE);

        // X icon
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int halfSize = 10;
        double rotation = animationProgress * Math.PI / 4;

        g2.translate(cx, cy);
        g2.rotate(rotation);
        g2.drawLine(-halfSize, 0, halfSize, 0);
        g2.drawLine(0, -halfSize, 0, halfSize);
        g2.rotate(-rotation);
        g2.translate(-cx, -cy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Shadow
        g2.setColor(new Color(0, 0, 0, 30));
        g2.fillOval(cx - BUTTON_SIZE / 2 + 2, cy - BUTTON_SIZE / 2 + 2, BUTTON_SIZE, BUTTON_SIZE);

        // Button
        if (getModel().isRollover() && !expanded) {
            g2.setColor(PathColors.PRIMARY_LIGHT);
        } else {
            g2.setColor(PathColors.PRIMARY);
        }
        g2.fillOval(cx - BUTTON_SIZE / 2, cy - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE);

        // Plus icon
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        int halfSize = 10;
        double rotation = animationProgress * Math.PI / 4;

        g2.translate(cx, cy);
        g2.rotate(rotation);
        g2.drawLine(-halfSize, 0, halfSize, 0);
        g2.drawLine(0, -halfSize, 0, halfSize);

        g2.dispose();
    }
}
