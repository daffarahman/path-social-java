package madebydap.pathsocial.ui;

import madebydap.pathsocial.data.DataStore;
import madebydap.pathsocial.model.User;
import madebydap.pathsocial.ui.components.RoundedPanel;
import madebydap.pathsocial.ui.style.PathColors;
import madebydap.pathsocial.ui.style.PathFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Login and registration panel - clean, minimal design.
 */
public class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField displayNameField;
    private boolean isRegisterMode = false;
    private JPanel displayNameContainer;
    private JButton actionButton;
    private JLabel switchModeLabel;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(PathColors.BACKGROUND_WHITE);
        setLayout(new GridBagLayout());
        initComponents();
    }

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

        add(container);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(PathFonts.SMALL);
        label.setForeground(PathColors.TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return label;
    }

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

    private void toggleMode() {
        isRegisterMode = !isRegisterMode;
        displayNameContainer.setVisible(isRegisterMode);
        actionButton.setText(isRegisterMode ? "Create Account" : "Sign In");
        switchModeLabel.setText(isRegisterMode ? "Already have an account? Sign in" : "Create an account");
        revalidate();
        repaint();
    }

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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        displayNameField.setText("");
        isRegisterMode = false;
        displayNameContainer.setVisible(false);
        actionButton.setText("Sign In");
        switchModeLabel.setText("Create an account");
    }
}
