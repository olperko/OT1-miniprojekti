package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Cottage;
import com.mokkikodit.cottagereservation.model.CottageDAO;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.*;

public class CottageManager {

    private CottageDAO cottageDAO;

    @FXML
    private TableView<Cottage> cottageTableView;
    private DatabaseManagement databaseManagement;
    private ObservableList<Cottage> cottages = FXCollections.observableArrayList();

    @FXML private TableColumn<Cottage, Integer> cottageIdColumn;
    @FXML private TableColumn<Cottage, Integer> ownerIdColumn;
    @FXML private TableColumn<Cottage, String> cottageNameColumn;
    @FXML private TableColumn<Cottage, String> locationColumn;
    @FXML private TableColumn<Cottage, Double> priceColumn;
    @FXML private TableColumn<Cottage, Double> areaColumn;
    @FXML private TableColumn<Cottage, Integer> capacityColumn;
    @FXML private TableColumn<Cottage, String> descriptionColumn;

    @FXML private TextField cottageNameField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private TextField areaField;
    @FXML private TextField capacityField;
    @FXML private TextField ownerIdField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox reservedCheckBox;
    @FXML private Button saveChangesButton;

    @FXML
    public void initialize() {

        cottageIdColumn.setCellValueFactory(new PropertyValueFactory<>("cottageId"));
        ownerIdColumn.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        cottageNameColumn.setCellValueFactory(new PropertyValueFactory<>("cottageName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        cottageTableView.setItems(cottages);

        cottageTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showCottageDetails(newSelection);
            }
        });

        saveChangesButton.setOnAction(event -> {
            saveCottageDetails();
        });
    }

    public void setCottageDAO(CottageDAO cottageDAO) {
        this.cottageDAO = cottageDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.cottageDAO = new CottageDAO(db);
    }

    public void loadCottagesFromDatabase() {
        cottages.clear();

        try {
            Connection con = databaseManagement.getConnection();
            if (con == null) {
                System.err.println("Database connection is null");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cottages");

            while (rs.next()) {
                Cottage cottage = new Cottage(
                        rs.getInt("cottageId"),
                        rs.getBoolean("reserved"),
                        rs.getInt("ownerId"),
                        rs.getString("cottageName"),
                        rs.getString("location"),
                        rs.getDouble("area"),
                        rs.getDouble("price"),
                        rs.getInt("capacity"),
                        rs.getString("description")
                );
                cottages.add(cottage);
            }

        } catch (SQLException e) {
            System.err.println("Error loading cottages: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showCottageDetails(Cottage cottage) {
        cottageNameField.setText(cottage.getCottageName());
        locationField.setText(cottage.getLocation());
        priceField.setText(String.valueOf(cottage.getPrice()));
        areaField.setText(String.valueOf(cottage.getArea()));
        capacityField.setText(String.valueOf(cottage.getCapacity()));
        ownerIdField.setText(String.valueOf(cottage.getOwnerId()));
        descriptionArea.setText(String.valueOf(cottage.getDescription()));
        reservedCheckBox.setSelected(cottage.getIsReserved());
    }

    private void saveCottageDetails() {
        Cottage selected = cottageTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int ownerId = Integer.parseInt(ownerIdField.getText());
            String cottageName = cottageNameField.getText();
            String location = locationField.getText();
            double price = Double.parseDouble(priceField.getText());
            double area = Double.parseDouble(areaField.getText());
            int capacity = Integer.parseInt(capacityField.getText());
            String description = descriptionArea.getText();
            boolean isReserved = reservedCheckBox.isSelected();

            cottageDAO.updateCottage(
                    selected.getCottageId(),
                    ownerId,
                    isReserved,
                    cottageName,
                    location,
                    price,
                    area,
                    capacity,
                    description
            );

            selected.setOwnerId(ownerId);
            selected.setCottageName(cottageName);
            selected.setLocation(location);
            selected.setPrice(price);
            selected.setArea(area);
            selected.setCapacity(capacity);
            selected.setDescription(description);
            selected.setIsReserved(isReserved);

            cottageTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä mökin tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
