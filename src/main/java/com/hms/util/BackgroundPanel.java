package com.hms.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String resourcePath) {
        try {
            URL imgUrl = getClass().getResource(resourcePath);
            if (imgUrl != null) {
                backgroundImage = new ImageIcon(imgUrl).getImage();
            } else {
                System.err.println("Could not find image: " + resourcePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            
            // Add a slight dark overlay to make text more readable if needed
            g.setColor(new Color(0, 0, 0, 30)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
