import java.util.*;

class Floor {
    private int floorNumber;
    private List<List<Spot>> rows; // List of rows (each row is a list of spots)
    private PriorityQueue<Spot> availableSpots; // Priority queue for available spots

    public Floor(int floorNumber, int spotsPerFloor) {
        this.floorNumber = floorNumber;
        this.rows = new ArrayList<>();
        this.availableSpots = new PriorityQueue<>((a,b)->(a.getRowNumber()+a.getSpotId())-(b.getRowNumber()+b.getSpotId()));

        // Divide spots into rows of 10 spots each
        int rowCount = (int) Math.ceil((double) spotsPerFloor / 10); // Calculate number of rows
        for (int i = 0; i < rowCount; i++) {
            List<Spot> row = new ArrayList<>();
            int start = i * 10;
            int end = Math.min(start + 10, spotsPerFloor); // Ensure we don't exceed the total spots

            for (int j = 0; j < end-start; j++) {
                Spot spot = new Spot(j, floorNumber, i);
                row.add(spot);
                availableSpots.add(spot); // Add all spots to the priority queue initially
            }
            rows.add(row); // Add the row to the list of rows
        }
    }

    public List<List<Spot>> getRows() {
        return rows;
    }

    public PriorityQueue<Spot> getnear() {
        return availableSpots;
    }

    public int getAvailableSpots() {
        return availableSpots.size();
    }

    public void addAvailableSpot(Spot spot) {
        availableSpots.add(spot); // Add the spot back to the priority queue
    }

    public int getFloorNumber() {
        return floorNumber;
    }
}