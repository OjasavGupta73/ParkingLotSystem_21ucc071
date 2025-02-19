class Ticket {
    private int ticketId;
    private Vehicle vehicle;
    private Spot spot;

    public Ticket(int ticketId,Vehicle vehicle, Spot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getLicensePlate() {
        return vehicle.getLicensePlate();
    }
    public Vehicle getVehicle(){
        return vehicle;
    }

    public Spot getSpot() {
        return spot;
    }
}
