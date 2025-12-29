package gui;

import api.models.Customer;
import api.models.Employee;
import api.services.CarService;
import api.services.CustomerService;
import api.services.EmployeeService;
import api.services.RentalService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CustomerFormController {

    // ---------------------------------------------------------
    // FXML UI Components
    // ---------------------------------------------------------

    @FXML private TableView<Customer> tableCustomers;
    @FXML private TableColumn<Customer, String> colAfm;
    @FXML private TableColumn<Customer, String> colFullName;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colEmail;

    @FXML private TextField txtAfm;
    @FXML private TextField txtFullName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;

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

        loadTable();
        setupRowClickFill();
    }

    // ---------------------------------------------------------
    // Load Table Data
    // ---------------------------------------------------------

    private void loadTable() {
        ObservableList<Customer> list =
                FXCollections.observableArrayList(customerService.getAllCustomers());

        colAfm.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAfm()));
        colFullName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhoneNumber()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));

        tableCustomers.setItems(list);
    }

    // ---------------------------------------------------------
    // Autofill fields when clicking a row
    // ---------------------------------------------------------

    private void setupRowClickFill() {
        tableCustomers.setOnMouseClicked(event -> {
            Customer c = tableCustomers.getSelectionModel().getSelectedItem();
            if (c == null) return;

            txtAfm.setText(c.getAfm());
            txtFullName.setText(c.getFullName());
            txtPhone.setText(c.getPhoneNumber());
            txtEmail.setText(c.getEmail());
        });
    }

    // ---------------------------------------------------------
    // Add Customer
    // ---------------------------------------------------------

    @FXML
    public void handleAdd() {
        try {
            String afm = safe(txtAfm);
            String fullName = safe(txtFullName);
            String phone = safe(txtPhone);
            String email = safe(txtEmail);

            // Basic validation
            if (afm.isEmpty() || fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                lblStatus.setText("Fill all required fields.");
                return;
            }

            if (!afm.matches("\\d{9}")) {
                lblStatus.setText("AFM must be exactly 9 digits.");
                return;
            }

            Customer c = new Customer(afm, fullName, phone, email);

            customerService.addCustomer(c);
            lblStatus.setText("Customer added.");
            loadTable();

        } catch (Exception e) {
            lblStatus.setText("Add failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Update Customer
    // ---------------------------------------------------------

    @FXML
    public void handleUpdate() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select a customer.");
            return;
        }

        try {
            String fullName = safe(txtFullName);
            String phone = safe(txtPhone);
            String email = safe(txtEmail);

            if (fullName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                lblStatus.setText("Fill all required fields.");
                return;
            }

            Customer newData = new Customer(selected.getAfm(), fullName, phone, email);

            customerService.updateCustomer(selected.getAfm(), newData);
            lblStatus.setText("Customer updated.");
            loadTable();

        } catch (Exception e) {
            lblStatus.setText("Update failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Delete Customer
    // ---------------------------------------------------------

    @FXML
    public void handleDelete() {
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select a customer.");
            return;
        }

        try {
            customerService.deleteCustomer(selected.getAfm());
            lblStatus.setText("Customer deleted.");
            loadTable();
        } catch (Exception e) {
            lblStatus.setText("Delete failed: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // Search Customers
    // ---------------------------------------------------------

    @FXML
    public void handleSearch() {
        String afm = txtAfm.getText();   // may be filled from table click
        String fullName = txtFullName.getText();
        String phone = txtPhone.getText();

        var results = customerService.searchCustomers(afm, fullName, phone);
        tableCustomers.setItems(FXCollections.observableArrayList(results));

        lblStatus.setText("Found " + results.size() + " customer(s).");
    }

    // ---------------------------------------------------------
    // Clear Filters
    // ---------------------------------------------------------

    @FXML
    public void handleClear() {
        txtAfm.clear();
        txtFullName.clear();
        txtPhone.clear();
        txtEmail.clear();

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
