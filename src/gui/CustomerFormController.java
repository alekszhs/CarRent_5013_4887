package gui;

import api.models.Customer;
import api.services.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CustomerFormController {

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

    private CustomerService customerService;


    // ================== Init from MainMenu ==================
    public void init(CustomerService service){
        this.customerService = service;
        loadTable();
        setupRowClickFill();
    }


    // ================== Load data to table ==================
    private void loadTable(){
        ObservableList<Customer> list =
                FXCollections.observableArrayList(customerService.getAllCustomers());

        colAfm.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAfm()));
        colFullName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhoneNumber()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));

        tableCustomers.setItems(list);
    }


    // ================== Autofill fields on table click ==================
    private void setupRowClickFill(){
        tableCustomers.setOnMouseClicked(event -> {
            Customer c = tableCustomers.getSelectionModel().getSelectedItem();
            if(c == null) return;

            txtAfm.setText(c.getAfm());
            txtFullName.setText(c.getFullName());
            txtPhone.setText(c.getPhoneNumber());
            txtEmail.setText(c.getEmail());
        });
    }


    // ================== Add ==================
    @FXML
    public void handleAdd(){
        try{
            Customer c = new Customer(
                    txtAfm.getText(),
                    txtFullName.getText(),
                    txtPhone.getText(),
                    txtEmail.getText()
            );

            customerService.addCustomer(c);
            lblStatus.setText("Customer added.");
            loadTable();

        } catch (Exception e){
            lblStatus.setText("Add failed.");
        }
    }


    // ================== Update ==================
    @FXML
    public void handleUpdate(){
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if(selected == null){
            lblStatus.setText("Select a customer.");
            return;
        }

        Customer newData = new Customer(
                txtAfm.getText(),
                txtFullName.getText(),
                txtPhone.getText(),
                txtEmail.getText()
        );

        try{
            customerService.updateCustomer(selected.getAfm(), newData);
            lblStatus.setText("Customer updated.");
            loadTable();

        } catch (Exception e){
            lblStatus.setText("Update failed.");
        }
    }


    // ================== Delete ==================
    @FXML
    public void handleDelete(){
        Customer selected = tableCustomers.getSelectionModel().getSelectedItem();
        if(selected == null){
            lblStatus.setText("Select a customer.");
            return;
        }

        try{
            customerService.deleteCustomer(selected.getAfm());
            lblStatus.setText("Customer deleted.");
            loadTable();

        } catch (Exception e){
            lblStatus.setText("Delete failed.");
        }
    }


    // ================== Back ==================
    @FXML
    public void goBack(){
        Stage stage = (Stage) ((Node) tableCustomers).getScene().getWindow();
        stage.close();
    }
}
