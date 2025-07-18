public class Main {
    public static void main(String[] args) throws InterruptedException {
        BandwidthManager manager = new BandwidthManager(50);
        manager.startSessionMonitor();  // Start monitor

        Thread student1 = new Thread(() -> {
            String studentId = "Student-1";
            try {
                manager.requestBandwidth(studentId, "academic", 25);
                Thread.sleep(4000);
                manager.requestBandwidth(studentId, "streaming", 10);
                Thread.sleep(7000);
                manager.releaseActivity(studentId, "streaming");
                Thread.sleep(2000);
                manager.releaseActivity(studentId, "academic");
            } catch (InterruptedException e) {
                System.out.println(studentId + " interrupted.");
            }
        });

        Thread student2 = new Thread(() -> {
            String studentId = "Student-2";
            try {
                manager.requestBandwidth(studentId, "calls", 20);
                Thread.sleep(8000);
                manager.releaseActivity(studentId, "calls");
            } catch (InterruptedException e) {
                System.out.println(studentId + " interrupted.");
            }
        });

        Thread student3 = new Thread(() -> {
            String studentId = "Student-3";
            try {
                manager.requestBandwidth(studentId, "gaming", 15);
                Thread.sleep(5000);
                manager.releaseActivity(studentId, "gaming");
            } catch (InterruptedException e) {
                System.out.println(studentId + " interrupted.");
            }
        });

        // Start all threads
        student1.start();
        student2.start();
        student3.start();

        // Wait for all to complete
        student1.join();
        student2.join();
        student3.join();

        System.out.println("Completed bandwidth allocation.");
    }
}
