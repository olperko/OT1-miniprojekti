package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.CottageDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewCottageController {

    @FXML private TextField cottageNameField;
    @FXML private TextField locationField;
    @FXML private TextField ownerIdField;
    @FXML private TextField priceField;
    @FXML private TextField areaField;
    @FXML private TextField capacityField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox reservedCheckBox;
    @FXML private Button saveButtonCottage;
    @FXML private Button cancelButtonCottage;

    private CottageDAO cottageDAO;
    private Runnable onSaveSuccess;

    public void setCottageDAO(CottageDAO dao) {
        this.cottageDAO = dao;
    }

    public void setOnSaveSuccess(Runnable r) {
        this.onSaveSuccess = r;
    }

    @FXML
    private void initialize() {

        saveButtonCottage.setOnAction(e -> saveCottage());

        cancelButtonCottage.setOnAction(e -> {
            ((Stage) cancelButtonCottage.getScene().getWindow()).close();
        });

        priceField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
        });

        areaField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                areaField.setText(oldValue);
            }
        });

        capacityField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                capacityField.setText(oldValue);
            }
        });

        ownerIdField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ownerIdField.setText(oldValue);
            }
        });
    }

    private void saveCottage() {
        try {
            int ownerId = Integer.parseInt(ownerIdField.getText());
            boolean reserved = reservedCheckBox.isSelected();
            String name = cottageNameField.getText().trim();
            String location = locationField.getText().trim();
            double price = Double.parseDouble(priceField.getText());
            double area = Double.parseDouble(areaField.getText());
            int capacity = Integer.parseInt(capacityField.getText());
            String description = descriptionArea.getText();

            if (cottageDAO == null) {
                System.out.println("Tietokantaan ei saatu yhteyttä.");
                return;
            }

            cottageDAO.insertCottage(ownerId, reserved, name, location, price, area, capacity, description);

            showAlert(Alert.AlertType.INFORMATION, "Onnistui",
                    "Mökki on lisätty!",
                    "Uusi mökki on lisätty tietokantaan.");

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }

            ((Stage) saveButtonCottage.getScene().getWindow()).close();

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
