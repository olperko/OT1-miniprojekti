module com.mokkikodit.cottagereservation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mokkikodit.cottagereservation to javafx.fxml;
    exports com.mokkikodit.cottagereservation;
}