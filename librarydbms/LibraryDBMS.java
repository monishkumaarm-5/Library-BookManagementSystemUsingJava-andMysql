import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class LibraryDBMS {
    private JFrame libraryFrame, dashboardFrame, recordFrame;
    private JTable recordTable;
    private JScrollPane scrollPane;
    private JLabel recordTitle, bookImageLabel;
    private JComboBox<String> bookGenreComboBox;
    private JTextField bookTitleTextField, bookAuthorTextField, bookIsbnTextField, bookPublisherTextField;
    private JFileChooser choosingImagePath;
    private Connection connection;

    public LibraryDBMS() {
        connectToDatabase();
        dashboard();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "password");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + ex.getMessage());
        }
    }

    private void dashboard() {
        dashboardFrame = new JFrame("Library Dashboard");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setLayout(null);
        dashboardFrame.getContentPane().setBackground(Color.BLACK);

        JButton showBooksButton = new JButton("Show Books");
        showBooksButton.setBounds(100, 100, 200, 50);
        showBooksButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRecords("BOOKS");
            }
        });
        dashboardFrame.add(showBooksButton);

        JButton addBookButton = new JButton("Add Book");
        addBookButton.setBounds(100, 200, 200, 50);
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        dashboardFrame.add(addBookButton);

        JButton loanBookButton = new JButton("Loan Book");
        loanBookButton.setBounds(100, 300, 200, 50);
        loanBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loanBook();
            }
        });
        dashboardFrame.add(loanBookButton);

        JButton manageLoansButton = new JButton("Manage Loans");
        manageLoansButton.setBounds(100, 400, 200, 50);
        manageLoansButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageBookLoans();
            }
        });
        dashboardFrame.add(manageLoansButton);

        dashboardFrame.setSize(500, 600);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
    }

    private void showRecords(String recordType) {
        dashboardFrame.dispose();
        recordFrame = new JFrame(recordType + " Records");
        recordFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recordFrame.setLayout(null);
        recordFrame.getContentPane().setBackground(Color.BLACK);

        recordTitle = new JLabel(recordType + " RECORDS");
        recordTitle.setForeground(Color.WHITE);
        recordTitle.setBounds(350, 40, 400, 75);
        recordFrame.add(recordTitle);

        JButton backButton = new JButton("Back");
        backButton.setBounds(890, 10, 75, 50);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                recordFrame.dispose();
                dashboard();
            }
        });
        recordFrame.add(backButton);

        recordTable = new JTable();
        scrollPane = new JScrollPane(recordTable);
        scrollPane.setBounds(10, 150, 970, 600);
        recordFrame.add(scrollPane);

        if (recordType.equals("BOOKS")) {
            loadBookRecords();
        }

        recordTable.setRowHeight(200);
        recordFrame.setSize(1000, 800);
        recordFrame.setLocationRelativeTo(null);
        recordFrame.setVisible(true);
    }

    private void loadBookRecords() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Title", "Author", "ISBN", "Publisher", "Genre", "Cover Image", "Availability"}, 0);
        recordTable.setModel(model);

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM books");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                String publisher = resultSet.getString("publisher");
                String genre = resultSet.getString("genre");
                Blob coverImageBlob = resultSet.getBlob("cover_image");
                ImageIcon coverImage = new ImageIcon(coverImageBlob.getBytes(1, (int) coverImageBlob.length()));
                Image scaledImage = coverImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                boolean availability = resultSet.getBoolean("availability");

                model.addRow(new Object[]{bookId, title, author, isbn, publisher, genre, scaledIcon, availability ? "Available" : "Loaned"});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading book records: " + ex.getMessage());
        }

        recordTable.getColumn("Cover Image").setCellRenderer(new ImageRenderer());
    }

    private void addBook() {
        libraryFrame = new JFrame("Add Book");
        libraryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        libraryFrame.setLayout(null);
        libraryFrame.getContentPane().setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Title:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(50, 50, 100, 30);
        libraryFrame.add(titleLabel);

        bookTitleTextField = new JTextField();
        bookTitleTextField.setBounds(150, 50, 200, 30);
        libraryFrame.add(bookTitleTextField);

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        authorLabel.setBounds(50, 100, 100, 30);
        libraryFrame.add(authorLabel);

        bookAuthorTextField = new JTextField();
        bookAuthorTextField.setBounds(150, 100, 200, 30);
        libraryFrame.add(bookAuthorTextField);

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setForeground(Color.WHITE);
        isbnLabel.setBounds(50, 150, 100, 30);
        libraryFrame.add(isbnLabel);

        bookIsbnTextField = new JTextField();
        bookIsbnTextField.setBounds(150, 150, 200, 30);
        libraryFrame.add(bookIsbnTextField);

        JLabel publisherLabel = new JLabel("Publisher:");
        publisherLabel.setForeground(Color.WHITE);
        publisherLabel.setBounds(50, 200, 100, 30);
        libraryFrame.add(publisherLabel);

        bookPublisherTextField = new JTextField();
        bookPublisherTextField.setBounds(150, 200, 200, 30);
        libraryFrame.add(bookPublisherTextField);

        JLabel genreLabel = new JLabel("Genre:");
        genreLabel.setForeground(Color.WHITE);
        genreLabel.setBounds(50, 250, 100, 30);
        libraryFrame.add(genreLabel);

        bookGenreComboBox = new JComboBox<>(new String[]{"Fiction", "Non-Fiction", "Science", "Fantasy", "Biography", "History"});
        bookGenreComboBox.setBounds(150, 250, 200, 30);
        libraryFrame.add(bookGenreComboBox);

        JLabel coverImageLabel = new JLabel("Cover Image:");
        coverImageLabel.setForeground(Color.WHITE);
        coverImageLabel.setBounds(50, 300, 100, 30);
        libraryFrame.add(coverImageLabel);

        JButton chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setBounds(150, 300, 200, 30);
        chooseImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choosingImagePath = new JFileChooser();
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
        libraryFrame.add(chooseImageButton);

        bookImageLabel = new JLabel();
        bookImageLabel.setBounds(manageLoanFrame.add(backButton);

        JTable loanTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(loanTable);
        scrollPane.setBounds(10, 150, 970, 600);
        manageLoanFrame.add(scrollPane);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Loan ID", "Book ID", "Borrower Name", "Loan Date", "Return Date"}, 0);
        loanTable.setModel(model);

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM loans");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int loanId = resultSet.getInt("loan_id");
                int bookId = resultSet.getInt("book_id");
                String borrowerName = resultSet.getString("borrower_name");
                Date loanDate = resultSet.getDate("loan_date");
                Date returnDate = resultSet.getDate("return_date");

                model.addRow(new Object[]{loanId, bookId, borrowerName, loanDate, returnDate});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading loan records: " + ex.getMessage());
        }

        manageLoanFrame.setSize(1000, 800);
        manageLoanFrame.setLocationRelativeTo(null);
        manageLoanFrame.setVisible(true);
    }

    private void loanBook() {
        JFrame loanBookFrame = new JFrame("Loan Book");
        loanBookFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loanBookFrame.setLayout(null);
        loanBookFrame.getContentPane().setBackground(Color.BLACK);

        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setForeground(Color.WHITE);
        bookIdLabel.setBounds(50, 50, 100, 30);
        loanBookFrame.add(bookIdLabel);

        JTextField bookIdTextField = new JTextField();
        bookIdTextField.setBounds(150, 50, 200, 30);
        loanBookFrame.add(bookIdTextField);

        JLabel borrowerNameLabel = new JLabel("Borrower Name:");
        borrowerNameLabel.setForeground(Color.WHITE);
        borrowerNameLabel.setBounds(50, 100, 150, 30);
        loanBookFrame.add(borrowerNameLabel);

        JTextField borrowerNameTextField = new JTextField();
        borrowerNameTextField.setBounds(200, 100, 200, 30);
        loanBookFrame.add(borrowerNameTextField);

        JLabel loanDateLabel = new JLabel("Loan Date:");
        loanDateLabel.setForeground(Color.WHITE);
        loanDateLabel.setBounds(50, 150, 100, 30);
        loanBookFrame.add(loanDateLabel);

        JTextField loanDateTextField = new JTextField();
        loanDateTextField.setBounds(150, 150, 200, 30);
        loanBookFrame.add(loanDateTextField);

        JLabel returnDateLabel = new JLabel("Return Date:");
        returnDateLabel.setForeground(Color.WHITE);
        returnDateLabel.setBounds(50, 200, 100, 30);
        loanBookFrame.add(returnDateLabel);

        JTextField returnDateTextField = new JTextField();
        returnDateTextField.setBounds(150, 200, 200, 30);
        loanBookFrame.add(returnDateTextField);

        JButton loanBookButton = new JButton("Loan Book");
        loanBookButton.setBounds(150, 250, 200, 30);
        loanBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookIdTextField.getText());
                    String borrowerName = borrowerNameTextField.getText();
                    Date loanDate = Date.valueOf(loanDateTextField.getText());
                    Date returnDate = Date.valueOf(returnDateTextField.getText());

                    if (borrowerName.isEmpty() || loanDate == null || returnDate == null) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                        return;
                    }

                    PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO loans (book_id, borrower_name, loan_date, return_date) VALUES (?, ?, ?, ?)");
                    statement.setInt(1, bookId);
                    statement.setString(2, borrowerName);
                    statement.setDate(3, loanDate);
                    statement.setDate(4, returnDate);
                    statement.executeUpdate();

                    PreparedStatement updateBookAvailability = connection.prepareStatement(
                        "UPDATE books SET availability = FALSE WHERE book_id = ?");
                    updateBookAvailability.setInt(1, bookId);
                    updateBookAvailability.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book loaned successfully!");
                    loanBookFrame.dispose();
                    manageBookLoans();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error loaning book: " + ex.getMessage());
                }
            }
        });
        loanBookFrame.add(loanBookButton);

        loanBookFrame.setSize(500, 400);
        loanBookFrame.setLocationRelativeTo(null);
        loanBookFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new LibraryDBMS();
    }

    // Custom table cell renderer to display images in JTable cells
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel();
                label.setIcon((ImageIcon) value);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}

