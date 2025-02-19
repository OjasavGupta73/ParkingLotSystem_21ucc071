//import java.util.Random;
//import java.util.Scanner;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.Lock;
//import java.util.concurrent.locks.ReentrantLock;
//
//class ParkingLotService {
//    private final ParkingLotManager parkingLot;
//    private Thread entrance1;
//    private Thread entrance2;
//    private final Lock lock = new ReentrantLock();
//    private final Condition entranceCompleteCondition = lock.newCondition();
//    private final Random random = new Random();
//
//    public ParkingLotService(ParkingLotManager parkingLot) {
//        this.parkingLot = parkingLot;
//    }
//
//    public void start() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Welcome to the Parking Lot System!");
//
//        while (true) {
//            System.out.println("==============================");
//            System.out.println("\nPlease select an option:");
//            System.out.println("1. Park a vehicle");
//            System.out.println("2. Unpark a vehicle");
//            System.out.println("3. Check available spots on a floor");
//            System.out.println("4. Check if the parking lot is full");
//            System.out.println("5. Find the location of a parked vehicle");
//            System.out.println("6. Exit");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume the newline character
//            try {
//                switch (choice) {
//                    case 1:
//                        parkVehicleRandomly(scanner);
//                        break;
//                    case 2:
//                        unparkVehicle(scanner);
//                        break;
//                    case 3:
//                        checkAvailableSpots(scanner);
//                        break;
//                    case 4:
//                        checkIfFull();
//                        break;
//                    case 5:
//                        findVehicleLocation(scanner);
//                        break;
//                    case 6:
//                        System.out.println("Thank you for using the Parking Lot System. Goodbye!");
//                        scanner.close();
//                        return;
//                    default:
//                        System.out.println("Invalid choice. Please try again.");
//                }
//            } catch (InvalidInputException | InterruptedException e) {
//                System.out.println("Error: " + e.getMessage());
//            }
//        }
//    }
//
//    private void parkVehicleRandomly(Scanner scanner) throws InterruptedException {
//        lock.lock();
//        try {
//            boolean activateFirstEntrance = random.nextBoolean();
//
//            if (activateFirstEntrance) {
//                entrance1 = new Thread(new Entrance(parkingLot, "Entrance 1", entranceCompleteCondition));
//                entrance2 = new Thread(new Entrance(parkingLot, "Entrance 2", entranceCompleteCondition));
//
//                entrance1.start();
//                entrance1.join(); // Wait for Entrance 1 to finish
//
//                entrance2.start();
//                entrance2.join(); // Wait for Entrance 2 to finish
//            } else {
//                entrance2 = new Thread(new Entrance(parkingLot, "Entrance 2", entranceCompleteCondition));
//                entrance1 = new Thread(new Entrance(parkingLot, "Entrance 1", entranceCompleteCondition));
//
//                entrance2.start();
//                entrance2.join(); // Wait for Entrance 2 to finish
//
//                entrance1.start();
//                entrance1.join(); // Wait for Entrance 1 to finish
//            }
//        } finally {
//            lock.unlock();
//        }
//    }
//
//    private void unparkVehicle(Scanner scanner) {
//        System.out.print("Enter the TicketId to unpark: ");
//        int ticketId = scanner.nextInt();
//        boolean unparked = parkingLot.unparkVehicle(ticketId);
//        if (unparked) {
//            System.out.println("Vehicle unparked successfully!\n Thanks for the Service!");
//        } else {
//            System.out.println("Vehicle not found or already unparked.");
//        }
//    }
//
//    private void checkAvailableSpots(Scanner scanner) throws InvalidInputException {
//        System.out.print("Enter the floor number: ");
//        int floor = scanner.nextInt();
//        validateInput(floor, "Floor number cannot be negative.");
//        int availableSpots = parkingLot.getAvailableSpotsPerFloor(floor - 1);
//        System.out.println("Available spots on floor " + floor + ": " + availableSpots);
//    }
//
//    private void checkIfFull() {
//        boolean isFull = parkingLot.isFull();
//        if (isFull) {
//            System.out.println("The parking lot is full.");
//        } else {
//            System.out.println("The parking lot is not full.");
//        }
//    }
//
//    private void findVehicleLocation(Scanner scanner) throws InvalidInputException {
//        System.out.print("Enter the ticket ID: ");
//        int ticketId = scanner.nextInt();
//        validateInput(ticketId, "Ticket ID cannot be negative.");
//        String location = parkingLot.getVehicleLocation(ticketId);
//        System.out.println("Vehicle location: " + location);
//    }
//
//    private void validateInput(int value, String errorMessage) throws InvalidInputException {
//        if (value <= 0) {
//            throw new InvalidInputException(errorMessage);
//        }
//    }
//}
