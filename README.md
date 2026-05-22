# Car Rental Management System 🚗

A robust Java desktop application designed for car rental companies to manage their fleet, clients, and active rentals. This project was developed as a collaborative assignment for the **Object-Oriented Programming** course during the academic year 2025-2026.

## 🌟 Key Features

- **Secure Employee Authentication:** Login/Logout mechanism tailored strictly for company employees with shared access to the system's data.
- **Fleet Management:** Add, edit, and search for vehicles using combined criteria (Brand, Model, Plate, Color, Status).
- **Client Management:** Independent registration and modification of clients with unique Tax IDs (ΑΦΜ).
- **Rental & Return Workflow:** Streamlined process for checking car availability, calculating rental dates, and processing returns with instant status updates.
- **Persistent Data Storage:** Automatic state saving and loading using relative file paths (CSV/Text files) to ensure seamless data persistence across application restarts.
- **Comprehensive History Logs:** View historical and active rental data filtered by individual client or specific vehicle.

---

## 🏗️ Architecture & Design Principles

The application strictly adheres to the principle of **Separation of Concerns**, splitting the codebase into distinct packages to decouple core business logic from the user interface:

- `api`: Contains the backend logic, data processing, object models (`Car`, `Client`, `Rental`, `Employee`), and file I/O operations. **No UI components exist in this layer.**
- `gui`: Handles the Desktop Graphical User Interface (built with [Swing / JavaFX - συμπλήρωσε τι βάλατε]) and interacts exclusively with the `api` layer.

### Applied OOP Concepts:
- **Encapsulation:** Strict data hiding using private fields and public getters/setters across all model entities.
- **Inheritance & Polymorphism:** Utilized to manage varying entities and shared behaviors effectively.
- **Data Validation:** Bulletproof input validation with user-friendly error dialogs directly on the GUI for edge cases (e.g., duplicate Tax IDs, negative values, missing fields).

---

## 📂 Project Structure

```text
CarRent_AEM1_AEM2/
├── src/
│   ├── api/          # Business logic, file I/O, and core system entities
│   └── gui/          # UI components, forms, and event listeners
├── data/             # Persistent storage files (users.csv, vehicles_with_plates.csv)
└── README.md
