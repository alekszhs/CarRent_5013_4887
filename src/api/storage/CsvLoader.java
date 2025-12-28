package api.storage;

import api.models.*;
import api.services.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class CsvLoader {

    // helper: open CSV from resources safely
    private BufferedReader openResourceCsv(String resourcePath) throws IOException {
        InputStream in = CsvLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IOException("Missing resource: " + resourcePath +
                    " (Make sure it exists under src/resources and Resources Root is set)");
        }
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    // =============================================================
    // EMPLOYEES
    // resource: /data/users.csv
    // name,surname,username,email,password
    // =============================================================
    public void loadEmployees(EmployeeService service) throws IOException {
        try (BufferedReader br = openResourceCsv("/data/users.csv")) {
            br.readLine(); // header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",");
                if (d.length < 5) continue;

                Employee e = new Employee(
                        d[0].trim() + " " + d[1].trim(),
                        d[2].trim(),
                        d[3].trim(),
                        d[4].trim()
                );

                service.addEmployee(e);
            }
        }
    }

    // =============================================================
    // CUSTOMERS
    // resource: /data/customers.csv
    // afm,fullName,phone,email
    // =============================================================

    // “σαν τα άλλα”: χωρίς filePath
    public void loadCustomers(CustomerService service) throws IOException {
        loadCustomersFromResources("/data/customers.csv", service);
    }

    // αν θες να δώσεις άλλο resource path
    public void loadCustomersFromResources(String resourcePath, CustomerService service) throws IOException {
        try (BufferedReader br = openResourceCsv(resourcePath)) {
            br.readLine(); // header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",");
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

    // =============================================================
    // CARS
    // resource: /data/vehicles_with_plates.csv
    // id,plate,brand,type,model,year,color,status
    // status: "Διαθέσιμο" -> AVAILABLE  else -> RENTED
    // =============================================================
    public void loadCars(CarService service) throws IOException {
        try (BufferedReader br = openResourceCsv("/data/vehicles_with_plates.csv")) {
            br.readLine(); // header

            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;

                String[] d = line.split(",");
                if (d.length < 8) continue;

                CarStatus status =
                        d[7].trim().equalsIgnoreCase("Διαθέσιμο") ?
                                CarStatus.AVAILABLE :
                                CarStatus.RENTED;

                // ΣΩΣΤΗ αντιστοίχιση: brand=d[2], type=d[3], model=d[4]
                Car car = new Car(
                        d[0].trim(),                 // id
                        d[1].trim(),                 // plate
                        d[2].trim(),                 // brand
                        d[4].trim(),                 // model  (FIX)
                        d[3].trim(),                 // type   (FIX)
                        Integer.parseInt(d[5].trim()),// year
                        d[6].trim(),                 // color
                        status
                );

                service.addCar(car);
            }
        }
    }

    // =============================================================
    // RENTALS
    // resource: /data/rentals.csv
    // rentalId,carId,afm,username,startDate,endDate,status
    //
    // Loads rentals from CSV without applying business validations.
    // Car availability is synchronized later based on ACTIVE rentals.
    // =============================================================
    public void loadRentals(
            RentalService rentalService,
            CarService carService,
            CustomerService customerService,
            EmployeeService employeeService
    ) throws IOException {

        try (BufferedReader br = openResourceCsv("/data/rentals.csv")) {
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
                } catch (Exception ex) {
                    status = RentalStatus.ACTIVE;
                }

                Car car = carService.findById(carId);
                Customer cust = customerService.findByAfm(afm);
                Employee emp = employeeService.findByUsername(username);

                if (car == null || cust == null || emp == null) continue;

                Rental rental = new Rental(rentalId, car, cust, emp, start, end);
                rental.setStatus(status);

                // Load “as-is” (χωρίς validations)
                rentalService.getAllRentals().add(rental);
            }
        }
    }
}
