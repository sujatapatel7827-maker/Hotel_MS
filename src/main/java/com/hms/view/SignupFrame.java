package com.hms.view;

import com.hms.dao.UserDAO;
import com.hms.model.User;
import com.hms.util.BackgroundPanel;
import javax.swing.*;
import java.awt.*;

public class SignupFrame extends JFrame {
    private JTextField txtUsername, txtFullName, txtEmail;
    private JPasswordField txtPassword;
    private JButton btnSignup, btnCancel;
    private UserDAO userDAO;

    public SignupFrame() {
        userDAO = new UserDAO();
        initComponents();
    }

    private void initComponents() {
        setTitle("Hotel Management System - Signup");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel bgPanel = new BackgroundPanel("/images/landing.png");
        bgPanel.setLayout(new GridBagLayout());

        JPanel signupCard = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 220)); // Midnight Blue Glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        signupCard.setOpaque(false);
        signupCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 5, 0);

        JLabel lblTitle = new JLabel("CREATE ACCOUNT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; formPanel.add(new JLabel("Full Name:"), gbc);
        txtFullName = new JTextField(15); gbc.gridx = 1; formPanel.add(txtFullName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(15); gbc.gridx = 1; formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15); gbc.gridx = 1; formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15); gbc.gridx = 1; formPanel.add(txtPassword, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        btnPanel.setOpaque(false);
        btnSignup = new JButton("Signup");
        btnSignup.setBackground(Color.BLACK);
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setPreferredSize(new Dimension(100, 35));

        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(Color.BLACK);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 35));

        btnPanel.add(btnSignup);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        signupCard.add(formPanel, BorderLayout.CENTER);
        bgPanel.add(signupCard);
        add(bgPanel);

        btnSignup.addActionListener(e -> handleSignup());
        btnCancel.addActionListener(e -> {
            new LandingFrame().setVisible(true);
            dispose();
        });
    }

    private void handleSignup() {
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(User.Role.STAFF); // Default role

        if (userDAO.createUser(user)) {
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            new LandingFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Signup failed. Username might be taken.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
