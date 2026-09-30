import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть назву товару: ");
        String product = scanner.nextLine();

        System.out.print("Введіть ціну: ");
        double price = scanner.nextDouble();

        System.out.print("Введіть кількість: ");
        int quantity = scanner.nextInt();

        System.out.print("Введіть знижку (%): ");
        double discount = scanner.nextDouble();

        double total = price * quantity;
        double discountAmount = total * discount / 100;
        double finalPrice = total - discountAmount;

        System.out.println("========== РАХУНОК ==========");
        System.out.printf("Товар: %s%n", product);
        System.out.printf("Ціна: %.2f грн%n", price);
        System.out.printf("Кількість: %d%n", quantity);
        System.out.printf("Вартість без знижки: %.2f грн%n", total);
        System.out.printf("Знижка: %.2f грн%n", discountAmount);
        System.out.printf("До сплати: %.2f грн%n", finalPrice);

        scanner.close();
    }
}