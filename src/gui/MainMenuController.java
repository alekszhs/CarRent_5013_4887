package gui;

import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class MainMenuController {

    private EmployeeService employeeService;
    private CarService carService;
    private CustomerService customerService;
    private RentalService rentalService;
    private Employee loggedEmployee;

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

    @FXML
    private void openCars(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CarForm.fxml"));
        Parent root = loader.load();

        CarFormController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, loggedEmployee);

        // Παίρνω το Stage από το κουμπί που πάτησε ο χρήστης
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.sizeToScene();
        stage.setTitle("Manage Cars");

    }

    @FXML
    private void openCustomers(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerForm.fxml"));
        Parent root = loader.load();

        CustomerFormController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, loggedEmployee);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.sizeToScene();
        stage.setTitle("Manage Customers");

    }

    @FXML
    private void openRentalForm(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RentalForm.fxml"));
        Parent root = loader.load();

        RentalFormController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, loggedEmployee);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.sizeToScene();
        stage.setTitle("New Rental");

    }

    @FXML
    private void openReturnRental(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReturnRental.fxml"));
        Parent root = loader.load();

        ReturnRentalController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, loggedEmployee);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.sizeToScene();
        stage.setTitle("Return Rental");
    }

    @FXML
    private void openHistory(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerRentalHistory.fxml"));
        Parent root = loader.load();

        CustomerRentalHistoryController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, loggedEmployee);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.sizeToScene();
        stage.setTitle("Rental History");
    }

    @FXML
    private void logout(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
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
