package rental.equalsdemo;

import rental.Car;
import rental.ElectricCar;

import java.util.Objects;

/**
 * Демонстрація: equals() через instanceof ламає симетрію, коли підклас додає поля.
 * Для порівняння — «правильні» класи з пакета rental (getClass()).
 */
public class EqualsSymmetryDemo {

    // ---- ВІДКРИТА ієрархія з equals через instanceof (ПОГАНИЙ варіант) ----
    static class InstCar {
        final String plate;
        final int doors;

        InstCar(String plate, int doors) { this.plate = plate; this.doors = doors; }

        @Override public boolean equals(Object o) {
            if (!(o instanceof InstCar c)) return false;         // приймає й підкласи!
            return plate.equals(c.plate) && doors == c.doors;
        }
        @Override public int hashCode() { return Objects.hash(plate, doors); }
    }

    static class InstElectricCar extends InstCar {
        final double batteryKwh;

        InstElectricCar(String plate, int doors, double batteryKwh) {
            super(plate, doors);
            this.batteryKwh = batteryKwh;
        }

        @Override public boolean equals(Object o) {
            if (!(o instanceof InstElectricCar e)) return false;  // звичайний InstCar сюди не пройде
            return super.equals(o) && batteryKwh == e.batteryKwh;
        }
        @Override public int hashCode() { return Objects.hash(super.hashCode(), batteryKwh); }
    }

    public static void main(String[] args) {
        System.out.println("=== instanceof: симетрія порушена ===");
        InstCar base = new InstCar("AA1111AA", 4);
        InstCar sub = new InstElectricCar("AA1111AA", 4, 75);
        System.out.println("base.equals(sub) = " + base.equals(sub));   // true
        System.out.println("sub.equals(base) = " + sub.equals(base));   // false  <- a.equals(b) != b.equals(a)
        System.out.println("hashCode збігаються: " + (base.hashCode() == sub.hashCode())
                + "  (рівні за base.equals, але хеші різні -> порушено контракт hashCode)");

        System.out.println("\n=== getClass(): симетрія збережена ===");
        Car car = new Car("AA1111AA", "Tesla", "Model 3", 2024, 80, 4);
        ElectricCar electric = new ElectricCar("AA1111AA", "Tesla", "Model 3", 2024, 80, 4, 75);
        System.out.println("car.equals(electric) = " + car.equals(electric));   // false
        System.out.println("electric.equals(car) = " + electric.equals(car));   // false
    }
}
