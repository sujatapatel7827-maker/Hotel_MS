package com.hms.view;

import com.hms.model.User;
import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel(User user) {
        setOpaque(false); // Make transparent to show MainDashboard background
        setLayout(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Hotel Management System");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 72));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblWelcome = new JLabel("Welcome, " + user.getFullName());
        lblWelcome.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        lblWelcome.setForeground(new Color(240, 240, 240));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(lblTitle);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(lblWelcome);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(centerPanel, gbc);
    }
}
