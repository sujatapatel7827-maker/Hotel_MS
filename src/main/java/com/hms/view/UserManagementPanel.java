package com.hms.view;

import com.hms.dao.UserDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO = new UserDAO();

    public UserManagementPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Staff Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);

        JButton btnAddStaff = new JButton("Add New Staff");
        btnAddStaff.setBackground(new Color(63, 81, 181));
        btnAddStaff.setForeground(Color.WHITE);
        btnAddStaff.addActionListener(e -> showAddStaffDialog());
        header.add(btnAddStaff, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Role", "Full Name", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(35);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);

        loadUserData();
    }

    private void loadUserData() {
        tableModel.setRowCount(0);
        List<com.hms.model.User> users = userDAO.getAllUsers();
        for (com.hms.model.User user : users) {
            tableModel.addRow(new Object[]{
                    user.getId(),
                    user.getUsername(),
                    user.getRole(),
                    user.getFullName(),
                    user.getEmail()
            });
        }
    }

    private void showAddStaffDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Staff", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUser = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JTextField txtName = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JComboBox<com.hms.model.User.Role> cmbRole = new JComboBox<>(com.hms.model.User.Role.values());

        int r = 0;
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; dialog.add(txtUser, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; dialog.add(txtPass, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; dialog.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; dialog.add(txtEmail, gbc);
        gbc.gridx = 0; gbc.gridy = r++; dialog.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; dialog.add(cmbRole, gbc);

        JButton btnSave = new JButton("Create Account");
        btnSave.setBackground(new Color(63, 81, 181));
        btnSave.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = r++; gbc.gridwidth = 2;
        dialog.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            com.hms.model.User u = new com.hms.model.User();
            u.setUsername(txtUser.getText());
            u.setPassword(new String(txtPass.getPassword()));
            u.setFullName(txtName.getText());
            u.setEmail(txtEmail.getText());
            u.setRole((com.hms.model.User.Role) cmbRole.getSelectedItem());

            if (userDAO.createUser(u)) {
                JOptionPane.showMessageDialog(dialog, "Staff Account Created!");
                dialog.dispose();
                loadUserData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Error creating account (Username might exist)");
            }
        });

        dialog.setVisible(true);
    }
}
