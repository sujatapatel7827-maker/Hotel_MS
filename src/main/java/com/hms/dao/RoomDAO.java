package com.hms.dao;

import com.hms.model.Room;
import com.hms.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rc.name as category_name FROM rooms r " +
                       "LEFT JOIN room_categories rc ON r.category_id = rc.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Room room = new Room();
                room.setRoomNumber(rs.getString("room_number"));
                room.setCategoryId(rs.getInt("category_id"));
                room.setStatus(Room.Status.valueOf(rs.getString("status")));
                room.setCategoryName(rs.getString("category_name"));
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public boolean updateRoomStatus(String roomNumber, Room.Status status) {
        String query = "UPDATE rooms SET status = ? WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status.name());
            stmt.setString(2, roomNumber);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addRoom(Room room) throws SQLException {
        String query = "INSERT INTO rooms (room_number, category_id, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getCategoryId());
            stmt.setString(3, room.getStatus().name());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateRoom(Room room) throws SQLException {
        String query = "UPDATE rooms SET category_id = ?, status = ? WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, room.getCategoryId());
            stmt.setString(2, room.getStatus().name());
            stmt.setString(3, room.getRoomNumber());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteRoom(String roomNumber) throws SQLException {
        String query = "DELETE FROM rooms WHERE room_number = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, roomNumber);
            return stmt.executeUpdate() > 0;
        }
    }
}
