public class StudentRequest {
    private final String studentId;
    private final String activity;
    private final int bandwidth;
    private final long lastAccessTime;

    public StudentRequest(String studentId, String activity, int bandwidth, long lastAccessTime) {
        this.studentId = studentId;
        this.activity = activity;
        this.bandwidth = bandwidth;
        this.lastAccessTime = lastAccessTime;
    }

    public int getPriority() {
        return BandwidthManager.getActivityPriority(activity);
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }
}
