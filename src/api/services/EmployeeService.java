package api.services;

import api.models.Employee;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides business logic for managing employees in the rental system.
 * <p>
 * This service handles operations such as adding new employees, validating
 * login credentials, searching by username or email, and removing employees.
 * It enforces data integrity rules such as unique usernames and emails, and
 * acts as the central authority for employee-related operations.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public class EmployeeService {

    // ==================== Fields ====================

    private final List<Employee> employees = new ArrayList<>();


    // ==================== Finders ====================

    /**
     * Finds an employee by their username (case-insensitive).
     *
     * @param username the username to search for
     * @return the matching employee, or null if not found
     * @throws IllegalArgumentException if username is null or blank
     */
    public Employee findByUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }

        username = username.trim();

        for (Employee employee : employees) {
            if (employee.getUsername().equalsIgnoreCase(username)) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Finds an employee by their email (case-insensitive).
     *
     * @param email the email to search for
     * @return the matching employee, or null if not found
     * @throws IllegalArgumentException if email is null or blank
     */
    public Employee findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty or null.");
        }

        email = email.trim();

        for (Employee employee : employees) {
            if (employee.getEmail().equalsIgnoreCase(email)) {
                return employee;
            }
        }
        return null;
    }


    // ==================== Add Employee ====================

    /**
     * Adds a new employee to the system.
     * Ensures that username and email are unique and all fields are valid.
     *
     * @param emp the employee to add
     * @throws IllegalArgumentException if validation fails
     */
    public void addEmployee(Employee emp) {

        if (emp == null)
            throw new IllegalArgumentException("Employee cannot be null.");

        // Validate email
        if (emp.getEmail() == null || emp.getEmail().isBlank())
            throw new IllegalArgumentException("Email cannot be null or empty.");

        String email = emp.getEmail().trim();
        if (findByEmail(email) != null)
            throw new IllegalArgumentException("Email already exists!");

        // Validate username
        if (emp.getUsername() == null || emp.getUsername().isBlank())
            throw new IllegalArgumentException("Username cannot be null or empty.");

        String username = emp.getUsername().trim();
        if (findByUsername(username) != null)
            throw new IllegalArgumentException("Username already exists!");

        // Validate full name
        if (emp.getFullName() == null || emp.getFullName().isBlank())
            throw new IllegalArgumentException("Full name cannot be null or empty.");

        // Validate password
        if (emp.getPassword() == null || emp.getPassword().isBlank())
            throw new IllegalArgumentException("Password cannot be null or empty.");

        employees.add(emp);
    }


    // ==================== Login ====================

    /**
     * Validates employee login credentials.
     *
     * @param username the username provided
     * @param password the password provided
     * @return true if login is successful, false otherwise
     * @throws IllegalArgumentException if username or password are null/blank
     */
    public boolean validateLogin(String username, String password) {

        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be null or empty.");

        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be null or empty.");

        username = username.trim();
        password = password.trim();

        Employee emp = findByUsername(username);
        if (emp == null)
            return false;

        return emp.getPassword().equals(password);
    }


    // ==================== Delete Employee ====================

    /**
     * Deletes an employee from the system.
     *
     * @param emp the employee to delete
     * @throws IllegalArgumentException if emp is null
     * @throws IllegalStateException if the employee does not exist
     */
    public void deleteEmployee(Employee emp) {
        if (emp == null)
            throw new IllegalArgumentException("Employee cannot be null.");

        boolean removed = employees.remove(emp);

        if (!removed)
            throw new IllegalStateException("Employee not found in list.");
    }


    // ==================== Getters ====================

    /**
     * Returns all registered employees.
     *
     * @return a copy of the employee list
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
}
