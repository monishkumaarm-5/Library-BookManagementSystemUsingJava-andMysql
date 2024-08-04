package com.librarydbms.ui;

import com.librarydbms.database.BookDAO;
import com.librarydbms.utils.ImageRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Blob;
import java.io.*;
import java.util.List;

public class BookRecordsFrame extends JFrame {
    private JTable recordTable;

    public BookRecordsFrame() {
        setTitle("Book Records");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        JLabel recordTitle = new JLabel("BOOK RECORDS");
        recordTitle.setForeground(Color.WHITE);
        recordTitle.setBounds(350, 40, 400, 75);
        add(recordTitle);

        JButton backButton = new JButton("Back");
        backButton.setBounds(890, 10, 75, 50);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardFrame();
                dispose();
            }
        });
        add(backButton);

        recordTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBounds(10, 150, 970, 600);
        add(scrollPane);

        loadBookRecords();

        recordTable.setRowHeight(200);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadBookRecords() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "ISBN", "Publisher", "Genre", "Cover Image", "Availability"}, 0);
        recordTable.setModel(model);

        BookDAO bookDAO = new BookDAO();
        List<Object[]> books = bookDAO.getAllBooks();

        for (Object[] book : books) {
            int bookId = (int) book[0];
            String title = (String) book[1];
            String author = (String) book[2];
            String isbn = (String) book[3];
            String publisher = (String) book[4];
            String genre = (String) book[5];
            Blob coverImageBlob = (Blob) book[6];
            boolean availability = (boolean) book[7];
            ImageIcon coverImage = new ImageIcon(coverImageBlob.getBytes(1, (int) coverImageBlob.length()));
            Image scaledImage = coverImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);

            model.addRow(new Object[]{bookId, title, author, isbn, publisher, genre, scaledIcon, availability ? "Available" : "Not Available"});
        }

        recordTable.getColumn("Cover Image").setCellRenderer(new ImageRenderer());
    }
}

