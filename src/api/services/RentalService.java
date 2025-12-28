package api.services;

import api.models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentalService {

    private final List<Rental> rentals = new ArrayList<>();

    /**
     * Generates the next unique rental ID automatically.
     * Format → R-0001, R-0002 ...
     * Scans existing rentals, finds maximum, increments.
     */
    public String generateRentalId() {
        int max = 0;

        for (Rental r : rentals) {
            try {
                int num = Integer.parseInt(r.getRentalId().replace("R-", ""));
                if (num > max) max = num;
            } catch (Exception ignored) {}
        }

        int newId = max + 1;
        return String.format("R-%04d", newId);
    }

    /**
     * Searches for a rental using rentalId.
     * @return Rental if found, otherwise null.
     */
    public Rental findById(String rentalId) {
        if (rentalId == null || rentalId.isBlank())
            return null;

        rentalId = rentalId.trim();

        for (Rental rental : rentals){
            if (rental.getRentalId().equalsIgnoreCase(rentalId)){
                return rental;
            }
        }
        return null;
    }

    /**
     * Adds a Rental object into system.
     * Validations performed:
     *  - rental != null
     *  - ID must be unique
     *  - car must be AVAILABLE
     *  - dates valid & not overlapping existing active rentals
     * @return true if successful, false otherwise
     */
    public boolean addRental(Rental rental) {

        if (rental == null) return false;

        // rentalId must be unique
        for (Rental r : rentals) {
            if (r.getRentalId().equalsIgnoreCase(rental.getRentalId())) return false;
        }

        // Car availability check
        if (rental.getCar().getStatus() != CarStatus.AVAILABLE) return false;

        // Dates validation
        LocalDate startDate = rental.getStartDate();
        LocalDate endDate   = rental.getEndDate();
        if (startDate == null || endDate == null) return false;
        if (endDate.isBefore(startDate)) return false;

        // Overlapping date check for same car with ACTIVE rentals
        for (Rental r : rentals) {
            boolean sameCar = r.getCar().equals(rental.getCar());
            boolean active  = r.getStatus() == RentalStatus.ACTIVE;
            boolean overlaps =
                    !endDate.isBefore(r.getStartDate()) &&
                            !startDate.isAfter(r.getEndDate());

            if (sameCar && active && overlaps) return false;
        }

        rentals.add(rental);
        rental.getCar().setStatus(CarStatus.RENTED);
        return true;
    }

    /**
     * Creates a new rental using raw data instead of a ready object.
     * Steps performed:
     *  - validate input
     *  - check availability & overlaps
     *  - generateRentalId()
     *  - create new Rental and add it to list
     * @return true if rent created successfully
     */
    public boolean rentCar(Car car, Customer customer, Employee employee,
                           LocalDate startDate, LocalDate endDate) {

        if (car == null || customer == null || employee == null) return false;
        if (startDate == null || endDate == null) return false;
        if (endDate.isBefore(startDate)) return false;
        if (car.getStatus() != CarStatus.AVAILABLE) return false;

        // check overlapping rentals
        for (Rental r : rentals) {
            boolean sameCar = r.getCar().equals(car);
            boolean active  = r.getStatus() == RentalStatus.ACTIVE;
            boolean overlaps =
                    !endDate.isBefore(r.getStartDate()) &&
                            !startDate.isAfter(r.getEndDate());

            if (sameCar && active && overlaps) return false;
        }

        String rentalId = generateRentalId();

        Rental rental = new Rental(
                rentalId, car, customer, employee,
                startDate, endDate
        );

        rentals.add(rental);
        car.setStatus(CarStatus.RENTED);
        return true;
    }

    /**
     * Completes a rental using rentalId.
     * If found & ACTIVE → mark COMPLETED and set car AVAILABLE.
     * @return true if successful, false if rental not found or already completed.
     */
    public boolean returnCar(String rentalId) {
        if (rentalId == null || rentalId.isBlank()) return false;

        for (Rental rental : rentals) {
            if (rental.getRentalId().equalsIgnoreCase(rentalId)) {
                if (rental.getStatus() == RentalStatus.COMPLETED) return false;

                rental.completeRental();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all rentals by specific customer AFM.
     * Returns empty list if AFM invalid or no rentals found.
     */
    public List<Rental> getRentalsByCustomer(String afm){
        if (afm == null || afm.isBlank()) return List.of();

        List<Rental> results = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getCustomer().getAfm().equalsIgnoreCase(afm)) {
                results.add(rental);
            }
        }
        return results;
    }

    /**
     * Returns all rentals associated with a specific car (by plate).
     * @return List of rentals (may be empty)
     */
    public List<Rental> getRentalsByCar(String plate){
        if (plate == null || plate.isBlank()) return List.of();

        List<Rental> results = new ArrayList<>();
        for (Rental rental : rentals){
            if (rental.getCar().getPlate().equalsIgnoreCase(plate)){
                results.add(rental);
            }
        }
        return results;
    }

    /**
     * Returns all currently active rentals.
     * An active rental is defined as a rental whose status is ACTIVE.
     * These rentals correspond to cars that are currently rented and have not yet been returned.
     * @return a list of active Rental objects.
     *         The list is empty if there are no active rentals.
     */
    public List<Rental> getActiveRentals() {
        List<Rental> results = new ArrayList<>();
        for (Rental r : rentals) {
            if (r.getStatus() == RentalStatus.ACTIVE) {
                results.add(r);
            }
        }
        return results;
    }


    /**
     * Checks if rental ID already exists.
     */
    public boolean rentalIdExists(String id) {
        if (id == null || id.isBlank()) return false;

        for (Rental r : rentals) {
            if (r.getRentalId().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    /**
     * Returns all rentals.
     */
    public List<Rental> getAllRentals(){
        return rentals;
    }
}

