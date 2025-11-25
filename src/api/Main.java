package api;

import api.models.Car;
import api.models.Customer;
import api.models.Employee;

public class Main {
    public static void main(String[] args) {
        Car c = new Car("1", "IKA1234", "Toyota", "Sedan", "Corolla", 2019, "Ασημί", true);
        System.out.println(c);
        Customer a = new Customer("163944459", "Alexandros Gkourdoglou", "6988702329", "alekszhs@gmail.com");
        System.out.println(a);
        Employee e = new Employee("Markos Aourelios", "alekszhs", "alexio_komo@hotmail.com", "01091997");
        System.out.println(e);
        System.out.println(e.loginMatch(e.getUsername(), e.getPassword()));
    }
}

