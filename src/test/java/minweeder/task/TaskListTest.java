package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void add_singleTask_increasesSizeAndIsRetrievable() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");

        tasks.add(todo);

        assertEquals(1, tasks.size());
        assertSame(todo, tasks.get(0));
    }

    @Test
    public void size_emptyList_returnsZero() {
        TaskList tasks = new TaskList();

        assertEquals(0, tasks.size());
    }

    @Test
    public void delete_existingIndex_removesAndReturnsTask() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("read book");
        Todo second = new Todo("return book");
        tasks.add(first);
        tasks.add(second);

        Task deleted = tasks.delete(0);

        assertSame(first, deleted);
        assertEquals(1, tasks.size());
        assertSame(second, tasks.get(0));
    }
}
