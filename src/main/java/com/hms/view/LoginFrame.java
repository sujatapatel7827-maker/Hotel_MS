package com.hms.view;

import com.hms.dao.UserDAO;
import com.hms.model.User;
import com.hms.util.BackgroundPanel;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Hotel Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500); // Larger size for background view
        setLocationRelativeTo(null);
        setResizable(false);

        // Use BackgroundPanel with landing image
        BackgroundPanel bgPanel = new BackgroundPanel("/images/landing.png");
        bgPanel.setLayout(new GridBagLayout());

        // Center Login Card
        JPanel loginCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 220)); // Midnight Blue Glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        loginCard.setLayout(new BorderLayout(20, 0));
        loginCard.setOpaque(false);
        loginCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Form Panel (Left side of card)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 5, 0);

        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        btnPanel.setOpaque(false);
        btnLogin = new JButton("Login");
        btnLogin.setBackground(Color.BLACK);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(100, 35));

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 35));

        btnPanel.add(btnLogin);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        loginCard.add(formPanel, BorderLayout.CENTER);

        // Person Icon (Right side of card)
        // Using a placeholder or label if icon not available, but let's try to simulate the image
        JLabel lblIcon = new JLabel();
        try {
            lblIcon.setText("👤"); 
            lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 120));
            lblIcon.setForeground(Color.WHITE);
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            lblIcon.setPreferredSize(new Dimension(150, 150));
        } catch (Exception e) {}
        loginCard.add(lblIcon, BorderLayout.EAST);

        bgPanel.add(loginCard);
        add(bgPanel);

        // Listeners
        btnLogin.addActionListener(e -> handleLogin());
        btnCancel.addActionListener(e -> {
            new LandingFrame().setVisible(true);
            dispose();
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Optional<User> authenticatedUser = userDAO.authenticate(username, password);
            if (authenticatedUser.isPresent()) {
                User user = authenticatedUser.get();
                dispose();
                new MainDashboard(user).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

