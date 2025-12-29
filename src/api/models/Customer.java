package api.models;

/**
 * Represents a customer who can rent cars from the company.
 * <p>
 * Each customer is uniquely identified by their AFM (Greek tax number) and
 * includes personal information such as full name, phone number, and email.
 * All fields are validated through setter methods to ensure data integrity
 * across the rental system.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public class Customer {

    // ==================== Fields ====================

    private String afm;         // Unique tax number (9 digits)
    private String fullName;    // Full name of the customer
    private String phoneNumber; // Phone number (10 digits in Greece)
    private String email;       // Email address


    // ==================== Constructor ====================

    /**
     * Creates a Customer object with the required fields.
     * All input is validated through setters.
     *
     * @param afm         the 9-digit tax number
     * @param fullName    the customer's full name
     * @param phoneNumber the customer's phone number (10 digits)
     * @param email       the customer's email
     *
     * @throws IllegalArgumentException if any field is invalid
     */
    public Customer(String afm, String fullName, String phoneNumber, String email) {
        setAfm(afm);
        setFullName(fullName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
    }


    // ==================== Getters ====================

    public String getAfm()         { return afm; }
    public String getFullName()    { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail()       { return email; }


    // ==================== Setters with Validation ====================

    /**
     * Sets the customer's AFM (must be exactly 9 digits).
     *
     * @param afm the tax number to assign
     * @throws IllegalArgumentException if the AFM is null, empty, or invalid
     */
    public void setAfm(String afm) {
        if (afm == null || afm.isBlank())
            throw new IllegalArgumentException("AFM cannot be null or empty.");

        afm = afm.trim();

        if (!afm.matches("\\d{9}"))
            throw new IllegalArgumentException("AFM must consist of exactly 9 digits.");

        this.afm = afm;
    }

    /**
     * Sets the customer's full name.
     *
     * @param fullName the full name to assign
     * @throws IllegalArgumentException if the name is null or empty
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Full name cannot be empty.");

        this.fullName = fullName.trim();
    }

    /**
     * Sets the customer's phone number (must be exactly 10 digits).
     *
     * @param phoneNumber the phone number to assign
     * @throws IllegalArgumentException if the number is null, empty, or invalid
     */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new IllegalArgumentException("Phone number cannot be empty.");

        phoneNumber = phoneNumber.trim();

        if (!phoneNumber.matches("\\d{10}"))
            throw new IllegalArgumentException("Phone number must have exactly 10 digits.");

        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the customer's email, validating its format.
     *
     * @param email the email address to assign
     * @throws IllegalArgumentException if the email is null, empty, or invalid
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email cannot be empty.");

        email = email.trim();

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Invalid email format.");

        this.email = email;
    }


    // ==================== Equals & HashCode ====================

    /**
     * Customers are considered equal if they share the same AFM.
     *
     * @param o the object to compare
     * @return true if the AFM matches, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer c = (Customer) o;
        return afm.equals(c.afm);
    }

    @Override
    public int hashCode() {
        return afm == null ? 0 : afm.hashCode();
    }


    // ==================== ToString ====================

    /**
     * Returns a readable representation of the customer.
     *
     * @return a formatted string containing the customer's details
     */
    @Override
    public String toString() {
        return fullName + " (" + afm + ") - " + phoneNumber + " / " + email;
    }
}
