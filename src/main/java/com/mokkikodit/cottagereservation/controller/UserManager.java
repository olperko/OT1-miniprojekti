package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Reservation;
import com.mokkikodit.cottagereservation.model.User;
import com.mokkikodit.cottagereservation.model.UserDAO;
import com.mokkikodit.cottagereservation.util.DatabaseManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserManager {

    private UserDAO userDAO;

    private DatabaseManagement databaseManagement;
    private ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    private TableView<User> userTableView;
    @FXML private TableColumn<User, Integer> userIdColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> ownedCottagesColumn;
    @FXML private TableColumn<User, String> phoneNumberColumn;
    @FXML private TableColumn<User, Boolean> isBusinessColumn;
    @FXML private TableColumn<User, String> additionalInfoUserColumn;

    @FXML private Button newUserButton;
    @FXML private TextField emailField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField ownedCottagesField;
    @FXML private TextField phoneNumberField;
    @FXML private CheckBox isBusinessCheckBox;
    @FXML private TextArea additionalInfoUserArea;
    @FXML private Button saveUserChangesButton;
    @FXML private Button removeUserButton;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.userDAO = new UserDAO(db);
    }

    @FXML public void initialize() {
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        ownedCottagesColumn.setCellValueFactory(new PropertyValueFactory<>("ownedCottages"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        isBusinessColumn.setCellValueFactory(new PropertyValueFactory<>("isBusiness"));
        additionalInfoUserColumn.setCellValueFactory(new PropertyValueFactory<>("additionalInfo"));

        userTableView.setItems(users);

        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showUserDetails(newSelection);
            }
        });

        saveUserChangesButton.setOnAction(event -> {
            saveUserDetails();
        });

        newUserButton.setOnAction(event -> {
            openNewUserDialog();
        });

        removeUserButton.setOnAction(event -> {
            User selected = userTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                users.remove(selected);
                userDAO.deleteUser(selected.getUserId());
                userTableView.refresh();
            }
        });
    }

    public void loadUsersFromDatabase() {
        users.clear();

        try {
            Connection con = databaseManagement.getConnection();
            if (con == null) {
                System.err.println("Database connection is null");
                return;
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                User user = new User(
                        rs.getInt("userId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("ownedCottages"),
                        rs.getString("phoneNumber"),
                        rs.getBoolean("isBusiness"),
                        rs.getString("additionalInfo")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Virhe ladatessa käyttäjiä: " + e.getMessage());

        }
    }

    private void showUserDetails(User user) {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        ownedCottagesField.setText(user.getOwnedCottages());
        phoneNumberField.setText(user.getPhoneNumber());
        isBusinessCheckBox.setSelected(user.getIsBusiness());
        additionalInfoUserArea.setText(user.getAdditionalInfo());
    }

    private void saveUserDetails() {
        User selected = userTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String ownedCottages = ownedCottagesField.getText();
            String phoneNumber = phoneNumberField.getText();
            boolean isBusiness = isBusinessCheckBox.isSelected();
            String additionalInfo = additionalInfoUserArea.getText();


            userDAO.updateUser(
                    selected.getUserId(),
                    firstName,
                    lastName,
                    email,
                    phoneNumber,
                    isBusiness,
                    additionalInfo
            );

            selected.setFirstName(firstName);
            selected.setLastName(lastName);
            selected.setEmail(email);
            selected.setOwnedCottages(ownedCottages);
            selected.setPhoneNumber(phoneNumber);
            selected.setBusiness(isBusiness);
            selected.setAdditionalInfo(additionalInfo);

            userTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä käyttäjän tietoja: " + e.getMessage());
        }
    }

    private void openNewUserDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/newUser.fxml"));
            Parent root = loader.load();

            NewUserController controller = loader.getController();
            controller.setUserDAO(this.userDAO);

            controller.setOnSaveSuccess( this::loadUsersFromDatabase );

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Uuden käyttäjän lisääminen");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            System.out.println("Virhe 'Uusi käyttäjä' -ikkunaa avatessa: " + e.getMessage());
        }
    }
}
