public class Table {
    int id;
    int capacity;
    boolean isAvailable;
    boolean isCleaning;

    public Table(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.isAvailable = true;
        this.isCleaning = false;
    }

    @Override
    public String toString() {
        return "Table " + id + " (" + capacity + "-seater)";
    }
}
