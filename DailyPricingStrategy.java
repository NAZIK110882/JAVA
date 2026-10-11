public class DailyPricingStrategy implements PricingStrategy {
    @Override
    public double calculateCost(double baseRate, int days) {
        return baseRate * days;
    }

    @Override
    public void printDescription() {
        System.out.println("Подобова оренда: тарифікація за кожен день.");
    }
}