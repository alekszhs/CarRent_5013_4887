package gui;

import api.models.Rental;
import api.services.RentalService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class CustomerRentalHistoryController {

    @FXML private TextField txtAfm;
    @FXML private TextField txtPlate;
    @FXML private TableView<Rental> tableResults;
    @FXML private TableColumn<Rental, String> colId;
    @FXML private TableColumn<Rental, String> colPlate;
    @FXML private TableColumn<Rental, String> colAfm;
    @FXML private TableColumn<Rental, String> colStart;
    @FXML private TableColumn<Rental, String> colEnd;
    @FXML private TableColumn<Rental, String> colStatus;
    @FXML private Label lblStatus;

    private RentalService rentalService;

    public void init(RentalService rentalService){
        this.rentalService = rentalService;
        setupTable();
    }

    private void setupTable(){
        colId.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getRentalId()));
        colPlate.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getCar().getPlate()));
        colAfm.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getCustomer().getAfm()));
        colStart.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getStartDate().toString()));
        colEnd.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getEndDate().toString()));
        colStatus.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getStatus().toString()));
    }

    @FXML
    private void handleSearch(){

        String afm = txtAfm.getText() == null ? "" : txtAfm.getText().trim();
        String plate = txtPlate.getText() == null ? "" : txtPlate.getText().trim();

        if (afm.isEmpty() && plate.isEmpty()) {
            lblStatus.setText("Enter AFM or Plate to search.");
            tableResults.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Rental> rentals;

        if (!afm.isEmpty()) {
            rentals = rentalService.getRentalsByCustomer(afm);
        } else {
            rentals = rentalService.getRentalsByCar(plate);
        }

        tableResults.setItems(FXCollections.observableArrayList(rentals));

        if (rentals.isEmpty()) {
            lblStatus.setText("No rentals found.");
        } else {
            lblStatus.setText("Found " + rentals.size() + " rentals.");
        }
    }

    @FXML
    private void handleClear(){
        txtAfm.clear();
        txtPlate.clear();
        tableResults.setItems(FXCollections.observableArrayList());
        lblStatus.setText("");
    }
}
