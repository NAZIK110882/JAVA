import java.util.ArrayList;
import java.util.List;

/** Plain Java checks; run with java RentalSystemTest (no external libraries). */
public class RentalSystemTest {
    private static int checks;
    private static void check(boolean condition, String message) {
        checks++;
        if (!condition) throw new AssertionError(message);
    }
    private static void approx(double actual, double expected) {
        check(Math.abs(actual - expected) < 0.000001, "Очікувалось " + expected + ", отримано " + actual);
    }
    private static final class FakeGateway extends LegacyPaymentGateway {
        String reference;
        long cents;
        @Override
        public boolean submitTransaction(String reference, long cents) {
            this.reference = reference;
            this.cents = cents;
            return true;
        }
    }
    public static void main(String[] args) {
        PricingStrategy daily = PricingStrategyFactory.forType("daily").createStrategy();
        PricingStrategy hourly = PricingStrategyFactory.forType("hourly").createStrategy();
        check(daily instanceof DailyPricingStrategy, "Невірна подобова фабрика");
        check(hourly instanceof HourlyPricingStrategy, "Невірна погодинна фабрика");
        approx(daily.calculateCost(100, 5), 500);
        approx(hourly.calculateCost(100, 5), 50);
        boolean unknownFailed = false;
        try { PricingStrategyFactory.forType("weekly"); }
        catch (IllegalArgumentException e) { unknownFailed = true; }
        check(unknownFailed, "Невідомий тариф має спричиняти помилку");

        RentalOrder order = new RentalOrder("TEST-1", new Car("Toyota", "Camry", 50), 5, daily);
        List<String> events = new ArrayList<>();
        OrderObserver first = (o, prev, next) -> events.add("log:" + prev + ":" + next);
        OrderObserver second = (o, prev, next) -> events.add("notify:" + prev + ":" + next);
        OrderObserver faulty = (o, prev, next) -> { throw new IllegalStateException("тестова помилка"); };
        order.addObserver(first);
        order.addObserver(faulty);
        order.addObserver(second);
        order.processOrder();
        check(order.getStatus() == RentalStatus.PROCESSED, "Невірний статус після обробки");
        check(events.size() == 2, "Помилка в одному Observer не повинна зупинити іншого");
        check(events.contains("log:NEW:PROCESSED"), "Не спрацював перший Observer");
        check(events.contains("notify:NEW:PROCESSED"), "Не спрацював другий Observer");
        order.removeObserver(faulty);
        order.removeObserver(second);
        order.setPricingStrategy(hourly);
        approx(order.getTotalCost(), 25);
        FakeGateway legacy = new FakeGateway();
        PaymentProcessor payment = new LegacyPaymentAdapter(legacy);
        check(order.payAndComplete(payment), "Оплата має пройти");
        check("TEST-1".equals(legacy.reference), "Adapter не передав reference");
        check(legacy.cents == 2500, "Adapter має переводити долари в центи");
        check(order.getStatus() == RentalStatus.COMPLETED, "Статус не оновився після оплати");
        check(events.contains("log:PROCESSED:COMPLETED"), "Observer не отримав завершення");
        check(events.size() == 3, "Відписаний Observer не має викликатися");
        boolean completedFailed = false;
        try { order.setPricingStrategy(daily); }
        catch (IllegalStateException e) { completedFailed = true; }
        check(completedFailed, "Завершене замовлення не можна змінювати");

        RentalOrder rejected = new RentalOrder("TEST-2", new Car("BMW", "X5", 120), 2, hourly);
        rejected.processOrder();
        check(!rejected.payAndComplete((id, total) -> false), "Оплата має відхилятися");
        check(rejected.getStatus() == RentalStatus.PROCESSED, "Відхилена оплата не завершує замовлення");
        System.out.println("Усі перевірки пройдено: " + checks);
    }
}
