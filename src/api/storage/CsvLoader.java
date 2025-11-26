package api.storage;

import api.models.*;
import api.services.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class CsvLoader {

    // =============================================================
    // EMPLOYEES
    // 1:name  2:surname  3:username  4:email  5:password
    // Header παρακάμπτεται πάντα χωρίς να ενδιαφέρει τι γράφει
    // =============================================================
    public void loadEmployees(String filePath, EmployeeService service) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

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
    // 1:id  2:plate  3:brand  4:type  5:model  6:year  7:color  8:status
    // status παίρνει "Διαθέσιμο" -> AVAILABLE  ή "Ενοικιασμένο" -> RENTED
    // =============================================================
    public void loadCars(String filePath, CarService service) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

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
    // RENTALS  (όταν φτιάξεις rentals.csv)
    // 1:rentalId  2:carId  3:afm  4:username  5:start  6:end
    // =============================================================
    public void loadRentals(
            String filePath,
            RentalService rentalService,
            CarService carService,
            CustomerService customerService,
            EmployeeService employeeService
    ) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

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
