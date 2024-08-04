package com.librarydbms.ui;

import com.librarydbms.database.BookDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class ManageLoansFrame extends JFrame {
    private JTable loanTable;

    public ManageLoansFrame() {
        setTitle("Manage Loans");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        JLabel loansTitle = new JLabel("MANAGE LOANS");
        loansTitle.setForeground(Color.WHITE);
        loansTitle.setBounds(350, 40, 400, 75);
        add(loansTitle);

        JButton backButton = new JButton("Back");
        backButton.setBounds(890, 10, 75, 50);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardFrame();
                dispose();
            }
        });
        add(backButton);

        loanTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setBounds(10, 150, 970, 600);
        add(scrollPane);

        loadLoans();

        loanTable.setRowHeight(30);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadLoans() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Loan ID", "Book ID", "Borrower Name", "Loan Date", "Return Date", "Action"}, 0);
        loanTable.setModel(model);

        BookDAO bookDAO = new BookDAO();
        List<Object[]> loans = bookDAO.getAllLoans();

        for (Object[] loan : loans) {
            int loanId = (int) loan[0];
            int bookId = (int) loan[1];
            String borrowerName = (String) loan[2];
            java.sql.Date loanDate = (java.sql.Date) loan[3];
            java.sql.Date returnDate = (java.sql.Date) loan[4];

            JButton returnButton = new JButton("Return");
            returnButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        bookDAO.returnBook(loanId, bookId);
                        JOptionPane.showMessageDialog(null, "Book returned successfully!");
                        loadLoans();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error returning book: " + ex.getMessage());
                    }
                }
            });

            model.addRow(new Object[]{loanId, bookId, borrowerName, loanDate, returnDate, returnButton});
        }
    }
}
