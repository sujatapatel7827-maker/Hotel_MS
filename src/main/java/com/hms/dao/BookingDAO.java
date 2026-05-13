package com.hms.dao;

import com.hms.model.Booking;
import com.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean isRoomAvailable(String roomNumber, Date checkIn, Date checkOut) {
        String query = "SELECT COUNT(*) FROM bookings WHERE room_number = ? " +
                       "AND status != 'CANCELLED' " +
                       "AND ((check_in_date BETWEEN ? AND ?) OR (check_out_date BETWEEN ? AND ?))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, roomNumber);
            stmt.setDate(2, checkIn);
            stmt.setDate(3, checkOut);
            stmt.setDate(4, checkIn);
            stmt.setDate(5, checkOut);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createBooking(Booking booking) throws SQLException {
        String query = "INSERT INTO bookings (guest_id, room_number, check_in_date, check_out_date, total_amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            // Check if Guest Exists
            String checkGuest = "SELECT id FROM guests WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkGuest)) {
                ps.setInt(1, booking.getGuestId());
                if (!ps.executeQuery().next()) throw new SQLException("Guest ID " + booking.getGuestId() + " not found!");
            }

            // Check if Room Exists and is Available
            String checkRoom = "SELECT status FROM rooms WHERE room_number = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkRoom)) {
                ps.setString(1, booking.getRoomNumber());
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) throw new SQLException("Room " + booking.getRoomNumber() + " not found!");
                if (!"AVAILABLE".equals(rs.getString("status"))) throw new SQLException("Room is not AVAILABLE!");
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, booking.getGuestId());
                stmt.setString(2, booking.getRoomNumber());
                stmt.setDate(3, booking.getCheckInDate());
                stmt.setDate(4, booking.getCheckOutDate());
                stmt.setDouble(5, booking.getTotalAmount());
                stmt.setString(6, booking.getStatus().name());

                int result = stmt.executeUpdate();
                
                // Update room status
                String updateRoom = "UPDATE rooms SET status = 'RESERVED' WHERE room_number = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateRoom)) {
                    updateStmt.setString(1, booking.getRoomNumber());
                    updateStmt.executeUpdate();
                }

                conn.commit();
                return result > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e; // Propagate detailed error
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkOut(int bookingId, String roomNumber) {
        String updateBooking = "UPDATE bookings SET status = 'CHECKED_OUT' WHERE id = ?";
        String updateRoom = "UPDATE rooms SET status = 'AVAILABLE' WHERE room_number = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement stmt1 = conn.prepareStatement(updateBooking)) {
                    stmt1.setInt(1, bookingId);
                    stmt1.executeUpdate();
                }
                try (PreparedStatement stmt2 = conn.prepareStatement(updateRoom)) {
                    stmt2.setString(1, roomNumber);
                    stmt2.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.*, g.first_name, g.last_name FROM bookings b " +
                       "JOIN guests g ON b.guest_id = g.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setId(rs.getInt("id"));
                booking.setGuestId(rs.getInt("guest_id"));
                booking.setRoomNumber(rs.getString("room_number"));
                booking.setCheckInDate(rs.getDate("check_in_date"));
                booking.setCheckOutDate(rs.getDate("check_out_date"));
                booking.setTotalAmount(rs.getDouble("total_amount"));
                booking.setStatus(Booking.Status.valueOf(rs.getString("status")));
                booking.setGuestName(rs.getString("first_name") + " " + rs.getString("last_name"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
