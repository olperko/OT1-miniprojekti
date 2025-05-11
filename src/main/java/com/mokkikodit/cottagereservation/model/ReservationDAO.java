package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.*;
import java.time.LocalDate;

public class ReservationDAO {

    private final DatabaseManagement dbManager;
    public ReservationDAO(DatabaseManagement dbManager) { this.dbManager = dbManager; }

    /**
     * Luo komentotekstin, joka luo varaukset-taulukon SQL-tietokantaan.
     */
    public void createReservationTable() {
        String sql = "CREATE TABLE IF NOT EXISTS reservations(" +
                "reservationId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId INTEGER, " +
                "cottageId INTEGER, " +
                "guestAmount INTEGER, " +
                "startDate TEXT, " +
                "endDate TEXT, " +
                "additionalInfo TEXT," +
                "reservationStatus TEXT," +
                "paymentStatus BOOLEAN)";

        try (Statement stmt = dbManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Varaukset-taulukko luotu onnistuneesti tietokantaan.");
        } catch (SQLException e) {
            System.err.println("Virhe varaukset taulukkoa luodessa: " + e.getMessage());
        }
    }


    public void insertReservation(int userId, int cottageId, int guestAmount, String startDate, String endDate, String reservationStatus, boolean paymentStatus, String additionalInfo) {
        String sql = "INSERT INTO reservations (userId, cottageId, guestAmount, startDate, endDate, reservationStatus, paymentStatus, additionalInfo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, cottageId);
            pstmt.setInt(3, guestAmount);
            pstmt.setString(4, startDate);
            pstmt.setString(5, endDate);
            pstmt.setString(6, reservationStatus);
            pstmt.setBoolean(7, paymentStatus);
            pstmt.setString(8, additionalInfo);
            pstmt.executeUpdate();
            System.out.println("Tietokannan varaukset-taulukko päivitetty.");
        }
        catch (SQLException e) {
            System.out.println("Tietokannan varaukset-taulukon päivitys ei onnistunut: " + e.getMessage());
        }
    }

    /**
     * Metodi poistaa varauksen SQL-tietokannasta.
     * @param reservationId varauksen ID
     */
    public void deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservationId = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
            System.out.println("Varaus poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma varauksen poistamisessa tietokannasta." + e.getMessage());
        }
    }

    public void updateReservation(int reservationId, int guestAmount, LocalDate startDate , LocalDate endDate, String reservationStatus, boolean paymentStatus, String additionalInfo) {
        String sql = "UPDATE reservations SET " +
                        "guestAmount = ?, " +
                        "startDate = ?, endDate = ?, " +
                        "reservationStatus = ?, paymentStatus = ?, " +
                        "additionalInfo = ? " +
                     "WHERE reservationId = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, guestAmount);
            pstmt.setDate(2, Date.valueOf(startDate));
            pstmt.setDate(3, Date.valueOf(endDate));
            pstmt.setString(4, reservationStatus);
            pstmt.setBoolean(5, paymentStatus);
            pstmt.setString(6, additionalInfo);
            pstmt.setInt(7, reservationId);
            pstmt.executeUpdate();
            System.out.println("Varauksen tiedot päivitetty onnistuneesti.");
        } catch (SQLException e) {
            System.out.println("Ongelma varauksen tietojen päivittämisessä: " + e.getMessage());
        }
    }

    public String getAllReservations() {
        String sql = "SELECT * FROM reservations";
        StringBuilder result = new StringBuilder();

        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("reservationId");
                int userId = rs.getInt("userId");
                int cottageId = rs.getInt("cottageId");
                int guestAmount = rs.getInt("guestAmount");
                String startDate = rs.getString("startDate");
                String endDate= rs.getString("endDate");
                String additionalInfo = rs.getString("endDate");
                String reservationStatus = rs.getString("additionalInfo");
                boolean paymentStatus = rs.getBoolean("paymentStatus");

                result.append("Varauksen id: ").append(id).append(". Varaajan id: ").append(userId).append(". Mökin id: ").append(cottageId).append(". Yöpyjien määrä: ").append(guestAmount).append(".\n Varauksen alkamisaika ").append(startDate).append(endDate).append(additionalInfo).append(". Varauksen loppumispäivä: ").append(reservationStatus).append(". Varauksen tila: ").append(paymentStatus).append(". Maksun tila").append(paymentStatus).append(".\n");
            }

        } catch (SQLException e) {
            System.out.println("Virhe varausten hakemisessa: " + e.getMessage());
        }
        return result.toString();
    }

}





