import java.math.BigDecimal;

/** Object adapter translating dollars into cents and renaming the method. */
public class LegacyPaymentAdapter implements PaymentProcessor {
    private final LegacyPaymentGateway gateway;

    public LegacyPaymentAdapter(LegacyPaymentGateway gateway) {
        this.gateway = java.util.Objects.requireNonNull(gateway, "gateway");
    }

    @Override
    public boolean pay(String orderId, double amountInDollars) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Потрібний ідентифікатор замовлення");
        }
        if (!Double.isFinite(amountInDollars) || amountInDollars < 0) {
            throw new IllegalArgumentException("Сума платежу має бути невід'ємною та скінченною");
        }
        long cents = BigDecimal.valueOf(amountInDollars)
                .movePointRight(2)
                .setScale(0, java.math.RoundingMode.HALF_UP)
                .longValueExact();
        return gateway.submitTransaction(orderId, cents);
    }
}
