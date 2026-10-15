public class HourlyPricingFactory extends PricingStrategyFactory {
    @Override
    public PricingStrategy createStrategy() {
        return new HourlyPricingStrategy();
    }
}
