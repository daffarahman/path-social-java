package madebydap.pathsocial.ui;

import madebydap.pathsocial.ui.style.PathColors;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract class yang menjadi parent dari semua panel konten di Path Social.
 * Menyediakan template method untuk inisialisasi UI dan mengimplementasikan Refreshable.
 * 
 * @author madebydap
 * @version 1.0
 */
public abstract class BasePanel extends JPanel implements Refreshable {
    
    /**
     * Konstruktor BasePanel.
     * Menginisialisasi background, layout, dan memanggil initComponents().
     */
    public BasePanel() {
        setBackground(PathColors.BACKGROUND);
        setLayout(new BorderLayout());
        initComponents();
    }
    
    /**
     * Menginisialisasi komponen UI panel.
     * Method abstract yang harus diimplementasikan oleh subclass.
     */
    protected abstract void initComponents();
    
    /**
     * Membuat header panel.
     * Method abstract yang harus diimplementasikan oleh subclass.
     * 
     * @return JPanel header
     */
    protected abstract JPanel createHeader();
}
