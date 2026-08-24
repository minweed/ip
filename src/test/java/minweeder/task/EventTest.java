package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class EventTest {

    @Test
    public void toFileString_incompleteEvent_returnsCorrectFormat() {
        Event event = new Event("project meeting", "Mon 2pm", "4pm");

        assertEquals("E | 0 | project meeting | Mon 2pm | 4pm", event.toFileString());
    }

    @Test
    public void toFileString_completedEvent_returnsCorrectFormat() {
        Event event = new Event("project meeting", "Mon 2pm", "4pm");
        event.mark();

        assertEquals("E | 1 | project meeting | Mon 2pm | 4pm", event.toFileString());
    }

    @Test
    public void toString_incompleteEvent_returnsDisplayFormat() {
        Event event = new Event("project meeting", "Mon 2pm", "4pm");

        assertEquals("[E][ ] project meeting (from: Mon 2pm to: 4pm)", event.toString());
    }
}
