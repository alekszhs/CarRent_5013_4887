package api.storage;

import api.models.*;
import api.services.*;

import java.io.*;
import java.nio.file.*;

public class FileStorage {

    private static final String BASE_DIR =
            System.getProperty("user.home") + File.separator + "car-rental-data";

    public FileStorage() {
        new File(BASE_DIR).mkdirs();
    }

    private BufferedWriter writer(String fileName) throws IOException {
        Path path = Paths.get(BASE_DIR, fileName);
        return Files.newBufferedWriter(path);
    }

    /* ================= EMPLOYEES ================= */

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

    /* ================= CARS ================= */

    public void saveCars(CarService service) throws IOException {
        try (BufferedWriter bw = writer("cars.csv")) {
            bw.write("id,plate,brand,model,type,year,color,status\n");

            for (Car c : service.getAllCars()) {
                bw.write(
                        c.getId() + "," +
                                c.getPlate() + "," +
                                c.getBrand() + "," +
                                c.getModel() + "," +
                                c.getType() + "," +
                                c.getYear() + "," +
                                c.getColor() + "," +
                                c.getStatus() + "\n"
                );
            }
        }
    }

    /* ================= CUSTOMERS ================= */

    public void saveCustomers(CustomerService service) throws IOException {
        try (BufferedWriter bw = writer("customers.csv")) {
            bw.write("afm,fullName,phone,email\n");

            for (Customer c : service.getAllCustomers()) {
                bw.write(
                        c.getAfm() + "," +
                                escape(c.getFullName()) + "," +
                                c.getPhoneNumber() + "," +
                                c.getEmail() + "\n"
                );
            }
        }
    }

    /* ================= RENTALS ================= */

    public void saveRentals(RentalService service) throws IOException {
        try (BufferedWriter bw = writer("rentals.csv")) {
            bw.write("rentalId,carPlate,customerAfm,employeeUsername,startDate,endDate,status\n");

            for (Rental r : service.getAllRentals()) {
                bw.write(
                        r.getRentalId() + "," +
                                r.getCar().getPlate() + "," +
                                r.getCustomer().getAfm() + "," +
                                r.getEmployee().getUsername() + "," +
                                r.getStartDate() + "," +
                                r.getEndDate() + "," +
                                r.getStatus() + "\n"
                );
            }
        }
    }

    /* ================= UTIL ================= */

    private String escape(String s) {
        if (s == null) return "";
        return s.replace(",", " ");
    }
}
