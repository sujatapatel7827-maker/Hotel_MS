package com.hms.view;

import com.hms.model.User;
import com.hms.util.BackgroundPanel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainDashboard extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<JButton> navButtons = new ArrayList<>();

    public MainDashboard(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Hotel Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in full screen

        // Main Layout
        setLayout(new BorderLayout());

        // Top Navigation Bar
        JPanel topNavBar = createTopNavBar();
        add(topNavBar, BorderLayout.NORTH);

        // Content Area with Background
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setOpaque(false); // Make content panel transparent to show frame background

        // Add Panels
        contentPanel.add(new HomePanel(currentUser), "Home");
        contentPanel.add(new RoomManagementPanel(), "Rooms");
        contentPanel.add(new GuestManagementPanel(), "Guests");
        contentPanel.add(new BookingPanel(), "Check-in");
        contentPanel.add(new CheckOutPanel(), "Check-out");
        contentPanel.add(new BillsPanel(), "Bills");
        
        if (currentUser.getRole() == User.Role.ADMIN) {
            contentPanel.add(new ReportPanel(), "Reports");
            contentPanel.add(new UserManagementPanel(), "Users");
        }

        // Use BackgroundPanel as the main container for content
        BackgroundPanel mainBg = new BackgroundPanel("/images/pool.png");
        mainBg.add(contentPanel, BorderLayout.CENTER);
        
        add(mainBg, BorderLayout.CENTER);
        
        // Set Home as initial active button
        if (!navButtons.isEmpty()) {
            setActiveButton(navButtons.get(0));
        }
    }

    private JPanel createTopNavBar() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(new Color(10, 25, 47)); // Dark Midnight Blue
        panel.setPreferredSize(new Dimension(0, 70));

        // Navigation Buttons
        panel.add(createNavButton("Home", "Home", "🏠"));
        panel.add(createNavButton("Check-in", "Check-in", "🏨"));
        panel.add(createNavButton("Check-out", "Check-out", "🔑"));
        panel.add(createNavButton("Bills", "Bills", "🧾"));
        panel.add(createNavButton("Rooms", "Rooms", "🛏️"));
        panel.add(createNavButton("Guests", "Guests", "👥"));

        if (currentUser.getRole() == User.Role.ADMIN) {
            panel.add(createNavButton("Reports", "Reports", "📊"));
            panel.add(createNavButton("Staff", "Users", "👮"));
        }

        panel.add(Box.createHorizontalGlue());

        // Logout
        JButton btnLogout = createNavButton("Logout", "Logout", "🚪");
        btnLogout.addActionListener(e -> {
            dispose();
            new LandingFrame().setVisible(true); // Back to Landing Frame
        });
        panel.add(btnLogout);

        return panel;
    }

    private JButton createNavButton(String text, String cardName, String icon) {
        JButton btn = new JButton(icon + " " + text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(new Color(25, 50, 100));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(8, 20, 8, 20));

        if (!text.equals("Logout")) {
            navButtons.add(btn);
        }

        btn.addActionListener(e -> {
            if (!cardName.equals("Logout")) {
                cardLayout.show(contentPanel, cardName);
                setActiveButton(btn);
            }
        });

        return btn;
    }

    private void setActiveButton(JButton activeButton) {
        for (JButton btn : navButtons) {
            if (btn == activeButton) {
                btn.setBackground(new Color(0, 150, 255)); // Electric Blue for active
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(new Color(25, 50, 100)); // Dark Blue for inactive
                btn.setForeground(Color.WHITE);
            }
        }
    }
}

