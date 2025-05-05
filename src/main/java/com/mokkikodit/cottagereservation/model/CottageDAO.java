package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CottageDAO {

    private DatabaseManagement databaseManagemer;

    public CottageDAO(DatabaseManagement databaseManager) {
        this.databaseManagemer = databaseManager;
    }

    /**
     * Luo komentotekstin, joka syötetään SQLite tietokantaan.
     */
    public void createCottageTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS cottages (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            owner_id INTEGER,
            reserved BOOLEAN,
            cottageName VARCHAR,
            location VARCHAR,
            price REAL,
            area REAL,
            capacity INTEGER,
            description VARCHAR
        );
        """;

        try (Statement stmt = databaseManagemer.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Mökki-taulukko luotu onnituneesti.");
        } catch (SQLException e) {
            System.out.println("Taulun luonti epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Asettaa yksittäisen mökin tiedot SQL-komennolla
     *
     * @param owner_id
     * @param reserved
     * @param cottageName
     * @param location
     * @param price
     * @param area
     * @param capacity
     * @param description
     */
    public void insertCottage(int owner_id, int reserved, String cottageName, String location, double price, double area, int capacity, String description) {

        Connection conn = databaseManagemer.getConnection();
        if (conn == null) {
            System.err.println("Mökkiä ei voitu lisätä, tietokannan yhteys on > null <.");
            return;
        }

        String sql = "INSERT INTO cottages (owner_id, reserved, cottageName, location, price, area, capacity, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseManagemer.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, owner_id);
            pstmt.setInt(2, reserved);
            pstmt.setString(3, cottageName);
            pstmt.setString(4, location);
            pstmt.setDouble(5, price);
            pstmt.setDouble(6, area);
            pstmt.setInt(7, capacity);
            pstmt.setString(8, description);
            pstmt.executeUpdate();
            System.out.println("Tietokannan mökki-taulukko päivitetty.");
        }
        catch (SQLException e) {
            System.out.println("Tietokannan mökki-taulukon päivitys ei onnistunut: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean deleteCottage(int id) {
        Connection conn = databaseManagemer.getConnection();
        if (conn == null) {
            System.err.println("Mökkiä ei voitu poistaa, tietokannan yhteys on > null <.");
            return false;
        }

        String sql = "DELETE FROM cottages WHERE id = ?";
        try (PreparedStatement pstmt = databaseManagemer.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Mökki poistettu tietokannasta.");
            return true;
        }
        catch (SQLException e)    {
            System.out.println("Ongelma mökin poistamisessa tietokannasta." + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void updateCottage(int owner_id, boolean reserved, String cottageName, String location, int price, int area, int capacity, String description) {
        String sql = "UPDATE cottages SET " +
                        "reserved = ?, SET name = ?, SET location = ?, SET price = ?, " +
                        "SET area = ?, SET capacity = ?, SET description = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = databaseManagemer.getConnection().prepareStatement(sql)) {
            pstmt.setBoolean(1, reserved);
            pstmt.setString(2, cottageName);
            pstmt.setString(3, location);
            pstmt.setInt(4, price);
            pstmt.setInt(5, area);
            pstmt.setInt(6, capacity);
            pstmt.setString(7, description);
            pstmt.executeUpdate();
            System.out.println("Tietokannan mökki " + owner_id + " päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma mökin " + owner_id + " tietojen päivittämisessä: " + e.getMessage());
        }
    }

    public void getAllCottages() {
        String sql = "SELECT * FROM cottages";

        try (Statement stmt = databaseManagemer.getConnection().prepareStatement(sql)) {
        } catch (SQLException e) {}

    }



}




