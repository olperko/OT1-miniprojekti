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


//KESKEN
public class ReviewManager {

    private ReviewDAO reviewDAO;

    private DatabaseManagement databaseManagement;
    private ObservableList<Review> reviews = FXCollections.observableArrayList();

    @FXML private TableView<Review> reviewTableView;
    @FXML private TableColumn<Review, Integer> reviewIdColumn;
    @FXML private TableColumn<Review, Integer> userIdColumn;
    @FXML private TableColumn<Review, Integer> cottageIdColumn;
    @FXML private TableColumn<Review, Double> scoreColumn;
    @FXML private TableColumn<Review, String> commentColumn;
    @FXML private TableColumn<Review, String> dateColumn;


    //KESKEN
    @FXML private Button newReviewButton;
    @FXML private TextField reviewIdField;
    @FXML private TextField userIdField;
    @FXML private TextField cottageIdField;
    @FXML private TextField scoreField;
    @FXML private TextField commentField;
    @FXML private TextField dateField;

    @FXML private Button saveChangesButton;

    @FXML
    public void initialize() {

        reviewIdColumn.setCellValueFactory(new PropertyValueFactory<>("reviewId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        cottageIdColumn.setCellValueFactory(new PropertyValueFactory<>("cottageId"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        reviewTableView.setItems(reviews);

        reviewTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showReviewDetails(newSelection);
            }
        });

        saveChangesButton.setOnAction(event -> {
            saveReservationDetails();
        });

        newReviewButton.setOnAction(event -> {
            //kesken
        });
    }


    public void setReviewDAO(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.reviewDAO = new ReviewDAO(db);
    }

    public void loadReservationsFromDatabase() {
        reviews.clear();

        try {
            Connection con = databaseManagement.getConnection();
            if (con == null) {
                System.err.println("Database connection is null");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reviews");

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("reservationId"),
                        rs.getInt("userId"),
                        rs.getInt("cottageId"),
                        rs.getDouble("score"),
                        rs.getString("comment"),
                        rs.getString("date")
                );
                reviews.add(review);
            }

        } catch (SQLException e) {
            System.err.println("Error loading review: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showReviewDetails(Review review) {
        reviewIdField.setText(String.valueOf(review.getReviewID()));
        userIdField.setText(String.valueOf(review.getUserID()));
        cottageIdField.setText(String.valueOf(review.getCottageId()));
        scoreField.setText(String.valueOf(review.getScore()));
        commentField.setText(review.getComment());
        dateField.setText(review.getDate());
    }

    private void saveReservationDetails() {
        Review selected = reviewTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int reviewID = Integer.parseInt(reviewIdColumn.getText());
            int userId = Integer.parseInt(userIdField.getText());
            int cottageId = Integer.parseInt(cottageIdField.getText());
            Double score = Double.parseDouble(scoreField.getText());
            String comment = commentField.getText();
            String date = dateField.getText();


            reviewDAO.updateReview(
                    selected.getReviewID(),
                    score,
                    comment,
                    date
            );

            selected.setReviewID(reviewID);
            selected.setScore(score);
            selected.setComment(comment);
            selected.setDate(date);

            reviewTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä arvostelun tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }
}





