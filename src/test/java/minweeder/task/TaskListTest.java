package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

    @Test
    public void find_keywordMatchesSomeTasks_returnsOnlyMatchingTasks() {
        TaskList tasks = new TaskList();
        Todo readBook = new Todo("read book");
        Todo returnBook = new Todo("return book");
        Todo joinClub = new Todo("join sports club");
        tasks.add(readBook);
        tasks.add(returnBook);
        tasks.add(joinClub);

        List<Task> matches = tasks.find("book");

        assertEquals(2, matches.size());
        assertSame(readBook, matches.get(0));
        assertSame(returnBook, matches.get(1));
    }

    @Test
    public void find_keywordMatchesNoTasks_returnsEmptyList() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        List<Task> matches = tasks.find("homework");

        assertTrue(matches.isEmpty());
    }

    @Test
    public void find_keywordDifferentCase_matchesCaseInsensitively() {
        TaskList tasks = new TaskList();
        Todo readBook = new Todo("read book");
        tasks.add(readBook);

        List<Task> matches = tasks.find("BOOK");

        assertEquals(1, matches.size());
        assertSame(readBook, matches.get(0));
    }

    @Test
    public void find_emptyList_returnsEmptyList() {
        TaskList tasks = new TaskList();

        List<Task> matches = tasks.find("book");

        assertTrue(matches.isEmpty());
    }
}
