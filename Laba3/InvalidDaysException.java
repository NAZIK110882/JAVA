public class InvalidDaysException extends RentalException {
    private int invalidDays;

    public InvalidDaysException(String message, int invalidDays) {
        super(message);
        this.invalidDays = invalidDays;
    }

    public int getInvalidDays() {
        return invalidDays;
    }
}