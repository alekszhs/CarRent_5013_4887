package api.services;

import api.models.Customer;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides business logic for managing customers in the rental system.
 * <p>
 * This service supports operations such as adding new customers, updating
 * existing records, searching by multiple criteria, and deleting customers.
 * AFM is treated as a unique and immutable identifier.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public class CustomerService {

    // ==================== Fields ====================

    private final List<Customer> customers = new ArrayList<>();


    // ==================== Finders ====================

    /**
     * Finds a customer by AFM.
     *
     * @param afm the AFM to search for
     * @return the matching customer, or null if not found
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


    // ==================== Add Customer ====================

    /**
     * Adds a new customer to the system.
     * <p>
     * Performs validation checks:
     * <ul>
     *     <li>Customer object must not be null</li>
     *     <li>AFM must not be null or empty</li>
     *     <li>AFM must be unique</li>
     *     <li>Full name must not be null or empty</li>
     *     <li>Phone number must not be null or empty</li>
     *     <li>Email must not be null or empty</li>
     * </ul>
     * </p>
     *
     * @param customer the customer to add
     * @throws IllegalArgumentException if validation fails
     */
    public void addCustomer(Customer customer) {

        if (customer == null)
            throw new IllegalArgumentException("Customer cannot be null.");

        if (customer.getAfm() == null || customer.getAfm().isBlank())
            throw new IllegalArgumentException("AFM cannot be null or empty.");

        String afm = customer.getAfm().trim();

        if (findByAfm(afm) != null)
            throw new IllegalArgumentException("A customer with this AFM already exists.");

        if (customer.getFullName() == null || customer.getFullName().isBlank())
            throw new IllegalArgumentException("Full name cannot be empty.");

        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().isBlank())
            throw new IllegalArgumentException("Phone cannot be empty.");

        if (customer.getEmail() == null || customer.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be empty.");

        customers.add(customer);
    }


    // ==================== Update Customer ====================

    /**
     * Updates an existing customer's information.
     * <p>
     * AFM is treated as immutable and cannot be changed.
     * </p>
     *
     * @param afm     the AFM of the customer to update
     * @param newData a Customer object containing updated values
     * @throws IllegalArgumentException if validation fails or customer does not exist
     */
    public void updateCustomer(String afm, Customer newData) {

        if (afm == null || afm.isBlank())
            throw new IllegalArgumentException("AFM cannot be empty.");

        if (newData == null)
            throw new IllegalArgumentException("New data cannot be null.");

        afm = afm.trim();

        Customer existing = findByAfm(afm);
        if (existing == null)
            throw new IllegalArgumentException("Customer with AFM " + afm + " does not exist.");

        // AFM cannot change
        if (newData.getAfm() == null || !afm.equals(newData.getAfm().trim()))
            throw new IllegalArgumentException("AFM cannot be changed.");

        if (newData.getFullName() == null || newData.getFullName().isBlank())
            throw new IllegalArgumentException("Full name cannot be empty.");

        if (newData.getPhoneNumber() == null || newData.getPhoneNumber().isBlank())
            throw new IllegalArgumentException("Phone number cannot be empty.");

        if (newData.getEmail() == null || newData.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be empty.");

        existing.setFullName(newData.getFullName().trim());
        existing.setPhoneNumber(newData.getPhoneNumber().trim());
        existing.setEmail(newData.getEmail().trim());
    }


    // ==================== Delete Customer ====================

    /**
     * Deletes a customer from the system.
     *
     * @param afm the AFM of the customer to delete
     * @throws IllegalArgumentException if AFM is invalid or customer does not exist
     */
    public void deleteCustomer(String afm) {

        if (afm == null || afm.isBlank())
            throw new IllegalArgumentException("AFM cannot be empty.");

        afm = afm.trim();

        Customer existing = findByAfm(afm);

        if (existing == null)
            throw new IllegalArgumentException("Customer with AFM " + afm + " does not exist.");

        customers.remove(existing);
    }


    // ==================== Getters ====================

    /**
     * Returns all customers currently stored in the service.
     *
     * @return list of customers
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }


    // ==================== Search ====================

    /**
     * Searches customers using optional criteria.
     * Any null or blank parameter is ignored.
     *
     * @param afm         AFM to match (optional)
     * @param fullName    full name to match (optional)
     * @param phoneNumber phone number to match (optional)
     * @return list of matching customers
     */
    public List<Customer> searchCustomers(String afm, String fullName, String phoneNumber) {

        List<Customer> results = new ArrayList<>();

        if (afm != null) afm = afm.trim();
        if (fullName != null) fullName = fullName.trim();
        if (phoneNumber != null) phoneNumber = phoneNumber.trim();

        for (Customer c : customers) {

            if (afm != null && !afm.isBlank()) {
                if (!c.getAfm().equalsIgnoreCase(afm)) continue;
            }

            if (fullName != null && !fullName.isBlank()) {
                if (!c.getFullName().equalsIgnoreCase(fullName)) continue;
            }

            if (phoneNumber != null && !phoneNumber.isBlank()) {
                if (!c.getPhoneNumber().equals(phoneNumber)) continue;
            }

            results.add(c);
        }

        return results;
    }
}

