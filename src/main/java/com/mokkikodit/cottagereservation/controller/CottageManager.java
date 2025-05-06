package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Cottage;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class CottageManager {

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
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
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
}
