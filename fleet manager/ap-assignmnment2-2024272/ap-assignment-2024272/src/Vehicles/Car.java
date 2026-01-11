package Vehicles;
import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;
import Interfaces.PassengerCarrier;
public class Car extends LandVehicle implements FuelConsumable, PassengerCarrier, Maintainable {
    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private boolean maintenanceNeeded;
    private double lastServiceMileage;
    public Car(String id, String model, double maxSpeed, int numWheels) throws InvalidOperationException {
        super(id, model, maxSpeed, numWheels);
        this.fuelLevel = 0.0;
        this.passengerCapacity = 5;
        this.currentPassengers = 0;
        this.maintenanceNeeded = false;
        this.lastServiceMileage = getCurrentMileage(); //  initialize service mileage
    }
    @Override
    public void move(double distance) throws InvalidOperationException {
        if (distance < 0) {
            throw new InvalidOperationException("Distance cannot be negative");
        }
        double fuelNeeded = distance / calculateFuelEfficiency();
        double projectedMileage = getCurrentMileage() + distance;
        if (!maintenanceNeeded && (projectedMileage-lastServiceMileage) >= 10000.0) {
            maintenanceNeeded = true;
            System.out.println(getId() + " now requires maintenance (mileage: " + this.getCurrentMileage() + ")");
        }
        if (fuelLevel < fuelNeeded) {
            System.out.println("Not enough fuel! Car cannot move.");
            return;
        }
        fuelLevel -= fuelNeeded;
        addMileage(distance);
        System.out.println("Driving on road for " + distance + " km. Fuel used: " + fuelNeeded);
    }
    @Override
    public double calculateFuelEfficiency() {
        return 15.0; 
    }
    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) {
            throw new InvalidOperationException("Refuel amount must be positive");
        }
        fuelLevel += amount;
        System.out.println("Car refueled: " + amount + " liters. Current fuel: " + fuelLevel);
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
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
        System.out.println("Car scheduled for maintenance.");
    }
    @Override
    public boolean needsMaintenance() {
        return getCurrentMileage() > 10000 || maintenanceNeeded;
    } 
    @Override
    public void performMaintenance() {
        if (this.maintenanceNeeded) {
            this.maintenanceNeeded = false; //  reset
            System.out.println("Maintenance performed for Car " + getId());
        } else {
            System.out.println("Car " + getId() + " does not need maintenance.");
        }
    }
}