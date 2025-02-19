import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
class ParkingLot implements ParkingLotManager {
    private int numFloors;
    private int spotsPerFloor;
    private List<Floor> floors;
    private Map<String, Spot> parkedVehicles;
    private Map<Integer, Ticket> tickets; // Map to store tickets by ticket ID
    private static int ticketCounter = 100; // Ticket ID counter
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
            Spot spot = findAvailableSpot(vehicle,fromGate1);

            if (spot != null) {
                spot.occupySpot(vehicle);
                parkedVehicles.put(licensePlate, spot);

                if (vehicle.getType() == VehicleType.TRUCK) {
                    // Occupy the next spot for Trucks
                    Spot nextSpot = floors.get(spot.getFloor()).getSpots().get(spot.getSpotId() + 1);
                    nextSpot.occupySpot(vehicle);
                    spot.setNextSpot(nextSpot); // Link the spots
                }

                // Generate a ticket
                int ticketId = ticketCounter++;
                Ticket ticket = new Ticket(ticketId, licensePlate, spot);
                tickets.put(ticketId, ticket);

                return ticket;
            }
            return null;
        } finally {
            lock.unlock(); // Release lock
        }
    }

    @Override
    public boolean unparkVehicle(int ticketId) {
        lock.lock(); // Acquire lock
        try {
            Ticket ticket = tickets.get(ticketId);
            if (ticket != null) {
                Spot spot = ticket.getSpot();
                spot.freeSpot();
                if (spot.getNextSpot() != null) {
                    spot.getNextSpot().freeSpot(); // Free the next spot for Trucks
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

    private Spot findAvailableSpot(Vehicle vehicle, boolean fromGate1) {
        if (fromGate1) {
            // Search from start for Gate 1
            for (Floor floor : floors) {
                for (Spot spot : floor.getSpots()) {
                    if (!spot.isOccupied()) {
                        if (vehicle.getType() == VehicleType.TRUCK) {
                            // Check if the next spot is also available for trucks
                            int nextSpotId = spot.getSpotId() + 1;
                            if (nextSpotId < spotsPerFloor && !floor.getSpots().get(nextSpotId).isOccupied()) {
                                return spot;
                            }
                        } else {
                            return spot;
                        }
                    }
                }
            }
        } else {
            // Search from end for Gate 2
            for (int i = floors.size() - 1; i >= 0; i--) {
                Floor floor = floors.get(i);
                for (int j = spotsPerFloor - 1; j >= 0; j--) {
                    Spot spot = floor.getSpots().get(j);
                    if (!spot.isOccupied()) {
                        if (vehicle.getType() == VehicleType.TRUCK) {
                            // Check if the next spot is also available for trucks
                            int nextSpotId = j - 1;
                            if (nextSpotId >= 0 && !floor.getSpots().get(nextSpotId).isOccupied()) {
                                return spot;
                            }
                        } else {
                            return spot;
                        }
                    }
                }
            }
        }
        return null;
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
                return "Floor: " + (spot.getFloor()+1) + ", Spots: " + spot.getSpotId() + " and " + spot.getNextSpot().getSpotId();
            } else {
                // For Cars and Bikes, return a single spot
                return "Floor: " + (spot.getFloor()+1) + ", Spot: " + spot.getSpotId();
            }
        }
        return "Ticket not found.";
    }

    @Override
    public int getAvailableSpotsPerFloor(int floor) {
        if (floor < 0 || floor >= numFloors) {
            throw new IllegalArgumentException("Invalid floor number.");
        }

        Floor selectedFloor = floors.get(floor);
        return selectedFloor.getAvailableSpots();
    }
    public List<Ticket> getAllParkedVehicles() {
        return new ArrayList<>(tickets.values());
    }

    public int getNumFloors() {
        return numFloors;
    }
}
