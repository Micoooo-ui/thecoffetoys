## thecoffetoys

# Torque & Treads Dealership

A desktop-based Car Dealership Management System built with **Java** and **JavaFX** that streamlines vehicle inventory management, customer tracking, and sales/transaction reporting with real-time updates and filtering.

**Made by:**
* Mico Abrasaldo
* Ron Gabriel Della
* Daryl Yales
* Christian Mondega

---

##  Overview

**Torque & Treads Dealership** provides an intuitive, responsive dashboard for dealership staff to manage stock levels, log sales and reservations, and monitor live revenue metrics. Built following an MVC-inspired architecture, changes in inventory (adding, editing, deleting, or marking a vehicle as sold) automatically synchronize across transaction records and dashboard statistics.

---

##  Key Features

* **Live Dashboard & Analytics**
  * Real-time summary cards tracking **Total Vehicles**, **Available In Stock**, and **Sold / Reserved** units.
  * Instant search by brand, model, or variant name, combined with status filtering (`All`, `Available`, `Reserved`, `Sold`).
* **Full Vehicle Inventory CRUD**
  * **Create:** Add new vehicles with realistic Philippine Suggested Retail Prices (SRP) via a modal form.
  * **Read:** View formatted pricing (`₱#,##0.00`) and live status badges in an interactive `TableView`.
  * **Update:** Edit vehicle details or click **SOLD** to immediately mark a unit as sold and log the sale.
  * **Delete:** Remove vehicles from inventory with automatic cleanup of linked records.
* **Synchronized Sales & Transactions**
  * Automatic transaction logging whenever vehicles are added, edited, reserved, or sold.
  * Real-time calculation of **Total Revenue**, **Completed Sales**, and **Active Purchases / Reservations**.
  * Filterable and searchable transaction history (`Sale`, `Reservation`, `Inventory Addition`).
* **Object-Oriented Architecture**
  * Designed around the **4 Pillars of OOP** (Encapsulation, Inheritance, Polymorphism, and Abstraction) for clean, maintainable, and scalable code.

---

##  Tech Stack

* **Language:** Java (JDK 17+)
* **UI Framework:** JavaFX (FXML, Scene Builder)
* **Data Structures:** Java Collections (`ArrayList`, `ObservableList`, `FilteredList`)
* **IDE:** IntelliJ IDEA

---

##  How to Run

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/Micoooo-ui/thecoffetoys.git](https://github.com/Micoooo-ui/thecoffetoys.git)
   cd thecoffetoys
