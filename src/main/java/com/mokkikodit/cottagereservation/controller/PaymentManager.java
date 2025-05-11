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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


public class PaymentManager {
    private PaymentDAO paymentDAO;

    private DatabaseManagement databaseManagement;
    private final ObservableList<Payment> payments = FXCollections.observableArrayList();

    @FXML
    private TableView<Payment> paymentTableView;
    @FXML private TableColumn<Payment, Integer> paymentIdColumn;
    @FXML private TableColumn<Payment, Integer> reservationIdColumn;
    @FXML private TableColumn<Payment, Integer> amountColumn;
    @FXML private TableColumn<Payment, String> paymentTypeColumn;
    @FXML private TableColumn<Payment, String> paymentStatusColumn;
    @FXML private TableColumn<Payment, Date> paymentDateColumn;
    @FXML private Button newPaymentButton;
    @FXML private TextField reservationIdField;
    @FXML private TextField amountField;
    @FXML private TextField paymentTypeField;
    @FXML private TextField paymentStatusField;
    @FXML private DatePicker paymentDatePicker;
    @FXML private Button savePaymentChangesButton;
    @FXML private Button removePaymentButton;

    @FXML
    public void initialize() {

        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        paymentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        paymentTableView.setItems(payments);

        paymentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showPaymentDetails(newSelection);
            }
        });

        savePaymentChangesButton.setOnAction(event -> savePaymentDetails());

        newPaymentButton.setOnAction(event -> openNewPaymentDialog());

        removePaymentButton.setOnAction(event -> {
            Payment selected = paymentTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                payments.remove(selected);
                paymentDAO.deletePayment(selected.getPaymentId());
                paymentTableView.refresh();
            }
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
                        rs.getString("paymentDate")
                );
                payments.add(payment);
            }

        } catch (SQLException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
    }

    private void showPaymentDetails(Payment payment) {
        reservationIdField.setText(String.valueOf(payment.getReservationId()));
        amountField.setText(String.valueOf(payment.getAmount()));
        paymentTypeField.setText(payment.getPaymentType());
        paymentStatusField.setText(payment.getPaymentStatus());
        paymentDatePicker.getValue();
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
            LocalDate paymentDate = paymentDatePicker.getValue();

            paymentDAO.updatePayment(
                    reservationId,
                    amount,
                    paymentType,
                    paymentStatus,
                    String.valueOf(paymentDate),
                    selected.getPaymentId()
            );

            selected.setReservationId(reservationId);
            selected.setAmount(amount);
            selected.setPaymentType(paymentType);
            selected.setPaymentStatus(paymentStatus);
            selected.setPaymentDate(String.valueOf(paymentDate));

            paymentTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä maksun tietoja: " + e.getMessage());
        }
    }

    private void openNewPaymentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/NewPayment.fxml"));
            Parent root = loader.load();

            NewPaymentController controller = loader.getController();
            controller.setPaymentDAO(this.paymentDAO);

            controller.setOnSaveSuccess(this::loadPaymentsFromDatabase);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Uuden mökin lisääminen");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Virhe 'Uusi maksu' -ikkunaa avatessa: " + e.getMessage());
        }
    }

    @FXML
    private TextField searchField;

    @FXML
    public void handleSearchPayments() {
        String query = searchField.getText();

        int paymentId = -1;
        int reservationId = -1;
        String paymentType = "";
        String paymentStatus = "";

        try {
            paymentId = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            paymentType = query;
            paymentStatus = query;
        }

        List<Payment> results = paymentDAO.searchPayments(paymentId, reservationId, paymentType, paymentStatus);

        StringBuilder resultText = new StringBuilder();
        for (Payment payment : results) {
            resultText.append("Maksun ID: ").append(payment.getPaymentId())
                    .append(", Varaus ID: ").append(payment.getReservationId())
                    .append(", Summa: ").append(payment.getAmount())
                    .append(", Tyyppi: ").append(payment.getPaymentType())
                    .append(", Tila: ").append(payment.getPaymentStatus())
                    .append(", Päivämäärä: ").append(payment.getPaymentDate())
                    .append("\n");
        }

        if(results.isEmpty()) {
            resultText.append("Tuloksia haulle: " + query + " ei löytynyt");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/payment_results.fxml"));

            Parent root = loader.load();

            PaymentResultsController controller = loader.getController();
            controller.setResultsText(resultText.toString());

            Stage stage = new Stage();
            stage.setTitle("Hakutulokset");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

