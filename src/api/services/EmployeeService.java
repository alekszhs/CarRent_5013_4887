package api.services;

import api.models.Employee;
import java.util.List;
import java.util.ArrayList;

// findByUsername(String username)
// Επιστρέφει υπάλληλο βάσει username (unique). Χρήσιμο στο login και στο addEmployee.

// addEmployee(Employee emp)
// Προσθήκη νέου υπαλλήλου με ελέγχους:
// - Το αντικείμενο να μην είναι null
// - Το username να είναι μοναδικό
// - Το email να είναι μοναδικό
// - Το fullName να μην είναι κενό
// - Το password να μην είναι κενό

// validateLogin(String username, String password)
// Έλεγχος διαπιστευτηρίων. Αν είναι σωστά, επιστρέφει τον Employee.
// Αν όχι, επιστρέφει null.

// deleteEmployee(String username)
// Διαγραφή υπαλλήλου από το σύστημα. Ρίχνει εξαίρεση αν δεν υπάρχει.

// getAllEmployees()
// Επιστρέφει όλους τους υπαλλήλους.

// Σημείωση: Το username είναι μοναδικό για κάθε υπάλληλο.

public class EmployeeService {
    public final List<Employee> employees = new ArrayList<>();

    public Employee findByUsername(String username){
        if (username == null || username.isBlank()){
            throw new IllegalArgumentException("Username is invalid.");
        }
        username = username.trim();

        for (Employee employee : employees){

        }
    }

}
