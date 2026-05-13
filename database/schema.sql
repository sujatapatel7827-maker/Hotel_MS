-- Hotel Management System Database Schema
CREATE DATABASE IF NOT EXISTS hotel_ms;
USE hotel_ms;

-- Users Table (Admin and Staff)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'STAFF') NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Room Categories
CREATE TABLE IF NOT EXISTS room_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL, -- e.g., Deluxe, Suite, Standard
    base_price DECIMAL(10, 2) NOT NULL,
    bed_type VARCHAR(50),
    amenities TEXT
);

-- Rooms Table
CREATE TABLE IF NOT EXISTS rooms (
    room_number VARCHAR(10) PRIMARY KEY,
    category_id INT,
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE') DEFAULT 'AVAILABLE',
    FOREIGN KEY (category_id) REFERENCES room_categories(id) ON DELETE SET NULL
);

-- Guests Table
CREATE TABLE IF NOT EXISTS guests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20) NOT NULL,
    address TEXT,
    id_proof_type VARCHAR(50),
    id_proof_number VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bookings Table
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_id INT,
    room_number VARCHAR(10),
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    total_amount DECIMAL(10, 2),
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'CHECKED_OUT') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    FOREIGN KEY (room_number) REFERENCES rooms(room_number)
);

-- Payments Table
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('CASH', 'CARD', 'UPI', 'ONLINE') NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_id VARCHAR(100),
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- Insert Default Admin (Password is 'admin123' hashed)
-- Use a tool to generate bcrypt hash if needed, but for now I'll use a placeholder or the app will have a setup phase.
-- Placeholder hash for 'admin123': $2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00dmGQu.0.uUBy
INSERT INTO users (username, password, role, full_name, email) 
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00dmGQu.0.uUBy', 'ADMIN', 'System Admin', 'admin@hotel.com');

-- Sample Room Categories
INSERT INTO room_categories (name, base_price, bed_type, amenities) VALUES 
('Standard', 1500.00, 'Single', 'WiFi, TV'),
('Deluxe', 3000.00, 'Double', 'WiFi, TV, AC, Mini Bar'),
('Suite', 6000.00, 'King', 'WiFi, TV, AC, Mini Bar, Living Room, Sea View');

-- Sample Rooms
INSERT INTO rooms (room_number, category_id, status) VALUES 
('101', 1, 'AVAILABLE'),
('102', 1, 'AVAILABLE'),
('201', 2, 'AVAILABLE'),
('202', 2, 'AVAILABLE'),
('301', 3, 'AVAILABLE');
