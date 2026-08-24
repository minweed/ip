package minweeder.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import minweeder.exception.MinweederException;
import minweeder.task.TaskList;
import minweeder.task.Todo;

public class ParserTest {

    @Test
    public void parseIndex_validIndexWithinRange_returnsZeroBasedIndex() throws MinweederException {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Todo("return book"));

        int index = Parser.parseIndex(new String[] {"mark", "2"}, tasks);

        assertEquals(1, index);
    }

    @Test
    public void parseIndex_missingArgument_throwsException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(MinweederException.class, () -> Parser.parseIndex(new String[] {"mark"}, tasks));
    }

    @Test
    public void parseIndex_nonNumericArgument_throwsException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(MinweederException.class, () -> Parser.parseIndex(new String[] {"mark", "two"}, tasks));
    }

    @Test
    public void parseIndex_emptyTaskList_throwsException() {
        TaskList tasks = new TaskList();

        assertThrows(MinweederException.class, () -> Parser.parseIndex(new String[] {"mark", "1"}, tasks));
    }

    @Test
    public void parseIndex_indexOutOfRange_throwsException() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));

        assertThrows(MinweederException.class, () -> Parser.parseIndex(new String[] {"mark", "5"}, tasks));
    }

    @Test
    public void parseDeadlineBy_validDateTime_parsesCorrectly() throws MinweederException {
        LocalDateTime result = Parser.parseDeadlineBy("2/12/2024 1800", "2/12/2024 1800");

        assertEquals(LocalDateTime.of(2024, 12, 2, 18, 0), result);
    }

    @Test
    public void parseDeadlineBy_invalidFormat_throwsException() {
        assertThrows(MinweederException.class,
                () -> Parser.parseDeadlineBy("2 December 2024", "2/12/2024 1800"));
    }
}
