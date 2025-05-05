package com.mokkikodit.cottagereservation.application;

import com.mokkikodit.cottagereservation.controller.CottageManager;
import com.mokkikodit.cottagereservation.model.Cottage;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.mokkikodit.cottagereservation.model.CottageDAO;

import java.io.IOException;

public class ReservationSystem extends Application {
    private CottageDAO cottageDAO;
    private DatabaseManagement databaseManagement;
    private CottageManager cottageManager;

    public void start(Stage primaryStage) throws IOException {

        databaseManagement = new DatabaseManagement();
        databaseManagement.connect();

        cottageDAO = new CottageDAO(databaseManagement);

        cottageDAO.createCottageTable();
        cottageDAO.insertCottage(15, 0, "Mökin nimi","Mökin sijainti", 129.99, 125.5, 7, "Mökin kuvaus");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/frontend.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);

        CottageManager cottageManager = fxmlLoader.getController();

        cottageManager.setDatabaseManagement(databaseManagement);
        cottageManager.loadCottagesFromDatabase();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cottage Reservation System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

