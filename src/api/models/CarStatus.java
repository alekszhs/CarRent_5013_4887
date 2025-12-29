package api.models;

/**
 * Represents the availability status of a car in the rental system.
 * <p>
 * A car can either be available for rental or currently rented by a customer.
 * This enum is used throughout the system to track and update the state of
 * each vehicle.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public enum CarStatus {
    AVAILABLE,   // The car is free and can be rented
    RENTED       // The car is currently rented
}