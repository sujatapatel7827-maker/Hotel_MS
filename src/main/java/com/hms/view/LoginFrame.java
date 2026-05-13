package com.hms.view;

import com.hms.dao.UserDAO;
import com.hms.model.User;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Hotel Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Logo/Header
        JLabel lblTitle = new JLabel("Welcome Back", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        JLabel lblSubTitle = new JLabel("Please login to your account", SwingConstants.CENTER);
        lblSubTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubTitle.setForeground(Color.GRAY);
        gbc.gridy = 1;
        mainPanel.add(lblSubTitle, gbc);

        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Username"), gbc);

        txtUsername = new JTextField(20);
        gbc.gridy = 3;
        mainPanel.add(txtUsername, gbc);

        // Password
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Password"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridy = 5;
        mainPanel.add(txtPassword, gbc);

        // Login Button
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBackground(new Color(63, 81, 181));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 10, 10, 10);
        mainPanel.add(btnLogin, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 10, 10, 10);
        JButton btnKiosk = new JButton("Guest Kiosk Mode (Check Booking Status)");
        btnKiosk.setBorder(BorderFactory.createEmptyBorder());
        btnKiosk.setContentAreaFilled(false);
        btnKiosk.setForeground(new Color(63, 81, 181));
        btnKiosk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnKiosk.addActionListener(e -> new GuestKioskFrame().setVisible(true));
        mainPanel.add(btnKiosk, gbc);

        btnLogin.addActionListener(e -> handleLogin());

        add(mainPanel);
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
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + user.getFullName());
                dispose();
                // Open Dashboard
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
