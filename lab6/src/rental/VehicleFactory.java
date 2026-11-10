package rental;

/**
 * Simple Factory (статичний метод). Замінює розгалуження if/switch по рядку-типу
 * в усьому іншому коді: далі працюють з Vehicle поліморфно.
 * Невідомий тип -> IllegalArgumentException (fail-fast, а не тихий null).
 *
 * Формат рядка: TYPE;plate;brand;model;year;rate;extra...
 *   CAR;AA1111AA;Toyota;Camry;2022;50;4
 *   ELECTRIC;AA2222BB;Tesla;Model3;2024;80;4;75
 *   TRUCK;AA3333CC;MAN;TGX;2020;120;18
 */
public final class VehicleFactory {

    private VehicleFactory() {}

    public static Vehicle fromCsv(String line) {
        String[] p = line.split(";");
        if (p.length < 6)
            throw new IllegalArgumentException("Замало полів: " + line);
        String type = p[0].trim().toUpperCase();
        int year = Integer.parseInt(p[4].trim());
        double rate = Double.parseDouble(p[5].trim());

        return switch (type) {
            case "CAR" -> new Car(p[1], p[2], p[3], year, rate, Integer.parseInt(p[6].trim()));
            case "ELECTRIC" -> new ElectricCar(p[1], p[2], p[3], year, rate,
                    Integer.parseInt(p[6].trim()), Double.parseDouble(p[7].trim()));
            case "TRUCK" -> new Truck(p[1], p[2], p[3], year, rate, Double.parseDouble(p[6].trim()));
            default -> throw new IllegalArgumentException("Невідомий тип транспорту: " + type);
        };
    }
}
