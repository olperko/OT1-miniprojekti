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
    @FXML private TextField beginningDateField;
    @FXML private TextField beginningTimeField;
    @FXML private TextField endDateField;
    @FXML private TextField endTimeField;
    @FXML private TextField reservationStatusField;
    @FXML private CheckBox paymentStatusCheckBox;
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
                        rs.getInt("ownerId"),
                        rs.getInt("guestAmount"),
                        rs.getString("beginningDate"),
                        rs.getString("beginningTime"),
                        rs.getString("endDate"),
                        rs.getString("endTime"),
                        rs.getString("reservationStatus"),
                        rs.getBoolean("paymentStatus")
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
        ownerIdField.setText(String.valueOf(reservation.getOwnerID()));
        guestAmountField.setText(String.valueOf(reservation.getGuestAmount()));
        beginningDateField.setText(reservation.getBeginningDate());
        beginningTimeField.setText(reservation.getBeginningTime());
        endDateField.setText(reservation.getEndDate());
        endTimeField.setText(reservation.getEndTime());

        paymentStatusCheckBox.setSelected(reservation.isPaymentStatus());
    }

    private void saveReservationDetails() {
        Reservation selected = reservationTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int userId = Integer.parseInt(userIdField.getText());
            int cottageId = Integer.parseInt(cottageIdField.getText());
            int ownerId = Integer.parseInt(ownerIdField.getText());
            int guestAmount = Integer.parseInt(guestAmountField.getText());
            String beginningDate = beginningDateField.getText();
            String beginningTime = beginningTimeField.getText();
            String endDate = endDateField.getText();
            String endTime = endTimeField.getText();
            String reservationStatus = reservationStatusField.getText();
            boolean paymentStatus = paymentStatusCheckBox.isSelected();


            reservationDAO.updateReservation(
                    selected.getReservationID(),
                    guestAmount,
                    beginningDate,
                    beginningTime,
                    endDate,
                    endTime,
                    reservationStatus,
                    paymentStatus
            );

            selected.setGuestAmount(guestAmount);
            selected.setBeginningDate(beginningDate);
            selected.setBeginningTime(beginningTime);
            selected.setEndDate(endDate);
            selected.setEndTime(endTime);
            selected.setReservationStatus(reservationStatus);
            selected.setPaymentStatus(paymentStatus);

            reservationTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä varauksen tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




