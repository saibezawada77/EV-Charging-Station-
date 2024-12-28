import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ChargingStation {
    private String name;
    private String location;
    private String chargerType; 
    private boolean isAvailable;

    public ChargingStation(String name, String location, String chargerType, boolean isAvailable) {
        this.name = name;
        this.location = location;
        this.chargerType = chargerType;
        this.isAvailable = isAvailable;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getChargerType() {
        return chargerType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

class UserPreference {
    private String preferredLocation;
    private String preferredChargerType;
    private boolean filterByAvailability;

    public UserPreference(String preferredLocation, String preferredChargerType, boolean filterByAvailability) {
        this.preferredLocation = preferredLocation;
        this.preferredChargerType = preferredChargerType;
        this.filterByAvailability = filterByAvailability;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public String getPreferredChargerType() {
        return preferredChargerType;
    }

    public boolean isFilterByAvailability() {
        return filterByAvailability;
    }
}
public class EVChargingSystem {
    private List<ChargingStation> chargingStations;
    private Scanner scanner;

    public EVChargingSystem() {
        chargingStations = new ArrayList<>();
        scanner = new Scanner(System.in);
        initializeStations(); // Initialize with some charging stations
    }

    private void initializeStations() {
        chargingStations.add(new ChargingStation("Station A", "Downtown", "Fast Charger", true));
        chargingStations.add(new ChargingStation("Station B", "Suburbs", "Regular Charger", true));
        chargingStations.add(new ChargingStation("Station C", "City Center", "Fast Charger", false));
        chargingStations.add(new ChargingStation("Station D", "Rural Area", "Regular Charger", true));
        chargingStations.add(new ChargingStation("Station E", "Downtown", "Regular Charger", true));
        chargingStations.add(new ChargingStation("Station F", "City Center", "Regular Charger", true));
    }
    public UserPreference getUserPreferences() {
        System.out.println("Select your preferred location:");
        String[] locations = {"1. Downtown", "2. Suburbs", "3. City Center", "4. Rural Area"};
        for (String loc : locations) {
            System.out.println(loc);
        }
        int locationChoice = Integer.parseInt(scanner.nextLine());
        String locationFilter = locations[locationChoice - 1].split(". ")[1];

        List<String> availableChargerTypes = new ArrayList<>();
        for (ChargingStation station : chargingStations) {
            if (station.getLocation().equalsIgnoreCase(locationFilter) && !availableChargerTypes.contains(station.getChargerType())) {
                availableChargerTypes.add(station.getChargerType());
            }
        }
        if (availableChargerTypes.isEmpty()) {
            System.out.println("No chargers are currently available for the selected location. Showing default options:");
            availableChargerTypes.add("Fast Charger");
            availableChargerTypes.add("Regular Charger");
        }
        System.out.println("Select your preferred charger type:");
        for (int i = 0; i < availableChargerTypes.size(); i++) {
            System.out.println((i + 1) + ". " + availableChargerTypes.get(i));
        }
        int typeChoice = Integer.parseInt(scanner.nextLine());
        String typeFilter = availableChargerTypes.get(typeChoice - 1);

        System.out.println("Do you want to filter by availability? (1. Yes / 2. No): ");
        int availabilityChoice = Integer.parseInt(scanner.nextLine());
        boolean filterByAvailability = (availabilityChoice == 1);

        return new UserPreference(locationFilter, typeFilter, filterByAvailability);
    }

    public List<ChargingStation> findChargingStations(UserPreference preference) {
        List<ChargingStation> matchingStations = new ArrayList<>();
        System.out.println("Matching Charging Stations:");
        for (ChargingStation station : chargingStations) {
            boolean matchesLocation = station.getLocation().equalsIgnoreCase(preference.getPreferredLocation());
            boolean matchesType = station.getChargerType().equalsIgnoreCase(preference.getPreferredChargerType());
            boolean matchesAvailability = !preference.isFilterByAvailability() || station.isAvailable();

            if (matchesLocation && matchesType && matchesAvailability) {
                matchingStations.add(station);
                System.out.println(station.getName() + " - " + station.getLocation() + " (" + station.getChargerType() + ")");
            }
        }

        if (matchingStations.isEmpty()) {
            System.out.println("No matching charging stations found.");
        }

        return matchingStations;
    }

    public void bookChargingSlot(List<ChargingStation> matchingStations) {
        if (matchingStations.isEmpty()) {
            System.out.println("No available stations to book.");
            return;
        }

        System.out.println("Available stations to book:");
        for (int i = 0; i < matchingStations.size(); i++) {
            ChargingStation station = matchingStations.get(i);
            System.out.println((i + 1) + ". " + station.getName() + " - " + station.getLocation() + " (" + station.getChargerType() + ")");
        }

        System.out.println("Enter the number of the charging station to book: ");
        int stationChoice = Integer.parseInt(scanner.nextLine());
        ChargingStation selectedStation = matchingStations.get(stationChoice - 1);

        if (selectedStation.isAvailable()) {
            selectedStation.setAvailable(false);
            System.out.println("Charging slot booked successfully at " + selectedStation.getName());
        } else {
            System.out.println("Sorry, no available slots at " + selectedStation.getName());
        }
    }

    public void run() {
        System.out.println("Welcome to the EV Charging System");

        while (true) {
            System.out.println("1. Find Charging Stations");
            System.out.println("2. Book Charging Slot");
            System.out.println("3. Exit");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                UserPreference preference = getUserPreferences();
                if (preference != null) {
                    List<ChargingStation> matchingStations = findChargingStations(preference);
                    bookChargingSlot(matchingStations);
                }
            } else if (choice == 2) {
                System.out.println("Please find stations first.");
            } else if (choice == 3) {
                System.out.println("Exiting the system. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public static void main(String[] args) {
        EVChargingSystem system = new EVChargingSystem();
        system.run();
    }
}
