package com.hms.view;

import javax.swing.*;
import java.awt.*;

import com.hms.dao.ReportDAO;
import java.util.Map;

public class ReportPanel extends JPanel {
    private ReportDAO reportDAO = new ReportDAO();
    private JLabel lblRevenue, lblOccupancy, lblBookings;

    public ReportPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Business Reports & Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        Map<String, Object> stats = reportDAO.getStats();

        lblRevenue = new JLabel(String.format("$%.2f", (Double)stats.getOrDefault("revenue", 0.0)));
        lblOccupancy = new JLabel(String.format("%.1f%%", (Double)stats.getOrDefault("occupancy", 0.0)));
        lblBookings = new JLabel(stats.getOrDefault("bookings", 0).toString());

        cardPanel.add(createStatCard("Total Revenue", lblRevenue, new Color(76, 175, 80)));
        cardPanel.add(createStatCard("Occupancy Rate", lblOccupancy, new Color(33, 150, 243)));
        cardPanel.add(createStatCard("Total Bookings", lblBookings, new Color(255, 152, 0)));

        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel lblValue, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(Color.GRAY);
        card.add(lblTitle, BorderLayout.NORTH);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(color);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
