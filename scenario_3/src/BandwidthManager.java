import java.util.*;

public class BandwidthManager {
    private final int totalBandwidth;
    private int allocatedBandwidth = 0;

    private final Map<String, StudentSession> activeSessions = new HashMap<>();
    private final Queue<StudentRequest> waitingQueue = new LinkedList<>();

    private final Object lock = new Object();

    public BandwidthManager(int totalBandwidth) {
        this.totalBandwidth = totalBandwidth;
    }

    // Activity priorities: academic(1), calls(2), streaming(3), gaming(4)
    public static int getActivityPriority(String activity) {
        return switch (activity.toLowerCase()) {
            case "academic" -> 1;
            case "calls" -> 2;
            case "streaming" -> 3;
            case "gaming" -> 4;
            default -> 0;
        };
    }

    public void requestBandwidth(String studentId, String activity, int bandwidth) throws InterruptedException {
        synchronized (lock) {
            System.out.println(studentId + " requests " + bandwidth + " for " + activity);

            StudentRequest request = new StudentRequest(studentId, activity, bandwidth, System.currentTimeMillis());

            // Queue the request
            waitingQueue.add(request);

            while (true) {
                if (!isNextInQueue(request)) {
                    lock.wait();
                } else if (allocatedBandwidth + bandwidth <= totalBandwidth) {
                    // Allocate bandwidth
                    allocatedBandwidth += bandwidth;
                    StudentSession session = activeSessions.computeIfAbsent(studentId, k -> new StudentSession(studentId));
                    session.addActivity(activity, bandwidth);
                    waitingQueue.remove(request);
                    System.out.println(studentId + " granted " + bandwidth + " for " + activity);
                    break;
                } else {
                    lock.wait();
                }
            }
        }
    }

    public void releaseActivity(String studentId, String activity) {
        synchronized (lock) {
            StudentSession session = activeSessions.get(studentId);
            if (session != null && session.hasActivity(activity)) {
                int bandwidth = session.getActivity(activity);
                allocatedBandwidth -= bandwidth;
                session.removeActivity(activity);
                System.out.println(studentId + " released " + bandwidth + " from " + activity);
                if (session.isEmpty()) {
                    activeSessions.remove(studentId);
                }
                lock.notifyAll();
            }
        }
    }

    public void connectionDropped(String studentId) {
        synchronized (lock) {
            StudentSession session = activeSessions.remove(studentId);
            if (session != null) {
                int released = session.totalBandwidth();
                allocatedBandwidth -= released;
                System.out.println(studentId + " disconnected, reclaiming " + released);
            }
            lock.notifyAll();
        }
    }

    private boolean isNextInQueue(StudentRequest request) {
        // Priority: lower number = higher priority
        return waitingQueue.stream()
                .allMatch(r -> comparePriority(request, r) <= 0);
    }

    private int comparePriority(StudentRequest r1, StudentRequest r2) {
        int p1 = r1.getPriority();
        int p2 = r2.getPriority();
        if (p1 != p2) return Integer.compare(p1, p2);
        // Fairness: older requests first
        return Long.compare(r1.getLastAccessTime(), r2.getLastAccessTime());
    }

    public void startSessionMonitor() {
        Thread monitor = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    long now = System.currentTimeMillis();
                    List<String> toRemove = new ArrayList<>();
                    for (Map.Entry<String, StudentSession> entry : activeSessions.entrySet()) {
                        if (now - entry.getValue().getLastAccess() > 30000) { // 30 sec idle timeout
                            toRemove.add(entry.getKey());
                        }
                    }
                    for (String studentId : toRemove) {
                        System.out.println("Connection recovery - Auto disconnecting " + studentId + " due to inactivity.");
                        connectionDropped(studentId);
                    }
                    lock.notifyAll();
                }
                try {
                    Thread.sleep(5000);  // Check every 5 seconds
                } catch (InterruptedException ignored) {}
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }

}