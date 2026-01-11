package Fleet;
import Exceptions.FileOperationException;
import Exceptions.InvalidOperationException;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;
import Vehicles.Vehicle;
import java.io.*;
import java.util.*;
public class FleetManager {
    private List<Vehicle> fleet;
    private Set<String> uniqueModels;
    private Set<String> sortedModels;
    public static final String DEFAULT_FILE = "data/fleetdata.csv";
    public FleetManager() {
        this.fleet = new ArrayList<>();
        this.uniqueModels = new HashSet<>();
        this.sortedModels = new TreeSet<>();
    }
    public void addVehicle(Vehicle v) throws InvalidOperationException {
        for (Vehicle existing : fleet) {
            if (existing.getId().equals(v.getId())) {
                throw new InvalidOperationException("Duplicate ID: " + v.getId());
            }
        }
        fleet.add(v);
        if (v.getModel() != null) {
            uniqueModels.add(v.getModel());
            sortedModels.add(v.getModel());
        }
    }
    public void removeVehicle(String id) throws InvalidOperationException {
        boolean removed = fleet.removeIf(v -> v.getId().equals(id));
        if (!removed)
            throw new InvalidOperationException("Vehicle with ID " + id + " not found.");
        rebuildModelSets();
    }
    private void rebuildModelSets() {
        uniqueModels.clear();
        sortedModels.clear();
        for (Vehicle v : fleet) {
            if (v.getModel() != null) {
                uniqueModels.add(v.getModel());
                sortedModels.add(v.getModel());
            }
        }
    }
    public void startAllJourneys(double distance) {
        for (Vehicle v : fleet) {
            try {
                v.move(distance);
            } catch (Exception e) {
                System.out.println("Error moving " + v.getId() + ": " + e.getMessage());
            }
        }
    }
    public double getTotalFuelConsumption(double distance) {
        double total = 0;
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                try {
                    total += ((FuelConsumable) v).consumeFuel(distance);
                } catch (Exception e) {
                    System.out.println("Fuel error for " + v.getId() + ": " + e.getMessage());
                }
            }
        }
        return total;
    }
    public void refuelAll(double amount) {
        for (Vehicle v : fleet) {
            if (v instanceof FuelConsumable) {
                try {
                    ((FuelConsumable) v).refuel(amount);
                } catch (Exception e) {
                    System.out.println("Error refueling " + v.getId() + ": " + e.getMessage());
                }
            }
        }
    }
    public void maintainAll() {
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable) {
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) {
                    m.performMaintenance();
                }
            }
        }
    }
    public List<Vehicle> getVehiclesNeedingMaintenance() {
        List<Vehicle> needing = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable && ((Maintainable) v).needsMaintenance()) {
                needing.add(v);
            }
        }
        return needing;
    }
    public List<Vehicle> searchByType(Class<?> type) {
        List<Vehicle> results = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (type.isInstance(v)) {
                results.add(v);
            }
        }
        return results;
    }
    public void sortFleetByEfficiency() {
        fleet.sort(Comparator.comparingDouble(Vehicle::calculateFuelEfficiency).reversed());
    }
    public void sortFleetBySpeed() {
        fleet.sort(Comparator.comparingDouble(Vehicle::getMaxSpeed).reversed());
    }
    public void sortFleetByModel() {
        fleet.sort(Comparator.comparing(Vehicle::getModel, Comparator.nullsLast(String::compareTo)));
    }
    public Vehicle getFastestVehicle() {
        if (fleet.isEmpty()) return null;
        return Collections.max(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }
    public Vehicle getSlowestVehicle() {
        if (fleet.isEmpty()) return null;
        return Collections.min(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Fleet Report ===\n");
        sb.append("Total Vehicles: ").append(fleet.size()).append("\n");
        Map<String, Integer> typeCounts = new HashMap<>();
        double totalMileage = 0;
        double totalEfficiency = 0;
        int countEfficiency = 0;
        for (Vehicle v : fleet) {
            String type = v.getClass().getSimpleName();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
            totalMileage += v.getCurrentMileage();
            double eff = v.calculateFuelEfficiency();
            if (eff > 0) {
                totalEfficiency += eff;
                countEfficiency++;
            }
        }
        for (String type : typeCounts.keySet()) {
            sb.append(type).append(": ").append(typeCounts.get(type)).append("\n");
        }
        sb.append("Total mileage: ").append(totalMileage).append(" km\n");
        if (countEfficiency > 0) {
            sb.append("Average efficiency: ").append(totalEfficiency / countEfficiency).append(" km/l\n");
        }
        sb.append("Maintenance needed: ").append(getVehiclesNeedingMaintenance().size()).append(" Vehicles\n");
        return sb.toString();
    }
    public List<Vehicle> getFleet() {
        return Collections.unmodifiableList(this.fleet);
    }
    public Set<String> getUniqueModels() {
        return Collections.unmodifiableSet(this.uniqueModels);
    }
    public Set<String> getSortedModels() {
        return Collections.unmodifiableSet(this.sortedModels);
    }
    public void saveToFile(String filename) throws FileOperationException {
        File f = new File(filename);
        File parent = f.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new FileOperationException("Unable to create directory: " + parent.getAbsolutePath());
            }
        }
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            pw.println("type,id,model,maxSpeed,efficiency,fuelLevel,currentPassengers,maintenanceNeeded,currentMileage");
            for (Vehicle v : fleet) {
                String type = v.getClass().getSimpleName();
                String modelRaw = v.getModel() == null ? "" : v.getModel();
                String model = "\"" + modelRaw.replace("\"", "\"\"") + "\"";
                String base = String.format("%s,%s,%s,%.2f,%.4f",
                        type, v.getId(), model, v.getMaxSpeed(), v.calculateFuelEfficiency());
                if (v instanceof Vehicles.Car) {
                    Vehicles.Car car = (Vehicles.Car) v;
                    pw.printf("%s,%.4f,%d,%b,%.4f%n",
                            base,
                            car.getFuelLevel(),
                            car.getCurrentPassengers(),
                            car.needsMaintenance(),
                            car.getCurrentMileage());
                } else {
                    double mileage = 0.0;
                    try { mileage = v.getCurrentMileage(); } catch (Exception ignored) {}
                    pw.printf("%s,,,,%.4f%n", base, mileage);
                }
            }
        } catch (IOException e) {
            throw new FileOperationException("Error saving fleet: " + e.getMessage(), e);
        }
    }
    public void loadFromFile(String filename) throws FileOperationException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileOperationException("File not found: " + filename);
        }
        System.out.println("=== FILE CONTENT START ===");
        try (BufferedReader preview = new BufferedReader(new FileReader(f))) {
            String linePreview;
            while ((linePreview = preview.readLine()) != null) {
                System.out.println(linePreview);
            }
        } catch (IOException e) {
            System.out.println("Could not preview file: " + e.getMessage());
        }
        System.out.println("=== FILE CONTENT END ===\n");
        int loaded = 0;
        int skipped = 0;
        List<String> errors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            this.fleet.clear();
            this.uniqueModels.clear();
            this.sortedModels.clear();
            br.mark(4096);
            String first = br.readLine();
            if (first == null) {
                System.out.println("Load: empty file.");
                return;
            }
            if (!first.toLowerCase().startsWith("type,")) {
                br.reset();
            }
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> partsList = new ArrayList<>();
                boolean inQuotes = false;
                StringBuilder cur = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '"') {
                        inQuotes = !inQuotes;
                        cur.append(c);
                    } else if (c == ',' && !inQuotes) {
                        partsList.add(cur.toString());
                        cur.setLength(0);
                    } else {
                        cur.append(c);
                    }
                }
                partsList.add(cur.toString());
                String[] parts = partsList.toArray(new String[0]);
                if (parts.length < 5) {
                    errors.add("Skipping malformed line (not enough columns): " + line);
                    skipped++;
                    continue;
                }
                try {
                    String type = parts[0].trim();
                    String id = parts[1].trim();
                    String modelField = parts[2].trim();
                    if (modelField.startsWith("\"") && modelField.endsWith("\"") && modelField.length() >= 2) {
                        modelField = modelField.substring(1, modelField.length() - 1).replace("\"\"", "\"");
                    }
                    double maxSpeed = 0.0;
                    try { maxSpeed = Double.parseDouble(parts[3].trim()); } catch (NumberFormatException ignored) {}
                    double efficiency = 0.0;
                    try { efficiency = Double.parseDouble(parts[4].trim()); } catch (NumberFormatException ignored) {}
                    Vehicle v = null;
                    if (type.equalsIgnoreCase("Car")) {
                        Vehicles.Car car = new Vehicles.Car(id, modelField, maxSpeed, 4);
                        if (parts.length >= 9) {
                            try {
                                String fuelStr = parts[5].trim();
                                String passengersStr = parts[6].trim();
                                String maintenanceStr = parts[7].trim();
                                String mileageStr = parts[8].trim();
                                double fuel = 0.0;
                                try { if (!fuelStr.isEmpty()) fuel = Double.parseDouble(fuelStr); } catch (NumberFormatException ignored) {}
                                int currPassengers = 0;
                                try { if (!passengersStr.isEmpty()) currPassengers = Integer.parseInt(passengersStr); } catch (NumberFormatException ignored) {}
                                boolean maintenance = false;
                                try { if (!maintenanceStr.isEmpty()) maintenance = Boolean.parseBoolean(maintenanceStr); } catch (Exception ignored) {}
                                double mileage = 0.0;
                                try { if (!mileageStr.isEmpty()) mileage = Double.parseDouble(mileageStr); } catch (NumberFormatException ignored) {}
                                if (fuel > 0) {
                                    try { car.refuel(fuel); } catch (Exception ignored) {}
                                }
                                int toBoard = Math.min(currPassengers, car.getPassengerCapacity());
                                for (int i = 0; i < toBoard; i++) {
                                    try { car.boardPassengers(1); } catch (Exception ignored) {}
                                }
                                if (!maintenance) {
                                    if (car.needsMaintenance()) car.performMaintenance();
                                } else {
                                    car.scheduleMaintenance();
                                }
                                double current = car.getCurrentMileage();
                                if (mileage > current) {
                                    try { car.addMileage(mileage - current); } catch (Exception ignored) {}
                                }
                                System.out.println("Loaded Car " + id + " (maintenanceNeeded=" + maintenance + ")");
                            } catch (Exception ignored) {}
                        }
                        v = car;
                    } else {
                        String[] data = new String[] { id, modelField, String.valueOf(maxSpeed), String.valueOf(efficiency) };
                        v = VehicleFactory.createVehicle(type, data);
                    }
                    if (v != null) {
                        try {
                            this.addVehicle(v);
                            loaded++;
                        } catch (InvalidOperationException ioe) {
                            errors.add("Skipping duplicate/invalid vehicle id " + id + " : " + ioe.getMessage());
                            skipped++;
                        }
                    } else {
                        errors.add("VehicleFactory returned null for type " + type + " (line: " + line + ")");
                        skipped++;
                    }
                } catch (Exception ex) {
                    errors.add("Error parsing line: " + line + " (" + ex.getMessage() + ")");
                    skipped++;
                }
            }
        } catch (IOException e) {
            throw new FileOperationException("Error loading fleet: " + e.getMessage(), e);
        }
        System.out.printf("Fleet load complete: %d loaded, %d skipped%n", loaded, skipped);
        if (!errors.isEmpty()) {
            System.out.println("Load errors:");
            errors.forEach(System.out::println);
        }
    }
}