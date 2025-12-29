# CarRent_5013_4887 — Car Rental Management System

## Description
A JavaFX desktop application for managing a car rental company: vehicles, customers, employees, and rental transactions.
Includes CRUD operations, rental workflow (create/return), history viewing, login/logout, and input validation.

---

## How to Run

### Requirements
- IntelliJ IDEA
- Java 17+
- JavaFX SDK (included in `javafx/`)

### Steps (IntelliJ)
1. Open IntelliJ IDEA → `File` → `Open` → select the project folder `CarRent_5013_XXXX`.
2. Go to `File` → `Project Structure` → `Libraries` and ensure JavaFX is linked from:
    - `javafx/lib`
3. Go to `Run` → `Edit Configurations...` and set **VM options** to:

```text
--module-path javafx/lib --add-modules javafx.controls,javafx.fxml
```

4. Run the project from `Main.java`.

---

## Project Structure

```text
CarRent_5013_XXXX/
├── docs/                          # Documentation & assignment files
│   └── Java Project 2025-2026_final.pdf
├── javafx/                        # JavaFX SDK (local dependency)
├── src/
│   ├── api/                       # Application core (business layer)
│   │   ├── models/                # Domain models (Car, Customer, Rental, etc.)
│   │   ├── services/              # Business logic & services
│   │   └── storage/               # CSV persistence & file handling
│   ├── gui/                       # JavaFX controllers
│   └── resources/
│       ├── data/                  # CSV seed / initial data files
│       └── gui/                   # FXML views
│
│   └── Main.java                  # Application entry point
│
├── .gitignore
└── README.md
```

> Note: folders like `.idea/`, `out/`, and `*.iml` are IDE/build-generated and should be ignored (not versioned).

---

## Features
- Add / Edit / Delete / Search Cars
- Add / Edit / Delete / Search Customers
- Add / Delete Employees
- Login / Logout (credentials)
- Create Rental with availability checks
- Return Rental with automatic status update
- Rental History view (by Customer or Car)
- Unique validation (AFM, Plate, Rental ID)
- GUI validation & error messages
- Dark theme UI (custom CSS)

---

## Initialization
On first run, the system loads:
- Default employees
- Two customers without rentals
- Sample vehicles

Initialization runs only once to preserve user changes.

---

## Notes
- Avoid using **absolute paths** in file access.
- If JavaFX jars are missing, re-link the `javafx/lib` folder in IntelliJ.
- Ensure VM options are set as shown above.

---

## Documentation
- Javadoc is provided for model and service classes.
- GUI classes are excluded from Javadoc (as per instructions).

---

## Team Contribution
- Αλέξανδρος Γκούρδογλου
- Θεμηστοκλής Κιουτσούκης
