package gui;

import api.models.Car;
import api.models.Customer;
import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.RentalService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import api.services.EmployeeService;

import java.time.LocalDate;

public class RentalFormController {

    @FXML private ComboBox<Car> carBox;
    @FXML private ComboBox<Customer> customerBox;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private Label lblStatus;

    private CarService carService;
    private EmployeeService employeeService;
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

        loadData();
    }

    private void loadData() {
        carBox.setItems(FXCollections.observableArrayList(
                carService.getAllCars().stream()
                        .filter(c -> c.getStatus() == api.models.CarStatus.AVAILABLE)
                        .toList()
        ));

        customerBox.setItems(FXCollections.observableArrayList(customerService.getAllCustomers()));
    }


    @FXML
    private void handleCreateRental(){

        Car car = carBox.getValue();
        Customer cust = customerBox.getValue();
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();

        if(car == null || cust == null || start == null || end == null){
            lblStatus.setText("Fill all fields!");
            return;
        }

        boolean success = rentalService.rentCar(car, cust, loggedEmployee, start, end);

        if (success) {
            lblStatus.setText("Rental created successfully.");
            carBox.setValue(null);
            customerBox.setValue(null);
            startDate.setValue(null);
            endDate.setValue(null);
            loadData(); // refresh AVAILABLE cars
        } else {
            lblStatus.setText("Rental could not be created.");
        }

    }

    @FXML
    public void goBack(ActionEvent event) throws Exception {
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
