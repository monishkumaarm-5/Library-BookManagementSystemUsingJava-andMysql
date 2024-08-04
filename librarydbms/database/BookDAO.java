package com.librarydbms.database;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO() {
        connection = DatabaseConnection.getConnection();
    }

    public List<Object[]> getAllBooks() {
        List<Object[]> books = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM books");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(new Object[]{
                    resultSet.getInt("book_id"),
                    resultSet.getString("title"),
                    resultSet.getString("author"),
                    resultSet.getString("isbn"),
                    resultSet.getString("publisher"),
                    resultSet.getString("genre"),
                    resultSet.getBlob("cover_image"),
                    resultSet.getBoolean("availability")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return books;
    }

    public void addBook(String title, String author, String isbn, String publisher, String genre, InputStream coverImage) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO books (title, author, isbn, publisher, genre, cover_image, availability) VALUES (?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, title);
        statement.setString(2, author);
        statement.setString(3, isbn);
        statement.setString(4, publisher);
        statement.setString(5, genre);
        statement.setBlob(6, coverImage);
        statement.setBoolean(7, true);
        statement.executeUpdate();
    }

    public void loanBook(int bookId, String borrowerName, Date loanDate, Date returnDate) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO book_loans (book_id, borrower_name, loan_date, return_date) VALUES (?, ?, ?, ?)");
        statement.setInt(1, bookId);
        statement.setString(2, borrowerName);
        statement.setDate(3, loanDate);
        statement.setDate(4, returnDate);
        statement.executeUpdate();

        PreparedStatement updateBookAvailability = connection.prepareStatement(
            "UPDATE books SET availability = ? WHERE book_id = ?");
        updateBookAvailability.setBoolean(1, false);
        updateBookAvailability.setInt(2, bookId);
        updateBookAvailability.executeUpdate();
    }

    public void returnBook(int loanId, int bookId) throws SQLException {
        PreparedStatement deleteLoanStatement = connection.prepareStatement(
            "DELETE FROM book_loans WHERE loan_id = ?");
        deleteLoanStatement.setInt(1, loanId);
        deleteLoanStatement.executeUpdate();

        PreparedStatement updateBookAvailability = connection.prepareStatement(
            "UPDATE books SET availability = ? WHERE book_id = ?");
        updateBookAvailability.setBoolean(1, true);
        updateBookAvailability.setInt(2, bookId);
        updateBookAvailability.executeUpdate();
    }

    public List<Object[]> getAllLoans() {
        List<Object[]> loans = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM book_loans");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                loans.add(new Object[]{
                    resultSet.getInt("loan_id"),
                    resultSet.getInt("book_id"),
                    resultSet.getString("borrower_name"),
                    resultSet.getDate("loan_date"),
                    resultSet.getDate("return_date")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return loans;
    }
}
