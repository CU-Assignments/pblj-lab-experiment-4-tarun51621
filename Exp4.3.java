import java.util.Arrays;
class TicketBookingSystem {
    private final boolean[] seats;
    public TicketBookingSystem(int numSeats) {
        seats = new boolean[numSeats];
        Arrays.fill(seats, false);
    }
    public synchronized void bookSeat(String user, int seatNumber, boolean isVIP) {
        if (seatNumber < 1 || seatNumber > seats.length) {
            System.out.println("Invalid seat number!");
            return;
        }
        if (seats[seatNumber - 1]) {
            System.out.println(user + ": Seat " + seatNumber + " is already booked!");
            return;
        }
        seats[seatNumber - 1] = true;
        System.out.println(user + " booked seat " + seatNumber);
    }
}
class UserThread extends Thread {
    private final TicketBookingSystem system;
    private final int seatNumber;
    private final boolean isVIP;
    public UserThread(TicketBookingSystem system, String name, int seatNumber, boolean isVIP) {
        super(name);
        this.system = system;
        this.seatNumber = seatNumber;
        this.isVIP = isVIP;
        if (isVIP) setPriority(MAX_PRIORITY);
        else setPriority(NORM_PRIORITY);
    }
    public void run() {
        system.bookSeat(getName(), seatNumber, isVIP);
    }
}
public class Main {
    public static void main(String[] args) {
        TicketBookingSystem system = new TicketBookingSystem(5);

        // Test Case 1: No Seats Available Initially
        // Expected Output: No bookings yet. (No users attempting)
        // Test Case 2: Successful Booking
        new UserThread(system, "Anish (VIP)", 1, true).start();
        new UserThread(system, "Bobby (Regular)", 2, false).start();
        new UserThread(system, "Charlie (VIP)", 3, true).start();
        // Test Case 3: Thread Priorities (VIP First)
        new UserThread(system, "Bobby (Regular)", 4, false).start();
        new UserThread(system, "Anish (VIP)", 4, true).start();
        // Test Case 4: Preventing Double Booking
        new UserThread(system, "Anish (VIP)", 1, true).start();
        new UserThread(system, "Bobby (Regular)", 1, false).start();
        // Test Case 5: Booking After All Seats Are Taken
        new UserThread(system, "New User (Regular)", 3, false).start();
        // Test Case 6: Invalid Seat Selection
        new UserThread(system, "User1", 0, false).start();
        new UserThread(system, "User2", 6, false).start();
        // Test Case 7: Simultaneous Bookings (Concurrency Test)
        for (int i = 0; i < 10; i++) {
            int seat = (i % 5) + 1;
            new UserThread(system, "User" + (i + 1), seat, i % 2 == 0).start();}}}

