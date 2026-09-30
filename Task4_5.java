import java.util.Scanner;

public class Task4_5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть ім'я: ");
        String name = scanner.nextLine();

        System.out.print("Введіть прізвище: ");
        String surname = scanner.nextLine();

        System.out.print("Введіть вік: ");
        int age = scanner.nextInt();

        System.out.print("Введіть зріст (м): ");
        double height = scanner.nextDouble();

        System.out.print("Введіть вагу (кг): ");
        double weight = scanner.nextDouble();

        // Розрахунок BMI
        double bmi = weight / (height * height);
        String category = "";

        // Визначення категорії
        if (bmi < 18.5) {
            category = "Недостатня вага";
        } else if (bmi >= 18.5 && bmi < 25) {
            category = "Нормальна вага";
        } else if (bmi >= 25 && bmi < 30) {
            category = "Надлишкова вага";
        } else {
            category = "Ожиріння";
        }

        System.out.println("========== PERSONAL PROFILE ==========");
        System.out.printf("Ім'я: %s%n", name);
        System.out.printf("Прізвище: %s%n", surname);
        System.out.printf("Вік: %d%n", age);
        System.out.printf("Зріст: %.2f м%n", height);
        System.out.printf("Вага: %.2f кг%n", weight);
        System.out.printf("BMI: %.2f (%s)%n", bmi, category);

        scanner.close();
    }
}