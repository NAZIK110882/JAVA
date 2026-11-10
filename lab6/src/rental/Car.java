package rental;

import java.time.Year;
import java.util.Objects;

/** Легковий автомобіль. Запечатаний: єдиний дозволений нащадок — ElectricCar. */
public sealed class Car extends Vehicle permits ElectricCar {

    private final int doors;

    public Car(String licensePlate, String brand, String model, int year, double dailyRate, int doors) {
        super(licensePlate, brand, model, year, dailyRate);   // має бути першим оператором
        if (doors < 2 || doors > 5)
            throw new IllegalArgumentException("Кількість дверей має бути 2..5");
        this.doors = doors;
        System.out.println("    [Car]         конструктор виконано: дверей = " + doors);
    }

    /** Скорочений: поточний рік, 4 двері. Делегування через this(...) — без дублювання. */
    public Car(String licensePlate, String brand, String model, double dailyRate) {
        this(licensePlate, brand, model, Year.now().getValue(), dailyRate, 4);
    }

    @Override
    public double calculateRentalCost(int days) {
        requirePositiveDays(days);
        double cost = days * getDailyRate();
        return days >= 7 ? cost * 0.90 : cost;   // знижка 10% від тижня
    }

    @Override
    public String describe() {
        return super.describe() + ", легковий, дверей: " + doors;
    }

    public int getDoors() { return doors; }

    @Override
    public String toString() {
        return super.toString() + " | дверей=" + doors;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;        // тут уже перевірено getClass()
        return doors == ((Car) o).doors;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), doors);
    }
}
