package api.models;

/**
 * This class Represents the Employee that is working for the company
 * @author Alexandros Gkourdoglou AM5013
 * @author /--/ XXXX(fill it later)
 */
public class Employee {
    private String fullName; // fullName of the employee
    private String username; // unique username for login
    private String email; // e-mail address of the Employee
    private String password; // unique password for login

    /**
     * Create an Employee object
     * @param fullName Name and Surname of the Employee
     * @param username Unique Employee's username for login
     * @param email email of the Employee
     * @param password Unique Employee's password for login
     */
    public Employee(String fullName, String username, String email, String password){
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    //Getters
    public String getFullName(){
        return fullName;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    // Setters with validation
    public void setFullName(String fullName) {
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Full name cannot be empty");
        this.fullName = fullName.trim();
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username cannot be empty");
        this.username = username.trim();
    }

    public void setEmail(String email) {
        if (email == null)
            throw new IllegalArgumentException("Email cannot be null");

        email = email.trim();

        if (!email.contains("@") || !email.substring(email.indexOf("@")).contains("."))
            throw new IllegalArgumentException("Invalid email format");

        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null)
            throw new IllegalArgumentException("Password cannot be null");
        this.password = password.trim();
    }

    //equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee e = (Employee) o;
        return username.equals(e.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }


    /**
     * Returns a simple readable representation of the employee.
     */
    @Override
    public String toString() {
        return fullName + " (" + username + ") - " + email;
    }

    /**
     * Checks if the given username and password match this employee's credentials.
     *
     * @param username the username to check
     * @param password the password to check
     * @return true if credentials match, false otherwise
     */
    public boolean loginMatch(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
}
