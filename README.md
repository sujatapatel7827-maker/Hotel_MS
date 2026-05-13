# 🏨 Hotel Management System (HMS)

A premium, feature-rich desktop application for managing hotel operations. Built with **Java Swing** and **MySQL**, featuring a modern UI and robust backend logic.

---

## ✨ Key Features

- **📊 Modern Dashboard**: Real-time business analytics and room status overview.
- **📅 Smart Booking**: Seamless check-in and check-out process with automated availability checks.
- **🏨 Room Management**: Full CRUD operations for managing different room types and pricing.
- **👥 Guest Management**: Detailed guest profiling and history tracking.
- **💳 Payment Gateway**: Simulated secure payment processing.
- **📑 PDF Invoicing**: Automated professional invoice generation using iText 7.
- **🛡️ Secure Access**: Role-based access control with BCrypt password hashing.
- **🏢 Self-Service Kiosk**: Interactive terminal for guests to check booking status.
- **⭐ Feedback System**: Integrated guest rating and review management.

---

## 🛠️ Tech Stack

- **Language**: Java 17+
- **UI Framework**: Java Swing (FlatLaf for modern design)
- **Database**: MySQL 8.0+
- **Build Tool**: Maven
- **Core Libraries**:
  - `FlatLaf`: For premium, high-DPI responsive UI.
  - `iText 7`: For PDF invoice generation.
  - `HikariCP`: For high-performance database connection pooling.
  - `BCrypt`: For secure password encryption.
  - `Ikonli`: For modern material design icons.

---

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher
- MySQL Server
- Maven

### Installation & Setup
1. **Clone the repository:**
   ```bash
   git clone https://github.com/sujatapatel7827-maker/Hotel_MS.git
   ```
2. **Setup Database:**
   - Create a database named `hotel_ms`.
   - Run the SQL script located in `database/schema.sql`.
3. **Configure Connection:**
   - Update your MySQL credentials in `src/main/java/com/hms/util/DatabaseConnection.java`.
4. **Build and Run:**
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.hms.HotelManagementApp"
   ```

---

## 📸 Screenshots

*(Add your project screenshots here to make it look even better!)*

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
