import api.services.*;
import api.storage.CsvLoader;
import api.storage.FileStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    private EmployeeService employeeService = new EmployeeService();
    private CarService carService = new CarService();
    private CustomerService customerService = new CustomerService();
    private RentalService rentalService = new RentalService();

    private CsvLoader loader = new CsvLoader();
    private FileStorage storage = new FileStorage();


    @Override
    public void start(Stage stage) throws Exception {

        loader.loadEmployees("data/users.csv", employeeService);
        loader.loadCars("data/vehicles_with_plates.csv", carService);
        loader.loadRentals("data/rentals.csv", rentalService, carService, customerService, employeeService); // θα φτιαχτεί μετά


        FXMLLoader fx = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));

        Scene scene = new Scene(fx.load());

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/gui/dark-theme.css")).toExternalForm()
        );


        gui.LoginController controller = fx.getController();
        controller.init(employeeService, carService, customerService, rentalService);

        stage.setScene(scene);
        stage.setTitle("Car Rental System - Login");
        stage.show();
    }


    @Override
    public void stop() {
        try {
            storage.saveEmployees("data/users.csv", employeeService);
            storage.saveCars("data/vehicles_with_plates.csv", carService);
            storage.saveRentals("data/rentals.csv", rentalService);
        } catch (Exception ignored) {}
    }


    public static void main(String[] args) {
        launch();
    }
}
