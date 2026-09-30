import java.util.Scanner;

public class Task1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть ім'я: ");
        String name = scanner.nextLine();

        System.out.print("Введіть прізвище: ");
        String surname = scanner.nextLine();

        System.out.print("Введіть вік: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Очищення залишку рядка

        System.out.print("Введіть групу: ");
        String group = scanner.nextLine();

        System.out.print("Введіть середній бал: ");
        double averageScore = scanner.nextDouble();

        System.out.println("========== АНКЕТА ==========");
        System.out.printf("Студент: %s %s%n", name, surname);
        System.out.printf("Вік: %d%n", age);
        System.out.printf("Група: %s%n", group);
        System.out.printf("Середній бал: %.2f%n", averageScore);

        scanner.close();
    }
}