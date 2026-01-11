package Vehicles;
import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;
import Interfaces.CargoCarrier;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;
public class CargoShip extends WaterVehicle implements CargoCarrier, Maintainable, FuelConsumable {
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;
    private double fuelLevel;
    public CargoShip(String id, String model, double maxSpeed, boolean hasSail) throws InvalidOperationException {
        super(id, model, maxSpeed, hasSail);
        this.cargoCapacity = 50000.0; // kg
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
        this.fuelLevel = 0.0;
    }
    @Override
    public void move(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        if (!hasSail()) { 
            double fuelNeeded = distance / calculateFuelEfficiency();
            if (fuelLevel < fuelNeeded) {
                System.out.println("Not enough fuel! Cargo ship cannot move.");
                return;
            }

            fuelLevel -= fuelNeeded;
        }
        addMileage(distance);
        System.out.println("Sailing with cargo for " + distance + " km.");
    }
    @Override
    public double calculateFuelEfficiency() {
        if (hasSail()) {
            return 0.0;
        }
        return 4.0;
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
        System.out.println("Cargo ship scheduled for maintenance.");
    }
    @Override
    public boolean needsMaintenance() {
        return getCurrentMileage() > 10000 || maintenanceNeeded;
    }
    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        System.out.println("Cargo ship maintenance performed. Ready to go!");
    }
    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (hasSail()) {
            System.out.println("This cargo ship uses sails and does not need fuel.");
            return;
        }
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive");
        }
        fuelLevel += amount;
        System.out.println("Cargo ship refueled: " + amount + " liters. Current fuel: " + fuelLevel);
    }
    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }
    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        if (hasSail()) {
            return 0.0;
        }
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelNeeded > fuelLevel) {
            throw new InsufficientFuelException("Not enough fuel to cover the distance");
        }
        fuelLevel -= fuelNeeded;
        return fuelNeeded;
    }
}