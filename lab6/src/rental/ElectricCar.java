package rental;

import java.util.Objects;

/**
 * Електромобіль — третій рівень ієрархії: Vehicle -> Car -> ElectricCar.
 * final: далі наслідувати немає сенсу, а запечатана ієрархія вимагає
 * final / sealed / non-sealed у кожного нащадка.
 */
public final class ElectricCar extends Car {

    private static final double CHARGING_FEE_PER_DAY = 5.0;

    private final double batteryKwh;

    public ElectricCar(String licensePlate, String brand, String model, int year,
                       double dailyRate, int doors, double batteryKwh) {
        super(licensePlate, brand, model, year, dailyRate, doors);
        if (batteryKwh <= 0)
            throw new IllegalArgumentException("Ємність батареї має бути > 0");
        this.batteryKwh = batteryKwh;
        System.out.println("    [ElectricCar] конструктор виконано: батарея = " + batteryKwh + " кВт·год");
    }

    /** Скорочений: 4 двері, поточний рік — делегує повному конструктору. */
    public ElectricCar(String licensePlate, String brand, String model, double dailyRate, double batteryKwh) {
        this(licensePlate, brand, model, java.time.Year.now().getValue(), dailyRate, 4, batteryKwh);
    }

    @Override
    public double calculateRentalCost(int days) {
        // Повторно використовуємо логіку Car і додаємо специфіку електромобіля.
        return super.calculateRentalCost(days) + days * CHARGING_FEE_PER_DAY;
    }

    @Override
    public String describe() {
        return super.describe() + ", електро, батарея: " + batteryKwh + " кВт·год";
    }

    public double getBatteryKwh() { return batteryKwh; }

    @Override
    public String toString() {
        return super.toString() + " | батарея=" + batteryKwh + " кВт·год";
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        return Double.compare(batteryKwh, ((ElectricCar) o).batteryKwh) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), batteryKwh);
    }
}
