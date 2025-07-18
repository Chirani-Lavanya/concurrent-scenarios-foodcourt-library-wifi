import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
;
public class StudyRoom {
    private final int roomNumber;
    private final Map<String, Semaphore> timeSlots;

    public StudyRoom(int roomNumber, String[] timeSlots) {
        this.roomNumber = roomNumber;
        this.timeSlots = new HashMap<>();
        for (String slot : timeSlots) {
            this.timeSlots.put(slot, new Semaphore(1));
        }
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean bookRoom(String timeSlot) {
        Semaphore semaphore = timeSlots.get(timeSlot);
        return semaphore != null && semaphore.tryAcquire();
    }

    public void releaseRoom(String timeSlot) {
        Semaphore semaphore = timeSlots.get(timeSlot);
        if (semaphore != null) {
            semaphore.release();
        }
    }

    public boolean isAvailable(String timeSlot) {
        Semaphore semaphore = timeSlots.get(timeSlot);
        return semaphore != null && semaphore.availablePermits() > 0;
    }
}
