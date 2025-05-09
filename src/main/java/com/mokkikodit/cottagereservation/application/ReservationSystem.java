package com.mokkikodit.cottagereservation.application;

import com.mokkikodit.cottagereservation.view.SystemUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservationSystem extends Application {

    public void start(Stage primaryStage) throws IOException {

        SystemUI systemUI = new SystemUI();
        Parent root = systemUI.initializeApplication();
        Scene scene = new Scene(root, 1165, 545);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Mökkikodit varausjärjestelmä");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

