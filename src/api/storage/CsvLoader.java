package api.storage;

import api.models.*;
import api.services.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * Utility class responsible for loading application data from CSV files
 * located inside the resources folder.
 * <p>
 * This loader initializes employees, customers, cars, and rentals by reading
 * predefined CSV files. It is typically used during application startup to
 * populate the service layer with initial data.
 * </p>
 *
 * <p><b>Important:</b> All CSV files must be placed under <code>src/main/resources/data</code>
 * and the folder must be marked as a Resources Root in IntelliJ.</p>
 *
 * <p>
 * The loader performs minimal validation and delegates business rules to the
 * corresponding service classes. Rentals are imported "as-is" and their
 * business logic (e.g., availability synchronization) is handled later.
 * </p>
 *
 * @author
 *     Αλέξανδρος Γκούρδογλου,
 *     Θεμιστοκλής Κιουτσούκης
 */
public class CsvLoader {

    // ==================== Utility: Open CSV Resource ====================

    /**
     * Opens a CSV file located inside the resources folder and returns a
     * {@link BufferedReader} for reading its contents.
     *
     * @param resourcePath the path to the CSV file inside the resources folder
     * @return a BufferedReader for reading the file
     * @throws IOException if the resource cannot be found or opened
     */
    private BufferedReader openResourceCsv(String resourcePath) throws IOException {
        InputStream in = CsvLoader.class.getResourceAsStream(resourcePath);

        if (in == null) {
            throw new IOException(
                    "Resource not found: " + resourcePath +
                            " (Ensure it exists under src/main/resources and is marked as Resources Root)"
            );
        }

        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }


// ==================== Load Employees ====================

    /**
     * Loads employees from <code>data/users.csv</code>.
     * <p>
     * Expected format:
     * <pre>
     * name,surname,username,email,password
     * </pre>
     * </p>
     *
     * @param service the EmployeeService to populate
     * @throws IOException if the CSV file cannot be read
     */
    public void loadEmployees(EmployeeService service) throws IOException {
        loadEmployeesFromResources("/data/users.csv", service);
    }

    /**
     * Loads employees from a custom CSV resource path.
     * <p>
     * This method contains the core implementation logic and can be used
     * for alternative datasets or automated test cases.
     * </p>
     *
     * <p>
     * Expected format:
     * <pre>
     * name,surname,username,email,password
     * </pre>
     * </p>
     *
     * @param resourcePath the path to the CSV file inside the resources folder
     * @param service      the EmployeeService to populate
     * @throws IOException if the CSV file cannot be read
     */
    public void loadEmployeesFromResources(String resourcePath, EmployeeService service) throws IOException {
        try (BufferedReader br = openResourceCsv(resourcePath)) {

            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",");
                if (d.length < 5) continue;

                Employee e = new Employee(
                        d[0].trim() + " " + d[1].trim(), // full name
                        d[2].trim(),                     // username
                        d[3].trim(),                     // email
                        d[4].trim()                      // password
                );

                service.addEmployee(e);
            }
        }
    }



    // ==================== Load Customers ====================

    /**
     * Loads customers from the default CSV file <code>data/customers.csv</code>.
     *
     * @param service the CustomerService to populate
     * @throws IOException if the CSV file cannot be read
     */
    public void loadCustomers(CustomerService service) throws IOException {
        loadCustomersFromResources("/data/customers.csv", service);
    }

    /**
     * Loads customers from a custom CSV resource path.
     *
     * @param resourcePath the path to the CSV file
     * @param service      the CustomerService to populate
     * @throws IOException if the CSV file cannot be read
     */
    public void loadCustomersFromResources(String resourcePath, CustomerService service) throws IOException {
        try (BufferedReader br = openResourceCsv(resourcePath)) {

            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",");
                if (d.length < 4) continue;

                Customer c = new Customer(
                        d[0].trim(), // afm
                        d[1].trim(), // full name
                        d[2].trim(), // phone
                        d[3].trim()  // email
                );

                service.addCustomer(c);
            }
        }
    }


    // ==================== Load Cars ====================

    /**
     * Loads cars from <code>data/vehicles_with_plates.csv</code>.
     * <p>
     * Expected format:
     * <pre>
     * id,plate,brand,type,model,year,color,status
     * </pre>
     * Status is interpreted as:
     * <ul>
     *     <li>"Διαθέσιμο" → AVAILABLE</li>
     *     <li>anything else → RENTED</li>
     * </ul>
     *
     * @param service the CarService to populate
     * @throws IOException if the CSV file cannot be read
     */
    public void loadCars(CarService service) throws IOException {
        loadCarsFromResources("/data/vehicles_with_plates.csv", service);
    }

    /**
     * Loads cars from a custom CSV resource path.
     * <p>
     * This method contains the core implementation logic and can be used
     * for alternative datasets or automated test cases.
     * </p>
     *
     * <p>
     * Expected format:
     * <pre>
     * id,plate,brand,type,model,year,color,status
     * </pre>
     * Status is interpreted as:
     * <ul>
     *     <li>"Διαθέσιμο" → AVAILABLE</li>
     *     <li>anything else → RENTED</li>
     * </ul>
     *
     * @param resourcePath the path to the CSV file inside the resources folder
     * @param service      the CarService to populate
     * @throws IOException if the CSV file cannot be read
     */
    public void loadCarsFromResources(String resourcePath, CarService service) throws IOException {
        try (BufferedReader br = openResourceCsv(resourcePath)) {

            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",");
                if (d.length < 8) continue;

                CarStatus status =
                        d[7].trim().equalsIgnoreCase("Διαθέσιμο")
                                ? CarStatus.AVAILABLE
                                : CarStatus.RENTED;

                Car car = new Car(
                        d[0].trim(),                   // id
                        d[1].trim(),                   // plate
                        d[2].trim(),                   // brand
                        d[4].trim(),                   // model
                        d[3].trim(),                   // type
                        Integer.parseInt(d[5].trim()), // year
                        d[6].trim(),                   // color
                        status
                );

                service.addCar(car);
            }
        }
    }



    // ==================== Load Rentals ====================

    /**
     * Loads rentals from <code>data/rentals.csv</code>.
     * <p>
     * Expected format:
     * <pre>
     * rentalId,carId,afm,username,startDate,endDate,status
     * </pre>
     * </p>
     *
     * <p>
     * Rentals are imported <b>without applying business rules</b>.
     * After loading, the system should synchronize car availability
     * based on ACTIVE rentals.
     * </p>
     *
     * @param rentalService   the RentalService to populate
     * @param carService      used to resolve car references
     * @param customerService used to resolve customer references
     * @param employeeService used to resolve employee references
     * @throws IOException if the CSV file cannot be read
     */
    public void loadRentals(
            RentalService rentalService,
            CarService carService,
            CustomerService customerService,
            EmployeeService employeeService
    ) throws IOException {
        loadRentalsFromResources(
                "/data/rentals.csv",
                rentalService,
                carService,
                customerService,
                employeeService
        );
    }

    /**
     * Loads rentals from a custom CSV resource path.
     * <p>
     * This method contains the core implementation logic and can be used
     * for alternative datasets or automated test cases.
     * </p>
     *
     * <p>
     * Expected format:
     * <pre>
     * rentalId,carId,afm,username,startDate,endDate,status
     * </pre>
     * </p>
     *
     * <p>
     * Rentals are imported <b>without applying business rules</b>.
     * After loading, the system should synchronize car availability
     * based on ACTIVE rentals.
     * </p>
     *
     * @param resourcePath    the path to the CSV file inside the resources folder
     * @param rentalService   the RentalService to populate
     * @param carService      used to resolve car references
     * @param customerService used to resolve customer references
     * @param employeeService used to resolve employee references
     * @throws IOException if the CSV file cannot be read
     */
    public void loadRentalsFromResources(
            String resourcePath,
            RentalService rentalService,
            CarService carService,
            CustomerService customerService,
            EmployeeService employeeService
    ) throws IOException {

        try (BufferedReader br = openResourceCsv(resourcePath)) {

            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",", -1);
                if (d.length < 7) continue;

                String rentalId = d[0].trim();
                String carId    = d[1].trim();
                String afm      = d[2].trim();
                String username = d[3].trim();

                LocalDate start;
                LocalDate end;
                try {
                    start = LocalDate.parse(d[4].trim());
                    end   = LocalDate.parse(d[5].trim());
                } catch (Exception ex) {
                    // malformed dates -> skip this line
                    continue;
                }

                RentalStatus status;
                try {
                    status = RentalStatus.valueOf(d[6].trim());
                } catch (Exception ex) {
                    status = RentalStatus.ACTIVE; // fallback
                }

                Car car = carService.findById(carId);
                Customer cust = customerService.findByAfm(afm);
                Employee emp = employeeService.findByUsername(username);

                if (car == null || cust == null || emp == null) {
                    continue;
                }

                try {
                    Rental rental = new Rental(rentalId, car, cust, emp, start, end);
                    rental.setStatus(status);

                    // Raw import (no business validation)
                    rentalService.importRental(rental);

                } catch (Exception ex) {
                    // invalid row -> skip
                    continue;
                }
            }
        }
    }

}