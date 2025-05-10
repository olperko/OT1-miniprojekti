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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


//KESKEN
//Muokataan lopullisesti kuntoon kun luokan front end valmistuu
public class ReservationManager {

    private ReservationDAO reservationDAO;

    private DatabaseManagement databaseManagement;
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML private TableColumn<Reservation, Integer> cottageIdReservationColumn;
    @FXML private TableColumn<Reservation, Integer> userIdColumn;
    @FXML private TableColumn<Reservation, Integer> guestAmountColumn;
    @FXML private TableColumn<Reservation, String> spanOfReservationColumn;
    @FXML private TableColumn<Reservation, String> reservationStatusColumn;
    @FXML private TableColumn<Reservation, Integer> paymentStatusColumn; // tähän boolean ollut reserved

    //Front end asiota, katsellaan tarkemmin kun front end luokan osalta tehty
    @FXML private Button newReservationButton;
    @FXML private TextField reservationIdField;
    @FXML private TextField userIdField;
    @FXML private TextField cottageIdReservationField;
    @FXML private TextField guestAmountField;
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private TextField reservationStatusField;
    @FXML private CheckBox paymentStatusCheckBox;
    @FXML private TextArea additionalInfoField;
    @FXML private Button saveChangesButton;

    @FXML
    public void initialize() {

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        cottageIdReservationColumn.setCellValueFactory(new PropertyValueFactory<>("cottageId"));
        guestAmountColumn.setCellValueFactory(new PropertyValueFactory<>("guestAmount"));
        spanOfReservationColumn.setCellValueFactory(new PropertyValueFactory<>("spanOfReservation"));
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
            openNewReservationDialog();
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
                        rs.getString("startDate"),
                        rs.getString("endDate"),
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

        reservationIdField.setText(String.valueOf(reservation.getReservationId()));
        userIdField.setText(String.valueOf(reservation.getUserId()));
        cottageIdReservationField.setText(String.valueOf(reservation.getCottageId()));
        guestAmountField.setText(String.valueOf(reservation.getGuestAmount()));
        startDateField.setValue(LocalDate.parse(reservation.getStartDate()));
        endDateField.setValue(LocalDate.parse(reservation.getEndDate()));
        reservationStatusField.setText(reservation.getReservationStatus());
        additionalInfoField.setText(reservation.getAdditionalInfo());

        paymentStatusCheckBox.setSelected(reservation.isPaymentStatus());
    }

    private void saveReservationDetails() {
        Reservation selected = reservationTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int guestAmount = Integer.parseInt(guestAmountField.getText());
            LocalDate startDate = startDateField.getValue();
            LocalDate endDate = endDateField.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String spanOfReservation = startDate.format(formatter) + " - " + endDate.format(formatter);
            String reservationStatus = reservationStatusField.getText();
            Boolean paymentStatus = paymentStatusCheckBox.isSelected();
            String additionalInfo = additionalInfoField.textProperty().getValue();


            reservationDAO.updateReservation(
                    selected.getReservationId(),
                    guestAmount,
                    startDate,
                    endDate,
                    reservationStatus,
                    paymentStatus,
                    additionalInfo
            );

            selected.setGuestAmount(guestAmount);
            selected.setStartDate(String.valueOf(startDate));
            selected.setEndDate(String.valueOf(endDate));
            selected.setReservationStatus(reservationStatus);
            selected.setPaymentStatus(paymentStatus);
            selected.setAdditionalInfo(additionalInfo);

            reservationTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä varauksen tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openNewReservationDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/newReservation.fxml"));
            Parent root = loader.load();

            NewReservationController controller = loader.getController();
            controller.setReservationDAO(this.reservationDAO);

            controller.setOnSaveSuccess(this::loadReservationsFromDatabase);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Uuden varauksen lisääminen");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




