package api.models;

/**
 * This class represents a person who can rent cars from the company
 * Each customer has a unique afm, full name, phone number, email adress.
 * @author Alexandros Gkourdoglou AM5013
 * @author /--/ XXXX(fill it later)
 */
public class Customer {
    private String afm; // Unique tax identification number
    private String fullName; // Name and surname
    private String phoneNumber; // 10 digits number
    private String email; // email address  example@gmail.com

    /**
     * Constructs a Customer object
     * @param afm unique tax number per Customer
     * @param fullName name and surname of the Customer
     * @param phoneNumber Phone number of the Customer
     * @param email email of the Customer
     */
    public Customer(String afm, String fullName, String phoneNumber, String email){
        this.afm = afm;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //Getters
    public String getAfm(){
        return afm;
    }

    public String getFullName(){
        return fullName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmail(){
        return email;
    }

    //Setters
    public void setAfm(String afm) {
        if (afm == null)
            throw new IllegalArgumentException("AFM cannot be null");

        afm = afm.trim();

        if (!afm.matches("\\d{9}"))
            throw new IllegalArgumentException("Invalid AFM format");

        this.afm = afm;
    }


    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null)
            throw new IllegalArgumentException("Phone number cannot be null");

        phoneNumber = phoneNumber.trim();

        if (!phoneNumber.matches("\\d{10}"))
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");

        this.phoneNumber = phoneNumber;
    }


    public void setEmail(String email) {
        if (email == null)
            throw new IllegalArgumentException("Email cannot be null");

        email = email.trim();

        if (!email.contains("@") || !email.substring(email.indexOf("@")).contains("."))
            throw new IllegalArgumentException("Invalid email format");

        this.email = email;
    }


    //equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer c = (Customer) o;
        return afm.equals(c.afm);
    }

    @Override
    public int hashCode() {
        return afm.hashCode();
    }


    /**
     * Returns a readable text representation of the customer.
     */
    @Override
    public String toString() {
        return fullName + " (" + afm + ") - " + phoneNumber + " / " + email;
    }
}