package api.services;

import api.models.Car;
import api.models.CarStatus;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Handles all business logic for cars:
 * loading, searching, adding, editing, and changing availability.
 */
public class CarService {

    private final List<Car> cars = new ArrayList<>();

    /**
     * Adds a new car, ensuring unique id & license plate + validation check.
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

        // unique checks
        if (findById(car.getId()) != null)
            return false; // ID already exists

        for (Car c : cars) {
            if (c.getPlate().equalsIgnoreCase(car.getPlate())) {
                return false; // license exists
            }
        }

        cars.add(car);
        return true; // SUCCESS
    }


    /**
     * Finds a car by its unique id.
     */
    public Car findById(String id) {
        if (id == null || id.isBlank() ){return null;}

        id = id.trim();
        if (id.isEmpty()) {
            return null;
        }

        for (Car c : cars ){
            if (c.getId().equals(id)){
                return c;
            }
        }
        return null;

    }

    /**
     * Finds a car by its license plate.
     * Returns the matching Car or null if no car is found.
     */
    public Car findByPlate(String plate) {
        if (plate == null || plate.isBlank()) {
            return null;
        }

        plate = plate.trim();

        for (Car c : cars) {
            // equalsIgnoreCase because plates are case-insensitive
            if (c.getPlate() != null && c.getPlate().equalsIgnoreCase(plate)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Updates an existing car with new resources.data.
     * Finds the car by its id, validates conflicts (e.g. license plate),
     * and updates all editable fields.
     *
     * @param id The id of the car to update.
     * @param newData A Car object containing the updated information.
     * @throws IllegalArgumentException if the car does not exist
     *                                  or if the new plate conflicts with another car.
     */
    public void updateCar(String id, Car newData) {
        // Find the existing car
        Car existing = findById(id);
        if (existing == null) {
            throw new IllegalArgumentException("Car with id " + id + " does not exist.");
        }

        // ---- License plate conflict check ----
        // Only check if user changed the plate
        if (!existing.getPlate().equalsIgnoreCase(newData.getPlate())) {

            Car carWithSamePlate = findByPlate(newData.getPlate());

            // If another car already has the new plate -> ERROR
            if (carWithSamePlate != null && carWithSamePlate != existing) {
                throw new IllegalArgumentException("License plate already in use by another car.");
            }
        }

        // ---- Update all fields ----
        existing.setPlate(newData.getPlate());
        existing.setBrand(newData.getBrand());
        existing.setModel(newData.getModel());
        existing.setType(newData.getType());
        existing.setYear(newData.getYear());
        existing.setColor(newData.getColor());
        existing.setStatus(newData.getStatus());
    }

    /**
     * Deletes a car from the system by its id.
     * If the car does not exist, an exception is thrown.
     *
     * @param id The id of the car to delete.
     * @throws IllegalArgumentException if the car is not found.
     */
    public void deleteCar(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be null or empty.");
        }

        id = id.trim();

        Car existing = findById(id);

        if (existing == null) {
            throw new IllegalArgumentException("Car with id " + id + " does not exist.");
        }

        cars.remove(existing);
    }



    /**
     * Returns all cars.
     */
    public List<Car> getAllCars() {
        return cars;
    }

    /**
     * Searches cars using multiple criteria.
     * Any null parameter is ignored.
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




    /**
     * Checks if a car is available for renting.
     *
     * @param id The id of the car to check.
     * @return true if the car exists and its status is AVAILABLE, false otherwise.
     */
    public boolean isCarAvailable(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }

        id = id.trim();

        Car c = findById(id);
        if (c == null) {
            return false; // car does not exist
        }

        return c.getStatus() == CarStatus.AVAILABLE;
    }


    /**
     * Changes the status of a car (AVAILABLE, RENTED, IN_SERVICE, DAMAGED, etc.)
     *
     * @param id The id of the car.
     * @param status The new status to apply.
     * @throws IllegalArgumentException if the car does not exist or the status is null.
     */
    public void setCarStatus(String id, CarStatus status) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be null or empty.");
        }

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }

        id = id.trim();

        Car c = findById(id);
        if (c == null) {
            throw new IllegalArgumentException("Car with id " + id + " does not exist.");
        }

        c.setStatus(status);
    }

}
