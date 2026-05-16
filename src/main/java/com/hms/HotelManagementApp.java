package com.hms;

import com.formdev.flatlaf.FlatLightLaf;
import com.hms.util.DatabaseInitializer;
import com.hms.view.LoginFrame;
import com.hms.view.LandingFrame;

import javax.swing.*;
import java.awt.*;

public class HotelManagementApp {
    public static void main(String[] args) {
        // Set up Modern Look and Feel
        try {
            // Enable custom window decorations for a more modern look
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            // Customizing global UI properties for a premium feel
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.thumbArc", 999);
            UIManager.put("ProgressBar.arc", 999);

            // Global colors for Dialogs and OptionPanes
            Color darkBlue = new Color(15, 32, 64);
            UIManager.put("OptionPane.background", darkBlue);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Panel.background", darkBlue);
            UIManager.put("Label.foreground", Color.WHITE);
            UIManager.put("Button.background", new Color(63, 81, 181));
            UIManager.put("Button.foreground", Color.WHITE);
            
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Initialize Database
        DatabaseInitializer.initialize();

        // Launch Application
        EventQueue.invokeLater(() -> {
            new LandingFrame().setVisible(true);
        });
    }
}
