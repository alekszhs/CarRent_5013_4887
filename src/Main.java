import api.models.Car;
import api.models.CarStatus;
import api.models.Rental;
import api.models.RentalStatus;
import api.services.*;
import api.storage.CsvLoader;
import api.storage.FileStorage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;


import java.util.Objects;

public class Main extends Application {

    private final EmployeeService employeeService = new EmployeeService();
    private final CarService carService = new CarService();
    private final CustomerService customerService = new CustomerService();
    private final RentalService rentalService = new RentalService();

    private final CsvLoader loader = new CsvLoader();     // resources seed
    private final FileStorage storage = new FileStorage(); // user.home persisted

    @Override
    public void start(Stage stage) throws Exception {

        if (storage.hasSavedState()) {
            storage.loadEmployees(employeeService);
            storage.loadCars(carService);
            storage.loadCustomers(customerService);
            storage.loadRentals(rentalService, carService, customerService, employeeService);

            // Always sync car statuses after loading
            syncCarStatusesFromRentals();

        } else {
            loader.loadEmployees(employeeService);
            loader.loadCars(carService);
            loader.loadCustomers(customerService);
            loader.loadRentals(rentalService, carService, customerService, employeeService);

            // Sync BEFORE saving initial state so saved CSVs are consistent
            syncCarStatusesFromRentals();

            storage.saveEmployees(employeeService);
            storage.saveCars(carService);
            storage.saveCustomers(customerService);
            storage.saveRentals(rentalService);
        }

        // Launch UI
        FXMLLoader fx = new FXMLLoader(getClass().getResource("/gui/Login.fxml"));
        Scene scene = new Scene(fx.load());

        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/gui/dark-theme.css")).toExternalForm()
        );

        gui.LoginController controller = fx.getController();
        controller.init(employeeService, carService, customerService, rentalService);

        stage.setScene(scene);
        stage.setTitle("Car Rental System");

        stage.getIcons().add(
                new Image(
                        Objects.requireNonNull(
                                getClass().getResourceAsStream("/gui/icon64.png")
                        )
                )
        );

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }


    /**
     * Ensures car availability matches rentals:
     * - If a car has an ACTIVE rental -> RENTED
     * - Else -> AVAILABLE
     */
    private void syncCarStatusesFromRentals() {
        // set all available first
        for (Car c : carService.getAllCars()) {
            c.setStatus(CarStatus.AVAILABLE);
        }

        // mark rented if active rental exists
        for (Rental r : rentalService.getAllRentals()) {
            if (r.getStatus() == RentalStatus.ACTIVE) {
                r.getCar().setStatus(CarStatus.RENTED);
            }
        }
    }

    @Override
    public void stop() {
        try {
            storage.saveEmployees(employeeService);
            storage.saveCars(carService);
            storage.saveCustomers(customerService);
            storage.saveRentals(rentalService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
