public class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public double calculateCost(double baseRate, int hours) {
        return (baseRate * 0.1) * hours;
    }
    
    @Override
    public void printDescription() {
        System.out.println("Погодинна оренда: ідеально для короткострокових поїздок.");
    }
}