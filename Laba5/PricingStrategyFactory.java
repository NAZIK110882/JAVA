/** Creator in the GoF Factory Method pattern. */
public abstract class PricingStrategyFactory {
    public abstract PricingStrategy createStrategy();

    /** A single selection point; clients do not instantiate concrete creators. */
    public static PricingStrategyFactory forType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип тарифу не може бути null");
        }
        switch (type.toLowerCase(java.util.Locale.ROOT).trim()) {
            case "daily":
                return new DailyPricingFactory();
            case "hourly":
                return new HourlyPricingFactory();
            default:
                throw new IllegalArgumentException("Невідомий тип тарифу: " + type);
        }
    }
}
