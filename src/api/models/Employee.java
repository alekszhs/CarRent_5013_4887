package api.models;

/**
 * Represents an employee of the rental company.
 * <p>
 * Employees are the only users allowed to log into the system. Each employee
 * has a unique username and email address, and the class provides full
 * validation through setter methods. A utility method is also included for
 * matching login credentials.
 * </p>
 *
 * @author Αλέξανδρος Γκούρδογλου
 * @author Θεμιστοκλής Κιουτσούκης
 */
public class Employee {

    // ==================== Fields ====================

    private String fullName;  // Employee's full name
    private String username;  // Unique username (used for login)
    private String email;     // Unique email address
    private String password;  // Password for login (stored as plain text in this assignment)


    // ==================== Constructor ====================

    /**
     * Creates an Employee object with the required fields.
     *
     * @param fullName employee's name and surname
     * @param username unique username (used for login)
     * @param email    unique email address
     * @param password employee's password
     *
     * @throws IllegalArgumentException if any field is invalid
     */
    public Employee(String fullName, String username, String email, String password) {
        this.setFullName(fullName);
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }


    // ==================== Getters ====================

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPassword() { return password; }


    // ==================== Setters with Validation ====================

    /**
     * Sets the employee's full name.
     *
     * @param fullName the full name to assign
     * @throws IllegalArgumentException if the name is null or blank
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Full name cannot be empty.");
        this.fullName = fullName.trim();
    }

    /**
     * Sets the employee's username.
     *
     * @param username the username to assign
     * @throws IllegalArgumentException if the username is null or blank
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty.");
        this.username = username.trim();
    }

    /**
     * Sets the employee's email, applying strict validation.
     *
     * @param email the email to assign
     * @throws IllegalArgumentException if the email is null, blank, or invalid
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email cannot be empty.");

        email = email.trim();

        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Invalid email format.");

        this.email = email;
    }

    /**
     * Sets the employee's password.
     * Does NOT trim because spaces may be intentional.
     *
     * @param password the password to assign
     * @throws IllegalArgumentException if the password is null or blank
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty.");
        this.password = password;
    }


    // ==================== Equals & HashCode ====================

    /**
     * Two employees are considered equal if they share the same username.
     * Usernames are compared case-insensitively.
     *
     * @param o the object to compare
     * @return true if usernames match, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee e = (Employee) o;
        return username.equalsIgnoreCase(e.username);
    }

    /**
     * Hashcode is based on lowercase username to remain consistent with equals().
     *
     * @return hashcode of the username
     */
    @Override
    public int hashCode() {
        return username == null ? 0 : username.toLowerCase().hashCode();
    }


    // ==================== ToString ====================

    /**
     * Returns a readable representation of the employee.
     *
     * @return formatted employee information
     */
    @Override
    public String toString() {
        return fullName + " (" + username + ") - " + email;
    }


    // ==================== Utility Methods ====================

    /**
     * Checks if the given username and password match the employee's credentials.
     *
     * @param username the username to check (case-insensitive)
     * @param password the password to check (case-sensitive)
     * @return true if both match, false otherwise
     */
    public boolean loginMatch(String username, String password) {
        return this.username.equalsIgnoreCase(username) &&
                this.password.equals(password);
    }
}