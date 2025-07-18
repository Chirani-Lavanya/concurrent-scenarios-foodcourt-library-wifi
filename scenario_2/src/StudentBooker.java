public class StudentBooker implements Runnable {
    private final String studentName;
    private final BookingSystem bookingSystem;

    public StudentBooker(String studentName, BookingSystem bookingSystem) {
        this.studentName = studentName;
        this.bookingSystem = bookingSystem;
    }

    @Override
    public void run() {
        String[] timeSlots = bookingSystem.getTimeSlots();
        while (true) {
            for (String timeSlot : timeSlots) {
                StudyRoom room = bookingSystem.bookAnyAvailableRoom(timeSlot);
                if (room != null) {
                    System.out.println(studentName + " booked room " + room.getRoomNumber() + " for " + timeSlot);
                    try {
                        Thread.sleep(2000); // Simulate study time
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    bookingSystem.releaseRoom(room, timeSlot);
                    System.out.println(studentName + " released room " + room.getRoomNumber() + " for " + timeSlot);
                    return;
                } else {
                    System.out.println(studentName + " couldn't book any room for " + timeSlot + ". Retrying...");
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
