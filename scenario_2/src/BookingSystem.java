import java.util.*;
import java.util.concurrent.*;

public class BookingSystem {
    private final List<StudyRoom> studyRooms;
    private final String[] timeSlots;

    public BookingSystem(int numberOfRooms, String[] timeSlots) {
        this.studyRooms = new CopyOnWriteArrayList<>();
        this.timeSlots = timeSlots;
        for (int i = 0; i < numberOfRooms; i++) {
            studyRooms.add(new StudyRoom(i + 1, timeSlots));
        }
    }

    public StudyRoom bookAnyAvailableRoom(String timeSlot) {
        for (StudyRoom room : studyRooms) {
            if (room.bookRoom(timeSlot)) {
                return room;
            }
        }
        return null;
    }

    public void releaseRoom(StudyRoom room, String timeSlot) {
        room.releaseRoom(timeSlot);
    }

    public String[] getTimeSlots() {
        return timeSlots;
    }

    public List<StudyRoom> getStudyRooms() {
        return studyRooms;
    }
}
