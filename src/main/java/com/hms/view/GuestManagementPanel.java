package com.hms.view;

import com.hms.dao.GuestDAO;
import com.hms.model.Guest;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class GuestManagementPanel extends JPanel {
    private JTable guestTable;
    private DefaultTableModel tableModel;
    private GuestDAO guestDAO;

    public GuestManagementPanel() {
        guestDAO = new GuestDAO();
        initComponents();
        loadGuestData();
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
        mainCard.setPreferredSize(new Dimension(900, 600));
        mainCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        UIManager.put("Label.foreground", Color.WHITE);
        JLabel lblTitle = new JLabel("GUEST MANAGEMENT");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle, BorderLayout.WEST);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setOpaque(false);

        JTextField txtSearch = new JTextField(15);
        txtSearch.setToolTipText("Search Guest Name...");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterTable(txtSearch.getText());
            }
        });
        actionPanel.add(new JLabel("Search:"));
        actionPanel.add(txtSearch);

        JButton btnAddGuest = new JButton("Add Guest");
        btnAddGuest.setBackground(new Color(76, 175, 80));
        btnAddGuest.setForeground(Color.WHITE);
        btnAddGuest.addActionListener(e -> showAddGuestDialog(null));
        actionPanel.add(btnAddGuest);

        JButton btnUpdateGuest = new JButton("Update");
        btnUpdateGuest.setBackground(new Color(255, 152, 0));
        btnUpdateGuest.setForeground(Color.WHITE);
        btnUpdateGuest.addActionListener(e -> {
            int row = guestTable.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(row, 0);
                Guest g = guestDAO.getAllGuests().stream().filter(gst -> gst.getId() == id).findFirst().orElse(null);
                showAddGuestDialog(g);
            } else {
                JOptionPane.showMessageDialog(this, "Select a guest to update!");
            }
        });
        actionPanel.add(btnUpdateGuest);

        JButton btnDeleteGuest = new JButton("Delete Guest");
        btnDeleteGuest.setBackground(new Color(244, 67, 54));
        btnDeleteGuest.setForeground(Color.WHITE);
        btnDeleteGuest.addActionListener(e -> handleDeleteGuest());
        actionPanel.add(btnDeleteGuest);

        header.add(actionPanel, BorderLayout.EAST);

        // Table
        String[] columns = {"ID", "Full Name", "Phone", "Email", "ID Proof"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        guestTable = new JTable(tableModel);
        guestTable.setRowHeight(35);
        guestTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        guestTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        guestTable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        guestTable.setForeground(Color.WHITE);
        guestTable.setGridColor(new Color(255, 255, 255, 50));
        
        guestTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        guestTable.getTableHeader().setBackground(new Color(25, 50, 100));
        guestTable.getTableHeader().setForeground(Color.WHITE);
        
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
        guestTable.setDefaultRenderer(Object.class, renderer);

        JScrollPane scrollPane = new JScrollPane(guestTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        mainCard.add(header, BorderLayout.NORTH);
        mainCard.add(scrollPane, BorderLayout.CENTER);
        
        add(mainCard, new GridBagConstraints());
    }

    private void filterTable(String query) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        guestTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query));
    }

    private void loadGuestData() {
        tableModel.setRowCount(0);
        List<Guest> guests = guestDAO.getAllGuests();
        for (Guest guest : guests) {
            tableModel.addRow(new Object[]{
                    guest.getId(),
                    guest.getFullName(),
                    guest.getPhone(),
                    guest.getEmail(),
                    guest.getIdProof()
            });
        }
    }

    private void handleDeleteGuest() {
        int selectedRow = guestTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a guest to delete");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete Guest ID " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (guestDAO.deleteGuest(id)) {
                JOptionPane.showMessageDialog(this, "Guest Deleted Successfully!");
                loadGuestData();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not delete guest (Active bookings found)");
            }
        }
    }

    private void showAddGuestDialog(Guest existingGuest) {
        String title = (existingGuest == null) ? "Register New Guest" : "Update Guest ID " + existingGuest.getId();
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.getContentPane().setBackground(new Color(15, 32, 64)); // Match Midnight Blue
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtFirst = new JTextField(15);
        JTextField txtLast = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JTextField txtPhone = new JTextField(15);
        JTextField txtAddress = new JTextField(15);
        JTextField txtIdProof = new JTextField(15);

        // Styling helper for labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        if (existingGuest != null) {
            txtFirst.setText(existingGuest.getFirstName());
            txtLast.setText(existingGuest.getLastName());
            txtEmail.setText(existingGuest.getEmail());
            txtPhone.setText(existingGuest.getPhone());
            txtAddress.setText(existingGuest.getAddress());
            txtIdProof.setText(existingGuest.getIdProof());
        }

        int r = 0;
        String[] labels = {"First Name:", "Last Name:", "Email:", "Phone:", "Address:", "ID Proof No:"};
        JComponent[] fields = {txtFirst, txtLast, txtEmail, txtPhone, txtAddress, txtIdProof};

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(labelFont);
            lbl.setForeground(textColor);
            gbc.gridx = 0; gbc.gridy = r;
            dialog.add(lbl, gbc);
            
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
            r++;
        }

        JButton btnSave = new JButton((existingGuest == null) ? "REGISTER GUEST" : "UPDATE GUEST");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(0, 40));
        gbc.gridx = 0; gbc.gridy = r++; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);
        dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            if (txtFirst.getText().trim().isEmpty() || txtLast.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name fields cannot be empty!");
                return;
            }
            if (txtPhone.getText().trim().length() < 10) {
                JOptionPane.showMessageDialog(dialog, "Invalid phone number!");
                return;
            }

            Guest guest = (existingGuest == null) ? new Guest() : existingGuest;
            guest.setFirstName(txtFirst.getText());
            guest.setLastName(txtLast.getText());
            guest.setEmail(txtEmail.getText());
            guest.setPhone(txtPhone.getText());
            guest.setAddress(txtAddress.getText());
            guest.setIdProof(txtIdProof.getText());

            boolean success;
            if (existingGuest == null) {
                success = guestDAO.createGuest(guest);
            } else {
                success = guestDAO.updateGuest(guest);
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Guest saved successfully!");
                dialog.dispose();
                loadGuestData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error: Could not save guest.");
            }
        });

        dialog.setVisible(true);
    }
}
