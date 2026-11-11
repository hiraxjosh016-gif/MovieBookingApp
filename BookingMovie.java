public class BookingMovie {
    private final String title;
    private final String duration;
    private final String startTime;
    private final double ticketPrice;

    public BookingMovie(String title, String duration, String startTime, double ticketPrice) {
        this.title = title;
        this.duration = duration;
        this.startTime = startTime;
        this.ticketPrice = ticketPrice;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getStartTime() {
        return startTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return title;
    }
}
