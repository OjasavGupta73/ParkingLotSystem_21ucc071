import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter the number of floors: ");
            int numFloors = scanner.nextInt();
            validateInput(numFloors, "Number of floors cannot be negative.");

            System.out.print("Enter the number of spots per floor: ");
            int spotsPerFloor = scanner.nextInt();
            validateInput(spotsPerFloor, "Number of spots per floor cannot be negative.");

            ParkingLotManager parkingLot = new ParkingLot(numFloors, spotsPerFloor);

            Thread entrance1 = new Thread(new Entrance(parkingLot, "Entrance 1"));
            entrance1.start();
//            Thread entrance2 = new Thread(new Entrance(parkingLot, "Entrance 2"));
//            entrance2.start();

        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private static void validateInput(int value, String errorMessage) throws InvalidInputException {
        if (value < 0) throw new InvalidInputException(errorMessage);
    }
}
