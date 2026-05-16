package com.hms.view;

import com.hms.dao.PaymentDAO;
import com.hms.util.BackgroundPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BillsPanel extends JPanel {
    private JTable billTable;
    private DefaultTableModel tableModel;

    public BillsPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setOpaque(false); // Make main panel transparent to show pool image

        // Main Glass Card (Large)
        JPanel tableCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(15, 32, 64, 180)); // Midnight Blue Glass
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
            }
        };
        tableCard.setOpaque(false);
        tableCard.setPreferredSize(new Dimension(1000, 600));
        tableCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("BILLING RECORDS");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        tableCard.add(lblTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Document", "Number", "Name", "Gender", "Country", "Room", "Price", "Mobile", "Email", "Date In", "Date Out", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        billTable = new JTable(tableModel);
        billTable.setOpaque(false);
        billTable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        billTable.setForeground(Color.WHITE);
        billTable.setGridColor(new Color(255, 255, 255, 50));
        
        billTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        billTable.getTableHeader().setBackground(new Color(25, 50, 100));
        billTable.getTableHeader().setForeground(Color.WHITE);
        
        // Dark row renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(new Color(0, 0, 0, 100)); // Darker rows
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(0, 150, 255, 150)); // Blue selection
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        billTable.setDefaultRenderer(Object.class, renderer);
        
        JScrollPane scrollPane = new JScrollPane(billTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Bottom Buttons Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setOpaque(false);
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(Color.BLACK);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setPreferredSize(new Dimension(120, 35));
        
        JButton btnBack = new JButton("Back");
        btnBack.setBackground(Color.BLACK);
        btnBack.setForeground(Color.WHITE);
        btnBack.setPreferredSize(new Dimension(100, 35));

        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);
        tableCard.add(bottomPanel, BorderLayout.SOUTH);

        // Add table card to center
        add(tableCard, new GridBagConstraints());
        
        loadDummyData();
    }

    private void loadDummyData() {
        tableModel.addRow(new Object[]{"Passport", "35261352", "ram kumar", "Male", "india", "1", "100", "9898768678", "ram12@gmail.com", "08/07/22", "08/07/22", "check out"});
        tableModel.addRow(new Object[]{"Passport", "2323434", "qqqqq", "Male", "in", "101", "1000", "232134324", "sdad@dsfd", "07/07/22", "07/07/22", "check out"});
    }
}
