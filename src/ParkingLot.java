import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

class ParkingLot implements ParkingLotManager {
    private int numFloors;
    private int spotsPerFloor;
    private List<Floor> floors;
    private Map<String, Spot> parkedVehicles;
    private Map<Integer, Ticket> tickets;
    private static int ticketCounter = 100;
    private final ReentrantLock lock = new ReentrantLock();

    public ParkingLot(int numFloors, int spotsPerFloor) {
        this.numFloors = numFloors;
        this.spotsPerFloor = spotsPerFloor;
        this.floors = new ArrayList<>();
        this.parkedVehicles = new HashMap<>();
        this.tickets = new HashMap<>();

        // Initialize floors
        for (int i = 0; i < numFloors; i++) {
            floors.add(new Floor(i, spotsPerFloor));
        }
    }

    @Override
    public Ticket parkVehicle(String licensePlate, String type, boolean fromGate1) {
        lock.lock(); // Acquire lock
        try {
            if (parkedVehicles.containsKey(licensePlate)) {
                System.out.println("Vehicle with license plate " + licensePlate + " is already parked.");
                return null;
            }

            Vehicle vehicle = new Vehicle(licensePlate, type);
            Spot spot = findNearestAvailableSpot(vehicle);

            if (spot != null) {
                spot.occupySpot(vehicle);
                parkedVehicles.put(licensePlate, spot);

                if (vehicle.getType() == VehicleType.TRUCK) {
                    // Occupy the next spot for Trucks
                    Spot nextSpot = findNextSpot(spot);
                    if (nextSpot != null) {
                        nextSpot.occupySpot(vehicle);
                        spot.setNextSpot(nextSpot); // Link the spots
                    }
                }

                // Generate a ticket
                int ticketId = ticketCounter++;
                Ticket ticket = new Ticket(ticketId, vehicle, spot);
                tickets.put(ticketId, ticket);

                return ticket;
            }
            return null;
        } finally {
            lock.unlock(); // Release lock
        }
    }

    private Spot findNearestAvailableSpot(Vehicle vehicle) {
        // Prioritize Floor 1 over Floor 2
        for (Floor floor : floors) {
            if (floor.getAvailableSpots() > 0) {
                PriorityQueue<Spot> availableSpots = floor.getnear();

                while (!availableSpots.isEmpty()) {
                    Spot spot = availableSpots.poll();
                    //System.out.println(spot.toString());// Get the nearest available spot
                    if (vehicle.getType() == VehicleType.TRUCK) {
                        // Check if the next spot is also available for trucks
                        Spot nextSpot = findNextSpot(spot);
                        if (nextSpot != null && !nextSpot.isOccupied()) {
                            return spot;
                        } else {
                            floor.addAvailableSpot(spot); // Add the spot back if next spot is not available
                        }
                    } else {
                        return spot;
                    }
                }
            }
        }
        return null;
    }

    private Spot findNextSpot(Spot spot) {
        int floorNumber = spot.getFloor();
        int rowNumber = spot.getRowNumber();
        int spotId = spot.getSpotId() + 1;

        List<Spot> row = floors.get(floorNumber).getRows().get(rowNumber);
        if (spotId < row.size()) {
            return row.get(spotId);
        }
        return null;
    }

    @Override
    public boolean unparkVehicle(int ticketId) {
        lock.lock(); // Acquire lock
        try {
            Ticket ticket = tickets.get(ticketId);
            if (ticket != null) {
                Spot spot = ticket.getSpot();
                spot.freeSpot();
                floors.get(spot.getFloor()).addAvailableSpot(spot); // Add the spot back to the priority queue

                if (spot.getNextSpot() != null) {
                    spot.getNextSpot().freeSpot(); // Free the next spot for Trucks
                    floors.get(spot.getFloor()).addAvailableSpot(spot.getNextSpot()); // Add the next spot back
                }

                parkedVehicles.remove(ticket.getLicensePlate());
                tickets.remove(ticketId); // Remove the ticket
                return true;
            }
            return false;
        } finally {
            lock.unlock(); // Release lock
        }
    }

    @Override
    public int getAvailableSpotsPerFloor(int floor) {
        if (floor < 0 || floor >= numFloors) {
            throw new IllegalArgumentException("Invalid floor number.");
        }
        return floors.get(floor).getAvailableSpots();
    }

    @Override
    public boolean isFull() {
        for (Floor floor : floors) {
            if (floor.getAvailableSpots() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getVehicleLocation(int ticketId) {
        Ticket ticket = tickets.get(ticketId);
        if (ticket != null) {
            Spot spot = ticket.getSpot();
            if (spot.getNextSpot() != null) {
                // For Trucks, return both spots
                return "Floor: " + (spot.getFloor() + 1) + ", Row: " + (spot.getRowNumber() + 1) + ", Spots: " + spot.getSpotId() + " and " + spot.getNextSpot().getSpotId();
            } else {
                // For Cars and Bikes, return a single spot
                return "Floor: " + (spot.getFloor() + 1) + ", Row: " + (spot.getRowNumber() + 1) + ", Spot: " + spot.getSpotId();
            }
        }
        return "Ticket not found.";
    }

    @Override
    public List<Ticket> getAllParkedVehicles() {
        return new ArrayList<>(tickets.values());
    }

    @Override
    public int getNumFloors() {
        return numFloors;
    }


    @Override
    public void printParkingLotLayout() {
        lock.lock();
        try {
            for (Floor floor : floors) {
                System.out.println("Floor " + (floor.getFloorNumber() + 1) + ":");
                for (List<Spot> row : floor.getRows()) {
                    System.out.println();
                    for (Spot spot : row) {

                        if (spot.isOccupied()) {
                            Vehicle vehicle = spot.getParkedVehicle();
                            if (vehicle.getType() == VehicleType.CAR) {
                                System.out.print("|C|");
                            } else if (vehicle.getType() == VehicleType.BIKE) {
                                System.out.print("|B|");
                            } else if (vehicle.getType() == VehicleType.TRUCK) {
                                if (spot.getNextSpot() != null) {
                                    System.out.print("|T T|");
                                    continue; // Skip the next spot
                                }
                            }
                        } else {
                            System.out.print("|X|");
                        }

                    }
                    System.out.println();
                }
                System.out.println(); // Extra line between floors
            }
        } finally {
            lock.unlock();
        }
    }
}


