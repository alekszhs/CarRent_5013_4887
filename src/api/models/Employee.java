package api.models;

/**
 * Represents an Employee of the rental company.
 * Employees are the only users allowed to log into the system.
 * Each employee has a unique username and a unique email.
 * This class includes full validation in setters
 * and provides a convenience method for login matching.
 * Author: Alexandros Gkourdoglou AM5013
 * Author: /--/ XXXX (fill later)
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
     * @param fullName Employee's name and surname
     * @param username Unique username (used for login)
     * @param email Unique email address
     * @param password Employee's password
     */
    public Employee(String fullName, String username, String email, String password) {
        this.setFullName(fullName);
        this.setUsername(username);
        this.setEmail(email);
        this.setPassword(password);
    }


    // ==================== Getters ====================

    /** @return the employee's full name */
    public String getFullName() { return fullName; }

    /** @return the employee's username */
    public String getUsername() { return username; }

    /** @return the employee's email */
    public String getEmail() { return email; }

    /** @return the employee's password */
    public String getPassword() { return password; }


    // ==================== Setters with Validation ====================

    /**
     * Sets the employee's full name.
     *
     * @param fullName the full name to set
     * @throws IllegalArgumentException if the full name is null or blank
     */
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Full name cannot be empty.");
        this.fullName = fullName.trim();
    }

    /**
     * Sets the employee's username.
     *
     * @param username the username to set
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
     * @param email the email to set
     * @throws IllegalArgumentException if email is null, blank, or invalid format
     */
    public void setEmail(String email) {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email cannot be empty.");

        email = email.trim();

        // Email Validation
        //^ --> start of string
        //[^@\\s] ---> No @ , No space , At least one character
        //+@ --> @ after the string
        //[^@\\s] ---> No @ , No space , At least one character
        //+\\. a real dot
        //[^@\\s] ---> No @ , No space , At least one character
        //+$ --> end of string
        //Example : alekszhs@gmail.com will pass    alek szhs@gmail.com will fail(space)
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))
            throw new IllegalArgumentException("Invalid email format.");

        this.email = email;
    }

    /**
     * Sets the employee's password.
     * Does NOT trim because spaces may be intentional.
     *
     * @param password the password to set
     * @throws IllegalArgumentException if password is null or blank
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Password cannot be empty.");
        this.password = password;
    }


    // ==================== Equals & HashCode ====================

    /**
     * Two employees are considered equal if they share the same username.
     * Usernames are case-insensitive.
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
     */
    @Override
    public int hashCode() {
        return username == null ? 0 : username.toLowerCase().hashCode();
    }


    // ==================== ToString ====================

    /**
     * Returns a simple readable representation of the employee.
     */
    @Override
    public String toString() {
        return fullName + " (" + username + ") - " + email;
    }

    // ==================== Utility Methods ====================

    /**
     * Checks if given username & password match the employee's credentials.
     *
     * @param username the username to check (case-insensitive)
     * @param password the password to check (case-sensitive)
     * @return true if matches, false otherwise
     */
    public boolean loginMatch(String username, String password) {
        return this.username.equalsIgnoreCase(username) &&
                this.password.equals(password);
    }
}

