package com.hms.dao;

import com.hms.util.DatabaseConnection;
import java.sql.*;

public class PaymentDAO {
    
    public boolean recordPayment(int bookingId, double amount, String method) {
        String query = "INSERT INTO payments (booking_id, amount, payment_method) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setDouble(2, amount);
            stmt.setString(3, method);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
