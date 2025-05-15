package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Review;
import com.mokkikodit.cottagereservation.model.ReviewDAO;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Optional;


public class ReviewManager {

    private ReviewDAO reviewDAO;

    private DatabaseManagement databaseManagement;
    private final ObservableList<Review> reviews = FXCollections.observableArrayList();

    @FXML private TableView<Review> reviewTableView;
    @FXML private TableColumn<Review, Integer> reviewIdColumn;
    @FXML private TableColumn<Review, Integer> reservationIdColumn;
    @FXML private TableColumn<Review, Double> scoreColumn;
    @FXML private TableColumn<Review, String> commentColumn;
    @FXML private TableColumn<Review, String> dateColumn;

    @FXML private Button newReviewButton;
    @FXML private Button removeReviewButton;
    @FXML private TextField searchField;
    @FXML private TextField reservationIdField;
    @FXML private TextField scoreField;
    @FXML private TextArea commentField;
    @FXML private TextField dateField;

    @FXML private Button saveChangesButton;

    @FXML
    public void initialize() {

        reviewIdColumn.setCellValueFactory(new PropertyValueFactory<>("reviewId"));
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        reviewTableView.setItems(reviews);

        reviewTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showReviewDetails(newSelection);
            }
        });

        saveChangesButton.setOnAction(event -> saveReviewDetails());

        newReviewButton.setOnAction(event -> openNewReviewDialog());

        removeReviewButton.setOnAction(event -> {
            Review selected = reviewTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Vahvista poisto");
                alert.setHeaderText("Haluatko varmasti poistaa arvioinnin?");
                alert.setContentText("Olet poistamassa arvioinnin: " + selected.getReviewId());

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    reviews.remove(selected);
                    reviewDAO.deleteReview(selected.getReviewId());
                    reviewTableView.refresh();
                }
            }
        });
    }


    public void setReviewDAO(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.reviewDAO = new ReviewDAO(db);
    }

    public void loadReviewsFromDatabase() {
        reviews.clear();

        try {
            Connection con = databaseManagement.getConnection();
            if (con == null) {
                System.err.println("Tietokantaan ei ole yhteyttä.");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reviews");

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("reviewId"),
                        rs.getInt("reservationId"),
                        rs.getDouble("score"),
                        rs.getString("comment"),
                        rs.getString("date")
                );
                reviews.add(review);
            }

        } catch (SQLException e) {
            System.err.println("Virhe ladatessa arviointeja: " + e.getMessage());
        }
    }

    private void showReviewDetails(Review review) {
        reservationIdField.setText(String.valueOf(review.getReservationId()));
        scoreField.setText(String.valueOf(review.getScore()));
        commentField.setText(review.getComment());
        dateField.setText(review.getDate());
    }

    private void saveReviewDetails() {
        Review selected = reviewTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int reservationId = Integer.parseInt(reservationIdField.getText());
            double score = Double.parseDouble(scoreField.getText());
            String comment = commentField.getText();
            String date = dateField.getText();


            reviewDAO.updateReview(
                    selected.getReviewId(),
                    reservationId,
                    score,
                    comment,
                    date
            );

            selected.setReservationId(reservationId);
            selected.setScore(score);
            selected.setComment(comment);
            selected.setDate(date);

            reviewTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä arvostelun tietoja: " + e.getMessage());
        }
    }

    private void openNewReviewDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/NewReview.fxml"));
            Parent root = loader.load();

            NewReviewController controller = loader.getController();
            controller.setReviewDAO(this.reviewDAO);

            controller.setOnSaveSuccess(this::loadReviewsFromDatabase);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Uuden arvioinnin lisääminen");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Virhe uuden arvioinnin lisääminen: " + e.getMessage());
        }
    }


    @FXML public void handleSearchReviews() {
        String query = searchField.getText();

        int reviewId = -1;
        String reservationId = "", score = "";

        try {
            reviewId = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            reservationId = query;
            score = query;
        }

        List<Review> results = reviewDAO.searchReviews(reviewId, reservationId, score);

        StringBuilder resultText = new StringBuilder();
        for (Review c : results) {
            resultText.append("Arviointi-ID: ").append(c.getReviewId()).append("\n")
                    .append("Varaus-ID: ").append(c.getReservationId()).append("\n")
                    .append("Pisteet: ").append(c.getScore()).append("\n")
                    .append("Kommentti: ").append(c.getComment()).append("\n")
                    .append("Päivämäärä: ").append(c.getDate()).append("\n")
                    .append("\n\n");
        }

        if (results.isEmpty()) {
            resultText.append("Hakusanalla ").append(query).append(" ei löytynyt arviointeja.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/review_results.fxml"));
            Parent root = loader.load();

            ReviewResultsController controller = loader.getController();
            controller.setResultsText(resultText.toString());

            Stage stage = new Stage();
            stage.setTitle("Arviointihaku");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Virhe arvioinnin hakemiesssa: " + e.getMessage());
        }
    }
}





