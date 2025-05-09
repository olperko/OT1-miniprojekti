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
                "spanOfReservation TEXT, " +
                "additionalInfo TEXT," +
                "reservationStatus TEXT," +
                "paymentStatus BOOLEAN)";

        try (Statement stmt = dbManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Varaukset-taulukko luotu onnistuneesti.");
        } catch (SQLException e) {
            System.err.println("Virhe varaukset taulukkoa luodessa: " + e.getMessage());
        }
    }


    public void insertReservation(int userID, int cottageID, int guestAmount, String spanOfReservation, String reservationStatus, boolean paymentStatus, String additionalInfo) {
        String sql = "INSERT INTO reservations (userID, cottageID, guestAmount, beginningDate, beginningTime, endDate, endTime) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, cottageID);
            pstmt.setInt(3, guestAmount);
            pstmt.setString(4, spanOfReservation);
            pstmt.setString(5, reservationStatus);
            pstmt.setBoolean(6, paymentStatus);
            pstmt.setString(7, additionalInfo);
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

    public void updateReservation(int id, int guestAmount, String spanOfReservation, String additionalInfo, String reservationStatus, boolean paymentStatus) {
        String sql = "UPDATE reservations SET " +
                        "guestAmount = ?, " +
                        "SET spanOfReservation = ?, SET additionalInfo = ?, " +
                        "SET reservationStatus = ?, SET paymentStatus = ?, " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, guestAmount);
            pstmt.setString(2, spanOfReservation);
            pstmt.setString(3, additionalInfo);
            pstmt.setString(4, reservationStatus);
            pstmt.setBoolean(5, paymentStatus);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
            System.out.println("Varauksen tiedot päivitetty onnistuneesti.");
        } catch (SQLException e) {
            System.out.println("Ongelma varauksen tietojen päivittämisessä: " + e.getMessage());
        }
    }

    // KESKEN
    //
    //
    public String getAllReservations() {
        String sql = "SELECT * FROM reservations";
        String result ="";

        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userID");
                int cottageId = rs.getInt("cottageID");
                int guestAmount = rs.getInt("guestAmount");
                String spanOfReservation = rs.getString("beginningDate");
                String additionalInfo = rs.getString("beginningTime");
                String reservationStatus = rs.getString("endDate");
                Boolean paymentStatus = rs.getBoolean("paymentStatus");

                result += "Varauksen id: " + id + ". Varaajan id: " + userId + ". Mökin id: "
                        + cottageId + ". Yöpyjien määrä: " + guestAmount + ".\n Varauksen alkamisaika "
                        + spanOfReservation + ". Varauksen loppumisaika: " + additionalInfo +
                        ". Varauksen loppumispäivä: " + reservationStatus + ". Varauksen tila: " + paymentStatus + ". Maksun tila" + paymentStatus + ".\n";
            }

        } catch (SQLException e) {
            System.out.println("Virhe varausten hakemisessa: " + e.getMessage());
        }
        return result;
    }

}





