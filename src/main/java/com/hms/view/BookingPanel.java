package com.hms.view;

import com.hms.dao.BookingDAO;
import com.hms.dao.PaymentDAO;
import com.hms.model.Booking;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class BookingPanel extends JPanel {
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;

    public BookingPanel() {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();
        initComponents();
        loadBookingData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblTitle = new JLabel("Booking Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(lblTitle, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        
        JButton btnNewBooking = new JButton("New Reservation");
        btnNewBooking.setBackground(new Color(33, 150, 243));
        btnNewBooking.setForeground(Color.WHITE);
        btnNewBooking.addActionListener(e -> showNewBookingDialog());
        btnPanel.add(btnNewBooking);

        JButton btnCheckOut = new JButton("Check-out");
        btnCheckOut.setBackground(new Color(244, 67, 54));
        btnCheckOut.setForeground(Color.WHITE);
        btnCheckOut.addActionListener(e -> handleCheckOut());
        btnPanel.add(btnCheckOut);

        header.add(btnPanel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Guest", "Room", "Check-in", "Check-out", "Status", "Amount"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);
        bookingTable.setRowHeight(35);
        bookingTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bookingTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadBookingData() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                    booking.getId(),
                    booking.getGuestName(),
                    booking.getRoomNumber(),
                    booking.getCheckInDate(),
                    booking.getCheckOutDate(),
                    booking.getStatus(),
                    String.format("$%.2f", booking.getTotalAmount())
            });
        }
    }

    private void showNewBookingDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Reservation", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtGuestId = new JTextField(20);
        JTextField txtRoomNo = new JTextField(20);
        JTextField txtIn = new JTextField("2026-05-13"); // Default today
        JTextField txtOut = new JTextField("2026-05-14");
        JTextField txtAmount = new JTextField(20);

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Guest ID:"), gbc);
        gbc.gridx = 1; dialog.add(txtGuestId, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1; dialog.add(txtRoomNo, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Check-in (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; dialog.add(txtIn, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Check-out (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; dialog.add(txtOut, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1; dialog.add(txtAmount, gbc);

        JButton btnSave = new JButton("Confirm Booking");
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = r++; gbc.gridwidth = 2;
        dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            try {
                if (txtGuestId.getText().trim().isEmpty() || txtRoomNo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Guest ID and Room Number are required!");
                    return;
                }

                Booking b = new Booking();
                b.setGuestId(Integer.parseInt(txtGuestId.getText().trim()));
                b.setRoomNumber(txtRoomNo.getText().trim());
                b.setCheckInDate(java.sql.Date.valueOf(txtIn.getText()));
                b.setCheckOutDate(java.sql.Date.valueOf(txtOut.getText()));
                b.setTotalAmount(Double.parseDouble(txtAmount.getText()));
                b.setStatus(Booking.Status.CONFIRMED);

                if (bookingDAO.createBooking(b)) {
                    JOptionPane.showMessageDialog(dialog, "Booking Confirmed!");
                    dialog.dispose();
                    loadBookingData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Booking Failed", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid data: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }

    private void handleCheckOut() {
        try {
            int selectedRow = bookingTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a booking to check-out");
                return;
            }

            Object idObj = tableModel.getValueAt(selectedRow, 0);
            if (idObj == null || idObj.toString().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Error: Selected booking has no ID. Cannot proceed.", "Critical Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bookingId = Integer.parseInt(idObj.toString());
            String roomNumber = tableModel.getValueAt(selectedRow, 2).toString();
            String status = tableModel.getValueAt(selectedRow, 5).toString();

            if ("CHECKED_OUT".equals(status)) {
                JOptionPane.showMessageDialog(this, "Already checked out!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to check-out Room " + roomNumber + "?", "Confirm Check-out", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                String extraStr = JOptionPane.showInputDialog(this, "Enter Extra Service Charges (if any):", "0.0");
                double extraCharges = 0;
                try {
                    extraCharges = Double.parseDouble(extraStr);
                } catch (Exception ex) {}

                double baseAmount = Double.parseDouble(tableModel.getValueAt(selectedRow, 6).toString().replace("$", ""));
                double totalAmount = baseAmount + extraCharges;
                String guestName = (String) tableModel.getValueAt(selectedRow, 1);

                // Open Payment Gateway
                PaymentGatewayDialog pg = new PaymentGatewayDialog((Frame)SwingUtilities.getWindowAncestor(this), totalAmount);
                pg.setVisible(true);

                if (!pg.isSuccessful()) {
                    JOptionPane.showMessageDialog(this, "Payment Cancelled or Failed.");
                    return;
                }

                if (bookingDAO.checkOut(bookingId, roomNumber)) {
                    paymentDAO.recordPayment(bookingId, totalAmount, "CASH");
                    try {
                        com.hms.util.InvoiceGenerator.generateInvoice(bookingId, guestName, roomNumber, totalAmount);
                        JOptionPane.showMessageDialog(this, "Checked-out Successfully!\nTotal Amount: $" + totalAmount + "\nInvoice saved to Desktop.");
                        
                        // Show Feedback Dialog
                        new FeedbackDialog((Frame)SwingUtilities.getWindowAncestor(this), bookingId).setVisible(true);
                    } catch (Throwable t) {
                        JOptionPane.showMessageDialog(this, "Checked-out Done, but Invoice failed: " + t.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                    loadBookingData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error during check-out", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "System Error: " + t.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            t.printStackTrace();
        }
    }
}
