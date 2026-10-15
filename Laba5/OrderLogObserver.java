public class OrderLogObserver implements OrderObserver {
    @Override
    public void onStatusChanged(RentalOrder order, RentalStatus previous, RentalStatus current) {
        System.out.printf("[ЛОГ] Замовлення %s: %s -> %s%n", order.getOrderId(), previous, current);
    }
}
