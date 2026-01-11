import Exceptions.*;
import Fleet.FleetManager;
import Interfaces.*;
import Vehicles.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        FleetManager manager = new FleetManager();
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n===== Fleet Management System =====");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Remove Vehicle");
            System.out.println("3. Start Journey");
            System.out.println("4. Refuel All Vehicles");
            System.out.println("5. Perform Maintenance");
            System.out.println("6. Generate Report");
            System.out.println("7. Save Fleet");
            System.out.println("8. Load Fleet");
            System.out.println("9. Search by Type");
            System.out.println("10. Sort Fleet by Efficiency");
            System.out.println("11. List Vehicles Needing Maintenance");
            System.out.println("12. Get Total Fuel Consumption");
            System.out.println("13. View Fastest and Slowest Vehicle");
            System.out.println("14. View Unique Models (HashSet)");
            System.out.println("15. View Sorted Models (TreeSet)");
            System.out.println("16. Sort Fleet by Speed");
            System.out.println("17. Sort Fleet by Model");
            System.out.println("18. Display Fleet Summary Report");
            System.out.println("19. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter vehicle type (Car/Truck/Bus/Airplane/CargoShip): ");
                        String vtype = sc.nextLine();
                        System.out.print("Enter ID: ");
                        String id = sc.nextLine();
                        System.out.print("Enter model: ");
                        String model = sc.nextLine();
                        System.out.print("Enter max speed: ");
                        double maxSpeed = sc.nextDouble();
                        sc.nextLine();
                        Vehicle v = null;
                        switch (vtype.toLowerCase()) {
                            case "car":
                                v = new Car(id, model, maxSpeed, 50);
                                break;
                            case "truck":
                                System.out.print("Enter cargo capacity: ");
                                int capacity = sc.nextInt();
                                sc.nextLine();
                                v = new Truck(id, model, maxSpeed, capacity);
                                break;
                            case "bus":
                                System.out.print("Enter passenger capacity: ");
                                int seats = sc.nextInt();
                                sc.nextLine();
                                v = new Bus(id, model, maxSpeed, seats);
                                break;
                            case "airplane":
                                System.out.print("Enter max altitude: ");
                                double altitude = sc.nextDouble();
                                sc.nextLine();
                                v = new Airplane(id, model, maxSpeed, altitude);
                                break;
                            case "cargoship":
                                System.out.print("Does it have sails? (true/false): ");
                                boolean hasSails = sc.nextBoolean();
                                sc.nextLine();
                                v = new CargoShip(id, model, maxSpeed, hasSails);
                                break;
                            default:
                                System.out.println("Invalid vehicle type.");
                                break;
                        }
                        if (v != null) {
                            manager.addVehicle(v);
                            System.out.println("Vehicle added successfully!");
                        }
                        break;
                    case 2:
                        System.out.print("Enter vehicle ID to remove: ");
                        String removeId = sc.nextLine();
                        manager.removeVehicle(removeId);
                        System.out.println("Vehicle removed (if existed).");
                        break;
                    case 3:
                        System.out.print("Enter Vehicle ID to start journey: ");
                        String journeyId = sc.nextLine();
                        Vehicle targetVehicle = null;
                        for (Vehicle vJ : manager.getFleet()) {
                            if (vJ.getId().equals(journeyId)) {
                                targetVehicle = vJ;
                                break;
                            }
                        }
                        if (targetVehicle == null) {
                            System.out.println("Error: Vehicle with ID " + journeyId + " not found.");
                            break;
                        }
                        try {
                            System.out.print("Enter distance: ");
                            double distance = sc.nextDouble();
                            sc.nextLine();
                            if (targetVehicle instanceof Bus) {
                                System.out.print("Enter number of passengers to board: ");
                                int p = sc.nextInt();
                                sc.nextLine();
                                ((Bus) targetVehicle).boardPassengers(p);
                            }
                            if (targetVehicle instanceof Truck) {
                                System.out.print("Enter cargo weight (kg): ");
                                double w = sc.nextDouble();
                                sc.nextLine();
                                ((Truck) targetVehicle).loadCargo(w);
                            }
                            if (targetVehicle instanceof CargoShip) {
                                System.out.print("Enter cargo weight (kg): ");
                                double w = sc.nextDouble();
                                sc.nextLine();
                                ((CargoShip) targetVehicle).loadCargo(w);
                            }
                            if (targetVehicle instanceof FuelConsumable) {
                                System.out.print("Enter fuel amount to refuel: ");
                                double f = sc.nextDouble();
                                sc.nextLine();
                                ((FuelConsumable) targetVehicle).refuel(f);
                            }
                            targetVehicle.move(distance);
                            System.out.println("Journey started for vehicle " + targetVehicle.getId());
                        } catch (OverloadException | InvalidOperationException e) {
                            System.out.println("Error: " + e.getMessage());
                        } catch (Exception e) {
                            System.out.println("Unexpected Error: " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Enter refuel amount: ");
                        double amount = sc.nextDouble();
                        System.out.println("Refueling all vehicles...");
                        manager.refuelAll(amount);
                        break;
                    case 5:
                        manager.maintainAll();
                        System.out.println("Maintenance performed where needed.");
                        break;
                    case 6:
                        System.out.println(manager.generateReport());
                        break;
                    case 7:
                        System.out.print("Enter filename to save to (or press Enter for default '" + FleetManager.DEFAULT_FILE + "'): ");
                        String saveName = sc.nextLine().trim();
                        if (saveName.isEmpty()) saveName = FleetManager.DEFAULT_FILE;
                        if (!saveName.endsWith(".csv")) saveName += ".csv";
                        if (!saveName.startsWith("data/")) saveName = "data/" + saveName;
                        try {
                            manager.saveToFile(saveName);
                            System.out.println("Fleet saved to " + saveName);
                        } catch (Exceptions.FileOperationException e) {
                            System.out.println("Save failed: " + e.getMessage());
                        }
                        break;
                    case 8:
                        System.out.print("Enter filename to load from (or press Enter for default '" + FleetManager.DEFAULT_FILE + "'): ");
                        String loadName = sc.nextLine().trim();
                        if (loadName.isEmpty()) loadName = FleetManager.DEFAULT_FILE;
                        if (!loadName.endsWith(".csv")) loadName += ".csv";
                        if (!loadName.startsWith("data/")) loadName = "data/" + loadName;
                        try {
                            manager.loadFromFile(loadName);
                            System.out.println("Fleet loaded from " + loadName);
                        } catch (Exceptions.FileOperationException e) {
                            System.out.println("Load failed: " + e.getMessage());
                        }
                        break;
                    case 9:
                        System.out.print("Enter type (Car/Truck/Bus/Airplane/CargoShip): ");
                        String type = sc.nextLine();
                        try {
                            Class<?> cls = Class.forName("Vehicles." + type);
                            for (Vehicle vType : manager.searchByType(cls)) {
                                System.out.println(vType);
                            }
                        } catch (ClassNotFoundException e) {
                            System.out.println("Invalid type.");
                        }
                        break;
                    case 10:
                        manager.sortFleetByEfficiency();
                        System.out.println("Fleet sorted by efficiency (highest first):");
                        for (Vehicle vEff : manager.getFleet()) {
                            System.out.println(vEff.getId() + " - " +
                                    vEff.getClass().getSimpleName() +
                                    " - Efficiency: " + vEff.calculateFuelEfficiency());
                        }
                        break;
                    case 11:
                        System.out.println("Vehicles needing maintenance:");
                        for (Vehicle vMain : manager.getVehiclesNeedingMaintenance()) {
                            System.out.println(vMain.getId() + " - " + vMain.getClass().getSimpleName());
                        }
                        break;
                    case 12:
                        System.out.print("Enter distance: ");
                        double dist = sc.nextDouble();
                        double totalFuel = manager.getTotalFuelConsumption(dist);
                        System.out.println("Total fuel consumption: " + totalFuel);
                        break;
                    case 13:
                        Vehicle fastest = manager.getFastestVehicle();
                        Vehicle slowest = manager.getSlowestVehicle();
                        if (fastest != null) {
                            System.out.println("Fastest: " + fastest.getId() + " - " +
                                    fastest.getClass().getSimpleName() +
                                    " (" + fastest.getMaxSpeed() + ")");
                        } else {
                            System.out.println("No vehicles.");
                        }
                        if (slowest != null) {
                            System.out.println("Slowest: " + slowest.getId() + " - " +
                                    slowest.getClass().getSimpleName() +
                                    " (" + slowest.getMaxSpeed() + ")");
                        }
                        break;
                    case 14:
                        System.out.println("Unique models:");
                        for (String m : manager.getUniqueModels()) {
                            System.out.println(" - " + m);
                        }
                        break;
                    case 15:
                        System.out.println("Sorted models:");
                        for (String m : manager.getSortedModels()) {
                            System.out.println(" - " + m);
                        }
                        break;
                    case 16:
                        manager.sortFleetBySpeed();
                        System.out.println("Fleet sorted by speed (highest first):");
                        for (Vehicle vS : manager.getFleet()) {
                            System.out.println(vS.getId() + " - " + vS.getClass().getSimpleName() + " - Speed: " + vS.getMaxSpeed());
                        }
                        break;
                    case 17:
                        manager.sortFleetByModel();
                        System.out.println("Fleet sorted by model (A-Z):");
                        for (Vehicle vM : manager.getFleet()) {
                            System.out.println(vM.getId() + " - " + vM.getClass().getSimpleName() + " - Model: " + vM.getModel());
                        }
                        break;
                    case 18:
                        System.out.println(manager.generateReport());
                        break;
                    case 19:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }
}