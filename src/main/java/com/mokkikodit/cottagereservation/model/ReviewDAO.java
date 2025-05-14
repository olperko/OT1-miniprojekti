package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReviewDAO {

    private DatabaseManagement dbManager;
    public ReviewDAO(DatabaseManagement dbManager) { this.dbManager = dbManager; }

    /**
     * Luo komentotekstin, joka luo varaukset-taulukon SQL-tietokantaan.
     */
    public void createReviewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS reviews(" +
                     "reviewId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "userId INTEGER, " +
                     "cottageId INTEGER, " +
                     "score REAL, " +
                     "comment TEXT, " +
                     "date TEXT)," +
                     "FOREIGN KEY (userID) REFERENCES users(userId)," +
                     "FOREIGN KEY (cottageID) REFERENCES cottages(cottageId))"
        ;
    }

    /**
     * Metodi lisää uuden arvioinnin SQL-tietokantaan.
     * @param userID arvin tekijän käyttäjä ID
     * @param cottageID arvioidun mökin ID
     * @param score käyttäjän antamat pisteet
     * @param comment käyttäjän antama kommentti
     * @param date arvioinnin päivämäärä
     */
    public void insertReview(int userID, int cottageID, int score, String comment, String date) {
        String sql = "INSERT INTO reservations (userID, cottageID, score, comment, date) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, cottageID);
            pstmt.setDouble(3, score);
            pstmt.setString(4, comment);
            pstmt.setString(5, date);
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
        String sql = "DELETE FROM reviews WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
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
     * @param score käyttäjän antamat pisteet
     * @param comment käyttäjän antama kommentti
     * @param postDate arvioinnin päivämäärä
     */
    public void updateReview(int id, double score, String comment, String postDate) {
        String sql = "UPDATE reviews SET " +
                        "score = ?, SET comment = ?, SET postDate = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setDouble(1, score);
            pstmt.setString(2, comment);
            pstmt.setString(3, postDate);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            System.out.println("Arvioinnin tiedot päivitetty onnistuneesti.");
        } catch (SQLException e) {
            System.out.println("Ongelma arvioinnin tietojen päivittämisessä: " + e.getMessage());
        }
    }

    // KESKEN
    //
    //
    public String getAllReviews() {
        String sql = "SELECT * FROM reviews";
        String result ="";

        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("userID");
                int cottageId = rs.getInt("cottageID");
                double score = rs.getDouble("score");
                String comment = rs.getString("comment");
                String date = rs.getString("date");

                result += "Arvion id: " + id + ". Arvioijan id: " + userId + ". Mökin id: "
                        + cottageId + ". Pisteet: " + score + ". Kommentti " + comment + ". Päivämäärä: " + date + ".\n";
            }

        } catch (SQLException e) {
            System.out.println("Virhe arvostelujen hakemisessa: " + e.getMessage());
        }
        return result;
    }

}





