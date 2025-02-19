class Ticket {
    private int ticketId;
    private String licensePlate;
    private Spot spot;

    public Ticket(int ticketId, String licensePlate, Spot spot) {
        this.ticketId = ticketId;
        this.licensePlate = licensePlate;
        this.spot = spot;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Spot getSpot() {
        return spot;
    }
}
