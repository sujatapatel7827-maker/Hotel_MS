package com.hms.view;

import com.hms.model.User;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("HMS Dashboard - " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Main Layout
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add Panels
        contentPanel.add(new HomePanel(currentUser), "Home");
        contentPanel.add(new RoomManagementPanel(), "Rooms");
        contentPanel.add(new GuestManagementPanel(), "Guests");
        contentPanel.add(new BookingPanel(), "Bookings");
        
        if (currentUser.getRole() == User.Role.ADMIN) {
            contentPanel.add(new ReportPanel(), "Reports");
            contentPanel.add(new UserManagementPanel(), "Users");
        }

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBackground(new Color(33, 33, 33));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Sidebar Header
        JLabel lblBrand = new JLabel("Grand Hotel");
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBrand.setBorder(BorderFactory.createEmptyBorder(30, 0, 50, 0));
        panel.add(lblBrand);

        // Navigation Buttons
        panel.add(createNavButton("Home", "Home"));
        panel.add(createNavButton("Rooms", "Rooms"));
        panel.add(createNavButton("Guests", "Guests"));
        panel.add(createNavButton("Bookings", "Bookings"));

        if (currentUser.getRole() == User.Role.ADMIN) {
            panel.add(createNavButton("Reports", "Reports"));
            panel.add(createNavButton("Users", "Users"));
        }

        panel.add(Box.createVerticalGlue());

        // Logout
        JButton btnLogout = createNavButton("Logout", "Logout");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        panel.add(btnLogout);

        return panel;
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(new Color(33, 33, 33));
        btn.setForeground(Color.LIGHT_GRAY);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 20, 0, 0));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(50, 50, 50));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(33, 33, 33));
                btn.setForeground(Color.LIGHT_GRAY);
            }
        });

        btn.addActionListener(e -> {
            if (!cardName.equals("Logout")) {
                cardLayout.show(contentPanel, cardName);
            }
        });

        return btn;
    }
}
