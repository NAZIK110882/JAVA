public class CustomerNotificationObserver implements OrderObserver {
    @Override
    public void onStatusChanged(RentalOrder order, RentalStatus previous, RentalStatus current) {
        System.out.printf("[КЛІЄНТ] Замовлення %s: новий статус — %s%n", order.getOrderId(), current);
    }
}
