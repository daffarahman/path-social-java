package madebydap.pathsocial.ui.components;

import madebydap.pathsocial.model.MomentType;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Floating Action Button merah khas Path dengan radial menu.
 * Menampilkan pilihan tipe moment dalam menu radial saat diklik.
 * Hanya menampilkan tipe moment yang bisa dibuat user (excludes FRIENDSHIP).
 * 
 * @author madebydap
 * @version 1.0
 */
public class FloatingActionButton extends JButton {
    
    /** Callback saat tipe moment dipilih */
    private Consumer<MomentType> onMomentTypeSelected;
    
    /** Window popup untuk radial menu */
    private JWindow popupWindow;
    
    /** Timer untuk animasi */
    private Timer animationTimer;
    
    /** Progress animasi (0-1) */
    private float animationProgress = 0f;
    
    /** Flag apakah menu sedang expanded */
    private boolean expanded = false;

    /** Ukuran tombol utama */
    private static final int BUTTON_SIZE = 52;
    
    /** Ukuran tombol mini di radial menu */
    private static final int MINI_BUTTON_SIZE = 40;
    
    /** Radius radial menu */
    private static final int EXPAND_RADIUS = 72;
    
    /** Ukuran popup window */
    private static final int POPUP_SIZE = 240;

    /**
     * Konstruktor FloatingActionButton.
     * Membuat tombol merah dengan icon plus.
     */
    public FloatingActionButton() {
        setPreferredSize(new Dimension(BUTTON_SIZE, BUTTON_SIZE));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addActionListener(e -> toggleExpanded());
    }

    /**
     * Mengatur callback untuk pemilihan tipe moment.
     * 
     * @param listener callback yang menerima MomentType
     */
    public void setOnMomentTypeSelected(Consumer<MomentType> listener) {
        this.onMomentTypeSelected = listener;
    }

    /**
     * Mengambil daftar tipe moment yang bisa dibuat user.
     * 
     * @return array MomentType yang user-creatable
     */
    private MomentType[] getCreatableTypes() {
        return Arrays.stream(MomentType.values())
                .filter(MomentType::isUserCreatable)
                .toArray(MomentType[]::new);
    }

    /**
     * Toggle antara expanded dan collapsed.
     */
    private void toggleExpanded() {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }

    /**
     * Membuka radial menu dengan animasi.
     */
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

    /**
     * Menutup radial menu dengan animasi.
     */
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

    /**
     * Mendapatkan tipe moment pada posisi tertentu di radial menu.
     * 
     * @param point posisi mouse
     * @return MomentType yang diklik atau null
     */
    private MomentType getMomentTypeAtPoint(Point point) {
        if (animationProgress < 0.8f) return null;

        Point center = new Point(POPUP_SIZE / 2, POPUP_SIZE / 2);
        MomentType[] types = getCreatableTypes();
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

    /**
     * Menggambar radial menu.
     * 
     * @param g2 graphics context
     */
    private void drawRadialMenu(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Point center = new Point(POPUP_SIZE / 2, POPUP_SIZE / 2);

        // Light overlay
        if (animationProgress > 0) {
            int size = (int)(EXPAND_RADIUS * 2.5f * animationProgress);
            g2.setColor(new Color(255, 255, 255, (int)(230 * animationProgress)));
            g2.fillOval(center.x - size/2, center.y - size/2, size, size);
        }

        // Mini buttons with icons
        MomentType[] types = getCreatableTypes();
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
            Icon icon = PathIcons.getMomentIcon(type, 18, Color.WHITE);
            icon.paintIcon(null, g2, x - 9, y - 9);
        }

        // Center button
        drawMainButtonAt(g2, center.x, center.y);
    }

    /**
     * Menggambar tombol utama pada posisi tertentu.
     * 
     * @param g2 graphics context
     * @param cx posisi x tengah
     * @param cy posisi y tengah
     */
    private void drawMainButtonAt(Graphics2D g2, int cx, int cy) {
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(cx - BUTTON_SIZE / 2 + 2, cy - BUTTON_SIZE / 2 + 2, BUTTON_SIZE, BUTTON_SIZE);

        g2.setColor(PathColors.PRIMARY);
        g2.fillOval(cx - BUTTON_SIZE / 2, cy - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE);

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
