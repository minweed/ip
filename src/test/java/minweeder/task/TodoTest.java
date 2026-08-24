package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {

    @Test
    public void toFileString_incompleteTask_returnsCorrectFormat() {
        Todo todo = new Todo("read book");

        assertEquals("T | 0 | read book", todo.toFileString());
    }

    @Test
    public void toFileString_completedTask_returnsCorrectFormat() {
        Todo todo = new Todo("read book");
        todo.mark();

        assertEquals("T | 1 | read book", todo.toFileString());
    }

    @Test
    public void toString_incompleteTask_returnsDisplayFormat() {
        Todo todo = new Todo("read book");

        assertEquals("[T][ ] read book", todo.toString());
    }
}
