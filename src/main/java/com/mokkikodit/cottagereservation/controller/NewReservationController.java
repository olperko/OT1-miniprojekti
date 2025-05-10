package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewReservationController {

    @FXML private TextField cottageIdReservationField;
    @FXML private TextField customerIdField;
    @FXML private TextField guestAmountField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox reservationStatusComboBox;
    @FXML private CheckBox paymentStatusCheckBox;
    @FXML private TextArea additionalInfoField;
    @FXML private Button saveReservationButton;
    @FXML private Button cancelReservationButton;

    private ReservationDAO reservationDAO;
    private Runnable onSaveSuccess;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

    public void setReservationDAO(ReservationDAO dao) { this.reservationDAO = dao; }

    public void setOnSaveSuccess(Runnable r) {
        this.onSaveSuccess = r;
    }

    @FXML
    private void initialize() {

        saveReservationButton.setOnAction(e -> saveReservation());

        cancelReservationButton.setOnAction(e -> {
            ((Stage) cancelReservationButton.getScene().getWindow()).close();
        });

        cottageIdReservationField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cottageIdReservationField.setText(oldValue);
            }
        });

        customerIdField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                customerIdField.setText(oldValue);
            }
        });

        guestAmountField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                guestAmountField.setText(oldValue);
            }
        });

    }

    private void saveReservation() {
        try {
            int userID = Integer.parseInt(customerIdField.getText());
            int cottageId = Integer.parseInt(cottageIdReservationField.getText());
            int guestAmount = Integer.parseInt(guestAmountField.getText());
            String startDate = String.valueOf(startDatePicker.getValue());
            String endDate = String.valueOf(endDatePicker.getValue());
            String startDateStr = (startDate != null) ? startDate.toString() : null;
            String endDateStr = (endDate != null) ? endDate.toString() : null;
            String reservationStatus = reservationStatusComboBox.getValue().toString();
            boolean paymentStatus = paymentStatusCheckBox.isSelected();
            String additionalInfo = additionalInfoField.getText();

            if (reservationDAO == null) {
                System.out.println("Tietokantaan ei saatu yhteyttä.");
                return;
            }

            reservationDAO.insertReservation(userID, cottageId, guestAmount, startDateStr, endDateStr, reservationStatus, paymentStatus, additionalInfo);

            showAlert(Alert.AlertType.INFORMATION, "Onnistui",
                    "Varaus on lisätty!",
                    "Uusi varaus on lisätty tietokantaan.");

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }

            ((Stage) saveReservationButton.getScene().getWindow()).close();

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Syöte virhe",
                    "Väärä numeraalinen formaatti.",
                    "Syötä vain numeroita kenttiin 'Varaus ID', 'Mökki ID', 'Asiakas ID' ja 'Asikasmäärä'.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Virhe",
                    "Ongelma varauksen lisäämisessä",
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


