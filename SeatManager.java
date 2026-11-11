public class SeatManager {
    static final int ROWS = 10;
    static final int SEATS_PER_ROW = 10;

    private final boolean[][][] seats;

    public SeatManager(int numberOfMovies) {
        seats = new boolean[numberOfMovies][ROWS][SEATS_PER_ROW];

        seats[0][0][2] = true;
        seats[0][1][4] = true;
        seats[1][3][1] = true;
    }

    public boolean isBooked(int movie, int row, int seat) {
        return seats[movie][row][seat];
    }

    public void bookSeat(int movie, int row, int seat) {
        seats[movie][row][seat] = true;
    }

    public int getBookedCount(int movie) {
        int booked = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int seat = 0; seat < SEATS_PER_ROW; seat++) {
                if (seats[movie][row][seat]) {
                    booked++;
                }
            }
        }
        return booked;
    }
}
