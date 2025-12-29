package api.services;

import api.models.Car;
import api.models.CarStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides business logic for managing cars in the rental system.
 * <p>
 * This service handles operations such as adding new cars, updating existing
 * ones, searching by multiple criteria, checking availability, and modifying
 * car status. It maintains an internal list of cars, which should be populated
 * through a storage layer during application initialization.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public class CarService {

    // ==================== Fields ====================

    private final List<Car> cars = new ArrayList<>();


    // ==================== Add Car ====================

    /**
     * Adds a new car to the system, ensuring that both the ID and license plate
     * are unique. Performs basic validation on required fields.
     *
     * @param car the car to add
     * @return true if the car was added successfully, false if ID or plate already exist
     * @throws IllegalArgumentException if the car or any required field is invalid
     */
    public boolean addCar(Car car) {

        if (car == null)
            throw new IllegalArgumentException("Car cannot be null!");

        if (car.getId() == null || car.getId().isBlank())
            throw new IllegalArgumentException("Invalid ID!");

        if (car.getPlate() == null || car.getPlate().isBlank())
            throw new IllegalArgumentException("Invalid license plate!");

        if (car.getStatus() == null)
            throw new IllegalArgumentException("Car status cannot be null!");

        // Unique ID check
        if (findById(car.getId()) != null)
            return false;

        // Unique plate check
        for (Car c : cars) {
            if (c.getPlate().equalsIgnoreCase(car.getPlate())) {
                return false;
            }
        }

        cars.add(car);
        return true;
    }


    // ==================== Finders ====================

    /**
     * Finds a car by its unique ID.
     *
     * @param id the car ID
     * @return the matching car, or null if not found
     */
    public Car findById(String id) {
        if (id == null || id.isBlank()) return null;

        id = id.trim();

        for (Car c : cars) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Finds a car by its license plate.
     *
     * @param plate the license plate to search for
     * @return the matching car, or null if not found
     */
    public Car findByPlate(String plate) {
        if (plate == null || plate.isBlank()) return null;

        plate = plate.trim();

        for (Car c : cars) {
            if (c.getPlate() != null && c.getPlate().equalsIgnoreCase(plate)) {
                return c;
            }
        }
        return null;
    }


    // ==================== Update Car ====================

    /**
     * Updates an existing car with new data. Ensures that the updated license
     * plate does not conflict with another car in the system.
     *
     * @param id      the ID of the car to update
     * @param newData a Car object containing updated values
     * @throws IllegalArgumentException if the car does not exist or validation fails
     */
    public void updateCar(String id, Car newData) {

        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Id cannot be null or empty.");

        if (newData == null)
            throw new IllegalArgumentException("New data cannot be null.");

        // Validate fields
        if (newData.getPlate() == null || newData.getPlate().isBlank())
            throw new IllegalArgumentException("Invalid license plate!");

        if (newData.getBrand() == null || newData.getBrand().isBlank())
            throw new IllegalArgumentException("Brand cannot be empty!");

        if (newData.getModel() == null || newData.getModel().isBlank())
            throw new IllegalArgumentException("Model cannot be empty!");

        if (newData.getType() == null || newData.getType().isBlank())
            throw new IllegalArgumentException("Type cannot be empty!");

        int currentYear = java.time.LocalDate.now().getYear();
        if (newData.getYear() < 1900 || newData.getYear() > currentYear)
            throw new IllegalArgumentException("Invalid manufacturing year: " + newData.getYear());

        if (newData.getColor() == null || newData.getColor().isBlank())
            throw new IllegalArgumentException("Color cannot be empty!");

        if (newData.getStatus() == null)
            throw new IllegalArgumentException("Car status cannot be null!");

        // Find existing
        Car existing = findById(id.trim());
        if (existing == null)
            throw new IllegalArgumentException("Car with id " + id + " does not exist.");

        // Plate conflict check
        if (!existing.getPlate().equalsIgnoreCase(newData.getPlate())) {
            Car conflict = findByPlate(newData.getPlate());
            if (conflict != null && conflict != existing)
                throw new IllegalArgumentException("License plate already in use by another car.");
        }

        // Update fields
        existing.setPlate(newData.getPlate().trim());
        existing.setBrand(newData.getBrand().trim());
        existing.setModel(newData.getModel().trim());
        existing.setType(newData.getType().trim());
        existing.setYear(newData.getYear());
        existing.setColor(newData.getColor().trim());
        existing.setStatus(newData.getStatus());
    }


    // ==================== Delete Car ====================

    /**
     * Deletes a car from the system.
     *
     * @param id the ID of the car to delete
     * @throws IllegalArgumentException if the car does not exist
     */
    public void deleteCar(String id) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Id cannot be null or empty.");

        id = id.trim();

        Car existing = findById(id);
        if (existing == null)
            throw new IllegalArgumentException("Car with id " + id + " does not exist.");

        cars.remove(existing);
    }


    // ==================== Getters ====================

    /**
     * Returns all cars currently stored in the service.
     *
     * @return list of cars
     */
    public List<Car> getAllCars() {
        return new ArrayList<>(cars);
    }


    // ==================== Search ====================

    /**
     * Searches cars using multiple optional criteria. Any null or blank parameter
     * is ignored.
     *
     * @return a list of cars matching all provided criteria
     */
    public ArrayList<Car> searchCars(
            String brand,
            String plate,
            String model,
            String color,
            CarStatus status
    ) {
        ArrayList<Car> results = new ArrayList<>();

        for (Car c : cars) {

            if (brand != null && !brand.isBlank()) {
                if (c.getBrand() == null || !c.getBrand().equalsIgnoreCase(brand))
                    continue;
            }

            if (plate != null && !plate.isBlank()) {
                if (c.getPlate() == null || !c.getPlate().equalsIgnoreCase(plate))
                    continue;
            }

            if (model != null && !model.isBlank()) {
                if (c.getModel() == null || !c.getModel().equalsIgnoreCase(model))
                    continue;
            }

            if (color != null && !color.isBlank()) {
                if (c.getColor() == null || !c.getColor().equalsIgnoreCase(color))
                    continue;
            }

            if (status != null) {
                if (c.getStatus() != status)
                    continue;
            }

            results.add(c);
        }

        return results;
    }


    // ==================== Availability ====================

    /**
     * Checks whether a car is available for renting.
     *
     * @param id the car ID
     * @return true if the car exists and is AVAILABLE
     */
    public boolean isCarAvailable(String id) {
        if (id == null || id.isBlank())
            return false;

        Car c = findById(id.trim());
        return c != null && c.getStatus() == CarStatus.AVAILABLE;
    }


    // ==================== Status Update ====================

    /**
     * Changes the status of a car.
     *
     * @param id     the car ID
     * @param status the new status
     * @throws IllegalArgumentException if the car does not exist or status is null
     */
    public void setCarStatus(String id, CarStatus status) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("Id cannot be null or empty.");

        if (status == null)
            throw new IllegalArgumentException("Status cannot be null.");

        Car c = findById(id.trim());
        if (c == null)
            throw new IllegalArgumentException("Car with id " + id + " does not exist.");

        c.setStatus(status);
    }
}
