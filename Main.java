<<<<<<< HEAD
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
=======
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Система прокату автомобілів ---");

        System.out.print("Введіть марку та модель авто: ");
        String carModel = scanner.nextLine();

        System.out.print("Введіть кількість днів прокату: ");
        int days = scanner.nextInt();

        System.out.print("Введіть вартість оренди за день ($): ");
        double pricePerDay = scanner.nextDouble();

        System.out.print("Введіть розмір персональної знижки (%): ");
        double discount = scanner.nextDouble();

        double totalCost = days * pricePerDay;
        double discountAmount = totalCost * (discount / 100.0);
        double finalPrice = totalCost - discountAmount;

        System.out.println("\n--- Чек оренди ---");
        System.out.printf("Автомобіль: %s%n", carModel);
        System.out.printf("Тривалість: %d днів%n", days);
        System.out.printf("Базова вартість: $%.2f%n", totalCost);
        System.out.printf("Знижка: $%.2f (%.1f%%)%n", discountAmount, discount);
        System.out.printf("До сплати: $%.2f%n", finalPrice);

        scanner.close();
>>>>>>> 9509f5443b5f97459dbef37ca36fa1d7592905db
    }
}