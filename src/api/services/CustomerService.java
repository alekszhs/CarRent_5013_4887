package api.services;

import api.models.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// findByAfm(String afm)
// Επιστρέφει τον πελάτη με το συγκεκριμένο ΑΦΜ ή null


// addCustomer(Customer customer)
// Προσθήκη νέου πελάτη με ελέγχους:
// - Το αντικείμενο να μην είναι null
// - Το ΑΦΜ να μην είναι κενό
// - Το ΑΦΜ να είναι μοναδικό στο σύστημα
// - Το ονοματεπώνυμο να μην είναι κενό
// - Το τηλέφωνο να μην είναι κενό
// - Το email να μην είναι κενό (και να έχει έλεγχο μορφής προαιρετικά)

// findByAfm(String afm)
// Αναζήτηση πελάτη βάσει ΑΦΜ (unique identifier)
// Επιστρέφει τον πελάτη ή null

// updateCustomer(String afm, Customer newData)
// Ενημερώνει τα στοιχεία του πελάτη:
// - ονοματεπώνυμο, τηλέφωνο, email
// Έλεγχος ότι:
// - Ο πελάτης υπάρχει
// - Το νέο ΑΦΜ δεν ανήκει σε άλλον πελάτη (αν αλλάξει)

// deleteCustomer(String afm)
// Διαγράφει πελάτη από το σύστημα
// Ρίχνει εξαίρεση αν ο πελάτης δεν υπάρχει

// getAllCustomers()
// Επιστρέφει όλους τους πελάτες

// searchCustomers(String afm, String name, String phone)
// Αναζήτηση πελατών με πολλαπλά προαιρετικά κριτήρια:
// - ΑΦΜ (προαιρετικό – ακριβές ταίριασμα)
// - Ονοματεπώνυμο (προαιρετικό – case-insensitive)
// - Τηλέφωνο (προαιρετικό – ακριβές)




/**
 * Handles all business logic related to customers:
 * loading, searching, creating and updating customer records.
 */
public class CustomerService {

    private final List<Customer> customers = new ArrayList<>();

    /**
     * Finds a customer by AFM.
     * Returns the matching customer or null if not found.
     */
    public Customer findByAfm(String afm) {
        if (afm == null || afm.isBlank()) {
            return null;
        }

        afm = afm.trim();

        for (Customer c : customers) {
            if (c.getAfm() != null && c.getAfm().equals(afm)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Adds a new customer into the system.
     * Performs validation checks:
     * - customer object must not be null
     * - AFM must not be null or empty
     * - AFM must be unique
     * - full name must not be null/empty
     * - phone must not be null/empty
     * - email must not be null/empty
     *
     * @param customer The customer to add.
     * @throws IllegalArgumentException if any validation fails.
     */
    public void addCustomer(Customer customer) {

        // Check null object
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        // Validate AFM
        if (customer.getAfm() == null || customer.getAfm().isBlank()) {
            throw new IllegalArgumentException("AFM cannot be null or empty.");
        }

        String afm = customer.getAfm().trim();

        // Check AFM uniqueness
        if (findByAfm(afm) != null) {
            throw new IllegalArgumentException("A customer with this AFM already exists.");
        }

        // Validate name
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }

        // Validate phone
        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Phone cannot be empty.");
        }

        // Validate email
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        // All good -> add customer
        customers.add(customer);
    }

    /**
     * Updates a customer's information.
     *
     * @param afm The AFM of the customer to update.
     * @param newData The new data for the customer.
     * @throws IllegalArgumentException if the AFM is invalid, customer not found,
     *                                  or if the new AFM conflicts with another customer.
     */
    public void updateCustomer(String afm, Customer newData) {

        if (afm == null || afm.isBlank()) {
            throw new IllegalArgumentException("AFM cannot be empty.");
        }

        if (newData == null) {
            throw new IllegalArgumentException("New data cannot be null.");
        }

        afm = afm.trim();

        Customer existing = findByAfm(afm);
        if (existing == null) {
            throw new IllegalArgumentException("Customer with AFM " + afm + " does not exist.");
        }

        // --- Check AFM conflict IF AFM is changed ---
        String newAfm = newData.getAfm();
        if (!afm.equals(newAfm)) {
            Customer other = findByAfm(newAfm);
            if (other != null && other != existing) {
                throw new IllegalArgumentException("Another customer already uses this AFM.");
            }
        }

        // --- Validate new fields ---
        if (newData.getFullName() == null || newData.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name cannot be empty.");
        }

        if (newData.getPhoneNumber() == null || newData.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be empty.");
        }

        if (newData.getEmail() == null || newData.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        // --- Update ---
        existing.setAfm(newData.getAfm());
        existing.setFullName(newData.getFullName());
        existing.setPhoneNumber(newData.getPhoneNumber());
        existing.setEmail(newData.getEmail());
    }

    /**
     * Deletes a customer from the system.
     *
     * @param afm The AFM of the customer to delete.
     * @throws IllegalArgumentException if the AFM is invalid or the customer does not exist.
     */
    public void deleteCustomer(String afm) {

        if (afm == null || afm.isBlank()) {
            throw new IllegalArgumentException("AFM cannot be empty.");
        }

        afm = afm.trim();

        Customer existing = findByAfm(afm);

        if (existing == null) {
            throw new IllegalArgumentException("Customer with AFM " + afm + " does not exist.");
        }

        customers.remove(existing);
    }

    /**
     * Return all customers.
     */
    public List<Customer> getAllCustomers(){
        return customers;
    }

    /**
     * Searches customers using optional criteria.
     * Any null or blank parameter is ignored.
     *
     * @return List of matching customers.
     */
    public List<Customer> searchCustomers(String afm, String fullName, String phoneNumber) {

        List<Customer> results = new ArrayList<>();

        // Normalize inputs
        if (afm != null) afm = afm.trim();
        if (fullName != null) fullName = fullName.trim();
        if (phoneNumber != null) phoneNumber = phoneNumber.trim();

        for (Customer c : customers) {

            // AFM match (if AFM is provided)
            if (afm != null && !afm.isBlank()) {
                if (!c.getAfm().equalsIgnoreCase(afm)) continue;
            }

            // Full name match (if provided) - case-insensitive
            if (fullName != null && !fullName.isBlank()) {
                if (!c.getFullName().equalsIgnoreCase(fullName)) continue;
            }

            // Phone number match (if provided)
            if (phoneNumber != null && !phoneNumber.isBlank()) {
                if (!c.getPhoneNumber().equals(phoneNumber)) continue;
            }

            results.add(c);
        }

        return results;
    }
}

