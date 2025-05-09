package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Payment;
import com.mokkikodit.cottagereservation.model.PaymentDAO;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

//KESKEN

public class PaymentManager {
    private PaymentDAO paymentDAO;

    private DatabaseManagement databaseManagement;
    private ObservableList<Payment> payments = FXCollections.observableArrayList();

    @FXML
    private TableView<Payment> paymentTableView;
    @FXML private TableColumn<Payment, Integer> paymentIdColumn;
    @FXML private TableColumn<Payment, Integer> reservationIdColumn;
    @FXML private TableColumn<Payment, Integer> amountColumn;
    @FXML private TableColumn<Payment, String> paymentTypeColumn;
    @FXML private TableColumn<Payment, String> paymentStatusColumn;
    @FXML private TableColumn<Payment, Date> confirmationDateColumn;

    @FXML private Button newPaymentButton;
    @FXML private TextField reservationIdField;
    @FXML private TextField amountField;
    @FXML private TextField paymentTypeField;
    @FXML private TextField paymentStatusField;
    @FXML private TextField confirmationDateField;
    @FXML private Button savePaymentChangesButton;

    @FXML
    public void initialize() {

        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        confirmationDateColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationDate"));


        paymentTableView.setItems(payments);

        paymentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showPaymentDetails(newSelection);
            }
        });

        savePaymentChangesButton.setOnAction(event -> {
            savePaymentDetails();
        });

        newPaymentButton.setOnAction(event -> {
            openNewCottageDialog();
        });
    }


    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.paymentDAO = new PaymentDAO(db);
    }

    public void loadPaymentsFromDatabase() {
        payments.clear();

        try {
            Connection con = databaseManagement.getConnection();
            if (con == null) {
                System.err.println("Database connection is null");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM payments");

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("paymentId"),
                        rs.getInt("reservationId"),
                        rs.getInt("amount"),
                        rs.getString("paymentType"),
                        rs.getString("paymentStatus"),
                        rs.getString("confirmatonDate")
                );
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.err.println("Error loading payments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showPaymentDetails(Payment payment) {
        reservationIdField.setText(String.valueOf(payment.getReservationID()));
        amountField.setText(String.valueOf(payment.getAmount()));
        paymentStatusField.setText(String.valueOf(payment.getPaymentStatus()));
        paymentTypeField.setText(String.valueOf(payment.getPaymentType()));
        confirmationDateField.setText(String.valueOf(payment.getConfirmationDate()));
    }

    private void savePaymentDetails() {
        Payment selected = paymentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int reservationId = Integer.parseInt(reservationIdField.getText());
            int amount = Integer.parseInt(amountField.getText());
            String paymentType = paymentTypeField.getText();
            String paymentStatus = paymentStatusField.getText();
            String confirmationDate = confirmationDateField.getText();


            paymentDAO.updatePayment(
                    selected.getPaymentID(),
                    reservationId,
                    amount,
                    paymentType,
                    paymentStatus,
                    confirmationDate
            );

            selected.setReservationID(reservationId);
            selected.setAmount(amount);
            selected.setPaymentType(paymentType);
            selected.setPaymentStatus(paymentStatus);
            selected.setConfirmationDate(confirmationDate);

            paymentTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä maksun tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //KESKEN, Cottage luokan metodi mallina, viimeistelen kun Payment luokan front end tehty
    private void openNewCottageDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/NewCottage.fxml"));
            Parent root = loader.load();

            NewCottageController controller = loader.getController();
            //controller.setCottageDAO(this.cottageDAO);

            controller.setOnSaveSuccess(() -> {
                //loadCottagesFromDatabase();
            });

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Uuden mökin lisääminen");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

