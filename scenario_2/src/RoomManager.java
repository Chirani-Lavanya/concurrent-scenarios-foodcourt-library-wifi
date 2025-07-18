
public class RoomManager implements Runnable {
    private final BookingSystem bookingSystem;

    public RoomManager(BookingSystem bookingSystem) {
        this.bookingSystem = bookingSystem;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5000); // Check every 5 seconds
                System.out.println("\n=== Room Manager Report ===");
                int total = 0, occupied = 0;
                for (StudyRoom room : bookingSystem.getStudyRooms()) {
                    for (String slot : bookingSystem.getTimeSlots()) {
                        total++;
                        if (!room.isAvailable(slot)) {
                            occupied++;
                        }
                    }
                }
                System.out.println("Occupied slots: " + occupied + "/" + total);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

