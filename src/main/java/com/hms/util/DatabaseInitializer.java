package com.hms.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseInitializer {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            System.out.println("Checking and initializing database tables...");

            // Users Table
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "role ENUM('ADMIN', 'STAFF') NOT NULL," +
                    "full_name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(100)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Force Reset Admin to 'admin123'
            String adminHash = PasswordUtil.hashPassword("admin123");
            stmt.executeUpdate("DELETE FROM users WHERE username = 'admin'");
            stmt.executeUpdate("INSERT INTO users (username, password, role, full_name, email) " +
                    "VALUES ('admin', '" + adminHash + "', 'ADMIN', 'System Admin', 'admin@hotel.com')");

            // Room Categories
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS room_categories (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(50) NOT NULL," +
                    "base_price DECIMAL(10, 2) NOT NULL," +
                    "bed_type VARCHAR(50)," +
                    "amenities TEXT)");

            // Populate categories if empty
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM room_categories")) {
                if (rs.next() && rs.getInt(1) == 0) {
                stmt.executeUpdate("INSERT INTO room_categories (name, base_price, bed_type, amenities) VALUES " +
                        "('Standard', 1500.00, 'Single', 'WiFi, TV')," +
                        "('Deluxe', 3000.00, 'Double', 'WiFi, TV, AC, Mini Bar')," +
                        "('Suite', 6000.00, 'King', 'WiFi, TV, AC, Mini Bar, Living Room, Sea View')");
            }
        }

        // Rooms
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rooms (" +
                    "room_number VARCHAR(10) PRIMARY KEY," +
                    "category_id INT," +
                    "status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE') DEFAULT 'AVAILABLE'," +
                    "FOREIGN KEY (category_id) REFERENCES room_categories(id) ON DELETE SET NULL)");

            // Guests
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS guests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "first_name VARCHAR(50) NOT NULL," +
                    "last_name VARCHAR(50) NOT NULL," +
                    "email VARCHAR(100)," +
                    "phone VARCHAR(20) NOT NULL," +
                    "address TEXT," +
                    "id_proof VARCHAR(100)," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            // Migration for id_proof
            try {
                stmt.executeQuery("SELECT id_proof FROM guests LIMIT 1");
            } catch (SQLException e) {
                stmt.executeUpdate("ALTER TABLE guests ADD COLUMN id_proof VARCHAR(100) AFTER address");
            }

            // Bookings
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS bookings (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "guest_id INT," +
                    "room_number VARCHAR(10)," +
                    "check_in_date DATE NOT NULL," +
                    "check_out_date DATE NOT NULL," +
                    "total_amount DECIMAL(10, 2)," +
                    "status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'CHECKED_OUT') DEFAULT 'PENDING'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (guest_id) REFERENCES guests(id)," +
                    "FOREIGN KEY (room_number) REFERENCES rooms(room_number))");

            // Payments
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS payments (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "booking_id INT," +
                    "amount DECIMAL(10, 2) NOT NULL," +
                    "payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "payment_method VARCHAR(50) DEFAULT 'CASH'," +
                    "FOREIGN KEY (booking_id) REFERENCES bookings(id))");

            // Feedback
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS feedback (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "booking_id INT," +
                    "rating INT CHECK (rating >= 1 AND rating <= 5)," +
                    "comments TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (booking_id) REFERENCES bookings(id))");

            System.out.println("Database initialization check complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
