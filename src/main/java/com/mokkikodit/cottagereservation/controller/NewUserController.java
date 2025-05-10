package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewUserController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneNumberField;
    @FXML private CheckBox isBusinessCheckBox;
    @FXML private TextArea additionalInfoField;
    @FXML private Button saveButtonUser;
    @FXML private Button cancelButtonUser;

    private UserDAO userDAO;
    private Runnable onSaveSuccess;

    public void setUserDAO(UserDAO dao) {
        this.userDAO = dao;
    }

    public void setOnSaveSuccess(Runnable r) {
        this.onSaveSuccess = r;
    }

    @FXML
    private void initialize() {

        saveButtonUser.setOnAction(e -> saveUser());

        cancelButtonUser.setOnAction(e -> {
            ((Stage) cancelButtonUser.getScene().getWindow()).close();
        });

    }

    private void saveUser() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            boolean isBusiness = isBusinessCheckBox.isSelected();
            String additionalInfo = additionalInfoField.getText();

            if (userDAO == null) {
                System.out.println("Tietokantaan ei saatu yhteyttä.");
                return;
            }

            userDAO.insertUser(firstName, lastName, email, phoneNumber, isBusiness, additionalInfo);

            showAlert(Alert.AlertType.INFORMATION, "Onnistui",
                    "Mökki on lisätty!",
                    "Uusi mökki on lisätty tietokantaan.");

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }

            ((Stage) saveButtonUser.getScene().getWindow()).close();

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Syöte virhe",
                    "Väärä numeraalinen formaatti.",
                    "Syötä vain numeroita kenttiin 'omistaja', 'hinta', 'pinta-ala' ja 'majoituskapasiteetti'.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Virhe",
                    "Ongelma mökin lisäämisessä",
                    "Virhe: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
