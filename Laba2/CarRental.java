import java.util.Objects;

public class CarRental {
    private String carModel;
    private int days;
    private double pricePerDay;

    // Конструктор
    public CarRental(String carModel, int days, double pricePerDay) {
        this.carModel = carModel;
        this.days = days;
        this.pricePerDay = pricePerDay;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    @Override
    public String toString() {
        return String.format("Авто: %-15s | Днів: %2d | Ціна/день: $%5.2f", carModel, days, pricePerDay);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false;
        
        CarRental carRental = (CarRental) o;
        return days == carRental.days &&
               Double.compare(carRental.pricePerDay, pricePerDay) == 0 &&
               Objects.equals(carModel, carRental.carModel);
    }
}