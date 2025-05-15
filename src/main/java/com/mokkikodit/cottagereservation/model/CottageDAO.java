package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CottageDAO {

    private final DatabaseManagement databaseManager;

    public CottageDAO(DatabaseManagement databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Luo komentotekstin, joka syötetään SQLite tietokantaan.
     */
    public void createCottageTable() {
        String sql =
            """
            CREATE TABLE IF NOT EXISTS cottages (
                cottageId INTEGER PRIMARY KEY AUTOINCREMENT,
                reserved BOOLEAN,
                ownerId INTEGER,
                cottageName VARCHAR,
                location VARCHAR,
                price REAL,
                area REAL,
                capacity INTEGER,
                description VARCHAR,
                FOREIGN KEY (ownerId) REFERENCES users(userId)
            );
            """;

        try (Statement stmt = databaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Mökki-taulukko luotu onnituneesti tietokantaan.");
        } catch (SQLException e) {
            System.out.println("Mökki-taulukon luonti epäonnistui: " + e.getMessage());
        }
    }

    public void insertCottage(int ownerId, boolean reserved, String cottageName, String location, double price, double area, int capacity, String description) {
        String sql = "INSERT INTO cottages (ownerId, reserved, cottageName, location, price, area, capacity, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            pstmt.setBoolean(2, reserved);
            pstmt.setString(3, cottageName);
            pstmt.setString(4, location);
            pstmt.setDouble(5, price);
            pstmt.setDouble(6, area);
            pstmt.setInt(7, capacity);
            pstmt.setString(8, description);
            pstmt.executeUpdate();
            System.out.println("Mökki lisätty onnistuneesti tietokantaan.");
        } catch (SQLException e) {
            System.out.println("Virhe mökin lisäämisessä: " + e.getMessage());
        }
    }

    public void deleteCottage(int cottageId) {
        Connection conn = databaseManager.getConnection();
        if (conn == null) {
            System.err.println("Mökkiä ei voitu poistaa, ei yhteyttä tietokantaan.");
            return;
        }

        String sql = "DELETE FROM cottages WHERE cottageId = ?";
        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, cottageId);
            pstmt.executeUpdate();
            System.out.println("Mökki poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma mökin poistamisessa tietokannasta." + e.getMessage());
        }
    }

    public void updateCottage(int cottageId, int ownerId, boolean reserved, String cottageName, String location, double price, double area, int capacity, String description) {
        String sql = "UPDATE cottages SET ownerId = ?, reserved = ?, cottageName = ?, location = ?, price = ?, area = ?, capacity = ?, description = ? WHERE cottageId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            pstmt.setBoolean(2, reserved);
            pstmt.setString(3, cottageName);
            pstmt.setString(4, location);
            pstmt.setDouble(5, price);
            pstmt.setDouble(6, area);
            pstmt.setInt(7, capacity);
            pstmt.setString(8, description);
            pstmt.setInt(9, cottageId);
            pstmt.executeUpdate();
            System.out.println("Tietokannan mökki " + cottageId + " päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma mökin " + ownerId + " tietojen päivittämisessä: " + e.getMessage());
        }
    }


    public List<Cottage> searchCottages(int cottageId, String cottageName, String location) {
        List<Cottage> results = new ArrayList<>();
        String sql;
        boolean hasId = cottageId != -1;

        try {
            Connection conn = databaseManager.getConnection();
            PreparedStatement stmt;

            if (hasId) {
                sql = "SELECT * FROM cottages WHERE cottageId = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, cottageId);
            } else {
                sql = "SELECT * FROM cottages WHERE cottageName LIKE ? OR location LIKE ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + cottageName + "%");
                stmt.setString(2, "%" + location + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cottage cottage = new Cottage(
                        rs.getInt("cottageId"),
                        rs.getBoolean("reserved"),
                        rs.getInt("ownerId"),
                        rs.getString("cottageName"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getDouble("area"),
                        rs.getInt("capacity"),
                        rs.getString("description")
                );
                results.add(cottage);
            }
        } catch (SQLException e) {
            System.err.println("Virhe mökkien hakemisessa: " + e.getMessage());
        }

        return results;
    }

}




