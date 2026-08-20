public class TaskList {
    private static final int CAPACITY = 100;
    private final Task[] tasks = new Task[CAPACITY];
    private int size = 0;

    public void add(Task task) throws MinweederException {
        if (this.size == CAPACITY) {
            throw new MinweederException("your list is full. You can only store up to " + CAPACITY + " tasks.");
        }
        this.tasks[this.size] = task;
        this.size++;
    }

    public Task get(int index) {
        return this.tasks[index];
    }

    public int size() {
        return this.size;
    }
}
