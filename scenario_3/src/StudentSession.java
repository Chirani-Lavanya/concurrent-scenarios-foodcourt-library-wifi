import java.util.*;

public class StudentSession {
    private final String studentId;
    private final Map<String, Integer> activities = new HashMap<>();
    private long lastAccess;

    public StudentSession(String studentId) {
        this.studentId = studentId;
        this.lastAccess = System.currentTimeMillis();
    }

    public void addActivity(String activity, int bandwidth) {
        activities.put(activity, bandwidth);
        updateLastAccess();
    }

    public int removeActivity(String activity) {
        int bw = activities.remove(activity);
        updateLastAccess();
        return bw;
    }

    public boolean hasActivity(String activity) {
        return activities.containsKey(activity);
    }

    public int getActivity(String activity) {
        return activities.get(activity);
    }

    public boolean isEmpty() {
        return activities.isEmpty();
    }

    public int totalBandwidth() {
        return activities.values().stream().mapToInt(Integer::intValue).sum();
    }

    public long getLastAccess() {
        return lastAccess;
    }

    private void updateLastAccess() {
        lastAccess = System.currentTimeMillis();
    }
}
