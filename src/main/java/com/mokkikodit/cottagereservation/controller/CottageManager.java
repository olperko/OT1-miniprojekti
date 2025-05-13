package com.mokkikodit.cottagereservation.controller;

import com.mokkikodit.cottagereservation.model.Cottage;
import com.mokkikodit.cottagereservation.model.CottageDAO;
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
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CottageManager {

    private CottageDAO cottageDAO;

    private DatabaseManagement databaseManagement;
    private final ObservableList<Cottage> cottages = FXCollections.observableArrayList();

    @FXML private TableView<Cottage> cottageTableView;
    @FXML private TableColumn<Cottage, Integer> cottageIdColumn;
    @FXML private TableColumn<Cottage, Integer> reservedColumn;
    @FXML private TableColumn<Cottage, Integer> ownerIdColumn;
    @FXML private TableColumn<Cottage, String> cottageNameColumn;
    @FXML private TableColumn<Cottage, String> locationColumn;
    @FXML private TableColumn<Cottage, Double> priceColumn;
    @FXML private TableColumn<Cottage, Double> areaColumn;
    @FXML private TableColumn<Cottage, Integer> capacityColumn;
    @FXML private TableColumn<Cottage, String> descriptionColumn;

    @FXML private TextField searchField;
    @FXML private Button newCottageButton;
    @FXML private TextField cottageNameField;
    @FXML private TextField locationField;
    @FXML private TextField priceField;
    @FXML private TextField areaField;
    @FXML private TextField capacityField;
    @FXML private TextField ownerIdField;
    @FXML private TextArea descriptionArea;
    @FXML private CheckBox reservedCheckBox;
    @FXML private Button saveChangesButton;
    @FXML private Button removeCottageButton;

    @FXML
    public void initialize() {

        cottageIdColumn.setCellValueFactory(new PropertyValueFactory<>("cottageId"));
        reservedColumn.setCellValueFactory(new PropertyValueFactory<>("reserved"));
        ownerIdColumn.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        cottageNameColumn.setCellValueFactory(new PropertyValueFactory<>("cottageName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        cottageTableView.setItems(cottages);

        cottageTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showCottageDetails(newSelection);
            }
        });

        saveChangesButton.setOnAction(event -> saveCottageDetails());

        newCottageButton.setOnAction(event -> openNewCottageDialog());

        removeCottageButton.setOnAction(event -> {
            Cottage selected = cottageTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Vahvista poisto");
                alert.setHeaderText("Haluatko varmasti poistaa mökin?");
                alert.setContentText("Olet poistamassa mökin: " + selected.getCottageId());

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    cottages.remove(selected);
                    cottageDAO.deleteCottage(selected.getCottageId());
                    cottageTableView.refresh();
                }
            }
        });
    }


    public void setCottageDAO(CottageDAO cottageDAO) {
        this.cottageDAO = cottageDAO;
    }

    public void setDatabaseManagement(DatabaseManagement db) {
        this.databaseManagement = db;
        this.cottageDAO = new CottageDAO(db);
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

    private void showCottageDetails(Cottage cottage) {
        cottageNameField.setText(cottage.getCottageName());
        locationField.setText(cottage.getLocation());
        priceField.setText(String.valueOf(cottage.getPrice()));
        areaField.setText(String.valueOf(cottage.getArea()));
        capacityField.setText(String.valueOf(cottage.getCapacity()));
        ownerIdField.setText(String.valueOf(cottage.getOwnerId()));
        descriptionArea.setText(String.valueOf(cottage.getDescription()));
        reservedCheckBox.setSelected(cottage.getReserved());
    }

    private void saveCottageDetails() {
        Cottage selected = cottageTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        try {
            int ownerId = Integer.parseInt(ownerIdField.getText());
            String cottageName = cottageNameField.getText();
            String location = locationField.getText();
            double price = Double.parseDouble(priceField.getText());
            double area = Double.parseDouble(areaField.getText());
            int capacity = Integer.parseInt(capacityField.getText());
            String description = descriptionArea.getText();
            boolean isReserved = reservedCheckBox.isSelected();

            cottageDAO.updateCottage(
                    selected.getCottageId(),
                    ownerId,
                    isReserved,
                    cottageName,
                    location,
                    price,
                    area,
                    capacity,
                    description
            );

            selected.setOwnerId(ownerId);
            selected.setCottageName(cottageName);
            selected.setLocation(location);
            selected.setPrice(price);
            selected.setArea(area);
            selected.setCapacity(capacity);
            selected.setDescription(description);
            selected.setReserved(isReserved);

            cottageTableView.refresh();

        } catch (NumberFormatException e) {
            System.out.println("Syötteessä on virheellinen tyyppi (int, string tms)" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Virhe päivittäessä mökin tietoja: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openNewCottageDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/NewCottage.fxml"));
            Parent root = loader.load();

            NewCottageController controller = loader.getController();
            controller.setCottageDAO(this.cottageDAO);

            controller.setOnSaveSuccess(() -> loadCottagesFromDatabase());

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Uuden mökin lisääminen");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleSearchCottages() {
        String query = searchField.getText();

        int cottageId = -1;
        String name = "", location = "";

        try {
            cottageId = Integer.parseInt(query);
        } catch (NumberFormatException e) {
            name = query;
            location = query;
        }

        List<Cottage> results = cottageDAO.searchCottages(cottageId, name, location);

        StringBuilder resultText = new StringBuilder();
        for (Cottage c : results) {
            resultText.append("Mökki-ID: ").append(c.getCottageId()).append("\n")
                    .append("Nimi: ").append(c.getCottageName()).append("\n")
                    .append("Sijainti: ").append(c.getLocation()).append("\n")
                    .append("Hinta: ").append(c.getPrice()).append("\n")
                    .append("Pinta-Ala: ").append(c.getArea()).append("\n")
                    .append("Kapasiteetti: ").append(c.getCapacity()).append("\n")
                    .append("Omistaja-ID: ").append(c.getOwnerId()).append("\n")
                    .append("Varattu: ").append(c.getReserved() ? "Kyllä" : "Ei").append("\n")
                    .append("Kuvaus: ").append(c.getDescription()).append("\n")
                    .append("\n\n");
        }

        if (results.isEmpty()) {
            resultText.append("Hakusanalla ").append(query).append(" ei löytynyt mökkejä.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mokkikodit/cottagereservation/cottage_results.fxml"));
            Parent root = loader.load();

            CottageResultsController controller = loader.getController();
            controller.setResultsText(resultText.toString());

            Stage stage = new Stage();
            stage.setTitle("Mökkihaku");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


