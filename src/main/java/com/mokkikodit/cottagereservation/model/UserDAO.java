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
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "ownedCottages TEXT, " +
                "role TEXT, " +
                "isBusiness BOOLEAN, " +
                "additionalInfo TEXT" +
                ")";

        try (Statement stmt = databaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("Käyttäjä-taulukko luotu onnituneesti.");
        } catch (SQLException e) {
            System.out.println("Taulun luonti epäonnistui: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Metodi joka suorittaa käyttäjän lisäämisen SQL-tietokantaan.
     * @param email käyttäjän sähköposti
     * @param firstName käyttäjän etunimi
     * @param lastName käyttäjän sukunimi
     * @param role käyttäjän rooli alustalla ( ostaja / myyjä )
     * @param isBusiness onko käyttäjä yritys
     */
    public void insertUser(String email, String firstName, String lastName, String ownedCottages, String role, boolean isBusiness, String additionalInfo) {

        String sql = "INSERT INTO users (email, firstName, lastName, role, isBusiness, additionalInfo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, ownedCottages);
            pstmt.setString(5, role);
            pstmt.setBoolean(6, isBusiness);
            pstmt.setString(7, additionalInfo);
            pstmt.executeUpdate();
            System.out.println("Tietokannan käyttäjä-taulukko päivitetty.");
        }
        catch (SQLException e) {
            System.out.println("Tietokannan käyttäjä-taulukon päivitys ei onnistunut: " + e.getMessage());
        }
    }

    /**
     * Metodi joka suorittaa käyttäjän poistamisen SQL-tietokannasta.
     * @param id poistettavan käyttäjän ID.
     */
    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

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
     * @param userID käyttäjä ID
     * @param email käyttäjän sähköposti
     * @param firstName käyttäjän etunimi
     * @param lastName käyttäjän sukunimi
     * @param role käyttäjän rooli alustalla ( ostaja / myyjä )
     * @param isBusiness onko käyttäjä yritys
     */
    public void updateUser(int userID, String email, String firstName, String lastName, String ownedCottages, String role, boolean isBusiness, String additionalInfo) {
        String sql = "UPDATE user SET " +
                "email = ?, SET firstName = ?, SET lastName = ?, SET role = ?, SET isBusiness = ? , SET additionalInfo = ?" +
                "WHERE id = ?";

        try (PreparedStatement pstmt = databaseManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, ownedCottages);
            pstmt.setString(4, role);
            pstmt.setBoolean(5, isBusiness);
            pstmt.setString(6, additionalInfo);
            pstmt.executeUpdate();
            System.out.println("Tietokannan käyttäjä > " + userID + " < päivitetty.");
        } catch (SQLException e) {
            System.out.println("Ongelma käyttäjän > " + userID + " < tietojen päivittämisessä: " + e.getMessage());
        }
    }

    // KESKEN
    //
    //
    //

    public String getAllUsers() {
        String sql = "SELECT * FROM users";
        String result ="";

        try (PreparedStatement stmt = databaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String email = rs.getString("email");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String role = rs.getString("role");
                Boolean isBusiness = rs.getBoolean("isBusiness");

                result += "Asiakkaan id: " + id + ". Sähköposti: " + email + ". etunimi: "
                        + firstName + ". sukunimi: " + lastName + ". role: " + role + ". Onko yritys: " + isBusiness + ".\n";
            }

        } catch (SQLException e) {
            System.out.println("Virhe asiakkaiden hakemisessa: " + e.getMessage());
        }
        return result;
    }

}





