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

        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Create a PR for this");

        System.out.println("Check this for PR");
        System.out.print("Cehck new branch changes");
    }

    private static void validateInput(int value, String errorMessage) throws InvalidInputException {
        if (value < 0) throw new InvalidInputException(errorMessage);
    }
}
