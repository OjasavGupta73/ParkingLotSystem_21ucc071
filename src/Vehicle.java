class Vehicle {
    private String licensePlate;
    private VehicleType type;

    public Vehicle(String licensePlate, String type) {
        this.licensePlate = licensePlate;
        this.type = VehicleType.valueOf(type.toUpperCase());
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }
}
