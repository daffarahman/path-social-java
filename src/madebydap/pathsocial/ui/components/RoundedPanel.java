package madebydap.pathsocial.ui.components;

import javax.swing.*;
import java.awt.*;

/**
 * Panel dengan sudut melengkung untuk tampilan premium.
 * Mendukung customisasi radius, warna background, dan border.
 * 
 * @author madebydap
 * @version 1.0
 */
public class RoundedPanel extends JPanel {
    
    /** Radius sudut panel */
    private int cornerRadius;
    
    /** Warna background panel */
    private Color backgroundColor;
    
    /** Flag untuk menggambar border */
    private boolean drawBorder;
    
    /** Warna border */
    private Color borderColor;

    /**
     * Konstruktor dengan radius saja.
     * 
     * @param cornerRadius radius sudut dalam pixel
     */
    public RoundedPanel(int cornerRadius) {
        this(cornerRadius, null);
    }

    /**
     * Konstruktor dengan radius dan warna background.
     * 
     * @param cornerRadius radius sudut dalam pixel
     * @param backgroundColor warna background panel
     */
    public RoundedPanel(int cornerRadius, Color backgroundColor) {
        this.cornerRadius = cornerRadius;
        this.backgroundColor = backgroundColor;
        this.drawBorder = false;
        this.borderColor = new Color(60, 70, 100);
        setOpaque(false);
    }

    /**
     * Mengatur radius sudut panel.
     * 
     * @param radius radius baru dalam pixel
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    /**
     * Mengatur warna background panel.
     * 
     * @param color warna background baru
     */
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        repaint();
    }

    /**
     * Mengatur apakah border digambar.
     * 
     * @param draw true untuk menggambar border
     */
    public void setDrawBorder(boolean draw) {
        this.drawBorder = draw;
        repaint();
    }

    /**
     * Mengatur warna border.
     * 
     * @param color warna border baru
     */
    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color bg = backgroundColor != null ? backgroundColor : getBackground();
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        if (drawBorder) {
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
