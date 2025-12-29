package gui;

import api.models.Car;
import api.models.Customer;
import api.models.Employee;
import api.models.CarStatus;

import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.LocalDate;

public class RentalFormController {

    // ---------------------------------------------------------
    // FXML UI Components
    // ---------------------------------------------------------

    @FXML private ComboBox<Car> carBox;
    @FXML private ComboBox<Customer> customerBox;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private Label lblStatus;
    @FXML private TextField txtCustomerAfm;

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

        loadData();
    }

    // ---------------------------------------------------------
    // Load Cars & Customers
    // ---------------------------------------------------------

    private void loadData() {

        // Load only AVAILABLE cars
        carBox.setItems(FXCollections.observableArrayList(
                carService.getAllCars().stream()
                        .filter(c -> c.getStatus() == CarStatus.AVAILABLE)
                        .toList()
        ));

        // Load all customers
        customerBox.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));
    }

    // ---------------------------------------------------------
    // Create Rental
    // ---------------------------------------------------------

    @FXML
    private void handleCreateRental() {

        Car car = carBox.getValue();
        Customer cust = customerBox.getValue();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        // Basic validation
        if (car == null || cust == null || start == null || end == null) {
            lblStatus.setText("Fill all fields!");
            return;
        }

        // Date validation
        if (end.isBefore(start)) {
            lblStatus.setText("End date cannot be before start date.");
            return;
        }

        // Try to create rental through service layer
        boolean success = rentalService.rentCar(car, cust, loggedEmployee, start, end);

        if (success) {
            lblStatus.setText("Rental created successfully.");

            // Clear fields
            carBox.setValue(null);
            customerBox.setValue(null);
            startDate.setValue(null);
            endDate.setValue(null);

            // Refresh available cars
            loadData();

        } else {
            lblStatus.setText("Rental could not be created.");
        }
    }

    // ---------------------------------------------------------
    // Find Customer by AFM
    // ---------------------------------------------------------

    @FXML
    private void handleFindCustomer() {

        String afm = txtCustomerAfm.getText() == null ? "" : txtCustomerAfm.getText().trim();

        if (afm.isBlank()) {
            lblStatus.setText("Give AFM to search.");
            return;
        }

        Customer found = customerService.findByAfm(afm);

        if (found == null) {
            lblStatus.setText("Customer not found.");
            customerBox.setValue(null);
            return;
        }

        // Ensure the customer exists in the combo list
        if (!customerBox.getItems().contains(found)) {
            customerBox.getItems().add(found);
        }

        customerBox.setValue(found);
        lblStatus.setText("Customer selected: " + found.getFullName());
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
