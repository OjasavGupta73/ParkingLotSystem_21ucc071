class Spot {
    private int spotId;
    private int floor;
    private boolean isOccupied;
    private Vehicle parkedVehicle;
    private Spot nextSpot; // Reference to the next spot for Trucks

    public Spot(int spotId, int floor) {
        this.spotId = spotId;
        this.floor = floor;
        this.isOccupied = false;
        this.parkedVehicle = null;
        this.nextSpot = null;
    }

    public int getSpotId() {
        return spotId;
    }

    public int getFloor() {
        return floor;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void occupySpot(Vehicle vehicle) {
        this.isOccupied = true;
        this.parkedVehicle = vehicle;
    }

    public void freeSpot() {
        this.isOccupied = false;
        this.parkedVehicle = null;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public Spot getNextSpot() {
        return nextSpot;
    }

    public void setNextSpot(Spot nextSpot) {
        this.nextSpot = nextSpot;
    }
}
