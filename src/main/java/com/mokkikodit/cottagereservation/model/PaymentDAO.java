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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "reservationId INTEGER, " +
                "amount REAL, " +
                "paymentType TEXT, " +
                "paymentStatus BOOLEAN, " +
                "confirmationDate TEXT, " +
                "FOREIGN KEY (reservationId) REFERENCES reservations(id)" +
                ")";

        try (Statement stmt = databaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Maksu-taulukko luotu onnituneesti.");
        } catch (SQLException e) {
            System.out.println("Taulun luonti epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodi lisää uuden maksun SQL-tietokantaan.
     * @param id maksun ID
     * @param reservationId maksuun yhdistetyn varauksen ID
     * @param amount maksun rahallinen määrä
     * @param paymentType maksutyyppi
     * @param paymentStatus maksun tila
     */
    public void insertPayment(int id, int reservationId, double amount, String paymentType, String paymentStatus) {

        String sql = "INSERT INTO payments (id, reservationId, amount, paymentType, paymentStatus) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, reservationId);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, paymentType);
            pstmt.setString(5, paymentStatus);
            pstmt.setString(6, String.valueOf(id));
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
        String sql = "DELETE FROM payments WHERE id = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Käyttäjä > " + id + " < poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma käyttäjän > " + id +  " < poistamisessa tietokannasta." + e.getMessage());
        }
    }

    /**
     * Metodi päivittää SQL-tietokannassa olevan maksun tiedot.
     *
     * @param id               maksun ID
     * @param reservationId    varauksen ID
     * @param amount           maksun rahallinen määrä
     * @param paymentType      maksutyyppi
     * @param paymentStatus    maksun tila
     * @param confirmationDate
     */
    public void updatePayment(int id, int reservationId, double amount, String paymentType, String paymentStatus, String confirmationDate) {
        String sql = "UPDATE payment SET " +
                        "reservationId = ?, SET amount = ?, " +
                        "SET paymentType = ?, SET paymentStatus = ?," +
                        "SET confirmationDate = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(2, reservationId);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, paymentType);
            pstmt.setString(5, paymentStatus);
            pstmt.setString(6, confirmationDate);
            pstmt.executeUpdate();
            System.out.println("Tietokannan maksu > " + id + " < päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma maksun > " + id + " < tietojen päivittämisessä: " + e.getMessage());
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
                int id = rs.getInt("id");
                int reservationId = rs.getInt("reservationId");
                double amount = rs.getDouble("amount");
                String paymentType = rs.getString("paymentType");
                boolean paymentStatus = rs.getBoolean("paymentStatus");

                result += "Maksun id: " + id + ". varauksen id: " + reservationId + ". Maksun summa: "
                        + amount + ". Maksutapa: " + paymentType + ". Maksettu: " + paymentStatus + ".\n";
            }

        } catch (SQLException e) {
            System.out.println("Virhe maksujen hakemisessa: " + e.getMessage());
        }
        return result;
    }

}






