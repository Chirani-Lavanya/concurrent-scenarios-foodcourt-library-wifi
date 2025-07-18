import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

public class FoodCourt {
    List<Table> tables;
    Lock lock;
    Condition isTableAvailable;

    public FoodCourt(int twoSeaters, int fourSeaters, int sixSeaters) throws FoodCourtIllegalArgumentException {
        int numberOfTables = twoSeaters + fourSeaters + sixSeaters;
        if (numberOfTables == 0)
            throw new FoodCourtIllegalArgumentException("No tables available in the food court!");

        tables = new ArrayList<>();
        lock = new ReentrantLock();
        isTableAvailable = lock.newCondition();
        // Create tables
        int id = 1;
        for (int i = 0; i < twoSeaters; i++) tables.add(new Table(id++, 2));
        for (int i = 0; i < fourSeaters; i++) tables.add(new Table(id++, 4));
        for (int i = 0; i < sixSeaters; i++) tables.add(new Table(id++, 6));
    }

    public Table occupyTable(int groupSize) throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                Optional<Table> table = findOptimalTable(groupSize);
                if (table.isPresent()) {
                    table.get().isAvailable = false;
                    System.out.println("Group of " + groupSize + " seated at " + table.get());
                    return table.get();
                }
                System.out.println("Group of " + groupSize + " waiting for a table...");
                isTableAvailable.await();
            }
        } finally {
            lock.unlock();
        }
    }

    private Optional<Table> findOptimalTable(int groupSize) {
        // Find the smallest suitable available table
        // Check an available table which is not also at cleaning stage and have the capacity to add the student group size.
        // Then sort the returned tables comparing the capacity and take the first option
        // (Don't seat a 2-person group at a 6-person table if smaller tables are available)
        return tables.stream()
                .filter(t -> t.isAvailable && !t.isCleaning && t.capacity >= groupSize)
                .sorted(Comparator.comparingInt(t -> t.capacity))
                .findFirst();
    }

    public void leaveTable(Table table) {
        lock.lock();
        try {
            System.out.println(table + " is now being cleaned...");
            table.isAvailable = false;
            table.isCleaning = true;
            // Simulate cleaning with a thread
            new Thread(() -> {
                try {
                    Thread.sleep(2000);  // simulate 2 seconds cleaning time
                    finishCleaning(table);
                } catch (InterruptedException e) {
                    System.out.println("Cleaning interrupted for " + table);
                }
            }).start();
        } finally {
            lock.unlock();
        }
    }

    private void finishCleaning(Table table) {
        lock.lock();
        try {
            table.isCleaning = false;
            table.isAvailable = true;
            System.out.println(table + " is now clean and available.");
            isTableAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
