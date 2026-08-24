package minweeder.task;

/**
 * Represents a task that spans a period of time, from a start to an end.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task with the given description, start, and end.
     *
     * @param description the description of the event.
     * @param from when the event starts.
     * @param to when the event ends.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileFields() + " | " + this.from + " | " + this.to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}