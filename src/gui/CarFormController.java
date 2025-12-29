package gui;

import api.models.Car;
import api.models.CarStatus;
import api.services.CarService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import api.models.Employee;
import api.services.EmployeeService;
import api.services.CustomerService;
import api.services.RentalService;

public class CarFormController {

    // ---------------------------------------------------------
    // FXML UI Components
    // ---------------------------------------------------------

    @FXML private TableView<Car> tableCars;
    @FXML private TableColumn<Car, String> colId;
    @FXML private TableColumn<Car, String> colPlate;
    @FXML private TableColumn<Car, String> colBrand;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colColor;
    @FXML private TableColumn<Car, CarStatus> colStatus;

    @FXML private TextField txtId;
    @FXML private TextField txtPlate;
    @FXML private TextField txtBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtColor;
    @FXML private TextField txtType;

    @FXML private ComboBox<CarStatus> cmbStatus;
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

        // Populate status dropdown
        cmbStatus.setItems(FXCollections.observableArrayList(CarStatus.values()));
        cmbStatus.setValue(CarStatus.AVAILABLE);

        // Load initial table data
        loadTable();

        // When user selects a row, populate the form fields
        tableCars.getSelectionModel().selectedItemProperty().addListener((obs, oldCar, newCar) -> {
            if (newCar == null) return;

            txtId.setText(newCar.getId());
            txtPlate.setText(newCar.getPlate());
            txtBrand.setText(newCar.getBrand());
            txtModel.setText(newCar.getModel());
            txtType.setText(newCar.getType());
            txtYear.setText(String.valueOf(newCar.getYear()));
            txtColor.setText(newCar.getColor());
            cmbStatus.setValue(newCar.getStatus());
        });
    }

    // ---------------------------------------------------------
    // Load Table Data
    // ---------------------------------------------------------

    private void loadTable() {
        ObservableList<Car> list = FXCollections.observableArrayList(carService.getAllCars());

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colPlate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPlate()));
        colBrand.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBrand()));
        colModel.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getModel()));
        colYear.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getYear()).asObject());
        colColor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getColor()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStatus()));

        tableCars.setItems(list);
    }

    // ---------------------------------------------------------
    // Add Car
    // ---------------------------------------------------------

    @FXML
    public void handleAdd() {
        try {
            // Read and sanitize input
            String id = safe(txtId);
            String plate = safe(txtPlate);
            String brand = safe(txtBrand);
            String model = safe(txtModel);
            String type = safe(txtType);
            String color = safe(txtColor);
            CarStatus status = cmbStatus.getValue();

            // Basic validation
            if (id.isEmpty() || plate.isEmpty() || brand.isEmpty() || model.isEmpty() ||
                    type.isEmpty() || color.isEmpty()) {
                lblStatus.setText("Fill all required fields.");
                return;
            }

            if (status == null) {
                lblStatus.setText("Select a status.");
                return;
            }

            // Validate year
            int year;
            try {
                year = Integer.parseInt(safe(txtYear));
            } catch (Exception ex) {
                lblStatus.setText("Year must be a number.");
                return;
            }

            int currentYear = java.time.LocalDate.now().getYear();
            if (year < 1900 || year > currentYear + 1) {
                lblStatus.setText("Invalid year.");
                return;
            }

            // Create car object
            Car car = new Car(id, plate, brand, model, type, year, color, status);

            // Add through service
            if (carService.addCar(car)) {
                lblStatus.setText("Car added successfully.");
                loadTable();
                tableCars.getSelectionModel().clearSelection();
            } else {
                lblStatus.setText("Car could not be added (duplicate ID or plate).");
            }

        } catch (Exception e) {
            lblStatus.setText("Invalid input.");
        }
    }

    // ---------------------------------------------------------
    // Update Car
    // ---------------------------------------------------------

    @FXML
    public void handleUpdate() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select a car first.");
            return;
        }

        try {
            String plate = safe(txtPlate);
            String brand = safe(txtBrand);
            String model = safe(txtModel);
            String type = safe(txtType);
            String color = safe(txtColor);
            CarStatus status = cmbStatus.getValue();

            if (plate.isEmpty() || brand.isEmpty() || model.isEmpty() ||
                    type.isEmpty() || color.isEmpty()) {
                lblStatus.setText("Fill all required fields.");
                return;
            }

            if (status == null) {
                lblStatus.setText("Select a status.");
                return;
            }

            int year;
            try {
                year = Integer.parseInt(safe(txtYear));
            } catch (Exception ex) {
                lblStatus.setText("Year must be a number.");
                return;
            }

            int currentYear = java.time.LocalDate.now().getYear();
            if (year < 1900 || year > currentYear + 1) {
                lblStatus.setText("Invalid year.");
                return;
            }

            // Create updated car object
            Car newData = new Car(
                    selected.getId(), // ID stays the same
                    plate, brand, model, type, year, color, status
            );

            carService.updateCar(selected.getId(), newData);
            lblStatus.setText("Car updated successfully.");
            loadTable();

        } catch (Exception e) {
            lblStatus.setText("Update failed.");
        }
    }

    // ---------------------------------------------------------
    // Delete Car
    // ---------------------------------------------------------

    @FXML
    public void handleDelete() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select a car first.");
            return;
        }

        try {
            carService.deleteCar(selected.getId());
            lblStatus.setText("Car deleted.");
            loadTable();
        } catch (Exception e) {
            lblStatus.setText("Delete failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Search Cars
    // ---------------------------------------------------------

    @FXML
    public void handleSearch() {
        String brand = txtBrand.getText();
        String plate = txtPlate.getText();
        String model = txtModel.getText();
        String color = txtColor.getText();
        CarStatus status = cmbStatus.getValue(); // null means "ignore"

        var results = carService.searchCars(brand, plate, model, color, status);
        tableCars.setItems(FXCollections.observableArrayList(results));

        lblStatus.setText("Found " + results.size() + " car(s).");
    }

    // ---------------------------------------------------------
    // Clear Filters
    // ---------------------------------------------------------

    @FXML
    public void handleClear() {
        txtId.clear();
        txtPlate.clear();
        txtBrand.clear();
        txtModel.clear();
        txtType.clear();
        txtYear.clear();
        txtColor.clear();

        cmbStatus.setValue(null); // null = ignore status in search

        loadTable();
        lblStatus.setText("Filters cleared.");
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

    // ---------------------------------------------------------
    // Utility
    // ---------------------------------------------------------

    private String safe(TextField tf) {
        return tf.getText() == null ? "" : tf.getText().trim();
    }
}
