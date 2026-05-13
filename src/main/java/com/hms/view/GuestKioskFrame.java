package com.hms.view;

import com.hms.dao.BookingDAO;
import com.hms.model.Booking;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GuestKioskFrame extends JFrame {
    private BookingDAO bookingDAO = new BookingDAO();

    public GuestKioskFrame() {
        setTitle("Hotel Self-Service Kiosk");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel header = new JPanel();
        header.setBackground(new Color(63, 81, 181));
        header.add(new JLabel("<html><h1 style='color:white'>Welcome to Grand Hotel Kiosk</h1></html>"));
        add(header, BorderLayout.NORTH);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel lbl = new JLabel("Enter your Phone Number to check booking status:", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0; main.add(lbl, gbc);

        JTextField txtPhone = new JTextField(20);
        txtPhone.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 1; main.add(txtPhone, gbc);

        JButton btnSearch = new JButton("Find My Booking");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSearch.setBackground(new Color(76, 175, 80));
        btnSearch.setForeground(Color.WHITE);
        gbc.gridy = 2; main.add(btnSearch, gbc);

        JTextArea txtResult = new JTextArea(8, 30);
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtResult.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        gbc.gridy = 3; main.add(new JScrollPane(txtResult), gbc);

        btnSearch.addActionListener(e -> {
            String phone = txtPhone.getText().trim();
            if (phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a phone number!");
                return;
            }
            
            txtResult.setText("Searching for bookings...");

            try (java.sql.Connection conn = com.hms.util.DatabaseConnection.getConnection()) {
                String query = "SELECT b.room_number, b.status, b.check_in_date, g.first_name " +
                               "FROM bookings b JOIN guests g ON b.guest_id = g.id " +
                               "WHERE g.phone = ?";
                java.sql.PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, phone);
                java.sql.ResultSet rs = pstmt.executeQuery();

                StringBuilder sb = new StringBuilder();
                boolean found = false;
                while (rs.next()) {
                    if (!found) sb.append("WELCOME ").append(rs.getString("first_name").toUpperCase()).append("!\n\n");
                    sb.append("Room Number : ").append(rs.getString("room_number")).append("\n")
                      .append("Status      : ").append(rs.getString("status")).append("\n")
                      .append("Check-in    : ").append(rs.getDate("check_in_date")).append("\n")
                      .append("--------------------------------\n");
                    found = true;
                }
                
                if (found) {
                    txtResult.setText(sb.toString());
                } else {
                    txtResult.setText("No active booking found for:\n" + phone + 
                        "\n\nHint: Please ensure you have booked a room for this guest via Admin Panel first.");
                }
            } catch (java.sql.SQLException ex) {
                txtResult.setText("Database Error: " + ex.getMessage());
            }
        });

        gbc.gridy = 3; 
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        main.add(new JScrollPane(txtResult), gbc);

        add(main, BorderLayout.CENTER);

        JButton btnBack = new JButton("Back to Login");
        btnBack.addActionListener(e -> dispose());
        add(btnBack, BorderLayout.SOUTH);
    }
}
