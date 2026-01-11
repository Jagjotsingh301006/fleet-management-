package Vehicles;
import Exceptions.InvalidOperationException;
public abstract class Vehicle implements Comparable<Vehicle> {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMileage;
    public Vehicle(String id, String model, double maxSpeed) throws InvalidOperationException {
        if (id == null || id.isEmpty()) {
            throw new InvalidOperationException("Vehicle ID cannot be empty");
        }
        this.id = id;
        this.model = model;
        this.maxSpeed = maxSpeed;
        this.currentMileage = 0.0;
    }
    public abstract void move(double distance) throws InvalidOperationException;
    public abstract double calculateFuelEfficiency();
    public abstract double estimateJourneyTime(double distance);
    public void displayInfo() {
        System.out.println("ID: " + id + ", Model: " + model +
                           ", Max Speed: " + maxSpeed +
                           ", Mileage: " + currentMileage);
    }
    public double getCurrentMileage() {
        return currentMileage;
    }
    public String getId() {
        return id;
    }
    public double getMaxSpeed() {
        return maxSpeed;
    }
    public String getModel() {
        return model;
    }
    public void addMileage(double distance) {
        this.currentMileage += distance;
    }
    @Override
    public int compareTo(Vehicle other) {
        return Double.compare(this.calculateFuelEfficiency(), other.calculateFuelEfficiency());
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " [ID=" + id +
               ", Model=" + model +
               ", MaxSpeed=" + maxSpeed +
               ", Mileage=" + currentMileage +
               ", FuelEfficiency=" + calculateFuelEfficiency() + "]";
    }
}