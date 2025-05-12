package com.mokkikodit.cottagereservation.view;

import com.mokkikodit.cottagereservation.controller.CottageManager;
import com.mokkikodit.cottagereservation.controller.PaymentManager;
import com.mokkikodit.cottagereservation.controller.ReservationManager;
import com.mokkikodit.cottagereservation.controller.UserManager;
import com.mokkikodit.cottagereservation.model.*;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class SystemUI {

    public Parent initializeApplication() throws IOException {

        DatabaseManagement databaseManagement = new DatabaseManagement();
        databaseManagement.connect();

        CottageDAO cottageDAO = new CottageDAO(databaseManagement);
        ReservationDAO reservationDAO = new ReservationDAO(databaseManagement);
        UserDAO userDAO = new UserDAO(databaseManagement);
        PaymentDAO paymentDAO = new PaymentDAO(databaseManagement);
        ReviewDAO reviewDAO = new ReviewDAO(databaseManagement);

        cottageDAO.createCottageTable();
        reservationDAO.createReservationTable();
        userDAO.createUserTable();
        paymentDAO.createPaymentTable();
        reviewDAO.createReviewTable();

        FXMLLoader infoLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/info.fxml"));
        Parent infoRoot = infoLoader.load();

        FXMLLoader cottageLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/cottageview.fxml"));
        Parent cottageRoot = cottageLoader.load();
        CottageManager cottageManager = cottageLoader.getController();
        cottageManager.setDatabaseManagement(databaseManagement);
        cottageManager.setCottageDAO(cottageDAO);
        cottageManager.loadCottagesFromDatabase();

        FXMLLoader reservationLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/reservationview.fxml"));
        Parent reservationRoot = reservationLoader.load();
        ReservationManager reservationManager = reservationLoader.getController();
        reservationManager.setDatabaseManagement(databaseManagement);
        reservationManager.setReservationDAO(reservationDAO);
        reservationManager.loadReservationsFromDatabase();

        FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/userview.fxml"));
        Parent userRoot = userLoader.load();
        UserManager userManager = userLoader.getController();
        userManager.setDatabaseManagement(databaseManagement);
        userManager.setUserDAO(userDAO);
        userManager.loadUsersFromDatabase();

        FXMLLoader paymentLoader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/paymentview.fxml"));
        Parent paymentRoot = paymentLoader.load();
        PaymentManager paymentManager = paymentLoader.getController();
        paymentManager.setDatabaseManagement(databaseManagement);
        paymentManager.setPaymentDAO(paymentDAO);
        paymentManager.loadPaymentsFromDatabase();

        Tab infoTab = new Tab("Info", infoRoot);
        infoTab.setClosable(false);

        Tab cottageTab = new Tab("Mökit", cottageRoot);
        cottageTab.setClosable(false);

        Tab reservationTab = new Tab("Varaukset", reservationRoot);
        reservationTab.setClosable(false);

        Tab userTab = new Tab("Käyttäjät", userRoot);
        userTab.setClosable(false);

        Tab paymentTab = new Tab("Maksut", paymentRoot);
        paymentTab.setClosable(false);

        return new TabPane(infoTab, cottageTab, reservationTab, userTab, paymentTab);
    }
}
