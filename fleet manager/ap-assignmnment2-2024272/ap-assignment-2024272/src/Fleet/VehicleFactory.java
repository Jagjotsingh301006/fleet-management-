package Fleet;
import Exceptions.InvalidOperationException;
import Vehicles.*;
public class VehicleFactory {
    public static Vehicle createVehicle(String type, String[] data) throws InvalidOperationException {
        switch (type) {
            case "Car":
                // Car(String id, String model, double maxSpeed, int fuelTankCapacity)
                int carTank = 50;
                if (data.length > 3 && !data[3].isEmpty()) {
                    try { carTank = Integer.parseInt(data[3]); } catch (NumberFormatException ignored) {}
                }
                return new Car(data[0], data[1], Double.parseDouble(data[2]), carTank);
            case "Truck":
                int truckCap = 1000;
                if (data.length > 3 && !data[3].isEmpty()) {
                    try { truckCap = Integer.parseInt(data[3]); } catch (NumberFormatException ignored) {}
                }
                return new Truck(data[0], data[1], Double.parseDouble(data[2]), truckCap);
            case "Bus":
                int seats = 40;
                if (data.length > 3 && !data[3].isEmpty()) {
                    try { seats = Integer.parseInt(data[3]); } catch (NumberFormatException ignored) {}
                }
                return new Bus(data[0], data[1], Double.parseDouble(data[2]), seats);
            case "Airplane":
                double altitude = 10000;
                if (data.length > 3 && !data[3].isEmpty()) {
                    try { altitude = Double.parseDouble(data[3]); } catch (NumberFormatException ignored) {}
                }
                return new Airplane(data[0], data[1], Double.parseDouble(data[2]), altitude);
            case "CargoShip":
                boolean hasSails = false;
                if (data.length > 3 && !data[3].isEmpty()) {
                    hasSails = Boolean.parseBoolean(data[3]);
                }
                return new CargoShip(data[0], data[1], Double.parseDouble(data[2]), hasSails);
            default:
                throw new InvalidOperationException("Unknown vehicle type: " + type);
        }
    }
}
