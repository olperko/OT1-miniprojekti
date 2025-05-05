module com.mokkikodit.cottagereservation {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.mokkikodit.cottagereservation.application;
    exports com.mokkikodit.cottagereservation.controller;

    opens com.mokkikodit.cottagereservation.controller to javafx.fxml;
}