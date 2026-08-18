public class TaskList {
    private static final int CAPACITY = 100;
    private final Task[] tasks = new Task[CAPACITY];
    private int size = 0;

    public void add(Task task) {
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
