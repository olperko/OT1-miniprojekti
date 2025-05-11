package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PaymentDAO {

    private DatabaseManagement databaseManager;
    public PaymentDAO(DatabaseManagement dbManager) { this.databaseManager = dbManager; }

    /**
     * Luo komentotekstin, joka luo maksut-taulukon SQL-tietokantaan.
     */
    public void createPaymentTable() {
        String sql = "CREATE TABLE IF NOT EXISTS payments (" +
                "paymentId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "reservationId INTEGER, " +
                "amount REAL, " +
                "paymentType TEXT, " +
                "paymentStatus TEXT, " +
                "paymentDate TEXT, " +
                "FOREIGN KEY (reservationId) REFERENCES reservations(id)" +
                ")";

        try (Statement stmt = databaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Maksu-taulukko luotu onnituneesti tietokantaan.");
        } catch (SQLException e) {
            System.out.println("Taulun luonti epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodi lisää uuden maksun SQL-tietokantaan.
     * @param reservationId maksuun yhdistetyn varauksen ID
     * @param amount maksun rahallinen määrä
     * @param paymentType maksutyyppi
     * @param paymentStatus maksun tila
     */
    public void insertPayment(int reservationId, double amount, String paymentType, String paymentStatus, String paymentDate) {

        String sql = "INSERT INTO payments (reservationId, amount, paymentType, paymentStatus, paymentDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, paymentType);
            pstmt.setString(4, paymentStatus);
            pstmt.setString(5, paymentDate);

            pstmt.executeUpdate();
            System.out.println("Tietokannan maksut-taulukko päivitetty.");
        }
        catch (SQLException e) {
            System.out.println("Tietokannan maksut-taulukon päivitys ei onnistunut: " + e.getMessage());
        }
    }

    /**
     * Metodi poistaa maksun SQL-tietokannasta.
     * @param id poistettavan maksun ID
     */
    public void deletePayment(int id) {
        String sql = "DELETE FROM payments WHERE paymentId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Maksu > " + id + " < poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma maksun > " + id +  " < poistamisessa tietokannasta." + e.getMessage());
        }
    }

    /**
     * Metodi päivittää SQL-tietokannassa olevan maksun tiedot.
     *
     * @param paymentId        maksun ID
     * @param reservationId    varauksen ID
     * @param amount           maksun rahallinen määrä
     * @param paymentType      maksutyyppi
     * @param paymentStatus    maksun tila
     * @param paymentDate      maksupäivämäärä
     */
    public void updatePayment(int reservationId, double amount, String paymentType, String paymentStatus, String paymentDate, int paymentId) {
        String sql = "UPDATE payments SET " +
                        "reservationId = ?, amount = ?, " +
                        "paymentType = ?, paymentStatus = ?," +
                        "paymentDate = ? " +
                     "WHERE paymentId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, paymentType);
            pstmt.setString(4, paymentStatus);
            pstmt.setString(5, paymentDate);
            pstmt.setInt(6, paymentId);
            pstmt.executeUpdate();
            System.out.println("Tietokannan maksu > " + paymentId + " < päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma maksun > " + paymentId + " < tietojen päivittämisessä: " + e.getMessage());
        }
    }

    // KESKEN
    //
    //
    public String getAllPayments() {
        String sql = "SELECT * FROM payments";
        String result ="";

        try (PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int paymentId = rs.getInt("paymentId");
                int reservationId = rs.getInt("reservationId");
                double amount = rs.getDouble("amount");
                String paymentType = rs.getString("paymentType");
                String paymentStatus = rs.getString("paymentStatus");
                String paymentDate = rs.getString("paymentDate");

                result += "Maksun id: " + paymentId + ". varauksen id: " + reservationId + ". Maksun summa: "
                        + amount + ". Maksutapa: " + paymentType + ". Maksettu: " + paymentStatus + ".\n";
            }

        } catch (SQLException e) {
            System.out.println("Virhe maksujen hakemisessa: " + e.getMessage());
        }
        return result;
    }

}






