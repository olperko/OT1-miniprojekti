package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    private DatabaseManagement databaseManager;
    public UserDAO(DatabaseManagement databaseManager) { this.databaseManager = databaseManager; }

    /**
     * Luo komentotekstin, joka luo käyttäjä-taulukon SQL-tietokantaan.
     */
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users(" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "email TEXT, " +
                "phoneNumber TEXT, " +
                "pastReservations TEXT, " +
                "ownedCottages TEXT, " +
                "isBusiness BOOLEAN, " +
                "additionalInfo TEXT" +
                ")";

        try (Statement stmt = databaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Käyttäjä-taulukko luotu onnituneesti tietokantaan.");
        } catch (SQLException e) {
            System.out.println("Mökki-taulukon luonti epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodi joka suorittaa käyttäjän lisäämisen SQL-tietokantaan.
     * @param email käyttäjän sähköposti
     * @param firstName käyttäjän etunimi
     * @param lastName käyttäjän sukunimi
     * @param isBusiness onko käyttäjä yritys
     */
    public void insertUser(String firstName, String lastName, String email, String phoneNumber, boolean isBusiness, String additionalInfo) {

        String sql = "INSERT INTO users (firstName, lastName, email, phoneNumber, isBusiness, additionalInfo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setBoolean(5, isBusiness);
            pstmt.setString(6, additionalInfo);
            pstmt.executeUpdate();
            System.out.println("Käyttäjä lisätty onnistuneesti tietokantaan.");
        }
        catch (SQLException e) {
            System.out.println("Virhe käyttäjän lisäämisessä: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodi joka suorittaa käyttäjän poistamisen SQL-tietokannasta.
     * @param id poistettavan käyttäjän ID.
     */
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE userId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Käyttäjä poistettu tietokannasta.");
        }
        catch (SQLException e)    {
            System.out.println("Ongelma käyttäjän poistamisessa tietokannasta." + e.getMessage());
        }
    }

    /**
     * Metodi joka suorittaa käyttäjän tietojen päivittämisen SQL-tietokannassa.
     * @param userId käyttäjä ID
     * @param email käyttäjän sähköposti
     * @param firstName käyttäjän etunimi
     * @param lastName käyttäjän sukunimi
     * @param isBusiness onko käyttäjä yritys
     */
    public void updateUser(int userId, String firstName, String lastName, String email, String phoneNumber, boolean isBusiness, String additionalInfo) {
        String sql = "UPDATE users SET " +
                        "firstName = ?, lastName = ?, email = ?, phoneNumber = ?, isBusiness = ?, additionalInfo = ? " +
                     "WHERE userId = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, phoneNumber);
            pstmt.setBoolean(5, isBusiness);
            pstmt.setString(6, additionalInfo);
            pstmt.setInt(7, userId);
            pstmt.executeUpdate();
            System.out.println("Tietokannan käyttäjä > " + userId + " < päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma käyttäjän > " + userId + " < tietojen päivittämisessä: " + e.getMessage());
        }
    }

    public String getAllUsers() {
        String sql = "SELECT * FROM users";
        String result ="";

        try (PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("userId");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phoneNumber");
                Boolean isBusiness = rs.getBoolean("isBusiness");
                String additionalInfo = rs.getString("additionalInfo");

                result +=
                        "Asiakkaan id: " + userId + ". Sähköposti: " + email +
                        ". etunimi: " + firstName + ". sukunimi: " + lastName +
                        ". Puhelinnumero: " + phoneNumber + ". Sähköposti: " + email +
                        ". Onko yritys: " + isBusiness + ". Lisätiedot: " + additionalInfo + ".\n";
            }

        } catch (SQLException e) {
            System.out.println("Virhe asiakkaiden hakemisessa: " + e.getMessage());
        }
        return result;
    }

}





