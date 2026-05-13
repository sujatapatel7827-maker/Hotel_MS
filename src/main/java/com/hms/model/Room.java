package com.hms.model;

public class Room {
    private String roomNumber;
    private int categoryId;
    private Status status;
    private String categoryName; // Join field for UI

    public enum Status {
        AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE
    }

    public Room() {}

    public Room(String roomNumber, int categoryId, Status status) {
        this.roomNumber = roomNumber;
        this.categoryId = categoryId;
        this.status = status;
    }

    // Getters and Setters
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
