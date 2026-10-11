public class Main {
    public static void main(String[] args) {
        Car car1 = new Car("Toyota", "Camry", 50.0);
        Car car2 = new Car("BMW", "X5", 120.0);

        System.out.println("=== Поліморфізм інтерфейсів у масиві ===");
        PricingStrategy[] strategies = {
            new DailyPricingStrategy(),
            new HourlyPricingStrategy()
        };

        for (PricingStrategy strategy : strategies) {
            strategy.printDescription(); 
            System.out.println("Розрахована вартість для базової ставки 100 за 5 одиниць часу: " 
                               + strategy.calculateCost(100, 5) + "$\n");
        }

        System.out.println("=== Патерн Strategy ===");
        
        RentalOrder order1 = new RentalOrder(car1, 5, new DailyPricingStrategy());
        order1.processOrder();

        System.out.println("-> Клієнт змінив тип оренди на погодинний (runtime зміна стратегії):");
        order1.setPricingStrategy(new HourlyPricingStrategy());
        
        RentalOrder order2 = new RentalOrder(car1, 12, new HourlyPricingStrategy()); 
        order2.processOrder();
    }
}