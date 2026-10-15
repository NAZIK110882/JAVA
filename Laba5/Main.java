public class Main {
    public static void main(String[] args) {
        Car car1 = new Car("Toyota", "Camry", 50.0);
        Car car2 = new Car("BMW", "X5", 120.0);

        System.out.println("=== Поліморфізм та Factory Method ===");
        String[] types = { "daily", "hourly" };
        PricingStrategy[] strategies = new PricingStrategy[types.length];
        for (int i = 0; i < types.length; i++) {
            strategies[i] = PricingStrategyFactory.forType(types[i]).createStrategy();
        }
        for (PricingStrategy strategy : strategies) {
            strategy.printDescription();
            System.out.println("Розрахована вартість для базової ставки 100 за 5 одиниць часу: "
                    + strategy.calculateCost(100, 5) + "$\n");
        }

        System.out.println("=== Strategy + Observer ===");
        RentalOrder order1 = new RentalOrder("ORD-001", car1, 5,
                PricingStrategyFactory.forType("daily").createStrategy());
        order1.addObserver(new OrderLogObserver());
        order1.addObserver(new CustomerNotificationObserver());
        order1.processOrder();

        System.out.println("-> Клієнт змінив тип оренди на погодинний (runtime зміна стратегії):");
        order1.setPricingStrategy(PricingStrategyFactory.forType("hourly").createStrategy());
        // As in lab 4, the strategy can change while the order is active.
        RentalOrder order2 = new RentalOrder("ORD-002", car1, 12,
                PricingStrategyFactory.forType("hourly").createStrategy());
        order2.processOrder();

        System.out.println("=== Adapter: оплата зовнішнім API ===");
        PaymentProcessor payments = new LegacyPaymentAdapter(new LegacyPaymentGateway());
        boolean paid = order1.payAndComplete(payments);
        System.out.println("Результат оплати: " + (paid ? "успішно" : "відхилено"));
        System.out.println("Підсумковий статус: " + order1.getStatus());
    }
}
