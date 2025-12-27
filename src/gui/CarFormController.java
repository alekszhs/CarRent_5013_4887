package gui;

import api.models.Car;
import api.models.CarStatus;
import api.services.CarService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import api.models.Employee;
import api.services.EmployeeService;
import api.services.CustomerService;
import api.services.RentalService;

public class CarFormController {

    @FXML private TableView<Car> tableCars;
    @FXML private TableColumn<Car, String> colId;
    @FXML private TableColumn<Car, String> colPlate;
    @FXML private TableColumn<Car, String> colBrand;
    @FXML private TableColumn<Car, String> colModel;
    @FXML private TableColumn<Car, Integer> colYear;
    @FXML private TableColumn<Car, String> colColor;
    @FXML private TableColumn<Car, CarStatus> colStatus;

    @FXML private TextField txtId;
    @FXML private TextField txtPlate;
    @FXML private TextField txtBrand;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtColor;

    @FXML private TextField txtType;
    @FXML private ComboBox<CarStatus> cmbStatus;


    @FXML private Label lblStatus;

    private CarService carService;
    private EmployeeService employeeService;
    private CustomerService customerService;
    private RentalService rentalService;
    private Employee loggedEmployee;

    // === init για injection από Menu ===
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

        // status combo setup
        cmbStatus.setItems(FXCollections.observableArrayList(CarStatus.values()));
        cmbStatus.setValue(CarStatus.AVAILABLE);

        // load table data
        loadTable();

        // listener
        tableCars.getSelectionModel().selectedItemProperty().addListener((obs, oldCar, newCar) -> {
            if (newCar == null) return;

            txtId.setText(newCar.getId());
            txtPlate.setText(newCar.getPlate());
            txtBrand.setText(newCar.getBrand());
            txtModel.setText(newCar.getModel());
            txtType.setText(newCar.getType());
            txtYear.setText(String.valueOf(newCar.getYear()));
            txtColor.setText(newCar.getColor());
            cmbStatus.setValue(newCar.getStatus());
        });
    }




    // === Φόρτωση δεδομένων στον πίνακα ===
    private void loadTable(){
        ObservableList<Car> list = FXCollections.observableArrayList(carService.getAllCars());

        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colPlate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPlate()));
        colBrand.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBrand()));
        colModel.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getModel()));
        colYear.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getYear()).asObject());
        colColor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getColor()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getStatus()));

        tableCars.setItems(list);
    }


    // === ADD ===
    @FXML
    public void handleAdd() {
        try {
            Car car = new Car(
                    txtId.getText(),
                    txtPlate.getText(),
                    txtBrand.getText(),
                    txtModel.getText(),
                    txtType.getText(),
                    Integer.parseInt(txtYear.getText()),
                    txtColor.getText(),
                    cmbStatus.getValue()
            );

            if (carService.addCar(car)) {
                lblStatus.setText("Car added successfully.");
                loadTable();
            } else {
                lblStatus.setText("Car could not be added (duplicate ID or plate).");
            }

        } catch (Exception e) {
            lblStatus.setText("Invalid input: " + e.getMessage());
        }
    }



    // === UPDATE ===
    @FXML
    public void handleUpdate() {
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if (selected == null) {
            lblStatus.setText("Select a car first.");
            return;
        }

        try {
            Car newData = new Car(
                    selected.getId(),              // id stays the same
                    txtPlate.getText(),
                    txtBrand.getText(),
                    txtModel.getText(),
                    txtType.getText(),
                    Integer.parseInt(txtYear.getText()),
                    txtColor.getText(),
                    cmbStatus.getValue()
            );

            carService.updateCar(selected.getId(), newData);

            lblStatus.setText("Car updated successfully.");
            loadTable();
        } catch (Exception e) {
            lblStatus.setText("Update failed: " + e.getMessage());
        }
    }

    // === SEARCH ===
    @FXML
    public void handleSearch() {
        String brand = txtBrand.getText();
        String plate = txtPlate.getText();
        String model = txtModel.getText();
        String color = txtColor.getText();
        CarStatus status = cmbStatus.getValue(); // μπορεί να είναι null -> ignore στο service

        var results = carService.searchCars(brand, plate, model, color, status);
        tableCars.setItems(FXCollections.observableArrayList(results));

        lblStatus.setText("Found " + results.size() + " car(s).");
    }

    // === CLEAR ===
    @FXML
    public void handleClear() {
        txtId.clear();
        txtPlate.clear();
        txtBrand.clear();
        txtModel.clear();
        txtType.clear();
        txtYear.clear();
        txtColor.clear();

        cmbStatus.setValue(null); // ώστε το search να αγνοεί status

        loadTable();
        lblStatus.setText("Filters cleared.");
    }




    // === DELETE ===
    @FXML
    public void handleDelete(){
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if(selected == null){
            lblStatus.setText("Select a car first.");
            return;
        }

        carService.deleteCar(selected.getId());
        lblStatus.setText("Car deleted.");
        loadTable();
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
