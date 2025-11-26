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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;

    private EmployeeService employeeService;
    private CarService carService;
    private CustomerService customerService;
    private RentalService rentalService;

    public void init(EmployeeService empService,
                     CarService carService,
                     CustomerService custService,
                     RentalService rentalService) {

        this.employeeService = empService;
        this.carService = carService;
        this.customerService = custService;
        this.rentalService = rentalService;
    }

    @FXML
    private void handleLoginButton() throws Exception {

        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();

        if (!employeeService.validateLogin(user, pass)) {
            lblStatus.setText("❌ Λάθος στοιχεία");
            return;
        }

        Employee logged = employeeService.findByUsername(user);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainMenu.fxml"));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();
        controller.init(employeeService, carService, customerService, rentalService, logged);


        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
