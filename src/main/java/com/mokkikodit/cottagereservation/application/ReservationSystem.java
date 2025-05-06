package com.mokkikodit.cottagereservation.application;

import com.mokkikodit.cottagereservation.controller.CottageManager;
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
        //cottageDAO.insertCottage(16, false, "Toisen mökin nimi","Toisen mökin sijainti", 1129.99, 1255.5, 70, "Toisen mökin kuvaus");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/frontend.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1500, 500);

        CottageManager cottageManager = fxmlLoader.getController();

        cottageManager = fxmlLoader.getController();
        cottageManager.setDatabaseManagement(databaseManagement);
        cottageManager.setCottageDAO(cottageDAO);
        cottageManager.loadCottagesFromDatabase();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cottage Reservation System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

