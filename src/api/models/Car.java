package api.models;
import api.models.CarStatus;

/**
 * @version 1.0
 * This class represent a car that can be rented.
 * Every car has a unique id,license plate, brand, type, model, year of construction, colour and status.
 * @author Alexandros Gkourdoglou AM5013
 * @author /--/ XXXX (I fill it later)
 */
public class Car {
    private String id;
    private String plate;
    private String brand;
    private String type;
    private String model;
    private int year;
    private String color;
    private CarStatus status;


    /**
     * Constructs a Car object
     * @param id unique id for every car
     * @param plate license plates
     * @param brand brand of the car
     * @param type type of the car
     * @param model model of the car
     * @param year year of the car's construction
     * @param color colour of the car
     * @param status if its rented or available
     */
    public Car(String id, String plate, String brand, String model, String type,
               int year, String color, CarStatus status) {

        //check valid year input.
        int currentYear = java.time.LocalDate.now().getYear();
        if (year < 1900 || year > currentYear) {
            throw new IllegalArgumentException("Invalid manufacturing year: " + year);
        }

        this.year = year;
        this.id = id;
        this.plate = plate;
        this.brand = brand;
        this.model = model;
        this.type = type;
        this.color = color;
        this.status = status;
    }


    //Getters
    public String getId(){
        return id;
    }

    public String getPlate(){
        return plate;
    }

    public String getBrand(){
        return brand;
    }

    public String getType(){
        return type;
    }

    public String getModel(){
        return model;
    }

    public int getYear(){
        return year;
    }

    public String getColor(){
        return color;
    }


    public CarStatus getStatus() {
        return status;
    }

    //Setters
    public void setId(String id){
        this.id = id;
    }


    public void setPlate(String plate){
        this.plate = plate;
    }


    public void setBrand(String brand){
        this.brand = brand;
    }


    public void setType(String type){
        this.type = type;
    }


    public void setModel(String model){
        this.model = model;
    }


    public void setYear(int year) {
        int currentYear = java.time.LocalDate.now().getYear();

        //check valid year input.
        if (year < 1900 || year > currentYear) {
            throw new IllegalArgumentException("Invalid manufacturing year: " + year);
        }

        this.year = year;
    }



    public void setColor(String color){
        this.color = color;
    }


    public void setStatus(CarStatus status) {
        this.status = status;
    }

    //equals and hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return id.equals(car.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    /**
     * Returns the credentials and infos for the car.
     */
    @Override
    public String toString() {
        return "[" + id + "] " + plate + " - " + brand + " " + model + " (" + color + ") "
                + (status == CarStatus.AVAILABLE ? "Διαθέσιμο" : "Ενοικιασμένο");

    }
}
