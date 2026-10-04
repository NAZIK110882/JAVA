import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CarRental[] rentals = new CarRental[2];

        System.out.println("--- Система прокату автомобілів (ЛР3) ---");

        try {
            for (int i = 0; i < 3; i++) {
                System.out.println("\nВведення даних для авто №" + (i + 1));
                
                System.out.print("Модель авто: ");
                String model = scanner.next();

                System.out.print("Кількість днів: ");
                int days = scanner.nextInt();

                System.out.print("Ціна за день: ");
                double price = scanner.nextDouble(); 

                rentals[i] = createRentalWithLogging(model, days, price);
                System.out.println("Успішно додано: " + rentals[i].toString());
            }

        } catch (InputMismatchException e) {
            System.out.println("ПОМИЛКА ВВЕДЕННЯ: Очікувалось число, а введено текст!");
            
        } catch (InvalidPriceException e) {
            System.out.println("ДОМЕННА ПОМИЛКА: " + e.getMessage());
            System.out.println("Ви ввели некоректну ціну: $" + e.getInvalidPrice());

        } catch (RentalException e) {
            System.out.println("ЗАГАЛЬНА ПОМИЛКА ОРЕНДИ: " + e.getMessage());
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("СИСТЕМНА ПОМИЛКА: Спроба зберегти авто, але в базі (масиві) більше немає місця!");
            
        } finally {
            System.out.println("\n[СИСТЕМА]: Закриття потоків вводу. Завершення роботи програми.");
            scanner.close();
        }
    }

    public static CarRental createRentalWithLogging(String model, int days, double price) throws RentalException {
        try {
            return new CarRental(model, days, price);
        } catch (RentalException e) {
            System.out.println("[LOG]: Спроба створення некоректного договору для авто " + model);
            throw e; 
        }
    }
}