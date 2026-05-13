package com.hms.view;

import javax.swing.*;
import java.awt.*;

public class PaymentGatewayDialog extends JDialog {
    private boolean paymentSuccessful = false;
    private double amount;

    public PaymentGatewayDialog(Frame owner, double amount) {
        super(owner, "Secure Payment Gateway", true);
        this.amount = amount;
        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(owner);

        JPanel mainPanel = new JPanel(new CardLayout());
        CardLayout cl = (CardLayout) mainPanel.getLayout();

        // Screen 1: Method Selection
        JPanel methodPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        methodPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JLabel lblTotal = new JLabel("Payable Amount: $" + amount, SwingConstants.CENTER);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        JButton btnCard = new JButton("Credit / Debit Card");
        JButton btnUPI = new JButton("UPI (PhonePe/GPay)");
        JButton btnCash = new JButton("Pay via Cash");

        methodPanel.add(lblTotal);
        methodPanel.add(btnCard);
        methodPanel.add(btnUPI);
        methodPanel.add(btnCash);

        // Screen 2: Card Details
        JPanel cardDetails = new JPanel(new GridBagLayout());
        cardDetails.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5); gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JTextField txtCard = new JTextField(16);
        JTextField txtExp = new JTextField(5);
        JPasswordField txtCvv = new JPasswordField(3);
        JButton btnPayCard = new JButton("Pay Now");
        btnPayCard.setBackground(new Color(76, 175, 80));
        btnPayCard.setForeground(Color.WHITE);

        gbc.gridx=0; gbc.gridy=0; cardDetails.add(new JLabel("Card Number:"), gbc);
        gbc.gridx=0; gbc.gridy=1; cardDetails.add(txtCard, gbc);
        gbc.gridx=0; gbc.gridy=2; cardDetails.add(new JLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx=0; gbc.gridy=3; cardDetails.add(txtExp, gbc);
        gbc.gridx=0; gbc.gridy=4; cardDetails.add(new JLabel("CVV:"), gbc);
        gbc.gridx=0; gbc.gridy=5; cardDetails.add(txtCvv, gbc);
        gbc.gridx=0; gbc.gridy=6; cardDetails.add(btnPayCard, gbc);

        // Add to card layout
        mainPanel.add(methodPanel, "METHODS");
        mainPanel.add(cardDetails, "CARD");

        btnCard.addActionListener(e -> cl.show(mainPanel, "CARD"));
        btnCash.addActionListener(e -> processPayment("CASH"));
        btnUPI.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Please scan QR code on the counter or enter UPI ID.");
            processPayment("UPI");
        });

        btnPayCard.addActionListener(e -> {
            if(txtCard.getText().length() < 16) {
                JOptionPane.showMessageDialog(this, "Invalid Card Number!");
                return;
            }
            processPayment("CARD");
        });

        add(mainPanel, BorderLayout.CENTER);
    }

    private void processPayment(String method) {
        // Simulated loading
        JProgressBar pb = new JProgressBar();
        pb.setIndeterminate(true);
        pb.setString("Processing Transaction...");
        pb.setStringPainted(true);
        
        getContentPane().removeAll();
        add(pb, BorderLayout.CENTER);
        revalidate(); repaint();

        Timer timer = new Timer(2000, e -> {
            paymentSuccessful = true;
            JOptionPane.showMessageDialog(this, "Payment Successful via " + method + "!");
            dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    public boolean isSuccessful() {
        return paymentSuccessful;
    }
}
