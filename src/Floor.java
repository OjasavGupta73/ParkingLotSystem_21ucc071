import java.util.ArrayList;
import java.util.List;

class Floor {
    private int floorNumber;
    private List<Spot> spots;

    public Floor(int floorNumber, int spotsPerFloor) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        for (int i = 0; i < spotsPerFloor; i++) {
            spots.add(new Spot(i, floorNumber));
        }
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public int getAvailableSpots() {
        int count = 0;
        for (Spot spot : spots) {
            if (!spot.isOccupied()) {
                count++;
            }
        }
        return count;
    }
}