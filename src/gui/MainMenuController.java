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
    private void openCars() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CarForm.fxml"));
        Parent root = loader.load();

        CarFormController controller = loader.getController();
        controller.init(carService);

        Stage stage = new Stage();
        stage.setTitle("Manage Cars");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void openCustomers() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerForm.fxml"));
        Parent root = loader.load();

        CustomerFormController controller = loader.getController();
        controller.init(customerService);

        Stage stage = new Stage();
        stage.setTitle("Manage Customers");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void openRentalForm() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("RentalForm.fxml"));
        Parent root = loader.load();

        RentalFormController controller = loader.getController();
        controller.init(carService, customerService, rentalService, loggedEmployee);

        Stage stage = new Stage();
        stage.setTitle("New Rental");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void openHistory(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerRentalHistory.fxml"));
            Parent root = loader.load();

            CustomerRentalHistoryController controller = loader.getController();
            controller.init(rentalService);

            Stage stage = new Stage();
            stage.setTitle("Rental History");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @FXML
    private void openReturnRental() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReturnRental.fxml"));
        Parent root = loader.load();

        ReturnRentalController controller = loader.getController();
        controller.init(rentalService);

        Stage stage = new Stage();
        stage.setTitle("Return Rental");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void logout() throws Exception {
        // κλείνω το τρέχον παράθυρο
        Stage current = (Stage) javafx.stage.Window.getWindows().stream()
                .filter(Stage.class::isInstance)
                .map(Stage.class::cast)
                .filter(Stage::isFocused)
                .findFirst()
                .orElse(null);

        if (current != null) {
            current.close();
        }

        // ξαναδείχνω Login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService);

        Stage stage = new Stage();
        stage.setTitle("Car Rental - Login");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
