package com.hms.view;

import com.hms.dao.RoomDAO;
import com.hms.model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomManagementPanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomDAO roomDAO;
    
    private JTextField txtRoomNo, txtPrice;
    private JComboBox<String> cmbRoomType, cmbBedType, cmbAvailable, cmbStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnBack;

    public RoomManagementPanel() {
        roomDAO = new RoomDAO();
        initComponents();
        loadRoomData();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        // Main Glass Card
        JPanel mainCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 180)); // Midnight Blue Glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        mainCard.setOpaque(false);
        mainCard.setPreferredSize(new Dimension(1000, 600));
        mainCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Split Pane inside Card
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        // Left Panel: Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        UIManager.put("Label.foreground", Color.WHITE);

        JLabel lblTitle = new JLabel("ROOMS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 1; gbc.gridx = 0; formPanel.add(new JLabel("Room Number:"), gbc);
        txtRoomNo = new JTextField(15); gbc.gridx = 1; formPanel.add(txtRoomNo, gbc);

        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Availability:"), gbc);
        cmbAvailable = new JComboBox<>(new String[]{"Available", "Occupied"});
        gbc.gridx = 1; formPanel.add(cmbAvailable, gbc);

        gbc.gridy = 3; gbc.gridx = 0; formPanel.add(new JLabel("Status:"), gbc);
        cmbStatus = new JComboBox<>(new String[]{"Clean", "Dirty"});
        gbc.gridx = 1; formPanel.add(cmbStatus, gbc);

        gbc.gridy = 4; gbc.gridx = 0; formPanel.add(new JLabel("Price:"), gbc);
        txtPrice = new JTextField(15); gbc.gridx = 1; formPanel.add(txtPrice, gbc);

        gbc.gridy = 5; gbc.gridx = 0; formPanel.add(new JLabel("Bed Type:"), gbc);
        cmbBedType = new JComboBox<>(new String[]{"Single", "Double"});
        gbc.gridx = 1; formPanel.add(cmbBedType, gbc);

        // Buttons
        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        btnPanel.setOpaque(false);
        btnAdd = createStyledButton("Add Room");
        btnUpdate = createStyledButton("Update");
        btnDelete = createStyledButton("Delete");
        btnBack = createStyledButton("Back");

        btnPanel.add(btnAdd); btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete); btnPanel.add(btnBack);

        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 0, 0, 0);
        formPanel.add(btnPanel, gbc);

        // Right Panel: Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        String[] columns = {"Room No", "Availability", "Status", "Price", "Bed Type"};
        tableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(30);
        roomTable.setOpaque(false);
        roomTable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomTable.setForeground(Color.WHITE);
        roomTable.setGridColor(new Color(255, 255, 255, 50));
        
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomTable.getTableHeader().setBackground(new Color(25, 50, 100));
        roomTable.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(new Color(0, 0, 0, 100));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(0, 150, 255, 150));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        roomTable.setDefaultRenderer(Object.class, renderer);
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(formPanel);
        splitPane.setRightComponent(tablePanel);

        mainCard.add(splitPane, BorderLayout.CENTER);
        add(mainCard, new GridBagConstraints());

        // Listeners
        btnAdd.addActionListener(e -> handleAddRoom());
        btnUpdate.addActionListener(e -> handleUpdateRoom());
        btnDelete.addActionListener(e -> handleDeleteRoom());
        btnBack.addActionListener(e -> {
            Window win = SwingUtilities.getWindowAncestor(this);
            if (win instanceof JFrame) {
                // Return to dashboard logic or just clear
                clearForm();
            }
        });
    }

    private void handleAddRoom() {
        if (txtRoomNo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Room Number");
            return;
        }
        try {
            Room room = new Room();
            room.setRoomNumber(txtRoomNo.getText());
            room.setCategoryId(1); // Default category for now
            room.setStatus(cmbAvailable.getSelectedItem().equals("Available") ? Room.Status.AVAILABLE : Room.Status.OCCUPIED);
            
            if (roomDAO.addRoom(room)) {
                JOptionPane.showMessageDialog(this, "Room Added Successfully!");
                loadRoomData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding room");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void handleUpdateRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a room to update");
            return;
        }
        try {
            String roomNo = (String) tableModel.getValueAt(selectedRow, 0);
            Room room = new Room();
            room.setRoomNumber(roomNo);
            room.setCategoryId(1);
            room.setStatus(cmbAvailable.getSelectedItem().equals("Available") ? Room.Status.AVAILABLE : Room.Status.OCCUPIED);
            
            if (roomDAO.updateRoom(room)) {
                JOptionPane.showMessageDialog(this, "Room Updated Successfully!");
                loadRoomData();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating room: " + ex.getMessage());
        }
    }

    private void handleDeleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a room to delete");
            return;
        }
        String roomNo = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete Room " + roomNo + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (roomDAO.deleteRoom(roomNo)) {
                    JOptionPane.showMessageDialog(this, "Room Deleted!");
                    loadRoomData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting room: " + ex.getMessage());
            }
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    private void clearForm() {
        txtRoomNo.setText("");
        txtPrice.setText("");
        cmbRoomType.setSelectedIndex(0);
        cmbBedType.setSelectedIndex(0);
    }

    private void loadRoomData() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                room.getRoomNumber(),
                room.getStatus().toString().toLowerCase(),
                room.getStatus() == Room.Status.AVAILABLE ? "1000" : "1200", // Dummy price for visual
                "Single",
                "AC"
            });
        }
    }
}


