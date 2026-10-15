public interface OrderObserver {
    void onStatusChanged(RentalOrder order, RentalStatus previous, RentalStatus current);
}
