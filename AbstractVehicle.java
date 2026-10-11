public abstract class AbstractVehicle implements Serviceable {
    protected String brand;
    protected String model;
    protected double baseRate;

    public AbstractVehicle(String brand, String model, double baseRate) {
        this.brand = brand;
        this.model = model;
        this.baseRate = baseRate;
    }

    public void printVehicleInfo() {
        System.out.printf("Транспорт: %s %s, Базова ставка: %.2f$\n", brand, model, baseRate);
    }

    public abstract String getVehicleCategory();
}