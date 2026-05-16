package com.hms.view;

import com.hms.dao.BookingDAO;
import com.hms.dao.PaymentDAO;
import javax.swing.*;
import java.awt.*;

public class BookingPanel extends JPanel {
    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;
    
    private JTextField txtIdDetails, txtName, txtCountry, txtEmail, txtMobile, txtDate, txtBedType, txtRoomType, txtPrice;
    private JComboBox<String> cmbId, cmbGender, cmbRoomNo;
    private JButton btnAdd, btnClose, btnPrice;

    public BookingPanel() {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Make main panel transparent to show pool image

        // Form Panel (Glass Effect Card)
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 180)); // Midnight Blue Glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("CHECK-IN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 20, 0);
        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Styling for all labels in this panel
        UIManager.put("Label.foreground", Color.WHITE);

        // Row 1
        gbc.gridy = 1; gbc.gridx = 0; formPanel.add(new JLabel("ID:"), gbc);
        cmbId = new JComboBox<>(new String[]{"Passport", "Aadhar Card", "Driving License"});
        gbc.gridx = 1; formPanel.add(cmbId, gbc);
        
        gbc.gridx = 2; formPanel.add(new JLabel("ID Details:"), gbc);
        txtIdDetails = new JTextField(15);
        gbc.gridx = 3; formPanel.add(txtIdDetails, gbc);

        // Row 2
        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Name:"), gbc);
        txtName = new JTextField(15);
        gbc.gridx = 1; formPanel.add(txtName, gbc);
        
        gbc.gridx = 2; formPanel.add(new JLabel("Gender:"), gbc);
        cmbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        gbc.gridx = 3; formPanel.add(cmbGender, gbc);

        // Row 3
        gbc.gridy = 3; gbc.gridx = 0; formPanel.add(new JLabel("Mobile No:"), gbc);
        txtMobile = new JTextField(15);
        gbc.gridx = 1; formPanel.add(txtMobile, gbc);
        
        gbc.gridx = 2; formPanel.add(new JLabel("Country:"), gbc);
        txtCountry = new JTextField(15);
        gbc.gridx = 3; formPanel.add(txtCountry, gbc);

        // Row 4
        gbc.gridy = 4; gbc.gridx = 0; formPanel.add(new JLabel("Check-In Date:"), gbc);
        txtDate = new JTextField(15);
        gbc.gridx = 1; formPanel.add(txtDate, gbc);
        
        gbc.gridx = 2; formPanel.add(new JLabel("Room Number:"), gbc);
        cmbRoomNo = new JComboBox<>(new String[]{"1", "2", "3", "4", "5"});
        gbc.gridx = 3; formPanel.add(cmbRoomNo, gbc);

        // Row 5
        gbc.gridy = 5; gbc.gridx = 0; formPanel.add(new JLabel("Room Type:"), gbc);
        txtRoomType = new JTextField(15);
        gbc.gridx = 1; formPanel.add(txtRoomType, gbc);
        
        gbc.gridx = 2; formPanel.add(new JLabel("Price:"), gbc);
        txtPrice = new JTextField(15);
        gbc.gridx = 3; formPanel.add(txtPrice, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setOpaque(false);
        
        btnAdd = new JButton("Add");
        btnAdd.setBackground(Color.BLACK);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(120, 40));
        
        btnClose = new JButton("Close");
        btnClose.setBackground(Color.BLACK);
        btnClose.setForeground(Color.WHITE);
        btnClose.setPreferredSize(new Dimension(120, 40));

        btnPrice = new JButton("Price");
        btnPrice.setBackground(Color.BLACK);
        btnPrice.setForeground(Color.WHITE);
        btnPrice.setPreferredSize(new Dimension(120, 40));

        btnPanel.add(btnAdd);
        btnPanel.add(btnClose);
        btnPanel.add(btnPrice);

        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 4;
        gbc.insets = new Insets(30, 0, 0, 0);
        formPanel.add(btnPanel, gbc);

        // Add form to center of main panel
        add(formPanel, new GridBagConstraints());

        // Listeners
        btnAdd.addActionListener(e -> handleBooking());
        btnPrice.addActionListener(e -> {
            // Simple price logic based on room number for now
            String room = (String) cmbRoomNo.getSelectedItem();
            txtPrice.setText("1500"); // Fixed price for testing
        });
        btnClose.addActionListener(e -> clearForm());
    }

    private void handleBooking() {
        if (txtName.getText().isEmpty() || txtMobile.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Guest Name and Mobile");
            return;
        }
        try {
            com.hms.model.Booking booking = new com.hms.model.Booking();
            booking.setGuestId(1); // Default for demo, should be looked up from guests table
            booking.setRoomNumber((String) cmbRoomNo.getSelectedItem());
            booking.setCheckInDate(new java.sql.Date(System.currentTimeMillis()));
            booking.setCheckOutDate(new java.sql.Date(System.currentTimeMillis() + 86400000)); // +1 day
            booking.setTotalAmount(Double.parseDouble(txtPrice.getText().isEmpty() ? "0" : txtPrice.getText()));
            booking.setStatus(com.hms.model.Booking.Status.CONFIRMED);

            if (bookingDAO.createBooking(booking)) {
                JOptionPane.showMessageDialog(this, "Booking Successful for " + txtName.getText());
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Booking Failed: Check if room is available.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtMobile.setText("");
        txtIdDetails.setText("");
        txtCountry.setText("");
        txtEmail.setText("");
        txtDate.setText("");
        txtPrice.setText("");
        txtRoomType.setText("");
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return btn;
    }
}


