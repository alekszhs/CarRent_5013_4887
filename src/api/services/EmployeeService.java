package api.services;

import api.models.Employee;
import java.util.List;
import java.util.ArrayList;

public class EmployeeService {
    public final List<Employee> employees = new ArrayList<>();

    /**
     * Finds an employee by their username.
     * <p>
     * This method validates the input username and searches through the internal
     * employee list for a matching username. Usernames are compared in a
     * case-insensitive manner.
     * </p>
     *
     * @param username the username of the employee to search for.
     * @return the Employee object if found, otherwise null.
     * @throws IllegalArgumentException if the provided username is null or blank.
     */
    public Employee findByUsername(String username){
        if (username == null || username.isBlank()){
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        username = username.trim();

        for (Employee employee : employees){
            if (employee.getUsername().equalsIgnoreCase(username)){
                return employee;
            }
        }
        return null;
    }

    /**
     * Finds an employee by their email.
     * <p>
     * This method validates the input email and searches the internal
     * employee list for a matching email. Comparison is case-insensitive.
     * </p>
     *
     * @param email the email of the employee to search for.
     * @return the Employee object if found, otherwise null.
     * @throws IllegalArgumentException if the provided email is null or blank.
     */
    public Employee findByEmail(String email){
        if (email == null || email.isBlank()){
            throw new IllegalArgumentException("email cannot be empty or null.");
        }

        email = email.trim();

        for (Employee employee : employees){
            if(employee.getEmail().equalsIgnoreCase(email)){
                return employee;
            }
        }
        return null;
    }

    /**
     * Adds a new employee to the system.
     * Ensures all fields are valid and username/email are unique.
     *
     * @param emp the employee to add
     * @throws IllegalArgumentException if validation fails
     */
    public void addEmployee(Employee emp) {

        // Null object check
        if (emp == null) {
            throw new IllegalArgumentException("Employee cannot be null.");
        }

        // Validate email
        if (emp.getEmail() == null || emp.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty.");
        }
        String email = emp.getEmail().trim();
        if (findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // Validate username
        if (emp.getUsername() == null || emp.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        String username = emp.getUsername().trim();
        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }

        // Validate full name
        if (emp.getFullName() == null || emp.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or empty.");
        }

        // Validate password (no uniqueness needed)
        if (emp.getPassword() == null || emp.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        // If everything is valid → add employee
        employees.add(emp);
    }

    /**
     * Validates employee login credentials.
     * <p>
     * This method checks that both username and password are valid,
     * finds the employee by username, and verifies that the stored
     * password matches exactly.
     * </p>
     *
     * @param username the username provided by the user
     * @param password the password provided by the user
     * @return true if login is successful, false otherwise
     * @throws IllegalArgumentException if username or password are null/blank
     */
    public boolean validateLogin(String username, String password) {

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }

        username = username.trim();
        password = password.trim();

        // Find employee once (efficient)
        Employee emp = findByUsername(username);
        if (emp == null) {
            return false; // no such user
        }

        // Correct password
        return emp.getPassword().equals(password);
    }

    /**
     * Deletes an employee from the list.
     *
     * @param emp The employee object to delete. Cannot be null.
     * @throws IllegalArgumentException If the provided employee is null.
     * @throws IllegalStateException    If the employee does not exist in the list.
     */
    public void deleteEmployee(Employee emp) {
        if (emp == null) {
            // Validation: you cannot delete a null employee reference
            throw new IllegalArgumentException("Employee cannot be null.");
        }

        // remove(emp) returns true if the employee existed and was removed
        boolean removed = employees.remove(emp);

        if (!removed) {
            // Removal failed → the employee was not found in the list
            throw new IllegalStateException("Employee not found in list.");
        }
    }

    /**
     * Returns a full list of all registered employees.
     *
     * @return A List containing all Employee objects.
     */
    public List<Employee> getAllEmployees() {
        return employees; // Direct reference (mutable list)
    }
}
