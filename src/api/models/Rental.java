package api.models;

import java.time.LocalDate;

/**
 * Represents a rental of a car by a customer.
 * <p>
 * Each rental has a unique rental ID, refers to one car and one customer,
 * stores the employee handling the process, and includes start/end dates.
 * Rentals start as ACTIVE and can later be completed.
 * </p>
 */
public class Rental {

    // ==================== Fields ====================

    private String rentalId;      // Unique rental code
    private Car car;              // Rented car
    private Customer customer;    // Customer renting the car
    private Employee employee;    // Employee creating the rental
    private LocalDate startDate;  // Start date
    private LocalDate endDate;    // End date
    private RentalStatus status;  // ACTIVE or COMPLETED


    // ==================== Constructor ====================

    /**
     * Creates a Rental object.
     *
     * @param rentalId unique rental identifier
     * @param car the rented car
     * @param customer the customer renting the car
     * @param employee the employee responsible for the rental
     * @param startDate rental start date
     * @param endDate rental end date
     *
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Rental(String rentalId, Car car, Customer customer, Employee employee,
                  LocalDate startDate, LocalDate endDate) {

        // Validate required fields
        if (rentalId == null || rentalId.isBlank())
            throw new IllegalArgumentException("Rental ID cannot be null or empty.");

        if (car == null)
            throw new IllegalArgumentException("Car cannot be null.");

        if (customer == null)
            throw new IllegalArgumentException("Customer cannot be null.");

        if (employee == null)
            throw new IllegalArgumentException("Employee cannot be null.");

        if (startDate == null)
            throw new IllegalArgumentException("Start date cannot be null.");

        if (endDate == null)
            throw new IllegalArgumentException("End date cannot be null.");

        // Validate date order
        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("End date cannot be before start date.");

        // Assign fields
        this.rentalId = rentalId.trim();
        this.car = car;
        this.customer = customer;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;

        // New rentals always start as active
        this.status = RentalStatus.ACTIVE;

        // Car becomes rented immediately
        this.car.setStatus(CarStatus.RENTED);
    }


    // ==================== Getters ====================

    public String getRentalId()  { return rentalId; }
    public Car getCar()          { return car; }
    public Customer getCustomer(){ return customer; }
    public Employee getEmployee(){ return employee; }
    public LocalDate getStartDate(){ return startDate; }
    public LocalDate getEndDate()  { return endDate; }
    public RentalStatus getStatus(){ return status; }


    // ==================== Setters ====================

    /**
     * Updates the end date of the rental.
     * The new end date must still be >= start date.
     *
     * @param endDate the new end date
     */
    public void setEndDate(LocalDate endDate) {
        if (endDate == null)
            throw new IllegalArgumentException("End date cannot be null.");

        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("End date cannot be earlier than start date.");

        this.endDate = endDate;
    }

    /**
     * Sets the rental status (ACTIVE or COMPLETED)
     *
     * @param status the new rental status
     */
    public void setStatus(RentalStatus status) {
        if (status == null)
            throw new IllegalArgumentException("Rental status cannot be null.");

        this.status = status;
    }


    // ==================== Business Logic ====================

    /**
     * Marks the rental as completed and makes the car available again.
     */
    public void completeRental() {
        this.status = RentalStatus.COMPLETED;
        this.car.setStatus(CarStatus.AVAILABLE);
    }


    // ==================== Equals & HashCode ====================

    /**
     * Rentals are equal if they share the same rental ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rental)) return false;
        Rental r = (Rental) o;
        return rentalId.equals(r.rentalId);
    }

    @Override
    public int hashCode() {
        return rentalId == null ? 0 : rentalId.hashCode();
    }


    // ==================== ToString ====================

    /**
     * Returns a readable representation of the rental.
     */
    @Override
    public String toString() {
        return "Rental [" + rentalId + "] "
                + customer.getFullName()
                + " rented " + car.getPlate()
                + " from " + startDate
                + " to " + endDate
                + " (" + status + ")";
    }
}
