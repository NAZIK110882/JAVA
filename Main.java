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
    }
}