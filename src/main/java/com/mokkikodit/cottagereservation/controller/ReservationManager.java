package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Reservation;
import com.mokkikodit.cottagereservation.model.ReservationDAO;
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
//Muokataan lopullisesti kuntoon kun luokan front end valmistuu
public class ReservationManager {

    private ReservationDAO reservationDAO;

    private DatabaseManagement databaseManagement;
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML private TableColumn<Reservation, Integer> userIdColumn;
    @FXML private TableColumn<Reservation, Integer> cottageIdColumn;
    @FXML private TableColumn<Reservation, Integer> ownerIdColumn;
    @FXML private TableColumn<Reservation, Integer> guestAmountColumn;
    @FXML private TableColumn<Reservation, String> beginningDateColumn;
    @FXML private TableColumn<Reservation, String> beginningTimeColumn;
    @FXML private TableColumn<Reservation, String> endDateColumn;
    @FXML private TableColumn<Reservation, String> endTimeColumn;
    @FXML private TableColumn<Reservation, String> reservationStatusColumn;
    @FXML private TableColumn<Reservation, Integer> paymentStatusColumn; // tähän boolean ollut reserved

    //Front end asiota, katsellaan tarkemmin kun front end luokan osalta tehty
    @FXML private Button newReservationButton;
    @FXML private TextField reservationIdField;
    @FXML private TextField userIdField;
    @FXML private TextField cottageIdField;
    @FXML private TextField ownerIdField;
    @FXML private TextField guestAmountField;
    @FXML private TextField spanOfReservationField;
    @FXML private TextField reservationStatusField;
    @FXML private CheckBox paymentStatusCheckBox;
    @FXML private TextArea additonalInfoField;
    @FXML private Button saveChangesButton;

    @FXML
    public void initialize() {

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        cottageIdColumn.setCellValueFactory(new PropertyValueFactory<>("cottageId"));
        ownerIdColumn.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        guestAmountColumn.setCellValueFactory(new PropertyValueFactory<>("guestAmount"));
        beginningDateColumn.setCellValueFactory(new PropertyValueFactory<>("beginningTime"));
        beginningTimeColumn.setCellValueFactory(new PropertyValueFactory<>("beginningDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        reservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStatus"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        reservationTableView.setItems(reservations);

        reservationTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showReservationDetails(newSelection);
            }
        });

        saveChangesButton.setOnAction(event -> {
            saveReservationDetails();
        });

        newReservationButton.setOnAction(event -> {
            //openNewCottageDialog();
        });
    }


    public void setReservationDAO(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.reservationDAO = new ReservationDAO(db);
    }

    public void loadReservationsFromDatabase() {
        reservations.clear();

        try {
            Connection con = databaseManagement.getConnection();
            if (con == null) {
                System.err.println("Database connection is null");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM reservations");

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("reservationId"),
                        rs.getInt("userId"),
                        rs.getInt("cottageId"),
                        rs.getInt("guestAmount"),
                        rs.getString("spanOfReservation"),
                        rs.getString("reservationStatus"),
                        rs.getBoolean("paymentStatus"),
                        rs.getString("additionalInfo")
                );
                reservations.add(reservation);
            }

        } catch (SQLException e) {
            System.err.println("Error loading reservations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showReservationDetails(Reservation reservation) {
        reservationIdField.setText(String.valueOf(reservation.getReservationID()));
        userIdField.setText(String.valueOf(reservation.getUserID()));
        cottageIdField.setText(String.valueOf(reservation.getCottageId()));
        guestAmountField.setText(String.valueOf(reservation.getGuestAmount()));
        spanOfReservationField.setText(reservation.getSpanOfReservation());
        reservationStatusField.setText(reservation.getReservationStatus());
        additonalInfoField.setText(reservation.getAdditionalInfo());

        paymentStatusCheckBox.setSelected(reservation.isPaymentStatus());
    }

    private void saveReservationDetails() {
        Reservation selected = reservationTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int userID = Integer.parseInt(userIdField.getText());
            int guestAmount = Integer.parseInt(guestAmountField.getText());
            String spanOfReservation = spanOfReservationField.getText();
            String reservationStatus = reservationStatusField.getText();
            String paymentStatus = String.valueOf(paymentStatusCheckBox.isSelected());
            String additionalInfo = additonalInfoField.textProperty().getValue();


            reservationDAO.updateReservation(
                    selected.getReservationID(),
                    guestAmount,
                    spanOfReservation,
                    reservationStatus,
                    paymentStatus,
                    Boolean.parseBoolean(additionalInfo)
            );

            selected.setGuestAmount(guestAmount);
            selected.setSpanOfReservation(spanOfReservation);
            selected.setReservationStatus(reservationStatus);
            selected.setPaymentStatus(Boolean.parseBoolean(paymentStatus));
            selected.setAdditionalInfo(additionalInfo);

            reservationTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä varauksen tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




