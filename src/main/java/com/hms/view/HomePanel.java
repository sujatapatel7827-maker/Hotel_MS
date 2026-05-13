package com.hms.view;

import com.hms.model.User;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    private User currentUser;

    public HomePanel(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        JLabel lblWelcome = new JLabel("Welcome, " + currentUser.getFullName());
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        // Stats Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        cardsPanel.add(createStatCard("Total Rooms", "50", new Color(103, 58, 183)));
        cardsPanel.add(createStatCard("Available", "32", new Color(76, 175, 80)));
        cardsPanel.add(createStatCard("Occupied", "15", new Color(244, 67, 54)));
        cardsPanel.add(createStatCard("Today's Revenue", "$4,500", new Color(33, 150, 243)));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String value, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(Color.GRAY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(bgColor);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createVerticalStrut(10));
        card.add(lblValue);

        return card;
    }
}
