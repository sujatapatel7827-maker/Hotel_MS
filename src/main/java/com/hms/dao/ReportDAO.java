package com.hms.dao;

import com.hms.util.DatabaseConnection;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ReportDAO {

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // Total Revenue
            String revenueQuery = "SELECT SUM(total_amount) FROM bookings WHERE status = 'CHECKED_OUT'";
            try (PreparedStatement stmt = conn.prepareStatement(revenueQuery);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) stats.put("revenue", rs.getDouble(1));
            }

            // Occupancy Rate
            String totalRoomsQuery = "SELECT COUNT(*) FROM rooms";
            String occupiedRoomsQuery = "SELECT COUNT(*) FROM rooms WHERE status = 'OCCUPIED' OR status = 'RESERVED'";
            try (Statement stmt = conn.createStatement()) {
                ResultSet rsTotal = stmt.executeQuery(totalRoomsQuery);
                int total = rsTotal.next() ? rsTotal.getInt(1) : 0;
                
                ResultSet rsOcc = stmt.executeQuery(occupiedRoomsQuery);
                int occupied = rsOcc.next() ? rsOcc.getInt(1) : 0;
                
                double rate = (total > 0) ? (occupied * 100.0 / total) : 0.0;
                stats.put("occupancy", rate);
            }

            // Total Bookings
            String bookingsQuery = "SELECT COUNT(*) FROM bookings";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(bookingsQuery)) {
                if (rs.next()) stats.put("bookings", rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }
}
