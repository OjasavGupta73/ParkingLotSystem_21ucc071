import java.util.List;

interface ParkingLotManager {
    Ticket parkVehicle(String licensePlate, String type,boolean fromgate1);

    boolean unparkVehicle(int ticketId);

    int getAvailableSpotsPerFloor(int floor);

    boolean isFull();

    String getVehicleLocation(int ticketId);

    int getNumFloors();

    List<Ticket> getAllParkedVehicles();

    void printParkingLotLayout();
}
