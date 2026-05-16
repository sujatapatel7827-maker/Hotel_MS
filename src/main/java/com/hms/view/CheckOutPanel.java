package com.hms.view;

import com.hms.dao.BookingDAO;
import com.hms.dao.RoomDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CheckOutPanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    
    private JTextField txtRoomSearch, txtName, txtInDate, txtOutDate, txtMobile, txtPrice, txtDays, txtTotal, txtEmail;
    private JButton btnSearch, btnCheckOut, btnLoad;

    public CheckOutPanel() {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Make main panel transparent to show pool image

        // Main Glass Card
        JPanel mainCard = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 180)); // Midnight Blue Glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        mainCard.setOpaque(false);
        mainCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        UIManager.put("Label.foreground", Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("CHECK-OUT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainCard.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);

        // Form Row 1
        gbc.gridy = 1; gbc.gridx = 0; mainCard.add(new JLabel("Room No:"), gbc);
        txtRoomSearch = new JTextField(10);
        gbc.gridx = 1; mainCard.add(txtRoomSearch, gbc);
        btnSearch = new JButton("Search");
        btnSearch.setBackground(Color.BLACK);
        btnSearch.setForeground(Color.WHITE);
        gbc.gridx = 2; mainCard.add(btnSearch, gbc);

        // Fields
        String[] labels = {"Name:", "Check-in:", "Check-out:", "Mobile:", "Price:", "Days:", "Total:", "Email:"};
        JTextField[] fields = {
            txtName = new JTextField(15), txtInDate = new JTextField(15),
            txtOutDate = new JTextField(15), txtMobile = new JTextField(15),
            txtPrice = new JTextField(15), txtDays = new JTextField(15),
            txtTotal = new JTextField(15), txtEmail = new JTextField(15)
        };

        for (int i = 0; i < 4; i++) {
            gbc.gridy = i + 2;
            gbc.gridx = 0; mainCard.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; mainCard.add(fields[i], gbc);
            gbc.gridx = 2; mainCard.add(new JLabel(labels[i+4]), gbc);
            gbc.gridx = 3; mainCard.add(fields[i+4], gbc);
        }

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);
        btnCheckOut = new JButton("Check-out");
        btnCheckOut.setBackground(Color.BLACK);
        btnCheckOut.setForeground(Color.WHITE);
        btnCheckOut.setPreferredSize(new Dimension(150, 40));
        
        btnLoad = new JButton("Load Data");
        btnLoad.setBackground(Color.BLACK);
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setPreferredSize(new Dimension(150, 40));
        
        btnPanel.add(btnCheckOut);
        btnPanel.add(btnLoad);

        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 0, 0, 0);
        mainCard.add(btnPanel, gbc);

        // Add main card to center
        add(mainCard, new GridBagConstraints());

        // Actions
        btnCheckOut.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "do you want to print bill", "Select an Option", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Printing Bill...");
                new FeedbackDialog((Frame) SwingUtilities.getWindowAncestor(this), 1).setVisible(true);
            }
        });
        loadDummyData();
    }

    private void loadDummyData() {
        // This is a placeholder since the table was removed from UI in card layout
        // If you need the table back, we should put it in a split pane or below the card
    }
}
