package com.hms.dao;

import com.hms.util.DatabaseConnection;
import java.sql.*;

public class FeedbackDAO {
    public boolean saveFeedback(int bookingId, int rating, String comments) {
        String query = "INSERT INTO feedback (booking_id, rating, comments) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            stmt.setInt(2, rating);
            stmt.setString(3, comments);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
