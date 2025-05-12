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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class ReservationManager {

    private ReservationDAO reservationDAO;

    private DatabaseManagement databaseManagement;
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML private TableColumn<Reservation, Integer> cottageIdReservationColumn;
    @FXML private TableColumn<Reservation, Integer> userIdColumn;
    @FXML private TableColumn<Reservation, Integer> guestAmountColumn;
    @FXML private TableColumn<Reservation, String> spanOfReservationColumn;
    @FXML private TableColumn<Reservation, String> reservationStatusColumn;
    @FXML private TableColumn<Reservation, Integer> paymentStatusColumn;
    @FXML private TableColumn<Reservation, String> additionalReservationInfo;

    @FXML private Button newReservationButton;
    @FXML private TextField userIdField;
    @FXML private TextField cottageIdReservationField;
    @FXML private TextField guestAmountField;
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private ComboBox reservationStatusComboBox;
    @FXML private CheckBox paymentStatusCheckBox;
    @FXML private TextArea additionalInfoField;
    @FXML private Button saveChangesButton;
    @FXML private Button removeReservationButton;

    @FXML
    public void initialize() {

        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        cottageIdReservationColumn.setCellValueFactory(new PropertyValueFactory<>("cottageId"));
        guestAmountColumn.setCellValueFactory(new PropertyValueFactory<>("guestAmount"));
        spanOfReservationColumn.setCellValueFactory(new PropertyValueFactory<>("spanOfReservation"));
        reservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStatus"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        additionalReservationInfo.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

        reservationTableView.setItems(reservations);

        reservationTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showReservationDetails(newSelection);
            }
        });

        saveChangesButton.setOnAction(event -> saveReservationDetails());

        newReservationButton.setOnAction(event -> openNewReservationDialog());

        removeReservationButton.setOnAction(event -> {
            Reservation selected = reservationTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                reservations.remove(selected);
                reservationDAO.deleteReservation(selected.getReservationId());
                reservationTableView.refresh();
            }
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
                String startDateStr = rs.getString("startDate");
                String endDateStr = rs.getString("endDate");
                LocalDate startDate, endDate;
                try {
                    startDate = LocalDate.parse(startDateStr);
                } catch (Exception e) {
                    startDate = Instant.ofEpochMilli(Long.parseLong(startDateStr)).atZone(ZoneId.systemDefault()).toLocalDate();
                }
                try {
                    endDate = LocalDate.parse(endDateStr);
                } catch (Exception e) {
                    endDate = Instant.ofEpochMilli(Long.parseLong(endDateStr)).atZone(ZoneId.systemDefault()).toLocalDate();
                }

                Reservation reservation = new Reservation(
                        rs.getInt("reservationId"),
                        rs.getInt("userId"),
                        rs.getInt("cottageId"),
                        rs.getInt("guestAmount"),
                        startDate,
                        endDate,
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

        userIdField.setText(String.valueOf(reservation.getUserId()));
        cottageIdReservationField.setText(String.valueOf(reservation.getCottageId()));
        guestAmountField.setText(String.valueOf(reservation.getGuestAmount()));
        startDateField.setValue(reservation.getStartDate());
        endDateField.setValue(reservation.getEndDate());
        reservationStatusComboBox.setValue(reservation.getReservationStatus());
        paymentStatusCheckBox.setSelected(reservation.isPaymentStatus());
        additionalInfoField.setText(reservation.getAdditionalInfo());
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
            String reservationStatus = (String) reservationStatusComboBox.getValue();
            Boolean paymentStatus = paymentStatusCheckBox.isSelected();
            String additionalInfo = additionalInfoField.getText();


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
            selected.setStartDate(startDate);
            selected.setEndDate(endDate);
            selected.setReservationStatus(reservationStatus);
            selected.setPaymentStatus(paymentStatus);
            selected.setAdditionalInfo(additionalInfo);

            reservationTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Virhe:" + e.getMessage());
            e.printStackTrace();
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

    @FXML
    private TextField searchField;
    public void handleSearchReservations() {
        String query = searchField.getText();

        int reservationId = -1;
        try {
            reservationId = Integer.parseInt(query);
        } catch (NumberFormatException e) {

        }

        StringBuilder resultText = new StringBuilder();

        if (reservationId != -1) {
            String sql = "SELECT * FROM reservations WHERE reservationId = ?";
            try (PreparedStatement stmt = databaseManagement.getConnection().prepareStatement(sql)) {
                stmt.setInt(1, reservationId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        resultText.append(formatReservation(rs));
                    }
                }
            } catch (SQLException e) {
                resultText.append("Virhe varauksen hakemisessa: ").append(e.getMessage());
            }
        } else {
            ReservationDAO reservationDAO = new ReservationDAO(databaseManagement);
            resultText.append(reservationDAO.getAllReservations());
        }

        if (resultText.length() == 0) {
            resultText.append("Hakusanalla ").append(query).append(" ei löytynyt varauksia.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/reservation_results.fxml"));
            Parent root = loader.load();

            ReservationResultsController controller = loader.getController();
            controller.setResultsText(resultText.toString());

            Stage stage = new Stage();
            stage.setTitle("Varaushaku");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Resultsetin muotoilu yhdeksi varaukseksi
    private String formatReservation(ResultSet rs) throws SQLException {
        int id = rs.getInt("reservationId");
        int userId = rs.getInt("userId");
        int cottageId = rs.getInt("cottageId");
        int guestAmount = rs.getInt("guestAmount");
        String startDate = rs.getString("startDate");
        String endDate = rs.getString("endDate");
        String additionalInfo = rs.getString("additionalInfo");
        String reservationStatus = rs.getString("reservationStatus");
        boolean paymentStatus = rs.getBoolean("paymentStatus");

        return String.format(
                "Varaus ID: %d, Käyttäjä ID: %d, Mökki ID: %d, Yöpyjien määrä: %d\nAlkupäivä: %s, Loppupäivä: %s\nTila: %s, Maksu: %s\nLisätiedot: %s\n\n",
                id, userId, cottageId, guestAmount, startDate, endDate,
                reservationStatus, paymentStatus ? "Maksettu" : "Ei maksettu", additionalInfo
        );
    }

}




