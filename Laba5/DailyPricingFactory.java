public class DailyPricingFactory extends PricingStrategyFactory {
    @Override
    public PricingStrategy createStrategy() {
        return new DailyPricingStrategy();
    }
}
