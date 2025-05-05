package com.mokkikodit.cottagereservation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManagement {
    private static Connection connection;
    private final String DATABASE_URL = "jdbc:sqlite:mokkikodit.db";

    public boolean connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }

            connection = DriverManager.getConnection(DATABASE_URL);

            if (connection != null) {
                System.out.println("Connected to the database successfully.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Tietokannan yhteys suljettu.");
            }
        } catch (SQLException e) {
            System.out.println("Tietokannan sulkeminen ei onnistunut: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection: " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }
}
