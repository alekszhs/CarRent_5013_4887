package gui;

import api.models.Car;
import api.models.Customer;
import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.RentalService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

public class RentalFormController {

    @FXML private ComboBox<Car> carBox;
    @FXML private ComboBox<Customer> customerBox;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private Label lblStatus;

    private CarService carService;
    private CustomerService customerService;
    private RentalService rentalService;
    private Employee loggedEmployee;

    public void init(CarService carService, CustomerService customerService,
                     RentalService rentalService, Employee loggedEmployee) {

        this.carService = carService;
        this.customerService = customerService;
        this.rentalService = rentalService;
        this.loggedEmployee = loggedEmployee;

        loadData();
    }

    private void loadData(){
        carBox.setItems(FXCollections.observableArrayList(carService.getAllCars()));
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

        if(success)
            lblStatus.setText("Rental created successfully.");
        else
            lblStatus.setText("Rental could not be created.");
    }

    @FXML
    private void goBack(){
        Stage stage = (Stage) ((Node)carBox).getScene().getWindow();
        stage.close();
    }
}
