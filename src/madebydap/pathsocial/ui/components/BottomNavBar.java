package madebydap.pathsocial.ui.components;

import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * Navigation bar di bagian bawah aplikasi.
 * Menampilkan 3 tab: Profile, Moments, dan Friends.
 * 
 * @author madebydap
 * @version 1.0
 */
public class BottomNavBar extends JPanel {
    
    /** Callback saat tab dipilih */
    private Consumer<String> onNavigate;
    
    /** Panel yang sedang aktif */
    private String currentPanel = "timeline";

    /** Tinggi navigation bar */
    private static final int NAV_HEIGHT = 56;

    /**
     * Konstruktor BottomNavBar.
     * Membuat 3 tab navigasi.
     */
    public BottomNavBar() {
        setPreferredSize(new Dimension(0, NAV_HEIGHT));
        setBackground(PathColors.BACKGROUND_WHITE);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, PathColors.DIVIDER));
        setLayout(new GridLayout(1, 3));

        add(createNavItem("👤", "Profile", "profile"));
        add(createNavItem("🏠", "Moments", "timeline"));
        add(createNavItem("👥", "Friends", "friends"));
    }

    /**
     * Mengatur callback untuk navigasi.
     * 
     * @param listener callback yang menerima nama panel
     */
    public void setOnNavigate(Consumer<String> listener) {
        this.onNavigate = listener;
    }

    /**
     * Mengatur panel yang sedang aktif.
     * 
     * @param panel nama panel yang aktif
     */
    public void setCurrentPanel(String panel) {
        this.currentPanel = panel;
        repaint();
    }

    /**
     * Membuat item navigasi.
     * 
     * @param icon emoji icon untuk tab
     * @param label label teks tab
     * @param panelName nama panel tujuan
     * @return JPanel item navigasi
     */
    private JPanel createNavItem(String icon, String label, String panelName) {
        JPanel item = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isActive = panelName.equals(currentPanel);
                int centerX = getWidth() / 2;

                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
                g2.setColor(isActive ? PathColors.PRIMARY : PathColors.TEXT_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(icon, centerX - fm.stringWidth(icon) / 2, 26);

                g2.setFont(PathFonts.SMALL);
                fm = g2.getFontMetrics();
                g2.drawString(label, centerX - fm.stringWidth(label) / 2, 44);

                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onNavigate != null) {
                    onNavigate.accept(panelName);
                }
            }
        });
        return item;
    }
}
