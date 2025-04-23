package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class CottageDAO {

    private DatabaseManagement dbManager;
    public CottageDAO(DatabaseManagement dbManager) { this.dbManager = dbManager; }

    /**
     * Luo komentotekstin, joka syötetään SQLite tietokantaan.
     */
    public void createCottageTable() {
        String sql = "CREATE TABLE IF NOT EXISTS cottages(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ownerId INTEGER, " +
                "reserved BOOLEAN, " +
                "name TEXT, " +
                "location TEXT, " +
                "price REAL, " +
                "area REAL, " +
                "capacity INTEGER, " +
                "description TEXT)";

        try (Statement stmt = dbManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Mökki-taulu luotu.");
        } catch (SQLException e) {
            System.out.println("Taulun luonti epäonnistui: " + e.getMessage());
        }
    }

    /**
     * Asettaa yksittäisen mökin tiedot SQL-komennolla
     *
     * @param ownerID
     * @param reserved
     * @param name
     * @param location
     * @param price
     * @param area
     * @param capacity
     * @param description
     */
    public void insertCottage(int ownerID, int reserved, String name, String location, double price, double area, int capacity, String description) {

        String sql = "INSERT INTO cottages (ownerId, reserved, name, location, price, area, capacity, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, ownerID);
            pstmt.setInt(2, reserved);
            pstmt.setString(3, name);
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
        }
    }

    public void deleteCottage(int id) {
        String sql = "DELETE FROM cottages WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Mökki poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma mökin poistamisessa tietokannasta." + e.getMessage());
        }
    }

    public void updateCottage(int ownerId, boolean reserved, String name, String location, int price, int area, int capacity, String description) {
        String sql = "UPDATE cottages SET " +
                        "reserved = ?, SET name = ?, SET location = ?, SET price = ?, " +
                        "SET area = ?, SET capacity = ?, SET description = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setBoolean(1, reserved);
            pstmt.setString(2, name);
            pstmt.setString(3, location);
            pstmt.setInt(4, price);
            pstmt.setInt(5, area);
            pstmt.setInt(6, capacity);
            pstmt.setString(7, description);
            pstmt.executeUpdate();
            System.out.println("Tietokannan mökki " + ownerId + " päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma mökin " + ownerId + " tietojen päivittämisessä: " + e.getMessage());
        }
    }

    public void getAllCottages() {
        String sql = "SELECT * FROM cottages";

        try (Statement stmt = dbManager.getConnection().prepareStatement(sql)) {
        } catch (SQLException e) {}

    }



}




