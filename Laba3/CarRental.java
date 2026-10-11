public class CarRental {
    private String carModel;
    private int days;
    private double pricePerDay;

    public CarRental(String carModel, int days, double pricePerDay) throws InvalidDaysException, InvalidPriceException {
        if (days <= 0) {
            throw new InvalidDaysException("Кількість днів прокату має бути більшою за нуль!", days);
        }
        if (pricePerDay < 0) {
            throw new InvalidPriceException("Ціна за день не може бути від'ємною!", pricePerDay);
        }
        
        this.carModel = carModel;
        this.days = days;
        this.pricePerDay = pricePerDay;
    }

    @Override
    public String toString() {
        return String.format("Авто: %s | Днів: %d | Ціна/день: $%.2f", carModel, days, pricePerDay);
    }
}