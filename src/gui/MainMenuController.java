package gui;

import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class MainMenuController {

    // Services injected from LoginController
    private EmployeeService employeeService;
    private CarService carService;
    private CustomerService customerService;
    private RentalService rentalService;

    // Logged-in employee (used for rental creation)
    private Employee loggedEmployee;

    /**
     * Dependency injection from LoginController.
     * Keeps GUI free of business logic creation.
     */
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
    }

    // ---------------------------------------------------------
    // Navigation Helpers
    // ---------------------------------------------------------

    /**
     * Loads an FXML file and switches the current scene.
     * Used by all menu buttons to avoid repeated code.
     */
    private void switchScene(ActionEvent event, String fxmlPath, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        // Inject services into the next controller (if it has init())
        Object controller = loader.getController();
        try {
            controller.getClass()
                    .getMethod("init", EmployeeService.class, CarService.class,
                            CustomerService.class, RentalService.class, Employee.class)
                    .invoke(controller, employeeService, carService, customerService, rentalService, loggedEmployee);
        } catch (NoSuchMethodException ignored) {
            // Some screens (e.g., Login) have different init signature
        }

        // Replace current scene with the new one
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.sizeToScene();
        stage.setTitle(title);
    }

    // ---------------------------------------------------------
    // Menu Button Handlers
    // ---------------------------------------------------------

    @FXML
    private void handleOpenCars(ActionEvent event) throws Exception {
        // Navigate to car management screen
        switchScene(event, "/gui/CarForm.fxml", "Manage Cars");
    }

    @FXML
    private void handleOpenCustomers(ActionEvent event) throws Exception {
        // Navigate to customer management screen
        switchScene(event, "/gui/CustomerForm.fxml", "Manage Customers");
    }

    @FXML
    private void handleOpenEmployees(ActionEvent event) throws Exception {
        // Navigate to employee management screen
        switchScene(event, "/gui/EmployeeForm.fxml", "Manage Employees");
    }

    @FXML
    private void handleOpenRentalForm(ActionEvent event) throws Exception {
        // Navigate to rental creation screen
        switchScene(event, "/gui/RentalForm.fxml", "New Rental");
    }

    @FXML
    private void handleOpenReturnRental(ActionEvent event) throws Exception {
        // Navigate to rental return screen
        switchScene(event, "/gui/ReturnRental.fxml", "Return Rental");
    }

    @FXML
    private void handleOpenHistory(ActionEvent event) throws Exception {
        // Navigate to rental history screen
        switchScene(event, "/gui/CustomerRentalHistory.fxml", "Rental History");
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        // Load login screen (different init signature)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.setTitle("Car Rental - Login");
        stage.sizeToScene();
        stage.centerOnScreen();
    }
}