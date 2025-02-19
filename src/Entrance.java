import java.util.*;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Entrance implements Runnable {
    private ParkingLotManager parkingLot;
    private String entranceName;
    private static final Lock inputLock = new ReentrantLock();
    private static final Lock parkingLock = new ReentrantLock(); // Lock for parking
    private Condition parkingComplete;

    public Entrance(ParkingLotManager parkingLot, String entranceName) {
        this.parkingLot = parkingLot;
        this.entranceName = entranceName;
        this.parkingComplete = new ReentrantLock().newCondition(); // Create a condition for signaling
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true; // Control variable for the loop

        while (running) {
            inputLock.lock(); // Lock for input
            try {
                System.out.println("[" + entranceName + "] Please select an option:");
                System.out.println("1. Park a vehicle");
                System.out.println("2. Unpark a vehicle");
                System.out.println("3. Show vehicle location");
                System.out.println("4. Show total spots available");
                System.out.println("5. Show available spots on a floor");
                System.out.println("6. Show details of all parked vehicles");
                System.out.println("7. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        parkVehicle(scanner);
                        break;
                    case 2:
                        unparkVehicle(scanner);
                        break;
                    case 3:
                        showVehicleLocation(scanner);
                        break;
                    case 4:
                        showTotalSpotsAvailable();
                        break;
                    case 5:
                        showAvailableSpotsOnFloor(scanner);
                        break;
                    case 6:
                        showDetailsOfAllParkedVehicles();
                        break;
                    case 7:
                        running = false;
                      //  return;// Exit loop
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            } finally {
                inputLock.unlock(); // Release input lock
            }
        }
        scanner.close();
    }

    private void parkVehicle(Scanner scanner) {
        parkingLock.lock(); // Lock for parking operation
        try {
            System.out.print("Enter license plate: ");
            String licensePlate = scanner.nextLine();
            System.out.print("Enter vehicle type (Car, Bike, Truck): ");
            String type = scanner.nextLine();

            boolean fromGate1 = "Entrance 1".equals(entranceName); // Determine gate based on entrance name
            Ticket ticket = parkingLot.parkVehicle(licensePlate, type, fromGate1);
            if (ticket != null) {
                System.out.println("[" + entranceName + "] Vehicle parked successfully! Ticket ID: " + ticket.getTicketId());
                parkingComplete.signal(); // Signal that parking is complete
            } else {
                System.out.println("[" + entranceName + "] Failed to park the vehicle. No available spots or duplicate license plate.");
            }
        } finally {
            parkingLock.unlock(); // Release parking lock
        }
    }

    private void unparkVehicle(Scanner scanner) {
        System.out.print("Enter the Ticket ID to unpark: ");
        int ticketId = scanner.nextInt();

        parkingLock.lock(); // Lock for un-parking operation
        try {
            boolean unparked = parkingLot.unparkVehicle(ticketId);
            if (unparked) {
                System.out.println("[" + entranceName + "] Vehicle unparked successfully!");
            } else {
                System.out.println("[" + entranceName + "] Vehicle not found or already unparked.");
            }
        } finally {
            parkingLock.unlock(); // Release un-parking lock
        }
    }

    private void showVehicleLocation(Scanner scanner) {
        System.out.print("Enter the Ticket ID to find the vehicle location: ");
        int ticketId = scanner.nextInt();

        String location = parkingLot.getVehicleLocation(ticketId);
        System.out.println("Vehicle location: " + location);
    }

    private void showTotalSpotsAvailable() {
        int totalSpots = 0;
        for (int i = 0; i < parkingLot.getNumFloors(); i++) {
            totalSpots += parkingLot.getAvailableSpotsPerFloor(i);
        }
        System.out.println("Total available spots in the parking lot: " + totalSpots);
    }

    private void showAvailableSpotsOnFloor(Scanner scanner) {
        System.out.print("Enter the floor number: ");
        int floorNumber = scanner.nextInt();

        int availableSpots = parkingLot.getAvailableSpotsPerFloor(floorNumber - 1);
        System.out.println("Available spots on floor " + floorNumber + ": " + availableSpots);
    }

    private void showDetailsOfAllParkedVehicles() {
        List<Ticket> tickets = parkingLot.getAllParkedVehicles();

        if (tickets.isEmpty()) {
            System.out.println("No vehicles are currently parked.");
            return;
        }
            System.out.println("===============================");
            System.out.println("Details of all parked vehicles:");
        for (Ticket ticket : tickets) {
            System.out.println("===============================");
            System.out.println("Ticket ID: " + ticket.getTicketId() +
                    "\n" +
                            " License Plate: " + ticket.getLicensePlate() +
                    "\n" +
                            " Spot: Floor " + (ticket.getSpot().getFloor() + 1) +
                    "\n" +
                            " Spot ID: " + ticket.getSpot().getSpotId());

        }
        System.out.println("===============================");
    }
}
