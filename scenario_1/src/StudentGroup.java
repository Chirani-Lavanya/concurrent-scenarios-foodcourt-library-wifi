import java.util.*;

public class StudentGroup extends Thread {
    int groupSize;
    FoodCourt foodCourt;

    public StudentGroup(int groupSize, FoodCourt foodCourt) throws FoodCourtIllegalArgumentException {
        if (groupSize <= 0)
            throw new FoodCourtIllegalArgumentException("Invalid group size: " + groupSize);
        this.groupSize = groupSize;
        this.foodCourt = foodCourt;
    }

    @Override
    public void run() {
        try {
            Table table = foodCourt.occupyTable(groupSize);
            // Simulate eating time
            int eatingTime = (1 + new Random().nextInt(3)) * 1000;
            Thread.sleep(eatingTime);
            System.out.println("Student group of " + groupSize + " leaving " + table);
            foodCourt.leaveTable(table);
        } catch (InterruptedException e) {
            System.out.println("Student group of " + groupSize + " was interrupted.");
        }
    }
}