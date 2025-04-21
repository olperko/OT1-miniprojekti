package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReservationDAO {

    private DatabaseManagement dbManager;
    public ReservationDAO(DatabaseManagement dbManager) { this.dbManager = dbManager; }

    /**
     * Luo komentotekstin, joka luo varaukset-taulukon SQL-tietokantaan.
     */
    public void createReservationTable() {
        String sql = "CREATE TABLE IF NOT EXISTS reservations(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userID INTEGER, " +
                "cottageID INTEGER, " +
                "guestAmount INTEGER, " +
                "beginningDate TEXT, " +
                "beginningTime TEXT, " +
                "endDate TEXT, " +
                "endTime TEXT" +
                "reservationStatus TEXT" +
                "paymentStatus BOOLEAN)";
    }

    /**
     * Metodi lisää uuden varauksen SQL-tietokantaan.
     * @param userID
     * @param cottageID
     * @param guestAmount
     * @param beginningDate
     * @param beginningTime
     * @param endDate
     * @param endTime
     */
    public void insertReservation(int userID, int cottageID, int guestAmount, String beginningDate, String beginningTime, String endDate, String endTime) {
        String sql = "INSERT INTO reservations (userID, cottageID, guestAmount, beginningDate, beginningTime, endDate, endTime) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, cottageID);
            pstmt.setInt(3, guestAmount);
            pstmt.setString(4, beginningDate);
            pstmt.setString(5, beginningTime);
            pstmt.setString(6, endDate);
            pstmt.setString(7, endTime);
            pstmt.executeUpdate();
            System.out.println("Tietokannan varaukset-taulukko päivitetty.");
        }
        catch (SQLException e) {
            System.out.println("Tietokannan varaukset-taulukon päivitys ei onnistunut: " + e.getMessage());
        }
    }

    /**
     * Metodi poistaa varauksen SQL-tietokannasta.
     * @param id varauksen ID
     */
    public void deleteReservation(int id) {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Varaus poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma varauksen poistamisessa tietokannasta." + e.getMessage());
        }
    }

    /**
     * Metodi päivittää SQL-tietokannassa olevan varauksen tiedot.
     * @param guestAmount vieraiden määrä
     * @param beginningDate varauksen alkamispäivämäärä
     * @param beginningTime varauksen alkamiskellonaika
     * @param endDate varauksen päättymispäivämäärä
     * @param endTime varauksen päättymiskellonaika
     */
    public void updateReservation(int id, int guestAmount, String beginningDate, String beginningTime, String endDate, String endTime, String reservationStatus, boolean paymentStatus) {
        String sql = "UPDATE reservations SET " +
                        "guestAmount = ?, " +
                        "SET beginningDate = ?, SET beginningTime = ?, " +
                        "SET endDate = ?, SET endTime = ?, " +
                        "SET reservationStatus = ?, SET paymentStatus = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, guestAmount);
            pstmt.setString(2, beginningDate);
            pstmt.setString(3, beginningTime);
            pstmt.setString(4, endDate);
            pstmt.setString(5, endTime);
            pstmt.setString(6, reservationStatus);
            pstmt.setBoolean(7, paymentStatus);
            pstmt.setInt(8, id);
            pstmt.executeUpdate();
            System.out.println("Varauksen tiedot päivitetty onnistuneesti.");
        } catch (SQLException e) {
            System.out.println("Ongelma varauksen tietojen päivittämisessä: " + e.getMessage());
        }
    }

    // KESKEN
    //
    //
    public void getAllReservations() {
        String sql = "SELECT * FROM reservations";

        try (Statement stmt = dbManager.getConnection().prepareStatement(sql)) {
        } catch (SQLException e) {}

    }

}





