package com.hms.view;

import com.hms.dao.UserDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserDAO userDAO = new UserDAO();

    public UserManagementPanel() {
        initComponents();
        loadUserData();
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

        JLabel title = new JLabel("Staff Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);

        JButton btnAddStaff = new JButton("Add New Staff");
        btnAddStaff.setBackground(Color.BLACK);
        btnAddStaff.setForeground(Color.WHITE);
        btnAddStaff.addActionListener(e -> showAddStaffDialog());
        header.add(btnAddStaff, BorderLayout.EAST);

        String[] columns = {"ID", "Username", "Role", "Full Name", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(35);
        userTable.setOpaque(false);
        userTable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.setForeground(Color.WHITE);
        userTable.setGridColor(new Color(255, 255, 255, 50));
        
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        userTable.getTableHeader().setBackground(new Color(25, 50, 100));
        userTable.getTableHeader().setForeground(Color.WHITE);
        
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
        userTable.setDefaultRenderer(Object.class, renderer);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        mainCard.add(header, BorderLayout.NORTH);
        mainCard.add(scrollPane, BorderLayout.CENTER);
        
        add(mainCard, new GridBagConstraints());
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
        dialog.getContentPane().setBackground(new Color(15, 32, 64)); // Midnight Blue
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUser = new JTextField(15);
        JPasswordField txtPass = new JPasswordField(15);
        JTextField txtName = new JTextField(15);
        JTextField txtEmail = new JTextField(15);
        JComboBox<com.hms.model.User.Role> cmbRole = new JComboBox<>(com.hms.model.User.Role.values());

        // Styling for labels
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color textColor = Color.WHITE;

        int r = 0;
        String[] labels = {"Username:", "Password:", "Full Name:", "Email:", "Role:"};
        JComponent[] fields = {txtUser, txtPass, txtName, txtEmail, cmbRole};

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

        JButton btnSave = new JButton("CREATE ACCOUNT");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(63, 81, 181));
        btnSave.setForeground(Color.WHITE);
        btnSave.setPreferredSize(new Dimension(0, 40));
        gbc.gridx = 0; gbc.gridy = r++; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);
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
