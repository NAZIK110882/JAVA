import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RentalOrder {
    private final String orderId;
    private final Car car;
    private final int duration;
    private PricingStrategy pricingStrategy;
    private RentalStatus status = RentalStatus.NEW;
    private final List<OrderObserver> observers = new ArrayList<>();

    /** Kept for compatibility with labs 1–4. */
    public RentalOrder(Car car, int duration, PricingStrategy pricingStrategy) {
        this("ORDER", car, duration, pricingStrategy);
    }

    public RentalOrder(String orderId, Car car, int duration, PricingStrategy pricingStrategy) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Ідентифікатор замовлення не може бути порожнім");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Тривалість оренди має бути додатною");
        }
        this.orderId = orderId;
        this.car = Objects.requireNonNull(car, "car");
        this.duration = duration;
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy, "pricingStrategy");
    }

    public String getOrderId() { return orderId; }
    public RentalStatus getStatus() { return status; }

    public double getTotalCost() {
        return pricingStrategy.calculateCost(car.baseRate, duration);
    }

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        if (status == RentalStatus.COMPLETED) {
            throw new IllegalStateException("Не можна змінити тариф завершеної оренди");
        }
        this.pricingStrategy = Objects.requireNonNull(pricingStrategy, "pricingStrategy");
    }

    public void addObserver(OrderObserver observer) {
        Objects.requireNonNull(observer, "observer");
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }

    private void changeStatus(RentalStatus newStatus) {
        if (status == newStatus) return;
        RentalStatus previous = status;
        status = newStatus;
        for (OrderObserver observer : new ArrayList<>(observers)) {
            try {
                observer.onStatusChanged(this, previous, newStatus);
            } catch (RuntimeException ex) {
                // One listener failure does not prevent other notifications.
                System.err.println("Помилка спостерігача: " + ex.getMessage());
            }
        }
    }

    public void processOrder() {
        if (status == RentalStatus.COMPLETED) {
            throw new IllegalStateException("Замовлення вже завершено");
        }
        car.printVehicleInfo();
        pricingStrategy.printDescription();
        System.out.println("Загальна вартість оренди: " + getTotalCost() + "$\n");
        changeStatus(RentalStatus.PROCESSED);
    }

    public boolean payAndComplete(PaymentProcessor processor) {
        Objects.requireNonNull(processor, "processor");
        if (status != RentalStatus.PROCESSED) {
            throw new IllegalStateException("Перед оплатою потрібно обробити замовлення");
        }
        boolean success = processor.pay(orderId, getTotalCost());
        if (success) changeStatus(RentalStatus.COMPLETED);
        return success;
    }
}
