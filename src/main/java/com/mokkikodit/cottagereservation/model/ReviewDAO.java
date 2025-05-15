package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private final DatabaseManagement databaseManager;
    public ReviewDAO(DatabaseManagement databaseManager) { this.databaseManager = databaseManager; }

    /**
     * Luo komentotekstin, joka luo varaukset-taulukon SQL-tietokantaan.
     */
    public void createReviewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS reviews (" +
                "reviewId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "reservationId INTEGER, " +
                "score REAL, " +
                "comment TEXT, " +
                "date TEXT, " +
                "FOREIGN KEY (reservationId) REFERENCES reservations(reservationId)" +
                ");";
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Reviews-taulukko luotu onnistuneesti tietokantaan.");
        } catch (SQLException e) {
            System.out.println("Reviews-taulukon luonti epäonnistui: " + e.getMessage());
        }
    }


    /**
     * Metodi lisää uuden arvioinnin SQL-tietokantaan.
     * @param reservationId arvin tekijän käyttäjä ID
     * @param score käyttäjän antamat pisteet
     * @param comment käyttäjän antama kommentti
     * @param date arvioinnin päivämäärä
     */
    public void insertReview(int reservationId, double score, String comment, String date) {
        String sql = "INSERT INTO reviews (reservationId, score, comment, date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setDouble(2, score);
            pstmt.setString(3, comment);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
            System.out.println("Tietokannan arvioinnit-taulukko päivitetty.");
        }
        catch (SQLException e) {
            System.out.println("Tietokannan arvioinnit-taulukon päivitys ei onnistunut: " + e.getMessage());
        }
    }

    /**
     * Metodi poistaa arvioinnin SQL-tietokannasta.
     * @param id arvioinnin ID
     */
    public void deleteReview(int id) {
        String sql = "DELETE FROM reviews WHERE reviewId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Arviointi poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma arvioinnin poistamisessa tietokannasta." + e.getMessage());
        }
    }

    /**
     * Metodi päivittää arvioinnin SQL-tietokannassa.
     * @param id arvioinnin ID
     * @param reservationId varauksen ID
     * @param score käyttäjän antamat pisteet
     * @param comment käyttäjän antama kommentti
     * @param date arvioinnin päivämäärä
     */
    public void updateReview(int id, int reservationId, double score, String comment, String date) {
        String sql = "UPDATE reviews SET reservationId = ?, score = ?, comment = ?, date = ? WHERE reviewId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.setDouble(2, score);
            pstmt.setString(3, comment);
            pstmt.setString(4, date);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("Arvioinnin tiedot päivitetty onnistuneesti.");
        } catch (SQLException e) {
            System.out.println("Ongelma arvioinnin tietojen päivittämisessä: " + e.getMessage());
        }
    }


    public String getAllReviews() {
        String sql = "SELECT * FROM reviews";
        StringBuilder result = new StringBuilder();

        try (PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("reviewId");
                int reservationId = rs.getInt("reservationId");
                double score = rs.getDouble("score");
                String comment = rs.getString("comment");
                String date = rs.getString("date");

                result.append("Arvion id: ").append(id).append(". Varaus id: ").append(reservationId).append(". Pisteet: ").append(score).append(". Kommentti ").append(comment).append(". Päivämäärä: ").append(date).append(".\n");
            }

        } catch (SQLException e) {
            System.out.println("Virhe arvostelujen hakemisessa: " + e.getMessage());
        }
        return result.toString();
    }

    public List<Review> searchReviews(int reviewId, String reservationId, String score) {
        List<Review> results = new ArrayList<>();
        String sql;
        boolean hasId = reviewId != -1;

        try {
            Connection conn = databaseManager.getConnection();
            PreparedStatement stmt;

            if (hasId) {
                sql = "SELECT * FROM reviews WHERE reviewId = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, reviewId);
            } else {
                sql = "SELECT * FROM reviews WHERE reservationId LIKE ? OR score LIKE ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, "%" + reservationId + "%");
                stmt.setString(2, "%" + score + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("reviewId"),
                        rs.getInt("reservationId"),
                        rs.getDouble("score"),
                        rs.getString("comment"),
                        rs.getString("date")
                );
                results.add(review);
            }
        } catch (SQLException e) {
            System.err.println("Virhe arviointien hakemisessa: " + e.getMessage());

        }

        return results;
    }

}





