package com.hms.view;

import com.hms.dao.FeedbackDAO;
import javax.swing.*;
import java.awt.*;

public class FeedbackDialog extends JDialog {
    private int selectedRating = 0;
    private FeedbackDAO feedbackDAO = new FeedbackDAO();

    public FeedbackDialog(Frame owner, int bookingId) {
        super(owner, "Guest Feedback", true);
        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("How was your stay?");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createVerticalStrut(20));

        // Rating Stars
        JPanel starPanel = new JPanel();
        JButton[] stars = new JButton[5];
        for (int i = 0; i < 5; i++) {
            int rating = i + 1;
            stars[i] = new JButton("★");
            stars[i].setFont(new Font("Segoe UI", Font.PLAIN, 30));
            stars[i].setFocusPainted(false);
            stars[i].setBorderPainted(false);
            stars[i].setContentAreaFilled(false);
            stars[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            stars[i].addActionListener(e -> {
                selectedRating = rating;
                for (int j = 0; j < 5; j++) {
                    stars[j].setForeground(j < rating ? new Color(255, 193, 7) : Color.LIGHT_GRAY);
                }
            });
            starPanel.add(stars[i]);
        }
        mainPanel.add(starPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel lblComment = new JLabel("Comments (Optional):");
        lblComment.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblComment);

        JTextArea txtComments = new JTextArea(3, 20);
        txtComments.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(new JScrollPane(txtComments));
        mainPanel.add(Box.createVerticalStrut(20));

        JButton btnSubmit = new JButton("Submit Feedback");
        btnSubmit.setBackground(new Color(63, 81, 181));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSubmit.addActionListener(e -> {
            if (selectedRating == 0) {
                JOptionPane.showMessageDialog(this, "Please select a rating!");
                return;
            }
            if (feedbackDAO.saveFeedback(bookingId, selectedRating, txtComments.getText())) {
                JOptionPane.showMessageDialog(this, "Thank you for your feedback!");
                dispose();
            }
        });
        mainPanel.add(btnSubmit);

        add(mainPanel, BorderLayout.CENTER);
    }
}
