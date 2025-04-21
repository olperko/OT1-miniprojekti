package com.mokkikodit.cottagereservation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManagement {
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:mokkikodit.db";

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Tietokantaan yhdistäminen onnistui.");
        } catch (SQLException e) {
            System.out.println("Onglema tietokantaan yhdistäessä: " + e.getMessage());
        }
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
        return connection;
    }
}
