package gui;

import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblStatus;
    @FXML private javafx.scene.control.Button btnLogin;

    // Services injected from the main application
    private EmployeeService employeeService;
    private CarService carService;
    private CustomerService customerService;
    private RentalService rentalService;

    @FXML
    public void initialize() {
        // Allow pressing ENTER to trigger login
        btnLogin.setDefaultButton(true);

        // ENTER inside password field triggers login
        txtPassword.setOnAction(e -> handleLoginButton());

        // ENTER inside username field moves focus to password
        txtUsername.setOnAction(e -> txtPassword.requestFocus());
    }

    /**
     * Called by the main application to inject services.
     * This keeps the controller free of business logic creation.
     */
    public void init(EmployeeService empService,
                     CarService carService,
                     CustomerService custService,
                     RentalService rentalService) {

        this.employeeService = empService;
        this.carService = carService;
        this.customerService = custService;
        this.rentalService = rentalService;
    }

    // ------------------ LOGIN HANDLER ------------------ //

    @FXML
    private void handleLoginButton() {

        try {
            String user = txtUsername.getText().trim();
            String pass = txtPassword.getText().trim();

            // Basic empty-field validation
            if (user.isEmpty() || pass.isEmpty()) {
                lblStatus.setText("Συμπλήρωσε όλα τα πεδία");
                return;
            }

            // Validate credentials through the service layer
            if (!employeeService.validateLogin(user, pass)) {
                lblStatus.setText("Λάθος στοιχεία");
                return;
            }

            // Retrieve the logged-in employee object
            Employee logged = employeeService.findByUsername(user);

            // Load the Main Menu FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainMenu.fxml"));
            Parent root = loader.load();

            // Pass services + logged user to the next controller
            MainMenuController controller = loader.getController();
            controller.init(employeeService, carService, customerService, rentalService, logged);

            // Replace login screen with main menu
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.setTitle("Car Rental System");

        } catch (Exception e) {
            // Generic fallback error message for the user
            lblStatus.setText("Σφάλμα συστήματος — δοκίμασέ το πάλι");

            // Debug output (acceptable for development)
            e.printStackTrace();
        }
    }
}

