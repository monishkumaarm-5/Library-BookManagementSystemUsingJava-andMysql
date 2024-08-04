package com.librarydbms.ui;

import com.librarydbms.database.BookDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;

public class LoanBookFrame extends JFrame {
    private JTextField bookIdTextField, borrowerNameTextField, loanDateTextField, returnDateTextField;

    public LoanBookFrame() {
        setTitle("Loan Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE);
        bookIdLabel.setBounds(50, 50, 100, 30);
        add(bookIdLabel);

        bookIdTextField = new JTextField();
        bookIdTextField.setBounds(150, 50, 200, 30);
        add(bookIdTextField);

        JLabel borrowerNameLabel = new JLabel("Borrower Name:");
        borrowerNameLabel.setForeground(Color.WHITE);
        borrowerNameLabel.setBounds(50, 100, 100, 30);
        add(borrowerNameLabel);

        borrowerNameTextField = new JTextField();
        borrowerNameTextField.setBounds(150, 100, 200, 30);
        add(borrowerNameTextField);

        JLabel loanDateLabel = new JLabel("Loan Date:");
        loanDateLabel.setForeground(Color.WHITE);
        loanDateLabel.setBounds(50, 150, 100, 30);
        add(loanDateLabel);

        loanDateTextField = new JTextField();
        loanDateTextField.setBounds(150, 150, 200, 30);
        add(loanDateTextField);

        JLabel returnDateLabel = new JLabel("Return Date:");
        returnDateLabel.setForeground(Color.WHITE);
        returnDateLabel.setBounds(50, 200, 100, 30);
        add(returnDateLabel);

        returnDateTextField = new JTextField();
        returnDateTextField.setBounds(150, 200, 200, 30);
        add(returnDateTextField);

        JButton loanBookButton = new JButton("Loan Book");
        loanBookButton.setBounds(150, 250, 200, 30);
        loanBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loanBookToDatabase();
            }
        });
        add(loanBookButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 75, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardFrame();
                dispose();
            }
        });
        add(backButton);

        setSize(500, 350);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loanBookToDatabase() {
        try {
            int bookId = Integer.parseInt(bookIdTextField.getText());
            String borrowerName = borrowerNameTextField.getText();
            Date loanDate = Date.valueOf(loanDateTextField.getText());
            Date returnDate = Date.valueOf(returnDateTextField.getText());

            BookDAO bookDAO = new BookDAO();
            bookDAO.loanBook(bookId, borrowerName, loanDate, returnDate);

            JOptionPane.showMessageDialog(null, "Book loaned successfully!");
            new DashboardFrame();
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loaning book: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid dates in the format YYYY-MM-DD.");
        }
    }
}