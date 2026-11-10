package rental;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        level1Polymorphism();
        level2ConstructorChain();
        level2InstanceofAndTypes();
        level3EqualsHashCode();
        level3SealedSwitch();
        level3CompositionAndFactory();
    }

    // ---------- РІВЕНЬ 1: поліморфізм ----------
    private static void level1Polymorphism() {
        header("РІВЕНЬ 1. Поліморфізм: List<Vehicle> з об'єктами різних підкласів");
        System.out.println("  (конструктори виводять повідомлення — див. ланцюжок нижче)");

        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Car("AA1111AA", "Toyota", "Camry", 2022, 50, 4));
        vehicles.add(new ElectricCar("AA2222BB", "Tesla", "Model 3", 2024, 80, 4, 75));
        vehicles.add(new Truck("AA3333CC", "MAN", "TGX", 2020, 120, 18));

        System.out.println("\n  Виклик перевизначених методів у циклі (динамічне зв'язування):");
        for (Vehicle v : vehicles) {
            System.out.println("  - " + v.describe());
            System.out.println("    toString : " + v);
            System.out.printf("    вартість за 3 дні: %.2f$, за 10 днів: %.2f$%n",
                    v.calculateRentalCost(3), v.calculateRentalCost(10));
        }
    }

    // ---------- РІВЕНЬ 2: ланцюжок конструкторів, this(...) ----------
    private static void level2ConstructorChain() {
        header("РІВЕНЬ 2. Ланцюжок конструкторів (Vehicle -> Car -> ElectricCar)");
        System.out.println("  new ElectricCar(...) — порядок: від вершини ієрархії вниз:");
        new ElectricCar("BB0001BB", "Nissan", "Leaf", 2023, 60, 5, 40);

        System.out.println("\n  Скорочений конструктор (this(...) -> повний -> super(...)):");
        new ElectricCar("BB0002BB", "Renault", "Zoe", 55, 52);
    }

    // ---------- РІВЕНЬ 2: instanceof, статичний/динамічний тип ----------
    private static void level2InstanceofAndTypes() {
        header("РІВЕНЬ 2. instanceof (pattern matching), статичний і динамічний тип");
        Vehicle v = new ElectricCar("CC0001CC", "Tesla", "Model Y", 2025, 90, 5, 82);
        // Статичний тип змінної v — Vehicle (що можна викликати: перевіряє компілятор).
        // Динамічний (фактичний) тип об'єкта — ElectricCar (яка версія методу виконається: вирішує JVM).
        System.out.println("\n  Статичний тип: Vehicle, динамічний: " + v.getClass().getSimpleName());
        System.out.println("  v.describe() -> версія з ElectricCar: " + v.describe());
        // v.getDoors();  // НЕ скомпілюється: у Vehicle немає getDoors()

        List<Vehicle> list = List.of(v,
                new Car("CC0002CC", "Skoda", "Octavia", 2021, 40, 4),
                new Truck("CC0003CC", "Volvo", "FH", 2019, 110, 20));
        for (Vehicle x : list) {
            if (x instanceof ElectricCar e) {
                System.out.println("  ElectricCar: батарея " + e.getBatteryKwh() + " кВт·год, дверей " + e.getDoors());
            } else if (x instanceof Car c) {
                System.out.println("  Car: дверей " + c.getDoors());
            } else if (x instanceof Truck t) {
                System.out.println("  Truck: вантажопідйомність " + t.getLoadCapacityTons() + " т");
            }
        }

        try {
            Car wrong = (Car) list.get(2);   // насправді Truck
            System.out.println(wrong);
        } catch (ClassCastException ex) {
            System.out.println("  Некоректне приведення (Car) до Truck -> ClassCastException");
        }
    }

    // ---------- РІВЕНЬ 3: equals / hashCode ----------
    private static void level3EqualsHashCode() {
        header("РІВЕНЬ 3. equals() / hashCode() та симетрія");
        Car car = new Car("DD0001DD", "Tesla", "Model 3", 2024, 80, 4);
        ElectricCar electric = new ElectricCar("DD0001DD", "Tesla", "Model 3", 2024, 80, 4, 75);

        System.out.println("  car.equals(electric)  = " + car.equals(electric));
        System.out.println("  electric.equals(car)  = " + electric.equals(car) + "   <- симетрично (getClass)");

        Car same1 = new Car("DD0002DD", "Skoda", "Fabia", 2020, 30, 4);
        Car same2 = new Car("DD0002DD", "Skoda", "Fabia", 2020, 30, 4);
        Set<Vehicle> set = new HashSet<>(List.of(same1));
        System.out.println("  Рівні об'єкти, hashCode збігається: " + (same1.hashCode() == same2.hashCode()));
        System.out.println("  HashSet.contains(рівний об'єкт) = " + set.contains(same2));
        System.out.println("  (порівняння з instanceof-версією, де симетрія ламається: rental.equalsdemo.EqualsSymmetryDemo)");
    }

    // ---------- РІВЕНЬ 3: sealed + switch з pattern matching ----------
    private static void level3SealedSwitch() {
        header("РІВЕНЬ 3. sealed + switch з pattern matching (без default)");
        List<Vehicle> vs = List.of(
                new Car("EE0001EE", "Kia", "Ceed", 2023, 45, 4),
                new ElectricCar("EE0002EE", "BYD", "Dolphin", 2024, 65, 5, 60),
                new Truck("EE0003EE", "Scania", "R450", 2021, 130, 25));
        for (Vehicle v : vs) System.out.println("  " + category(v));
    }

    /**
     * Немає default: sealed гарантує, що перелік підтипів скінченний і відомий компілятору.
     * Додамо новий підклас у permits — і цей switch ПЕРЕСТАНЕ компілюватися, поки його не обробимо.
     * Порядок важливий: ElectricCar раніше Car (інакше Car «поглине» ElectricCar -> помилка dominance).
     */
    static String category(Vehicle v) {
        return switch (v) {
            case ElectricCar e -> "Електро: " + e.getFullName() + ", батарея " + e.getBatteryKwh() + " кВт·год";
            case Car c         -> "Легковий: " + c.getFullName() + ", дверей " + c.getDoors();
            case Truck t       -> "Вантажний: " + t.getFullName() + ", " + t.getLoadCapacityTons() + " т";
        };
    }

    // ---------- РІВЕНЬ 3: композиція + фабрика ----------
    private static void level3CompositionAndFactory() {
        header("РІВЕНЬ 3. Композиція (Fleet has-a List<Vehicle>) та Simple Factory");
        Fleet fleet = new Fleet("Київський філіал");
        String[] data = {
                "CAR;FF0001FF;Toyota;Corolla;2022;48;4",
                "ELECTRIC;FF0002FF;Tesla;Model 3;2024;80;4;75",
                "TRUCK;FF0003FF;MAN;TGX;2020;120;18",
                "BUS;FF0004FF;Neoplan;Cityliner;2019;200;50"      // невідомий тип
        };
        for (String line : data) {
            try {
                fleet.add(VehicleFactory.fromCsv(line));
            } catch (IllegalArgumentException ex) {
                System.out.println("  Фабрика відхилила рядок: " + ex.getMessage());
            }
        }
        System.out.println("\n  " + fleet);
        System.out.printf("  Загальна вартість за 5 днів: %.2f$%n", fleet.totalCost(5));
        fleet.cheapest(5).ifPresent(v -> System.out.println("  Найдешевший: " + v.describe()));
        // fleet.getVehicles().clear();  // UnsupportedOperationException — інваріанти захищені
    }

    private static void header(String text) {
        System.out.println("\n==== " + text + " ====");
    }
}
