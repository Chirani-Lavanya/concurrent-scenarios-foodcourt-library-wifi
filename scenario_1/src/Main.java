import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize food court with 15 tables
            FoodCourt foodCourt = new FoodCourt(3, 7, 5);

            // Generate 50 student groups of random sizes (1-6)
            List<StudentGroup> groups = new ArrayList<>();

            for (int i = 0; i < 50; i++) {
                int groupSize = 1 + new Random().nextInt(6);
                groups.add(new StudentGroup(groupSize, foodCourt));
            }

            // Start all student group threads
            for (StudentGroup group : groups) {
                group.start();
                Thread.sleep(100); // delay arrivals slightly
            }

            // Wait for all groups to finish
            for (StudentGroup group : groups) {
                group.join();
            }

            System.out.println("All student groups finished dining.");

        } catch (FoodCourtIllegalArgumentException | InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }}