import java.util.Scanner;

public class Task3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть температуру в °C: ");
        double celsius = scanner.nextDouble();

        double fahrenheit = celsius * 9.0 / 5.0 + 32;
        double kelvin = celsius + 273.15;

        System.out.println("==== ТЕМПЕРАТУРА ====");
        System.out.printf("%.2f °C%n", celsius);
        System.out.printf("%.2f °F%n", fahrenheit);
        System.out.printf("%.2f K%n", kelvin);

        scanner.close();
    }
}