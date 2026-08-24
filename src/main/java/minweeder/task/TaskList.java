package minweeder.task;

import java.util.ArrayList;

/**
 * A simple ordered collection of {@link Task}s.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public void add(Task task) {
        tasks.add(task);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }
}
