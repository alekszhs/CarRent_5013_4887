package api.services;

import api.models.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business logic for managing rentals in the car rental system.
 * <p>
 * This service handles operations such as creating new rentals, validating
 * availability and date ranges, completing rentals, and retrieving rental
 * history for customers and cars. It also generates unique rental IDs and
 * ensures that overlapping rentals for the same car are not allowed.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public class RentalService {

    // ==================== Fields ====================

    private final List<Rental> rentals = new ArrayList<>();


    // ==================== ID Generation ====================

    /**
     * Generates the next unique rental ID automatically.
     * Format: R-0001, R-0002, ...
     *
     * @return a new unique rental ID
     */
    public String generateRentalId() {
        int max = 0;

        for (Rental r : rentals) {
            try {
                int num = Integer.parseInt(r.getRentalId().replace("R-", ""));
                if (num > max) max = num;
            } catch (Exception ignored) {}
        }

        return String.format("R-%04d", max + 1);
    }


    // ==================== Finders ====================

    /**
     * Searches for a rental by its rental ID.
     *
     * @param rentalId the rental ID to search for
     * @return the matching rental, or null if not found
     */
    public Rental findById(String rentalId) {
        if (rentalId == null || rentalId.isBlank())
            return null;

        rentalId = rentalId.trim();

        for (Rental rental : rentals) {
            if (rental.getRentalId().equalsIgnoreCase(rentalId)) {
                return rental;
            }
        }
        return null;
    }


    // ==================== Add Rental ====================

    /**
     * Adds a rental object to the system.
     * Validations performed:
     * <ul>
     *     <li>Rental object must not be null</li>
     *     <li>Rental ID must be unique</li>
     *     <li>Car must be AVAILABLE</li>
     *     <li>Dates must be valid</li>
     *     <li>No overlapping ACTIVE rentals for the same car</li>
     * </ul>
     *
     * @param rental the rental to add
     * @return true if added successfully, false otherwise
     */
    public boolean addRental(Rental rental) {

        if (rental == null) return false;

        // Unique ID check
        for (Rental r : rentals) {
            if (r.getRentalId().equalsIgnoreCase(rental.getRentalId()))
                return false;
        }

        // Car availability
        if (rental.getCar().getStatus() != CarStatus.AVAILABLE)
            return false;

        // Date validation
        LocalDate startDate = rental.getStartDate();
        LocalDate endDate = rental.getEndDate();
        if (startDate == null || endDate == null) return false;
        if (endDate.isBefore(startDate)) return false;

        // Overlap check
        for (Rental r : rentals) {
            boolean sameCar = r.getCar().equals(rental.getCar());
            boolean active = r.getStatus() == RentalStatus.ACTIVE;
            boolean overlaps =
                    !endDate.isBefore(r.getStartDate()) &&
                            !startDate.isAfter(r.getEndDate());

            if (sameCar && active && overlaps)
                return false;
        }

        rentals.add(rental);
        rental.getCar().setStatus(CarStatus.RENTED);
        return true;
    }


    // ==================== Create Rental ====================

    /**
     * Creates a new rental using raw data instead of a ready-made Rental object.
     *
     * @return true if the rental was created successfully
     */
    public boolean rentCar(Car car, Customer customer, Employee employee,
                           LocalDate startDate, LocalDate endDate) {

        if (car == null || customer == null || employee == null) return false;
        if (startDate == null || endDate == null) return false;
        if (endDate.isBefore(startDate)) return false;
        if (car.getStatus() != CarStatus.AVAILABLE) return false;

        // Overlap check
        for (Rental r : rentals) {
            boolean sameCar = r.getCar().equals(car);
            boolean active = r.getStatus() == RentalStatus.ACTIVE;
            boolean overlaps =
                    !endDate.isBefore(r.getStartDate()) &&
                            !startDate.isAfter(r.getEndDate());

            if (sameCar && active && overlaps)
                return false;
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


    // ==================== Return Rental ====================

    /**
     * Completes a rental by its rental ID.
     * If the rental is ACTIVE, it is marked as COMPLETED and the car becomes AVAILABLE.
     *
     * @param rentalId the rental ID
     * @return true if completed successfully, false otherwise
     */
    public boolean returnCar(String rentalId) {
        if (rentalId == null || rentalId.isBlank()) return false;

        for (Rental rental : rentals) {
            if (rental.getRentalId().equalsIgnoreCase(rentalId)) {
                if (rental.getStatus() == RentalStatus.COMPLETED)
                    return false;

                rental.completeRental();
                return true;
            }
        }
        return false;
    }


    // ==================== Rental History ====================

    /**
     * Returns all rentals associated with a specific customer AFM.
     *
     * @param afm the customer's AFM
     * @return list of rentals (may be empty)
     */
    public List<Rental> getRentalsByCustomer(String afm) {
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
     *
     * @param plate the car's license plate
     * @return list of rentals (may be empty)
     */
    public List<Rental> getRentalsByCar(String plate) {
        if (plate == null || plate.isBlank()) return List.of();

        List<Rental> results = new ArrayList<>();
        for (Rental rental : rentals) {
            if (rental.getCar().getPlate().equalsIgnoreCase(plate)) {
                results.add(rental);
            }
        }
        return results;
    }


    // ==================== Active Rentals ====================

    /**
     * Returns all currently active rentals.
     *
     * @return list of active rentals
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


    // ==================== Utility ====================

    /**
     * Checks if a rental ID already exists.
     *
     * @param id the rental ID
     * @return true if it exists, false otherwise
     */
    public boolean rentalIdExists(String id) {
        if (id == null || id.isBlank()) return false;

        for (Rental r : rentals) {
            if (r.getRentalId().equalsIgnoreCase(id))
                return true;
        }
        return false;
    }

    /**
     * Returns all rentals.
     *
     * @return a copy of the rental list
     */
    public List<Rental> getAllRentals() {
        return new ArrayList<>(rentals);
    }

    // ==================== Import Rental (Raw) ====================

    /**
     * Imports a rental "as-is" without applying business rules.
     * Used by loaders to restore persisted state.
     *
     * @param rental the rental to import
     * @return true if imported successfully, false otherwise
     */
    public boolean importRental(Rental rental) {
        if (rental == null) return false;
        if (rental.getRentalId() == null || rental.getRentalId().isBlank()) return false;
        if (rentalIdExists(rental.getRentalId())) return false;

        rentals.add(rental);
        return true;
    }

}