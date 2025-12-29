package gui;

import api.models.Rental;
import api.models.Employee;

import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class CustomerRentalHistoryController {

    // ---------------------------------------------------------
    // FXML UI Components
    // ---------------------------------------------------------

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

    // ---------------------------------------------------------
    // Services + Logged Employee
    // ---------------------------------------------------------

    private CarService carService;
    private EmployeeService employeeService;
    private CustomerService customerService;
    private RentalService rentalService;
    private Employee loggedEmployee;

    // ---------------------------------------------------------
    // Dependency Injection (called from MainMenu)
    // ---------------------------------------------------------

    public void init(EmployeeService empService,
                     CarService carService,
                     CustomerService customerService,
                     RentalService rentalService,
                     Employee loggedEmployee) {

        this.employeeService = empService;
        this.carService = carService;
        this.customerService = customerService;
        this.rentalService = rentalService;
        this.loggedEmployee = loggedEmployee;

        setupTable();
    }

    // ---------------------------------------------------------
    // Table Setup
    // ---------------------------------------------------------

    private void setupTable() {
        colId.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getRentalId()));
        colPlate.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getCar().getPlate()));
        colAfm.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getCustomer().getAfm()));
        colStart.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getStartDate().toString()));
        colEnd.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getEndDate().toString()));
        colStatus.setCellValueFactory(r -> new SimpleStringProperty(r.getValue().getStatus().toString()));
    }

    // ---------------------------------------------------------
    // Search Rentals
    // ---------------------------------------------------------

    @FXML
    private void handleSearch() {

        String afm = txtAfm.getText() == null ? "" : txtAfm.getText().trim();
        String plate = txtPlate.getText() == null ? "" : txtPlate.getText().trim();

        // Must provide at least one search criterion
        if (afm.isEmpty() && plate.isEmpty()) {
            lblStatus.setText("Enter AFM or Plate to search.");
            tableResults.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Rental> rentals;

        // Search by AFM or Plate
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

    // ---------------------------------------------------------
    // Clear Filters
    // ---------------------------------------------------------

    @FXML
    private void handleClear() {
        txtAfm.clear();
        txtPlate.clear();

        tableResults.setItems(FXCollections.observableArrayList());
        lblStatus.setText("");
    }

    // ---------------------------------------------------------
    // Navigation Back to Main Menu
    // ---------------------------------------------------------

    @FXML
    public void handleGoBack(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainMenu.fxml"));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, loggedEmployee);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);

        stage.sizeToScene();
        stage.setTitle("Main Menu");
    }
}
