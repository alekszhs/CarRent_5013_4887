package api.models;

/**
 * Represents the status of a rental.
 * <p>
 * A rental begins in ACTIVE state and is later marked as COMPLETED
 * once the car is returned and the process is finalized.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public enum RentalStatus {
    ACTIVE,     // Rental is ongoing
    COMPLETED   // Rental has been finished
}