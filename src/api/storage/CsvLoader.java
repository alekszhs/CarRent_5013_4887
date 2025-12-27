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
                    " (Make sure it exists under resources and Resources Root is set)");
        }
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    }

    // =============================================================
    // EMPLOYEES
    // resource: /data/users.csv
    // 1:name  2:surname  3:username  4:email  5:password
    // =============================================================
    public void loadEmployees(EmployeeService service) throws IOException {

        try (BufferedReader br = openResourceCsv("/data/users.csv")) {

            br.readLine(); // IGNORE HEADER

            String line;
            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");
                if (d.length < 5) continue;

                Employee e = new Employee(
                        d[0].trim() + " " + d[1].trim(), // fullname
                        d[2].trim(),                     // username
                        d[3].trim(),                     // email
                        d[4].trim()                      // password
                );

                service.addEmployee(e);
            }
        }
    }

    // =============================================================
    // CARS
    // resource: /data/vehicles_with_plates.csv
    // 1:id  2:plate  3:brand  4:type  5:model  6:year  7:color  8:status
    // status: "Διαθέσιμο" -> AVAILABLE  else -> RENTED
    // =============================================================
    public void loadCars(CarService service) throws IOException {

        try (BufferedReader br = openResourceCsv("/data/vehicles_with_plates.csv")) {

            br.readLine(); // IGNORE HEADER

            String line;
            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");
                if (d.length < 8) continue;

                CarStatus status =
                        d[7].trim().equalsIgnoreCase("Διαθέσιμο") ?
                                CarStatus.AVAILABLE :
                                CarStatus.RENTED;

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

    // =============================================================
    // RENTALS
    // resource: /data/rentals.csv
    // 1:rentalId  2:carId  3:afm  4:username  5:start  6:end
    // =============================================================
    public void loadRentals(
            RentalService rentalService,
            CarService carService,
            CustomerService customerService,
            EmployeeService employeeService
    ) throws IOException {

        try (BufferedReader br = openResourceCsv("/data/rentals.csv")) {

            br.readLine(); // IGNORE HEADER

            String line;
            while ((line = br.readLine()) != null) {

                String[] d = line.split(",");
                if (d.length < 6) continue;

                Rental rental = new Rental(
                        d[0].trim(),
                        carService.findById(d[1].trim()),
                        customerService.findByAfm(d[2].trim()),
                        employeeService.findByUsername(d[3].trim()),
                        LocalDate.parse(d[4].trim()),
                        LocalDate.parse(d[5].trim())
                );

                rentalService.addRental(rental);
            }
        }
    }
}
