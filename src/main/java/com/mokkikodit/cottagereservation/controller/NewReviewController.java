package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.ReviewDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NewReviewController {

    @FXML private TextField reservationIdField;
    @FXML private TextField scoreField;
    @FXML private TextField commentField;
    @FXML private TextField dateField;
    @FXML private Button saveReviewButton;
    @FXML private Button cancelReviewButton;


    private ReviewDAO reviewDAO;
    private Runnable onSaveSuccess;

    public void setReviewDAO(ReviewDAO dao) {
        this.reviewDAO = dao;
    }

    public void setOnSaveSuccess(Runnable r) {
        this.onSaveSuccess = r;
    }

    @FXML
    private void initialize() {

        saveReviewButton.setOnAction(e -> saveReview());

        cancelReviewButton.setOnAction(e -> {
            ((Stage) cancelReviewButton.getScene().getWindow()).close();
        });

    }

    private void saveReview() {
        try {
            int reservationId = Integer.parseInt(reservationIdField.getText());
            double score = Double.parseDouble(scoreField.getText());
            String comment = commentField.getText();
            String date = dateField.getText();

            if (reviewDAO == null) {
                System.out.println("Tietokantaan ei saatu yhteyttä.");
                return;
            }

            reviewDAO.insertReview(reservationId, score, comment, date);

            showAlert(Alert.AlertType.INFORMATION, "Onnistui",
                    "Arviointi on lisätty!",
                    "Uusi arviointi on lisätty tietokantaan.");

            if (onSaveSuccess != null) {
                onSaveSuccess.run();
            }

            ((Stage) saveReviewButton.getScene().getWindow()).close();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Virhe",
                    "Ongelma arvioinnin lisäämisessä",
                    "ID-kenttää tulee syöttää kokonaisluku.");
            return;
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
