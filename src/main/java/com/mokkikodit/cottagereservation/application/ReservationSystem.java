package com.mokkikodit.cottagereservation.application;

import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.application.Application;
import javafx.stage.Stage;

import com.mokkikodit.cottagereservation.model.CottageDAO;

public class ReservationSystem extends Application {
    private CottageDAO cottageDAO;
    private DatabaseManagement databaseManagement;

    public void start(Stage primaryStage) {
        databaseManagement = new DatabaseManagement();
        databaseManagement.connect();
        cottageDAO = new CottageDAO(databaseManagement);

        cottageDAO.createCottageTable();
        cottageDAO.insertCottage(15, 0, "Mökin nimi", "Mökin sijainti", 129.99, 125.5, 7, "Mökin kuvaus");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

