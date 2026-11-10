import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MovieTicketBooking extends JFrame {
    private static final BookingMovie[] MOVIES = {
        new BookingMovie("The Last Adventure", "2 hours 10 minutes", "7:00 PM", 8.02),
        new BookingMovie("Avengers", "2 hours", "2:00 PM", 8.02)
    };

    private final SeatManager seatManager = new SeatManager(MOVIES.length);
    private final JButton[][] seatButtons =
            new JButton[SeatManager.ROWS][SeatManager.SEATS_PER_ROW];
    private final JLabel availabilityLabel = new JLabel();
    private final JLabel movieDetailsLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel movieDurationLabel = new JLabel("", SwingConstants.CENTER);
    private final JComboBox<BookingMovie> movieChoices = new JComboBox<>(MOVIES);
    private final JLabel selectionLabel = new JLabel("Select one or more available seats.");
    private final JComboBox<String> paymentMethods =
            new JComboBox<>(new String[] {"Cash", "Card", "Mobile money"});

    private final boolean[][] selected =
            new boolean[SeatManager.ROWS][SeatManager.SEATS_PER_ROW];
    private int selectedMovie;

    public MovieTicketBooking() {
        setTitle("Movie Ticket Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(260, 570);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        add(createMovieInfo(), BorderLayout.NORTH);
        add(createSeatArea(), BorderLayout.CENTER);
        add(createBookingControls(), BorderLayout.SOUTH);
        movieChoices.addActionListener(event -> changeMovie());
        updateMovieInfo();
    }

    private JPanel createMovieInfo() {
        JPanel movieInfo = new JPanel(new GridLayout(5, 1, 0, 4));
        movieInfo.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JLabel title = new JLabel("Movie Ticket Booking", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        movieInfo.add(title);
        movieInfo.add(movieChoices);
        movieInfo.add(movieDetailsLabel);
        movieInfo.add(movieDurationLabel);
        movieInfo.add(availabilityLabel);
        availabilityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return movieInfo;
    }

    private JPanel createSeatArea() {
        JPanel seatArea = new JPanel(new BorderLayout(8, 8));
        seatArea.setBorder(BorderFactory.createEmptyBorder(5, 25, 5, 25));

        JLabel screen = new JLabel("SCREEN", SwingConstants.CENTER);
        screen.setOpaque(true);
        screen.setBackground(Color.LIGHT_GRAY);
        screen.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        seatArea.add(screen, BorderLayout.NORTH);

        JPanel seatGrid = new JPanel(new GridLayout(
                SeatManager.ROWS + 1, SeatManager.SEATS_PER_ROW + 1, 6, 6));
        seatGrid.add(new JLabel(""));

        for (int seat = 1; seat <= SeatManager.SEATS_PER_ROW; seat++) {
            seatGrid.add(new JLabel(String.valueOf(seat), SwingConstants.CENTER));
        }

        for (int row = 0; row < SeatManager.ROWS; row++) {
            seatGrid.add(new JLabel(String.valueOf((char) ('A' + row)), SwingConstants.CENTER));

            for (int seat = 0; seat < SeatManager.SEATS_PER_ROW; seat++) {
                final int currentRow = row;
                final int currentSeat = seat;
                JButton seatButton = new JButton();
                seatButton.setFocusPainted(false);
                seatButton.setOpaque(true);
                seatButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                seatButton.addActionListener(event -> selectSeat(currentRow, currentSeat));
                seatButtons[row][seat] = seatButton;
                seatGrid.add(seatButton);
                updateSeatButton(row, seat);
            }
        }

        seatArea.add(seatGrid, BorderLayout.CENTER);
        return seatArea;
    }

    private JPanel createBookingControls() {
        JPanel controls = new JPanel(new GridLayout(3, 1, 5, 5));
        controls.setBorder(BorderFactory.createEmptyBorder(5, 25, 15, 25));
        controls.add(selectionLabel);

        JPanel paymentPanel = new JPanel();
        paymentPanel.add(new JLabel("Payment method:"));
        paymentPanel.add(paymentMethods);
        controls.add(paymentPanel);

        JButton bookButton = new JButton("Book selected seats");
        bookButton.addActionListener(event -> bookSelectedSeat());
        controls.add(bookButton);

        return controls;
    }

    private void selectSeat(int row, int seat) {
        if (seatManager.isBooked(selectedMovie, row, seat)) {
            JOptionPane.showMessageDialog(this, "That seat is already booked.");
            return;
        }

        // Each click toggles only this seat; other selected seats are untouched
        selected[row][seat] = !selected[row][seat];
        updateSeatButton(row, seat);
        updateSelectionLabel();
    }

    private int countSelected() {
        int count = 0;
        for (int row = 0; row < SeatManager.ROWS; row++) {
            for (int seat = 0; seat < SeatManager.SEATS_PER_ROW; seat++) {
                if (selected[row][seat]) {
                    count++;
                }
            }
        }
        return count;
    }

    private String selectedSeatNames() {
        StringBuilder names = new StringBuilder();
        for (int row = 0; row < SeatManager.ROWS; row++) {
            for (int seat = 0; seat < SeatManager.SEATS_PER_ROW; seat++) {
                if (selected[row][seat]) {
                    if (names.length() > 0) {
                        names.append(", ");
                    }
                    names.append((char) ('A' + row)).append(seat + 1);
                }
            }
        }
        return names.toString();
    }

    private void updateSelectionLabel() {
        int count = countSelected();
        if (count == 0) {
            selectionLabel.setText("Select one or more available seats.");
        } else {
            double total = MOVIES[selectedMovie].getTicketPrice() * count;
            selectionLabel.setText("Selected: " + selectedSeatNames() + " (" + count
                    + (count == 1 ? " seat" : " seats") + ") - Total: "
                    + String.format("$%.2f", total));
        }
    }

    private void clearSelection() {
        for (int row = 0; row < SeatManager.ROWS; row++) {
            for (int seat = 0; seat < SeatManager.SEATS_PER_ROW; seat++) {
                selected[row][seat] = false;
            }
        }
        updateSelectionLabel();
    }

    private void bookSelectedSeat() {
        int count = countSelected();
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Please select at least one available seat first.");
            return;
        }

        // Read everything for the receipt BEFORE the selection is cleared
        String seatNames = selectedSeatNames();
        String paymentMethod = (String) paymentMethods.getSelectedItem();
        BookingMovie movie = MOVIES[selectedMovie];
        double total = movie.getTicketPrice() * count;

        for (int row = 0; row < SeatManager.ROWS; row++) {
            for (int seat = 0; seat < SeatManager.SEATS_PER_ROW; seat++) {
                if (selected[row][seat]) {
                    seatManager.bookSeat(selectedMovie, row, seat);
                    selected[row][seat] = false;
                    updateSeatButton(row, seat);
                }
            }
        }
        updateAvailability();
        updateSelectionLabel();

        JOptionPane.showMessageDialog(
                this,
                "Booking confirmed!\nMovie: " + movie.getTitle()
                        + "\nDuration: " + movie.getDuration()
                        + (movie.getStartTime().isEmpty()
                                ? "" : "\nStart time: " + movie.getStartTime())
                        + "\nSeats: " + seatNames + " (" + count + ")"
                        + "\nPrice per ticket: " + String.format("$%.2f", movie.getTicketPrice())
                        + "\nTotal: " + String.format("$%.2f", total)
                        + "\nPayment method: " + paymentMethod
                        + "\nPayment is simulated; no money was charged.");
    }

    private void updateSeatButton(int row, int seat) {
        JButton seatButton = seatButtons[row][seat];
        if (seatManager.isBooked(selectedMovie, row, seat)) {
            seatButton.setText("X");
            seatButton.setBackground(Color.LIGHT_GRAY);
            seatButton.setEnabled(false);
        } else if (selected[row][seat]) {
            seatButton.setText("Selected");
            seatButton.setBackground(new Color(120, 180, 255));
            seatButton.setEnabled(true);
        } else {
            seatButton.setText("O");
            seatButton.setBackground(new Color(170, 230, 170));
            seatButton.setEnabled(true);
        }
    }

    private void changeMovie() {
        selectedMovie = movieChoices.getSelectedIndex();
        clearSelection();
        updateMovieInfo();

        for (int row = 0; row < SeatManager.ROWS; row++) {
            for (int seat = 0; seat < SeatManager.SEATS_PER_ROW; seat++) {
                updateSeatButton(row, seat);
            }
        }
    }

    private void updateMovieInfo() {
        BookingMovie movie = MOVIES[selectedMovie];
        movieDetailsLabel.setText("Movie: " + movie.getTitle());
        String showtime = movie.getStartTime().isEmpty()
                ? "" : "    Start time: " + movie.getStartTime();
        movieDurationLabel.setText("Duration: " + movie.getDuration() + showtime
                + "    Ticket price: " + String.format("$%.2f", movie.getTicketPrice()));
        updateAvailability();
    }

    private void updateAvailability() {
        int booked = seatManager.getBookedCount(selectedMovie);
        int total = SeatManager.ROWS * SeatManager.SEATS_PER_ROW;
        availabilityLabel.setText("Available seats: " + (total - booked) + "    Booked seats: " + booked);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MovieTicketBooking().setVisible(true));
    }
}
