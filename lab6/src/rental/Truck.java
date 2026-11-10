package rental;

import java.time.Year;
import java.util.Objects;

/** Вантажівка — другий прямий нащадок Vehicle. */
public final class Truck extends Vehicle {

    private static final double SURCHARGE_PER_TON_PER_DAY = 8.0;

    private final double loadCapacityTons;

    public Truck(String licensePlate, String brand, String model, int year,
                 double dailyRate, double loadCapacityTons) {
        super(licensePlate, brand, model, year, dailyRate);
        if (loadCapacityTons <= 0)
            throw new IllegalArgumentException("Вантажопідйомність має бути > 0");
        this.loadCapacityTons = loadCapacityTons;
        System.out.println("    [Truck]       конструктор виконано: вантажопідйомність = " + loadCapacityTons + " т");
    }

    public Truck(String licensePlate, String brand, String model, double dailyRate, double loadCapacityTons) {
        this(licensePlate, brand, model, Year.now().getValue(), dailyRate, loadCapacityTons);
    }

    @Override
    public double calculateRentalCost(int days) {
        requirePositiveDays(days);
        return days * (getDailyRate() + loadCapacityTons * SURCHARGE_PER_TON_PER_DAY);
    }

    @Override
    public String describe() {
        return super.describe() + ", вантажівка, до " + loadCapacityTons + " т";
    }

    public double getLoadCapacityTons() { return loadCapacityTons; }

    @Override
    public String toString() {
        return super.toString() + " | вантажопідйомність=" + loadCapacityTons + " т";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        return Double.compare(loadCapacityTons, ((Truck) o).loadCapacityTons) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loadCapacityTons);
    }
}
