package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Cottage;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CottageManager {

    @FXML
    private TableView<Cottage> cottageTableView;
    private DatabaseManagement databaseManagement;
    private ObservableList<Cottage> cottages = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
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
                        rs.getInt("id"),
                        rs.getInt("owner_id"),
                        rs.getString("cottageName"),
                        rs.getString("location"),
                        rs.getDouble("price"),
                        rs.getDouble("area"),
                        rs.getInt("capacity"),
                        rs.getString("description")
                );
                cottages.add(cottage);
            }

            cottageTableView.getItems().setAll(cottages);

        } catch (SQLException e) {
            System.err.println("Error loading cottages: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
