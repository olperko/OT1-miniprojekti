package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.PaymentDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Text;

public class NewPaymentController {

    @FXML private TextField paymentReservationId;
    @FXML private TextField amountField;
    @FXML private TextField paymentTypeComboBox;
    @FXML private TextField paymentStatusComboBox;
    @FXML private DatePicker paymentDatePicker ;
    @FXML private Button savePaymentButton;
    @FXML private Button cancelPaymentButton;

    private PaymentDAO paymentDAO;
    private Runnable onSaveSuccess;

    public void setPaymentDAO(PaymentDAO dao) {
        this.paymentDAO = dao;
    }

    public void setOnSaveSuccess(Runnable r) {
        this.onSaveSuccess = r;
    }

    @FXML
    private void initialize() {

        savePaymentButton.setOnAction(e -> savePayment());

        cancelPaymentButton.setOnAction(e -> {
            ((Stage) cancelPaymentButton.getScene().getWindow()).close();
        });

        amountField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                amountField.setText(oldValue);
            }
        });

    }

    private void savePayment() {
        try {
            int reservationId = Integer.parseInt(paymentReservationId.getText());
            double amount = Double.parseDouble(amountField.getText());
            String paymentType = paymentTypeComboBox.getText();
            String paymentStatus = paymentStatusComboBox.getText();
            String paymentDateTS = String.valueOf(paymentDatePicker.getValue());
            String paymentDate = (paymentDateTS != null) ? paymentDateTS.toString() : null;

            if (paymentDAO == null) {
                System.out.println("Tietokantaan ei saatu yhteyttä.");
                return;
            }

            paymentDAO.insertPayment(reservationId, amount, paymentType, paymentStatus, paymentDate);

            showAlert(Alert.AlertType.INFORMATION, "Onnistui",
                    "Maksu on lisätty!",
                    "Uusi maksu on lisätty tietokantaan.");

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }

            ((Stage) savePaymentButton.getScene().getWindow()).close();

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Syöte virhe",
                    "Täytä kaikki kentät.",
                    "Luodaksesi uuden maksun, kaikki kentät on täytettävä.");
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Virhe",
                    "Ongelma maksun lisäämisessä",
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

