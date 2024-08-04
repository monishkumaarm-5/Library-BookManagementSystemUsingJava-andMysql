package com.librarydbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        setTitle("Library Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        JButton showBooksButton = new JButton("Show Books");
        showBooksButton.setBounds(100, 100, 200, 50);
        showBooksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BookRecordsFrame();
                dispose();
            }
        });
        add(showBooksButton);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.setBounds(100, 200, 200, 50);
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddBookFrame();
                dispose();
            }
        });
        add(addBookButton);

        JButton loanBookButton = new JButton("Loan Book");
        loanBookButton.setBounds(100, 300, 200, 50);
        loanBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoanBookFrame();
                dispose();
            }
        });
        add(loanBookButton);

        JButton manageLoansButton = new JButton("Manage Loans");
        manageLoansButton.setBounds(100, 400, 200, 50);
        manageLoansButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageLoansFrame();
                dispose();
            }
        });
        add(manageLoansButton);

        setSize(500, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
