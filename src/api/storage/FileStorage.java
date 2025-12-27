package api.storage;

import api.models.*;
import api.services.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles saving application data into CSV files.
 * Data is written to a user-safe directory (outside resources).
 */
public class FileStorage {

    // Base directory: ~/CarRent
    private Path baseDir() throws IOException {
        Path base = Paths.get(System.getProperty("user.home"), "CarRent");
        Files.createDirectories(base);
        return base;
    }

    private BufferedWriter writer(String filename) throws IOException {
        Path file = baseDir().resolve(filename);
        return Files.newBufferedWriter(file, StandardCharsets.UTF_8);
    }

    /**
     * Saves all employees into users.csv
     * Format: name,surname,username,email,password
     */
    public void saveEmployees(EmployeeService service) throws IOException {
        try (BufferedWriter bw = writer("users.csv")) {
            bw.write("name,surname,username,email,password\n");

            for (Employee e : service.getAllEmployees()) {
                String[] parts = e.getFullName().trim().split("\\s+", 2);
                String name = parts.length > 0 ? parts[0] : "";
                String surname = parts.length > 1 ? parts[1] : "";
                bw.write(name + "," + surname + "," +
                        e.getUsername() + "," + e.getEmail() + "," + e.getPassword() + "\n");
            }
        }
    }

    /**
     * Saves all cars into vehicles_with_plates.csv
     * Format: id,plate,brand,type,model,year,color,status
     */
    public void saveCars(CarService service) throws IOException {
        try (BufferedWriter bw = writer("vehicles_with_plates.csv")) {
            bw.write("id,plate,brand,type,model,year,color,status\n");

            for (Car c : service.getAllCars()) {
                bw.write(
                        c.getId() + "," + c.getPlate() + "," + c.getBrand() + "," +
                                c.getType() + "," + c.getModel() + "," + c.getYear() + "," +
                                c.getColor() + "," + c.getStatus() + "\n"
                );
            }
        }
    }

    /**
     * Saves all rentals into rentals.csv
     * Format: rentalId,carId,afm,username,startDate,endDate,status
     */
    public void saveRentals(RentalService service) throws IOException {
        try (BufferedWriter bw = writer("rentals.csv")) {
            bw.write("rentalId,carId,afm,username,startDate,endDate,status\n");

            for (Rental r : service.getAllRentals()) {
                bw.write(
                        r.getRentalId() + "," +
                                r.getCar().getId() + "," +
                                r.getCustomer().getAfm() + "," +
                                r.getEmployee().getUsername() + "," +
                                r.getStartDate() + "," +
                                r.getEndDate() + "," +
                                r.getStatus() + "\n"
                );
            }
        }
    }
}
