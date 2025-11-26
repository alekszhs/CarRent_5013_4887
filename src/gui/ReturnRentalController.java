package gui;

import api.models.Rental;
import api.models.RentalStatus;
import api.services.RentalService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class ReturnRentalController {

    @FXML private TextField txtRentalId;
    @FXML private TextField txtAfm;
    @FXML private TextField txtPlate;
    @FXML private TableView<Rental> tableActive;
    @FXML private TableColumn<Rental, String> colId;
    @FXML private TableColumn<Rental, String> colPlate;
    @FXML private TableColumn<Rental, String> colAfm;
    @FXML private TableColumn<Rental, String> colStart;
    @FXML private TableColumn<Rental, String> colEnd;
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
    }

    @FXML
    private void handleSearch(){
        String afm = txtAfm.getText() == null ? "" : txtAfm.getText().trim();
        String plate = txtPlate.getText() == null ? "" : txtPlate.getText().trim();

        List<Rental> base;

        if (!afm.isEmpty()) {
            base = rentalService.getRentalsByCustomer(afm);
        } else if (!plate.isEmpty()) {
            base = rentalService.getRentalsByCar(plate);
        } else {
            lblStatus.setText("Enter AFM or Plate.");
            tableActive.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Rental> active = base.stream()
                .filter(r -> r.getStatus() == RentalStatus.ACTIVE)
                .collect(Collectors.toList());

        tableActive.setItems(FXCollections.observableArrayList(active));

        if (active.isEmpty()) {
            lblStatus.setText("No active rentals found.");
        } else {
            lblStatus.setText("Active rentals: " + active.size());
        }
    }

    @FXML
    private void handleReturn(){
        String rentalId = txtRentalId.getText() == null ? "" : txtRentalId.getText().trim();
        if (rentalId.isEmpty()) {
            // αν έχει επιλεγεί από τον πίνακα, πάρε από εκεί
            Rental selected = tableActive.getSelectionModel().getSelectedItem();
            if (selected != null) {
                rentalId = selected.getRentalId();
            }
        }

        if (rentalId.isEmpty()) {
            lblStatus.setText("Provide Rental ID or select a row.");
            return;
        }

        boolean ok = rentalService.returnCar(rentalId);

        if (ok) {
            lblStatus.setText("Rental " + rentalId + " returned.");
            handleSearch(); // refresh
        } else {
            lblStatus.setText("Could not return rental (maybe already completed).");
        }
    }
}
