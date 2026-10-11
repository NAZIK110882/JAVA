public class RentalOrder {
    private Car car;
    private int duration;
    private PricingStrategy pricingStrategy; 

    public RentalOrder(Car car, int duration, PricingStrategy pricingStrategy) {
        this.car = car;
        this.duration = duration;
        this.pricingStrategy = pricingStrategy;
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void processOrder() {
        car.printVehicleInfo();
        pricingStrategy.printDescription();
        double totalCost = pricingStrategy.calculateCost(car.baseRate, duration);
        System.out.println("Загальна вартість оренди: " + totalCost + "$\n");
    }
}