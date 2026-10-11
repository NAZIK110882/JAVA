public class Car extends AbstractVehicle {
    
    public Car(String brand, String model, double baseRate) {
        super(brand, model, baseRate);
    }

    @Override
    public String getVehicleCategory() {
        return "Легковий автомобіль";
    }

    @Override
    public void performMaintenance() {
        System.out.println("Обслуговування авто: перевірка мастила, тиску в шинах та мийка.");
    }
}