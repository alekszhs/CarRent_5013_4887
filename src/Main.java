import api.services.*;
import api.storage.CsvLoader;
import api.storage.FileStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    private final EmployeeService employeeService = new EmployeeService();
    private final CarService carService = new CarService();
    private final CustomerService customerService = new CustomerService();
    private final RentalService rentalService = new RentalService();

    private final CsvLoader loader = new CsvLoader();
    private final FileStorage storage = new FileStorage();

    @Override
    public void start(Stage stage) throws Exception {

        // Load from resources (NO file paths)
        loader.loadEmployees(employeeService);
        loader.loadCars(carService);
        loader.loadRentals(rentalService, carService, customerService, employeeService);

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
            // Save to user folder (NO file paths)
            storage.saveEmployees(employeeService);
            storage.saveCars(carService);
            storage.saveRentals(rentalService);
        } catch (Exception ignored) {
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
