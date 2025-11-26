package api.storage;

import api.models.*;
import api.services.*;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Handles saving system data into CSV files.
 * Used when program closes OR when user manually chooses "Αποθήκευση".
 * Exports data generated/modified inside the application.
 */
public class FileStorage {

    /**
     * Saves all employees into CSV format.
     * Format:
     *   name,surname,username,email,password
     *
     * Writes fullName split into name + surname (requires 2 words!).
     *
     * @param path path to output CSV
     * @param service employee source (EmployeeService)
     */
    public void saveEmployees(String path, EmployeeService service) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write("name,surname,username,email,password\n");

        for (Employee e : service.getAllEmployees()) {
            String[] parts = e.getFullName().split(" "); // fullName -> split to first/last
            fw.write(parts[0] + "," + parts[1] + "," + e.getUsername() + "," + e.getEmail() + "," + e.getPassword() + "\n");
        }

        fw.close();
    }

    /**
     * Saves all cars into vehicles.csv
     * Format:
     *   id,plate,brand,type,model,year,color,status
     *
     * Status is written as ENUM → AVAILABLE/RENTED (όχι ελληνικά)
     *
     * @param path path to CSV file
     * @param service CarService containing car list
     */
    public void saveCars(String path, CarService service) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write("id,plate,brand,type,model,year,color,status\n");

        for (Car c : service.getAllCars()) {
            fw.write(
                    c.getId() + "," + c.getPlate() + "," + c.getBrand() + "," +
                            c.getType() + "," + c.getModel() + "," + c.getYear() + "," +
                            c.getColor() + "," + c.getStatus() + "\n"
            );
        }

        fw.close();
    }

    /**
     * Saves all rentals into rentals.csv
     * Format:
     *   rentalId,carId,afm,username,startDate,endDate,status
     *
     * Includes:
     *  - Car ID (όχι plate)
     *  - Customer AFM
     *  - Employee username
     *  - Dates serialized as ISO (yyyy-MM-dd)
     *
     * @param path output file path
     * @param service RentalService with rentals list
     */
    public void saveRentals(String path, RentalService service) throws IOException {
        FileWriter fw = new FileWriter(path);
        fw.write("rentalId,carId,afm,username,startDate,endDate,status\n");

        for (Rental r : service.getAllRentals()) {
            fw.write(
                    r.getRentalId() + "," + r.getCar().getId() + "," + r.getCustomer().getAfm() + "," +
                            r.getEmployee().getUsername() + "," + r.getStartDate() + "," + r.getEndDate() + "," + r.getStatus()
                            + "\n"
            );
        }

        fw.close();
    }
}
