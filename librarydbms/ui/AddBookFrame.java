package com.librarydbms.ui;

import com.librarydbms.database.BookDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;

public class AddBookFrame extends JFrame {
    private JTextField bookTitleTextField, bookAuthorTextField, bookIsbnTextField, bookPublisherTextField;
    private JComboBox<String> bookGenreComboBox;
    private JLabel bookImageLabel;
    private JFileChooser choosingImagePath;

    public AddBookFrame() {
        setTitle("Add Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 50, 100, 30);
        add(titleLabel);

        bookTitleTextField = new JTextField();
        bookTitleTextField.setBounds(150, 50, 200, 30);
        add(bookTitleTextField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setBounds(50, 100, 100, 30);
        add(authorLabel);

        bookAuthorTextField = new JTextField();
        bookAuthorTextField.setBounds(150, 100, 200, 30);
        add(bookAuthorTextField);

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        isbnLabel.setBounds(50, 150, 100, 30);
        add(isbnLabel);

        bookIsbnTextField = new JTextField();
        bookIsbnTextField.setBounds(150, 150, 200, 30);
        add(bookIsbnTextField);

        JLabel publisherLabel = new JLabel("Publisher:");
        publisherLabel.setForeground(Color.WHITE);
        publisherLabel.setBounds(50, 200, 100, 30);
        add(publisherLabel);

        bookPublisherTextField = new JTextField();
        bookPublisherTextField.setBounds(150, 200, 200, 30);
        add(bookPublisherTextField);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setBounds(50, 250, 100, 30);
        add(genreLabel);

        bookGenreComboBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Science", "Fantasy", "Biography", "History"});
        bookGenreComboBox.setBounds(150, 250, 200, 30);
        add(bookGenreComboBox);

        JLabel coverImageLabel = new JLabel("Cover Image:");
        coverImageLabel.setForeground(Color.WHITE);
        coverImageLabel.setBounds(50, 300, 100, 30);
        add(coverImageLabel);

        bookImageLabel = new JLabel();
        bookImageLabel.setBounds(150, 300, 200, 200);
        add(bookImageLabel);

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setBounds(150, 510, 200, 30);
        chooseImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int returnValue = choosingImagePath.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File imageFile = choosingImagePath.getSelectedFile();
                    try {
                        FileInputStream imageFileInputStream = new FileInputStream(imageFile);
                        byte[] imageBytes = imageFileInputStream.readAllBytes();
                        ImageIcon imageIcon = new ImageIcon(imageBytes);
                        Image scalingImage = imageIcon.getImage();
                        Image scaledImage = scalingImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImage);
                        bookImageLabel.setIcon(scaledIcon);
                        imageFileInputStream.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage());
                    }
                }
            }
        });
        add(chooseImageButton);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.setBounds(150, 550, 200, 30);
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBookToDatabase();
            }
        });
        add(addBookButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(10, 10, 75, 30);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardFrame();
                dispose();
            }
        });
        add(backButton);

        choosingImagePath = new JFileChooser();

        setSize(500, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBookToDatabase() {
        String title = bookTitleTextField.getText();
        String author = bookAuthorTextField.getText();
        String isbn = bookIsbnTextField.getText();
        String publisher = bookPublisherTextField.getText();
        String genre = (String) bookGenreComboBox.getSelectedItem();
        ImageIcon coverImageIcon = (ImageIcon) bookImageLabel.getIcon();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publisher.isEmpty() || genre.isEmpty() || coverImageIcon == null) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields and choose a cover image.");
            return;
        }

        try {
            File imageFile = new File(choosingImagePath.getSelectedFile().getAbsolutePath());
            FileInputStream fis = new FileInputStream(imageFile);
            BookDAO bookDAO = new BookDAO();
            bookDAO.addBook(title, author, isbn, publisher, genre, fis);
            fis.close();
            JOptionPane.showMessageDialog(null, "Book added successfully!");
            new DashboardFrame();
            dispose();
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Error adding book: " + ex.getMessage());
        }
    }
}
