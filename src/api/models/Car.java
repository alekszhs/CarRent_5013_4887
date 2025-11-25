package api.models;

/**
 * Represents a car in the rental system.
 * <p>
 * Each car has a unique id and license plate, and includes full details such as
 * brand, model, type, construction year, color, and current status
 * (AVAILABLE, RENTED, etc.).
 * </p>
 *
 * @version 1.1
 * @author Alexandros Gkourdoglou
 * @author /--/
 */
public class Car {

    // ==================== Fields ====================

    private String id;          // Unique car ID
    private String plate;       // License plate (unique)
    private String brand;       // Manufacturer
    private String type;        // Type (Sedan, SUV...)
    private String model;       // Model (Corolla, Civic...)
    private int year;           // Construction year
    private String color;       // Color
    private CarStatus status;   // AVAILABLE / RENTED / etc.


    // ==================== Constructor ====================

    /**
     * Constructs a Car object with full details.
     *
     * @param id    unique car ID
     * @param plate license plate of the vehicle
     * @param brand car manufacturer
     * @param model model of the car
     * @param type  category/type (SUV, Sedan, etc.)
     * @param year  construction year (1900–current year)
     * @param color color of the car
     * @param status current availability/status of the car
     *
     * @throws IllegalArgumentException if the year is invalid
     */
    public Car(String id, String plate, String brand, String model,
               String type, int year, String color, CarStatus status) {

        // Validate year
        int currentYear = java.time.LocalDate.now().getYear();
        if (year < 1900 || year > currentYear) {
            throw new IllegalArgumentException("Invalid manufacturing year: " + year);
        }

        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.year = year;
        this.color = color;
        this.status = status;
    }


    // ==================== Getters ====================

    public String getId()    { return id; }
    public String getPlate() { return plate; }
    public String getBrand() { return brand; }
    public String getType()  { return type; }
    public String getModel() { return model; }
    public int getYear()     { return year; }
    public String getColor() { return color; }
    public CarStatus getStatus() { return status; }


    // ==================== Setters with Validation ====================

    public void setId(String id) {
        this.id = id;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Sets the construction year.
     *
     * @param year manufacturing year (must be 1900–current year)
     */
    public void setYear(int year) {
        int currentYear = java.time.LocalDate.now().getYear();
        if (year < 1900 || year > currentYear) {
            throw new IllegalArgumentException("Invalid manufacturing year: " + year);
        }
        this.year = year;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }


    // ==================== Equals & HashCode ====================

    /**
     * Two cars are considered equal if they have the same unique ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return id.equals(car.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }


    // ==================== ToString ====================

    /**
     * Returns a readable string with all car information.
     */
    @Override
    public String toString() {
        return "[" + id + "] " +
                plate + " - " +
                brand + " " +
                model + " (" + color + ") - " +
                (status == CarStatus.AVAILABLE ? "Διαθέσιμο" : "Ενοικιασμένο");
    }
}
