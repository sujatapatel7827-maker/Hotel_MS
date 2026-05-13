package com.hms;

import com.formdev.flatlaf.FlatLightLaf;
import com.hms.util.DatabaseInitializer;
import com.hms.view.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class HotelManagementApp {
    public static void main(String[] args) {
        // Set up Modern Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Customizing global UI properties for a premium feel
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ProgressBar.arc", 999);
            
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Initialize Database
        DatabaseInitializer.initialize();

        // Launch Application
        EventQueue.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
