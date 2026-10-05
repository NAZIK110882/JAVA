import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Скільки договорів прокату хочете додати? ");
        int count = scanner.nextInt();
        scanner.nextLine();

        CarRental[] rentals = new CarRental[count];

        for (int i = 0; i < rentals.length; i++) {
            System.out.println("\n--- Договір №" + (i + 1) + " ---");
            System.out.print("Марка та модель: ");
            String model = scanner.nextLine();

            System.out.print("Кількість днів: ");
            int days = scanner.nextInt();

            System.out.print("Ціна за день ($): ");
            double price = scanner.nextDouble();
            scanner.nextLine();

            rentals[i] = new CarRental(model, days, price);
        }

        System.out.println("\n--- УСІ ДОГОВОРИ ПРОКАТУ ---");
        for (CarRental rental : rentals) {
            System.out.println(rental.toString());
        }

        int longTermRentals = 0;
        for (CarRental rental : rentals) {
            if (rental.getPricePerDay() > 50.0) {
                longTermRentals++;
            }
        }
        System.out.println("\nКількість авто з ціною оренди дорожче $50/день: " + longTermRentals);

        System.out.println("\n--- СОРТУВАННЯ ЗА ЦІНОЮ (Бульбашка) ---");
        for (int i = 0; i < rentals.length - 1; i++) {
            for (int j = 0; j < rentals.length - 1 - i; j++) {
                if (rentals[j].getPricePerDay() > rentals[j + 1].getPricePerDay()) {
                    CarRental temp = rentals[j];
                    rentals[j] = rentals[j + 1];
                    rentals[j + 1] = temp;
                }
            }
        }

        for (CarRental rental : rentals) {
            System.out.println(rental.toString());
        }

        System.out.println("\n--- ПОШУК ЗА ЗРАЗКОМ ---");
        CarRental targetRental = new CarRental("Volkswagen Golf", 5, 120.0);
        System.out.println("Шукаємо: " + targetRental.toString());

        boolean isFound = false;
        for (int i = 0; i < rentals.length; i++) {
            if (rentals[i].equals(targetRental)) {
                System.out.println("Знайдено! Збіг на індексі: " + i);
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            System.out.println("Такого договору в базі не знайдено.");
        }

        scanner.close();
    }
}
