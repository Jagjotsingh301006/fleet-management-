package Vehicles;
import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;
import Interfaces.CargoCarrier;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;
import Interfaces.PassengerCarrier;
public class Airplane extends AirVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {
    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;
    public Airplane(String id, String model, double maxSpeed, double maxAltitude) throws InvalidOperationException {
        super(id, model, maxSpeed, maxAltitude);
        this.fuelLevel = 0.0;
        this.passengerCapacity = 200;
        this.currentPassengers = 0;
        this.cargoCapacity = 10000.0; // kg
        this.currentCargo = 0.0;
        this.maintenanceNeeded = false;
    }
    @Override
    public void move(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        double fuelNeeded = distance / calculateFuelEfficiency();
        if (fuelLevel < fuelNeeded) {
            System.out.println("Not enough fuel! Airplane cannot move.");
            return;
        }
        fuelLevel -= fuelNeeded;
        addMileage(distance);
        System.out.println("Flying at altitude " + getMaxAltitude() +
                           " meters for " + distance + " km. Fuel used: " + fuelNeeded);
    }
    @Override
    public double calculateFuelEfficiency() {
        return 5.0;
    }
    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive");
        }
        fuelLevel += amount;
        System.out.println("Airplane refueled: " + amount + " liters. Current fuel: " + fuelLevel);
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
    public void boardPassengers(int count) throws OverloadException {
        if (currentPassengers + count > passengerCapacity) {
            throw new OverloadException("Too many passengers! Capacity is " + passengerCapacity);
        }
        currentPassengers += count;
        System.out.println(count + " passengers boarded. Now onboard: " + currentPassengers);
    }
    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count > currentPassengers) {
            throw new InvalidOperationException("Cannot disembark more passengers than onboard");
        }
        currentPassengers -= count;
        System.out.println(count + " passengers disembarked. Now onboard: " + currentPassengers);
    }
    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
    @Override
    public int getCurrentPassengers() {
        return currentPassengers;
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
        System.out.println("Airplane scheduled for maintenance.");
    }
    @Override
    public boolean needsMaintenance() {
        return getCurrentMileage() > 10000 || maintenanceNeeded;
    }
    @Override
    public void performMaintenance() {
        maintenanceNeeded = false;
        System.out.println("Airplane maintenance performed. Ready to go!");
    }
}