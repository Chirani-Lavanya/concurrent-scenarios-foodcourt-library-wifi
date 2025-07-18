import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        String[] timeSlots = {"9:00-10:00", "10:00-11:00", "11:00-12:00"};
        BookingSystem bookingSystem = new BookingSystem(8, timeSlots);
        // Start RoomManager thread
        Thread managerThread = new Thread(new RoomManager(bookingSystem));
        managerThread.start();

        // Start student booker threads
        for (int i = 0; i < 20; i++) {
            new Thread(new StudentBooker("Student " + (i + 1), bookingSystem)).start();
        }
    }
}