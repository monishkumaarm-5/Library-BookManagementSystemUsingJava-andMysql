package com.librarydbms.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "password");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return connection;
    }
}
