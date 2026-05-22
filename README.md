# Car Rental Management System 🚗

[![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Architecture](https://img.shields.io/badge/Architecture-Layered%20%2F%20Separation%20of%20Concerns-blue)](https://en.wikipedia.org/wiki/Separation_of_concerns)
[![Academic Project](https://img.shields.io/badge/Course-Object--Oriented%20Programming-red)](https://www.csd.auth.gr/)

A production-ready Java desktop application tailored for car rental agencies. Built strictly on Object-Oriented Programming (OOP) principles, this system enables corporate employees to seamlessly manage vehicle fleets, register clients, track rentals, and handle real-time car returns with reliable local persistence.

Developed as a collaborative final project for the **Object-Oriented Programming** course (Academic Year 2025-2026) at the Department of Informatics, Aristotle University of Thessaloniki (AUTh).

---

## 🏗️ Architecture & Design Choices

The project enforces a strict **Layered Architecture (Separation of Concerns)** to ensure high maintainability and decoupled code.

```text
CarRent_5013_4887/
└── src/
    ├── api/    # Core Domain Models & Business Logic (Pure Java, No UI components)
    └── gui/    # Presentation Layer (Java Swing, Windows, Forms & Event Listeners)
