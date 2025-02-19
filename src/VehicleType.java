public enum VehicleType {
    CAR(1, "car"), BIKE(1, "bike"), TRUCK(2, "truck");
    private final int req_spots;
    private final String name;

    VehicleType(int req_spots, String name) {
        this.name = name;
        this.req_spots = req_spots;
    }

    public int getReq_spots() {
        return req_spots;
    }

}

