package com.hms.view;

import com.hms.dao.RoomDAO;
import com.hms.model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomManagementPanel extends JPanel {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomDAO roomDAO;

    public RoomManagementPanel() {
        roomDAO = new RoomDAO();
        initComponents();
        loadRoomData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblTitle = new JLabel("Room Management");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.add(lblTitle, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JTextField txtSearch = new JTextField(15);
        txtSearch.setToolTipText("Search Room Number...");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable(txtSearch.getText());
            }
        });
        actionPanel.add(new JLabel("Search:"));
        actionPanel.add(txtSearch);

        JButton btnAddRoom = new JButton("Add Room");
        btnAddRoom.setBackground(new Color(63, 81, 181));
        btnAddRoom.setForeground(Color.WHITE);
        btnAddRoom.addActionListener(e -> showAddRoomDialog(null));
        actionPanel.add(btnAddRoom);

        JButton btnUpdateRoom = new JButton("Update");
        btnUpdateRoom.setBackground(new Color(255, 152, 0));
        btnUpdateRoom.setForeground(Color.WHITE);
        btnUpdateRoom.addActionListener(e -> {
            int row = roomTable.getSelectedRow();
            if (row != -1) {
                String roomNo = (String) tableModel.getValueAt(row, 0);
                Room r = roomDAO.getAllRooms().stream().filter(rm -> rm.getRoomNumber().equals(roomNo)).findFirst().orElse(null);
                showAddRoomDialog(r);
            } else {
                JOptionPane.showMessageDialog(this, "Select a room to update!");
            }
        });
        actionPanel.add(btnUpdateRoom);

        JButton btnDeleteRoom = new JButton("Delete Room");
        btnDeleteRoom.setBackground(new Color(244, 67, 54));
        btnDeleteRoom.setForeground(Color.WHITE);
        btnDeleteRoom.addActionListener(e -> handleDeleteRoom());
        actionPanel.add(btnDeleteRoom);

        header.add(actionPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Room Number", "Category", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(35);
        roomTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void filterTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        roomTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    private void loadRoomData() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomDAO.getAllRooms();
        for (Room room : rooms) {
            tableModel.addRow(new Object[]{
                    room.getRoomNumber(),
                    room.getCategoryName(),
                    room.getStatus()
            });
        }
    }

    private void handleDeleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete");
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete Room " + roomNumber + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (roomDAO.deleteRoom(roomNumber)) {
                    JOptionPane.showMessageDialog(this, "Room Deleted Successfully!");
                    loadRoomData();
                } else {
                    JOptionPane.showMessageDialog(this, "Error: Could not delete room (It might have active bookings)");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void showAddRoomDialog(Room existingRoom) {
        String title = (existingRoom == null) ? "Add New Room" : "Update Room " + existingRoom.getRoomNumber();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtRoomNo = new JTextField(15);
        if (existingRoom != null) {
            txtRoomNo.setText(existingRoom.getRoomNumber());
            txtRoomNo.setEditable(false);
        }

        String[] categories = {"Standard", "Deluxe", "Suite"};
        JComboBox<String> cmbCategory = new JComboBox<>(categories);
        if (existingRoom != null) cmbCategory.setSelectedIndex(existingRoom.getCategoryId() - 1);

        JComboBox<Room.Status> cmbStatus = new JComboBox<>(Room.Status.values());
        if (existingRoom != null) cmbStatus.setSelectedItem(existingRoom.getStatus());

        JButton btnSave = new JButton((existingRoom == null) ? "Save Room" : "Update Room");
        btnSave.setBackground(new Color(63, 81, 181));
        btnSave.setForeground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; dialog.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1; dialog.add(txtRoomNo, gbc);
        gbc.gridx = 0; gbc.gridy = 1; dialog.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; dialog.add(cmbCategory, gbc);
        gbc.gridx = 0; gbc.gridy = 2; dialog.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; dialog.add(cmbStatus, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            try {
                String roomNo = txtRoomNo.getText().trim();
                if (roomNo.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Room Number cannot be empty!");
                    return;
                }

                Room room = (existingRoom == null) ? new Room() : existingRoom;
                room.setRoomNumber(roomNo);
                room.setCategoryId(cmbCategory.getSelectedIndex() + 1);
                room.setStatus((Room.Status) cmbStatus.getSelectedItem());

                boolean success;
                if (existingRoom == null) {
                    success = roomDAO.addRoom(room);
                } else {
                    success = roomDAO.updateRoom(room);
                }

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Room saved successfully!");
                    dialog.dispose();
                    loadRoomData();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        dialog.setVisible(true);
    }
}
