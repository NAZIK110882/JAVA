/** Simulated external/legacy API: its signature cannot be edited. */
public class LegacyPaymentGateway {
    public boolean submitTransaction(String reference, long amountInCents) {
        System.out.printf("[LEGACY API] Платіж %s на %d центів прийнято%n", reference, amountInCents);
        return true;
    }
}
