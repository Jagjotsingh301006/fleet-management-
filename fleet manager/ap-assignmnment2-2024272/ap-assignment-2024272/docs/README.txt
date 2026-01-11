
The goal is to design and implement a "
        Fleet Management System
that models a variety of transportation vehicles and supports their management through journeys, fuel consumption, maintenance, and persistence.




Features
- **Vehicle Hierarchy**
  - Car 
  - Truck 
  - Bus 
  - Airplane
  - CargoShip

- **Core Operations**
  - Add or remove vehicles.
  - Start journeys .
  - Refuel all vehicles.
  - Perform and track maintenance.
  - Generate fleet reports.
  - Search and sort by type or efficiency.

- **Maintenance**
  - Vehicles require maintenance if they exceed **10,000 km** mileage.
  - `Maintainable` interface ensures consistent maintenance behavior.

- **Persistence**
  - Save fleet to `data/Sample_fleet.csv`.
  - Load fleet from file and restore mileage/attributes.

- **Exception Handling**
  - `InvalidOperationException` → invalid IDs or actions.
  - `OverloadException` → exceeding passenger/cargo capacity.
  - `InsufficientFuelException` → when journeys exceed available fuel.



data/
└── Sample_fleet.csv # CSV storage for fleet
docs/
├── README.txt # Original notes
├── UML_DIAGRAM.png # UML class diagram
└── UML-Diagram.jpg
src/
├── Main.java # Entry point (menu system)
├── Fleet/
│ ├── FleetManager.java # Fleet management logic
│ └── VehicleFactory.java # Vehicle creation from CSV
├── Vehicles/
│ ├── Vehicle.java # Abstract base class
│ ├── LandVehicle.java
│ ├── AirVehicle.java
│ ├── WaterVehicle.java
│ ├── Car.java
│ ├── Truck.java
│ ├── Bus.java
│ ├── Airplane.java
│ └── CargoShip.java
├── Interfaces/
│ ├── FuelConsumable.java
│ ├── Maintainable.java
│ ├── CargoCarrier.java
│ └── PassengerCarrier.java
└── Exceptions/
├── InvalidOperationException.java
├── OverloadException.java
└── InsufficientFuelException.java



Menu options:

1. Add Vehicle
2. Remove Vehicle
3. Start Journey
4. Refuel All Vehicles
5. Perform Maintenance
6. Generate Report
7. Save Fleet
8. Load Fleet
9. Search by Type
10. Sort Fleet by Efficiency
11. List Vehicles Needing Maintenance
12. Get Total Fuel Consumption
13. Exit



Example CSV (Sample_fleet.csv)

Car,C1,Honda Civic,180,5,12000
Truck,T1,Volvo FH,140,8000,9500
Bus,B1,Mercedes Sprinter,120,30,15000

HOW I AM RUNNING---

In terminal :

1. Open Terminal in Project Root

Make sure you are in the root folder of your project (where src/ is located).

Example:

cd path/to/project
2. Compile

You need to tell javac (Java compiler) where your src is and compile everything.

Run:

javac -d out src/Main.java src/Fleet/*.java src/Vehicles/*.java src/Interfaces/*.java
3. Run

Now run your program with:

java -cp out Main