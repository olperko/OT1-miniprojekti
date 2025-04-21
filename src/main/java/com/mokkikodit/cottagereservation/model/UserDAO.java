package com.mokkikodit.cottagereservation.model;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDAO {

    private DatabaseManagement dbManager;
    public UserDAO(DatabaseManagement dbManager) { this.dbManager = dbManager; }

    /**
     * Luo komentotekstin, joka luo käyttäjä-taulukon SQL-tietokantaan.
     */
    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "firstName TEXT, " +
                "lastName TEXT, " +
                "role TEXT, " +
                "isBusiness BOOLEAN)";
    }

    /**
     * Metodi joka suorittaa käyttäjän lisäämisen SQL-tietokantaan.
     * @param userID käyttäjä ID
     * @param email käyttäjän sähköposti
     * @param firstName käyttäjän etunimi
     * @param lastName käyttäjän sukunimi
     * @param role käyttäjän rooli alustalla ( ostaja / myyjä )
     * @param isBusiness onko käyttäjä yritys
     */
    public void insertUser(int userID, String email, String firstName, String lastName, String role, boolean isBusiness) {

        String sql = "INSERT INTO users (userID, email, firstName, lastName, role, isBusiness) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            pstmt.setString(2, email);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, role);
            pstmt.setBoolean(6, isBusiness);
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

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
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
    public void updateUser(int userID, String email, String firstName, String lastName, String role, boolean isBusiness) {
        String sql = "UPDATE user SET " +
                        "email = ?, SET firstName = ?, SET lastName = ?, SET role = ?, SET isBusiness = ? " +
                     "WHERE id = ?";

        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, role);
            pstmt.setBoolean(5, isBusiness);
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
    public void getAllUsers() {
        String sql = "SELECT * FROM users";

        try (Statement stmt = dbManager.getConnection().prepareStatement(sql)) {
        } catch (SQLException e) {}

    }

}





