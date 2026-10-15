/** The interface expected by our car-rental application. */
public interface PaymentProcessor {
    boolean pay(String orderId, double amountInDollars);
}
