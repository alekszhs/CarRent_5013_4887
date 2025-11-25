package api.models;
import api.models.CarStatus;
import api.models.RentalStatus;

import java.time.LocalDate;

/**
 * Represents a rental of a car by a customer.
 * Each rental has a unique id, a car, a customer,
 * a start and end date, and the employee who created the rental.
 */
public class Rental {

    private String rentalId;        // Unique rental code
    private Car car;               // Car being rented
    private Customer customer;     // Customer renting it
    private Employee employee;     // Employee handling the rental
    private LocalDate startDate;   // Rental start date
    private LocalDate endDate;     // Rental end date
    private RentalStatus status;   // ACTIVE or COMPLETED

    /**
     * Constructs a Rental object
     */
    public Rental(String rentalId, Car car, Customer customer, Employee employee,
                  LocalDate startDate, LocalDate endDate) {

        this.rentalId = rentalId;
        this.car = car;
        this.customer = customer;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = RentalStatus.ACTIVE;  // New rentals are always active
    }

    // Getters
    public String getRentalId() {
        return rentalId;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    // Setters
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    /**
     * Marks the rental as completed (car returned)
     */
    public void completeRental() {
        this.status = RentalStatus.COMPLETED;
        this.car.setStatus(CarStatus.AVAILABLE);
    }

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
