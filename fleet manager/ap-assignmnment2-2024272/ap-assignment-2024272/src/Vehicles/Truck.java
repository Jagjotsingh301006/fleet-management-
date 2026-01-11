package Vehicles;
import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;
import Interfaces.CargoCarrier;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;
public class Truck extends LandVehicle implements FuelConsumable, CargoCarrier, Maintainable {
    private double fuelLevel;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;
    public Truck(String id, String model, double maxSpeed, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, numWheels);
        this.fuelLevel = 0.0;
        this.cargoCapacity = 5000.0; // kg
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
    }
    @Override
    public void move(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        double efficiency = calculateFuelEfficiency();
        double fuelNeeded = distance / efficiency;
        if (fuelLevel < fuelNeeded) {
            System.out.println("Not enough fuel! Truck cannot move.");
            return;
        }
        fuelLevel -= fuelNeeded;
        addMileage(distance);
        System.out.println("Hauling cargo for " + distance + " km. Fuel used: " + fuelNeeded);
    }
    @Override
    public double calculateFuelEfficiency() {
        double efficiency = 8.0; // base 8 km/l
        if (currentCargo > (cargoCapacity / 2)) {
            efficiency *= 0.9; // reduce by 10% if > 50% loaded
        }
        return efficiency;
    }
    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive");
        }
        fuelLevel += amount;
        System.out.println("Truck refueled: " + amount + " liters. Current fuel: " + fuelLevel);
    }
    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelNeeded > fuelLevel) {
            throw new InsufficientFuelException("Not enough fuel to cover the distance");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }
    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (currentCargo + weight > cargoCapacity) {
            throw new OverloadException("Cargo overload! Capacity is " + cargoCapacity + " kg");
        }
        currentCargo += weight;
        System.out.println("Loaded " + weight + " kg. Current cargo: " + currentCargo);
    }
    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) {
            throw new InvalidOperationException("Cannot unload more than current cargo");
        }
        currentCargo -= weight;
        System.out.println("Unloaded " + weight + " kg. Current cargo: " + currentCargo);
    }
    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }
    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }
    @Override
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
        System.out.println("Truck scheduled for maintenance.");
    }
    @Override
    public boolean needsMaintenance() {
        return getCurrentMileage() > 10000 || maintenanceNeeded;
    }
    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        System.out.println("Truck maintenance performed. Ready to go!");
    }
}