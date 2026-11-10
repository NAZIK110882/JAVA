package rental;

import java.time.Year;
import java.util.Objects;

/**
 * Базовий абстрактний клас ієрархії. Запечатаний (sealed): компілятор знає
 * ПОВНИЙ перелік прямих нащадків — Car і Truck.
 *
 * Абстрактний, бо «просто транспортний засіб» без типу в прокаті не існує:
 * неможливо видати в оренду «щось». Екземпляр створювати не можна.
 */
public abstract sealed class Vehicle permits Car, Truck {

    private final String licensePlate;
    private final String brand;
    private final String model;
    private final int year;
    private final double dailyRate;

    /** Основний конструктор — уся ініціалізація та валідація тут. */
    public Vehicle(String licensePlate, String brand, String model, int year, double dailyRate) {
        if (licensePlate == null || licensePlate.isBlank())
            throw new IllegalArgumentException("Номерний знак не може бути порожнім");
        if (brand == null || brand.isBlank() || model == null || model.isBlank())
            throw new IllegalArgumentException("Марка і модель обов'язкові");
        if (year < 1900 || year > Year.now().getValue() + 1)
            throw new IllegalArgumentException("Некоректний рік випуску: " + year);
        if (dailyRate <= 0)
            throw new IllegalArgumentException("Добовий тариф має бути > 0");

        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
        System.out.println("    [Vehicle]     конструктор виконано: " + licensePlate);
    }

    /** Скорочений конструктор: рік = поточний. Делегує основному через this(...). */
    public Vehicle(String licensePlate, String brand, String model, double dailyRate) {
        this(licensePlate, brand, model, Year.now().getValue(), dailyRate);
    }

    /**
     * Абстрактний метод: формула вартості різна для кожного типу (знижки,
     * доплата за вантаж, зарядка), тож базовий клас не може дати змістовну
     * реалізацію за замовчуванням. Абстрактність ЗМУШУЄ кожен підклас її надати.
     */
    public abstract double calculateRentalCost(int days);

    /** Звичайний метод, який підкласи МОЖУТЬ перевизначити. */
    public String describe() {
        return getFullName() + " (" + year + ")";
    }

    /**
     * final: спосіб формування повної назви — частина «контракту» ієрархії
     * (використовується в звітах). Підкласи не повинні його змінювати.
     */
    public final String getFullName() {
        return brand + " " + model;
    }

    /** Спільна перевірка кількості днів для всіх підкласів. */
    protected static void requirePositiveDays(int days) {
        if (days <= 0) throw new IllegalArgumentException("Кількість днів має бути > 0");
    }

    public String getLicensePlate() { return licensePlate; }
    public String getBrand()        { return brand; }
    public String getModel()        { return model; }
    public int getYear()            { return year; }
    public double getDailyRate()    { return dailyRate; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + licensePlate + ", " + getFullName()
                + ", " + year + ", " + String.format("%.2f", dailyRate) + " $/добу]";
    }

    // getClass() (а не instanceof): рівними можуть бути лише об'єкти ТОГО САМОГО класу.
    // Це гарантує симетрію і транзитивність, навіть коли підкласи додають поля.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle v = (Vehicle) o;
        return year == v.year
                && Double.compare(dailyRate, v.dailyRate) == 0
                && licensePlate.equals(v.licensePlate)
                && brand.equals(v.brand)
                && model.equals(v.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate, brand, model, year, dailyRate);
    }
}
