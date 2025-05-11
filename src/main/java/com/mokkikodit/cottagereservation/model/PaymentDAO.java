package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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


    public List<Payment> searchPayments(int paymentId, int reservationId, String paymentType, String paymentStatus) {
        StringBuilder sql = new StringBuilder("SELECT * FROM payments WHERE 1=1");
        if (paymentId != -1) sql.append(" AND paymentId = ?");
        if (reservationId != -1) sql.append(" AND reservationId = ?");
        if (paymentType != null && !paymentType.isEmpty()) sql.append(" AND paymentType LIKE ?");
        if (paymentStatus != null && !paymentStatus.isEmpty()) sql.append(" AND paymentStatus LIKE ?");

        List<Payment> payments = new ArrayList<>();

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (paymentId != -1) pstmt.setInt(paramIndex++, paymentId);
            if (reservationId != -1) pstmt.setInt(paramIndex++, reservationId);
            if (paymentType != null && !paymentType.isEmpty()) pstmt.setString(paramIndex++, "%" + paymentType + "%");
            if (paymentStatus != null && !paymentStatus.isEmpty()) pstmt.setString(paramIndex++, "%" + paymentStatus + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Payment payment = new Payment(
                            rs.getInt("paymentId"),
                            rs.getInt("reservationId"),
                            rs.getInt("amount"),
                            rs.getString("paymentType"),
                            rs.getString("paymentStatus"),
                            rs.getString("paymentDate")
                    );
                    payments.add(payment);
                }
            }
        } catch (SQLException e) {
            System.out.println("Virhe maksujen hakemisessa hakuehdolla: " + e.getMessage());
        }

        return payments;
    }




}






