package com.hms.view;

import javax.swing.*;
import java.awt.*;

import com.hms.dao.ReportDAO;
import java.util.Map;

public class ReportPanel extends JPanel {
    private ReportDAO reportDAO = new ReportDAO();
    private JLabel lblTotalRevenue, lblTotalBookings, lblOccupancyRate;

    public ReportPanel() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Main Glass Card
        JPanel mainCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 200)); // Darker Midnight Blue
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        mainCard.setOpaque(false);
        mainCard.setPreferredSize(new Dimension(1000, 600));
        mainCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("BUSINESS REPORTS & ANALYTICS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        mainCard.add(title, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0));

        statsPanel.add(createStatCard("Total Revenue", lblTotalRevenue = new JLabel("$0.00"), new Color(76, 175, 80)));
        statsPanel.add(createStatCard("Total Bookings", lblTotalBookings = new JLabel("0"), new Color(33, 150, 243)));
        statsPanel.add(createStatCard("Occupancy Rate", lblOccupancyRate = new JLabel("0%"), new Color(255, 152, 0)));

        mainCard.add(statsPanel, BorderLayout.CENTER);
        
        add(mainCard, new GridBagConstraints());
    }

    private void loadData() {
        Map<String, Object> stats = reportDAO.getStats();
        lblTotalRevenue.setText(String.format("$%.2f", (Double)stats.getOrDefault("revenue", 0.0)));
        lblOccupancyRate.setText(String.format("%.1f%%", (Double)stats.getOrDefault("occupancy", 0.0)));
        lblTotalBookings.setText(stats.getOrDefault("bookings", 0).toString());
    }

    private JPanel createStatCard(String title, JLabel lblValue, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20)); // Subtle white highlight
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblTitle.setForeground(Color.WHITE); // Changed from GRAY to WHITE
        card.add(lblTitle, BorderLayout.NORTH);

        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(color);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }
}
