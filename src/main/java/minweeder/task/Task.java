package minweeder.task;

import java.time.LocalDate;

/**
 * A single item to be tracked, with a description and a completion state.
 * Concrete subclasses define the task's specific type and how it is stored.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a task that is not yet done.
     *
     * @param description what the task is
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public abstract String toFileString();

    public boolean isOccurringOn(LocalDate date) {
        return false;
    }

    protected String toFileFields() {
        return (this.isDone ? "1" : "0") + " | " + this.description;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }
}
