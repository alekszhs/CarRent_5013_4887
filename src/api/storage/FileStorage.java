package api.storage;

import api.models.*;
import api.services.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;

/**
 * Provides persistent storage for the car rental system using CSV files.
 * <p>
 * This class saves and loads employees, customers, cars, and rentals
 * into CSV files located under:
 * <pre>
 *   {user.home}/car-rental-data/
 * </pre>
 * The folder is automatically created if it does not exist.
 * </p>
 *
 * <p>
 * FileStorage is used after the first application run. If saved CSV files
 * exist, the system loads from them instead of the bundled resources.
 * </p>
 *
 * <p><b>Important:</b> This class performs minimal validation and delegates
 * business rules to the corresponding service classes.</p>
 *
 * @author
 *     Αλέξανδρος Γκούρδογλου,
 *     Θεμιστοκλής Κιουτσούκης
 */
public class FileStorage {

    // ==================== Base Directory ====================

    private static final String BASE_DIR =
            System.getProperty("user.home") + File.separator + "car-rental-data";

    /**
     * Creates the base directory if it does not already exist.
     */
    public FileStorage() {
        new File(BASE_DIR).mkdirs();
    }


    // ==================== IO Helpers ====================

    /**
     * Returns the full path of a file inside the storage directory.
     *
     * @param fileName the file name
     * @return a Path object pointing to the file
     */
    private Path path(String fileName) {
        return Paths.get(BASE_DIR, fileName);
    }

    /**
     * Checks whether a file exists in the storage directory.
     *
     * @param fileName the file name
     * @return true if the file exists
     */
    private boolean exists(String fileName) {
        return Files.exists(path(fileName));
    }

    /**
     * Opens a writer for a CSV file, creating or overwriting it.
     */
    private BufferedWriter writer(String fileName) throws IOException {
        return Files.newBufferedWriter(
                path(fileName),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    /**
     * Opens a reader for a CSV file.
     */
    private BufferedReader reader(String fileName) throws IOException {
        return Files.newBufferedReader(path(fileName), StandardCharsets.UTF_8);
    }

    /**
     * Checks whether all required CSV files exist.
     * If true, the system loads from FileStorage instead of bundled resources.
     *
     * @return true if all saved CSV files exist
     */
    public boolean hasSavedState() {
        return exists("employees.csv")
                && exists("cars.csv")
                && exists("customers.csv")
                && exists("rentals.csv");
    }


    // ==================== SAVE ====================

    /**
     * Saves all employees to employees.csv.
     */
    public void saveEmployees(EmployeeService service) throws IOException {
        try (BufferedWriter bw = writer("employees.csv")) {
            bw.write("fullName,username,email,password\n");
            for (Employee e : service.getAllEmployees()) {
                bw.write(
                        escape(e.getFullName()) + "," +
                                escape(e.getUsername()) + "," +
                                escape(e.getEmail()) + "," +
                                escape(e.getPassword()) + "\n"
                );
            }
        }
    }

    /**
     * Saves all cars to cars.csv.
     */
    public void saveCars(CarService service) throws IOException {
        try (BufferedWriter bw = writer("cars.csv")) {
            bw.write("id,plate,brand,model,type,year,color,status\n");
            for (Car c : service.getAllCars()) {
                bw.write(
                        escape(c.getId()) + "," +
                                escape(c.getPlate()) + "," +
                                escape(c.getBrand()) + "," +
                                escape(c.getModel()) + "," +
                                escape(c.getType()) + "," +
                                c.getYear() + "," +
                                escape(c.getColor()) + "," +
                                c.getStatus() + "\n"
                );
            }
        }
    }

    /**
     * Saves all customers to customers.csv.
     */
    public void saveCustomers(CustomerService service) throws IOException {
        try (BufferedWriter bw = writer("customers.csv")) {
            bw.write("afm,fullName,phone,email\n");
            for (Customer c : service.getAllCustomers()) {
                bw.write(
                        escape(c.getAfm()) + "," +
                                escape(c.getFullName()) + "," +
                                escape(c.getPhoneNumber()) + "," +
                                escape(c.getEmail()) + "\n"
                );
            }
        }
    }

    /**
     * Saves all rentals to rentals.csv.
     * Format:
     * rentalId,carId,afm,username,startDate,endDate,status
     */
    public void saveRentals(RentalService service) throws IOException {
        try (BufferedWriter bw = writer("rentals.csv")) {
            bw.write("rentalId,carId,afm,username,startDate,endDate,status\n");
            for (Rental r : service.getAllRentals()) {
                bw.write(
                        escape(r.getRentalId()) + "," +
                                escape(r.getCar().getId()) + "," +
                                escape(r.getCustomer().getAfm()) + "," +
                                escape(r.getEmployee().getUsername()) + "," +
                                r.getStartDate() + "," +
                                r.getEndDate() + "," +
                                r.getStatus() + "\n"
                );
            }
        }
    }


    // ==================== LOAD ====================

    /**
     * Loads employees from employees.csv.
     */
    public void loadEmployees(EmployeeService service) throws IOException {
        if (!exists("employees.csv")) return;

        try (BufferedReader br = reader("employees.csv")) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",", -1);
                if (d.length < 4) continue;

                Employee e = new Employee(
                        d[0].trim(),
                        d[1].trim(),
                        d[2].trim(),
                        d[3].trim()
                );

                service.addEmployee(e);
            }
        }
    }

    /**
     * Loads cars from cars.csv.
     */
    public void loadCars(CarService service) throws IOException {
        if (!exists("cars.csv")) return;

        try (BufferedReader br = reader("cars.csv")) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",", -1);
                if (d.length < 8) continue;

                CarStatus status;
                try {
                    status = CarStatus.valueOf(d[7].trim());
                } catch (Exception e) {
                    status = CarStatus.AVAILABLE;
                }

                Car car = new Car(
                        d[0].trim(),
                        d[1].trim(),
                        d[2].trim(),
                        d[3].trim(),
                        d[4].trim(),
                        Integer.parseInt(d[5].trim()),
                        d[6].trim(),
                        status
                );

                service.addCar(car);
            }
        }
    }

    /**
     * Loads customers from customers.csv.
     */
    public void loadCustomers(CustomerService service) throws IOException {
        if (!exists("customers.csv")) return;

        try (BufferedReader br = reader("customers.csv")) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",", -1);
                if (d.length < 4) continue;

                Customer c = new Customer(
                        d[0].trim(),
                        d[1].trim(),
                        d[2].trim(),
                        d[3].trim()
                );

                service.addCustomer(c);
            }
        }
    }

    /**
     * Loads rentals from rentals.csv.
     * <p>
     * Rentals are imported without business validation.
     * Status is applied after construction.
     * </p>
     */
    public void loadRentals(
            RentalService rentalService,
            CarService carService,
            CustomerService customerService,
            EmployeeService employeeService
    ) throws IOException {

        if (!exists("rentals.csv")) return;

        try (BufferedReader br = reader("rentals.csv")) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",", -1);
                if (d.length < 7) continue;

                String rentalId = d[0].trim();
                String carId    = d[1].trim();
                String afm      = d[2].trim();
                String username = d[3].trim();

                LocalDate start = LocalDate.parse(d[4].trim());
                LocalDate end   = LocalDate.parse(d[5].trim());

                RentalStatus status;
                try {
                    status = RentalStatus.valueOf(d[6].trim());
                } catch (Exception e) {
                    status = RentalStatus.ACTIVE;
                }

                Car car = carService.findById(carId);
                Customer cust = customerService.findByAfm(afm);
                Employee emp = employeeService.findByUsername(username);

                if (car == null || cust == null || emp == null) continue;

                Rental rental = new Rental(rentalId, car, cust, emp, start, end);
                rental.setStatus(status);

                rentalService.getAllRentals().add(rental);
            }
        }
    }


    // ==================== Utility ====================

    /**
     * Escapes commas in CSV fields by replacing them with spaces.
     *
     * @param s the input string
     * @return a safe CSV field
     */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace(",", " ");
    }
}