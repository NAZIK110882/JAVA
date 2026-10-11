public interface PricingStrategy {
    double calculateCost(double baseRate, int duration);

    default void printDescription() {
        System.out.println("Стандартна політика розрахунку вартості оренди.");
    }
}