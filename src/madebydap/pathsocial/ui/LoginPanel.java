package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.User;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Panel untuk login dan registrasi pengguna.
 * Mendukung mode login dan mode registrasi yang dapat diswitch.
 * 
 * @author madebydap
 * @version 1.0
 */
public class LoginPanel extends JPanel {
    
    /** Referensi ke frame utama */
    private final MainFrame mainFrame;
    
    /** Field input username */
    private JTextField usernameField;
    
    /** Field input password */
    private JPasswordField passwordField;
    
    /** Field input display name (hanya untuk registrasi) */
    private JTextField displayNameField;
    
    /** Flag mode registrasi */
    private boolean isRegisterMode = false;
    
    /** Container untuk field display name */
    private JPanel displayNameContainer;
    
    /** Tombol aksi (Sign In / Create Account) */
    private JButton actionButton;
    
    /** Label untuk switch mode */
    private JLabel switchModeLabel;

    /**
     * Konstruktor LoginPanel.
     * 
     * @param mainFrame referensi ke frame utama
     */
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(PathColors.BACKGROUND_WHITE);
        setLayout(new GridBagLayout());
        initComponents();
    }

    /**
     * Menginisialisasi komponen UI.
     */
    private void initComponents() {
        JPanel container = new JPanel();
        container.setBackground(PathColors.BACKGROUND_WHITE);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        // Logo
        JLabel logoLabel = new JLabel("path");
        logoLabel.setFont(new Font("Georgia", Font.ITALIC, 48));
        logoLabel.setForeground(PathColors.PRIMARY);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(logoLabel);

        container.add(Box.createVerticalStrut(8));

        JLabel taglineLabel = new JLabel("The smart journal");
        taglineLabel.setFont(PathFonts.SMALL);
        taglineLabel.setForeground(PathColors.TEXT_MUTED);
        taglineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(taglineLabel);

        container.add(Box.createVerticalStrut(50));

        // Username
        container.add(createFieldLabel("Username"));
        usernameField = createTextField();
        container.add(usernameField);
        container.add(Box.createVerticalStrut(16));

        // Password
        container.add(createFieldLabel("Password"));
        passwordField = createPasswordField();
        container.add(passwordField);
        container.add(Box.createVerticalStrut(16));

        // Display name (register only)
        displayNameContainer = new JPanel();
        displayNameContainer.setLayout(new BoxLayout(displayNameContainer, BoxLayout.Y_AXIS));
        displayNameContainer.setBackground(PathColors.BACKGROUND_WHITE);
        displayNameContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        displayNameContainer.add(createFieldLabel("Display Name"));
        displayNameField = createTextField();
        displayNameContainer.add(displayNameField);
        displayNameContainer.add(Box.createVerticalStrut(16));
        displayNameContainer.setVisible(false);
        displayNameContainer.setMaximumSize(new Dimension(260, 70));
        container.add(displayNameContainer);

        container.add(Box.createVerticalStrut(10));

        // Action button
        actionButton = createPrimaryButton("Sign In");
        actionButton.addActionListener(e -> handleAction());
        container.add(actionButton);

        container.add(Box.createVerticalStrut(20));

        // Switch mode
        switchModeLabel = new JLabel("Create an account");
        switchModeLabel.setFont(PathFonts.BODY);
        switchModeLabel.setForeground(PathColors.PRIMARY);
        switchModeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchModeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchModeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                toggleMode();
            }
        });
        container.add(switchModeLabel);

        container.add(Box.createVerticalStrut(30));

        // Clear data option
        JLabel clearDataLabel = new JLabel("Clear All Data");
        clearDataLabel.setFont(PathFonts.SMALL);
        clearDataLabel.setForeground(PathColors.ERROR);
        clearDataLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearDataLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearDataLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleClearData();
            }
        });
        container.add(clearDataLabel);

        add(container);
    }

    /**
     * Membuat label untuk field input.
     * 
     * @param text teks label
     * @return JLabel yang sudah di-style
     */
    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(PathFonts.SMALL);
        label.setForeground(PathColors.TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return label;
    }

    /**
     * Membuat text field dengan styling standar.
     * 
     * @return JTextField yang sudah di-style
     */
    private JTextField createTextField() {
        JTextField field = new JTextField(18);
        field.setFont(PathFonts.BODY);
        field.setForeground(PathColors.TEXT_PRIMARY);
        field.setBackground(PathColors.BACKGROUND_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.BORDER),
            BorderFactory.createEmptyBorder(8, 4, 8, 4)
        ));
        field.setMaximumSize(new Dimension(260, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }

    /**
     * Membuat password field dengan styling standar.
     * 
     * @return JPasswordField yang sudah di-style
     */
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(18);
        field.setFont(PathFonts.BODY);
        field.setForeground(PathColors.TEXT_PRIMARY);
        field.setBackground(PathColors.BACKGROUND_WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, PathColors.BORDER),
            BorderFactory.createEmptyBorder(8, 4, 8, 4)
        ));
        field.setMaximumSize(new Dimension(260, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        field.setHorizontalAlignment(JTextField.CENTER);
        return field;
    }

    /**
     * Membuat tombol primary dengan styling Path.
     * 
     * @param text teks tombol
     * @return JButton yang sudah di-style
     */
    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(PathColors.PRIMARY_DARK);
                } else {
                    g2.setColor(PathColors.PRIMARY);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                g2.setColor(Color.WHITE);
                g2.setFont(PathFonts.BODY_BOLD);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };
        button.setPreferredSize(new Dimension(260, 44));
        button.setMaximumSize(new Dimension(260, 44));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Toggle antara mode login dan registrasi.
     */
    private void toggleMode() {
        isRegisterMode = !isRegisterMode;
        displayNameContainer.setVisible(isRegisterMode);
        actionButton.setText(isRegisterMode ? "Create Account" : "Sign In");
        switchModeLabel.setText(isRegisterMode ? "Already have an account? Sign in" : "Create an account");
        revalidate();
        repaint();
    }

    /**
     * Handler untuk tombol aksi.
     * Melakukan login atau registrasi sesuai mode.
     */
    private void handleAction() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        DataStore dataStore = DataStore.getInstance();

        if (isRegisterMode) {
            String displayName = displayNameField.getText().trim();
            if (displayName.isEmpty()) {
                showError("Please enter a display name");
                return;
            }

            User user = dataStore.register(username, password, displayName);
            if (user == null) {
                showError("Username already exists");
                return;
            }

            dataStore.login(username, password);
            mainFrame.showPanel("timeline");
        } else {
            User user = dataStore.login(username, password);
            if (user == null) {
                showError("Invalid username or password");
                return;
            }
            mainFrame.showPanel("timeline");
        }
    }

    /**
     * Menampilkan dialog error.
     * 
     * @param message pesan error
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Mereset form ke kondisi awal.
     */
    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        displayNameField.setText("");
        isRegisterMode = false;
        displayNameContainer.setVisible(false);
        actionButton.setText("Sign In");
        switchModeLabel.setText("Create an account");
    }

    /**
     * Handler untuk menghapus semua data.
     * Menampilkan konfirmasi sebelum menghapus.
     */
    private void handleClearData() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "This will delete ALL saved data including:\n• All users and accounts\n• All moments and posts\n• All uploaded images\n\nAre you sure?",
            "Clear All Data",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.getInstance().clearAllData();
            JOptionPane.showMessageDialog(
                this,
                "All data has been cleared.\nSample users have been recreated.",
                "Data Cleared",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
