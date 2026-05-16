package com.hms.view;

import com.hms.util.BackgroundPanel;
import javax.swing.*;
import java.awt.*;

public class LandingFrame extends JFrame {

    public LandingFrame() {
        setTitle("Hotel Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Background Panel
        BackgroundPanel bgPanel = new BackgroundPanel("/images/landing.png");
        bgPanel.setLayout(null);
        setContentPane(bgPanel);

        // Buttons Container
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setOpaque(false);
        btnPanel.setBounds(50, 100, 200, 300);

        btnPanel.add(createNavButton("Login", new Color(46, 204, 113), "🔑"));
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(createNavButton("Signup", new Color(52, 152, 219), "👤"));
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(createNavButton("Kiosk", new Color(149, 165, 166), "🖥️"));
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(createNavButton("Exit", new Color(155, 89, 182), "🚪"));
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(createNavButton("Admin", new Color(230, 126, 34), "⚙️"));

        bgPanel.add(btnPanel);
    }

    private JButton createNavButton(String text, Color bg, String icon) {
        JButton btn = new JButton(icon + "  " + text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(150, 50));
        btn.setMaximumSize(new Dimension(150, 50));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btn.addActionListener(e -> {
            if (text.equals("Login") || text.equals("Admin")) {
                new LoginFrame().setVisible(true);
                this.dispose();
            } else if (text.equals("Signup")) {
                new SignupFrame().setVisible(true);
                this.dispose();
            } else if (text.equals("Kiosk")) {
                new GuestKioskFrame().setVisible(true);
            } else if (text.equals("Exit")) {
                System.exit(0);
            }
        });

        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LandingFrame().setVisible(true));
    }
}
