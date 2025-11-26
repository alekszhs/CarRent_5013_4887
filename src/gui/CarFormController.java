package gui;

import api.models.Car;
import api.models.CarStatus;
import api.services.CarService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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

    @FXML private Label lblStatus;

    private CarService carService;


    // === init για injection από Menu ===
    public void init(CarService carService){
        this.carService = carService;
        loadTable();
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
    public void handleAdd(){
        try{
            Car car = new Car(
                    txtId.getText(),
                    txtPlate.getText(),
                    txtBrand.getText(),
                    "Type?",
                    txtModel.getText(),
                    Integer.parseInt(txtYear.getText()),
                    txtColor.getText(),
                    CarStatus.AVAILABLE
            );

            if(carService.addCar(car)){
                lblStatus.setText("Car added successfully.");
                loadTable();
            } else {
                lblStatus.setText("Car could not be added.");
            }

        } catch (Exception e){
            lblStatus.setText("Invalid input.");
        }
    }


    // === UPDATE ===
    @FXML
    public void handleUpdate(){
        Car selected = tableCars.getSelectionModel().getSelectedItem();
        if(selected == null){
            lblStatus.setText("Select a car first.");
            return;
        }

        selected.setPlate(txtPlate.getText());
        selected.setBrand(txtBrand.getText());
        selected.setModel(txtModel.getText());
        selected.setYear(Integer.parseInt(txtYear.getText()));
        selected.setColor(txtColor.getText());

        lblStatus.setText("Car updated successfully.");
        loadTable();
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


    // === BACK ===
    @FXML
    public void goBack(){
        Stage stage = (Stage) ((Node) tableCars).getScene().getWindow();
        stage.close();
    }
}
