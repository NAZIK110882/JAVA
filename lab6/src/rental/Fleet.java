package rental;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * КОМПОЗИЦІЯ замість наслідування.
 * Автопарк МАЄ список транспорту (has-a), але НЕ Є списком (is-a хибне):
 * `class Fleet extends ArrayList<Vehicle>` відкрив би remove/clear/add(null),
 * не дав би підтримати інваріант «без дублікатів» і намертво прив'язав би
 * публічний API автопарку до ArrayList.
 */
public class Fleet {

    private final String name;
    private final List<Vehicle> vehicles = new ArrayList<>();

    public Fleet(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void add(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "vehicle");
        if (vehicles.contains(vehicle))
            throw new IllegalStateException("Транспорт уже є в автопарку: " + vehicle.getLicensePlate());
        vehicles.add(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);   // назовні — лише читання
    }

    public double totalCost(int days) {
        double sum = 0;
        for (Vehicle v : vehicles) sum += v.calculateRentalCost(days);   // поліморфізм
        return sum;
    }

    public Optional<Vehicle> cheapest(int days) {
        Vehicle best = null;
        for (Vehicle v : vehicles)
            if (best == null || v.calculateRentalCost(days) < best.calculateRentalCost(days)) best = v;
        return Optional.ofNullable(best);
    }

    public String getName() { return name; }

    @Override
    public String toString() {
        return "Fleet '" + name + "' (" + vehicles.size() + " од.)";
    }
}
