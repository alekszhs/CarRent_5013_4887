package gui;

import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EmployeeFormController {

    @FXML private TableView<Employee> tableEmployees;
    @FXML private TableColumn<Employee, String> colFullName;
    @FXML private TableColumn<Employee, String> colUsername;
    @FXML private TableColumn<Employee, String> colEmail;

    @FXML private TextField txtFullName;
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;

    @FXML private Label lblStatus;

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

        setupTable();
        loadTable();
        setupRowClickFill();
    }

    private void setupTable() {
        colFullName.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getFullName()));
        colUsername.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getUsername()));
        colEmail.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getEmail()));
    }

    private void loadTable() {
        ObservableList<Employee> list =
                FXCollections.observableArrayList(employeeService.getAllEmployees());
        tableEmployees.setItems(list);
    }

    private void setupRowClickFill() {
        tableEmployees.getSelectionModel().selectedItemProperty().addListener((obs, oldE, newE) -> {
            if (newE == null) return;

            txtFullName.setText(newE.getFullName());
            txtUsername.setText(newE.getUsername());
            txtEmail.setText(newE.getEmail());
            txtPassword.setText(newE.getPassword()); // για εργασία ok
        });
    }

    @FXML
    private void handleAdd() {
        try {
            Employee e = new Employee(
                    txtFullName.getText(),
                    txtUsername.getText(),
                    txtEmail.getText(),
                    txtPassword.getText()
            );

            employeeService.addEmployee(e);
            lblStatus.setText("Employee added.");
            handleClear();
            loadTable();

        } catch (Exception ex) {
            lblStatus.setText("Add failed: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Employee selected = tableEmployees.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select an employee.");
            return;
        }

        // Μην αφήνεις να σβήσει τον εαυτό του (συχνή παγίδα)
        if (loggedEmployee != null &&
                selected.getUsername().equalsIgnoreCase(loggedEmployee.getUsername())) {
            lblStatus.setText("You cannot delete the logged-in user.");
            return;
        }

        try {
            employeeService.deleteEmployee(selected);
            lblStatus.setText("Employee deleted.");
            handleClear();
            loadTable();

        } catch (Exception ex) {
            lblStatus.setText("Delete failed: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        txtFullName.clear();
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        tableEmployees.getSelectionModel().clearSelection();
        lblStatus.setText("");
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
