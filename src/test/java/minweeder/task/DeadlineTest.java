package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    public void isOccurringOn_sameDate_returnsTrue() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));

        assertTrue(deadline.isOccurringOn(LocalDate.of(2024, 12, 2)));
    }

    @Test
    public void isOccurringOn_sameDateDifferentTime_returnsTrue() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 23, 59));

        assertTrue(deadline.isOccurringOn(LocalDate.of(2024, 12, 2)));
    }

    @Test
    public void isOccurringOn_differentDate_returnsFalse() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));

        assertFalse(deadline.isOccurringOn(LocalDate.of(2024, 12, 3)));
    }

    @Test
    public void toFileString_returnsCorrectlyFormattedFields() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));

        assertEquals("D | 0 | submit report | 2024-12-02T18:00", deadline.toFileString());
    }

    @Test
    public void toFileString_afterMark_reflectsDoneStatus() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));
        deadline.mark();

        assertEquals("D | 1 | submit report | 2024-12-02T18:00", deadline.toFileString());
    }

    @Test
    public void toString_returnsDisplayFormat() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));

        assertEquals("[D][ ] submit report (by: Dec 02 2024, 6:00pm)", deadline.toString());
    }

    @Test
    public void equals_sameDescriptionAndDate_returnsTrue() {
        Deadline first = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));
        Deadline second = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void equals_differentDate_returnsFalse() {
        Deadline first = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));
        Deadline second = new Deadline("submit report", LocalDateTime.of(2024, 12, 3, 18, 0));

        assertNotEquals(first, second);
    }

    @Test
    public void equals_differentTaskType_returnsFalse() {
        Deadline deadline = new Deadline("submit report", LocalDateTime.of(2024, 12, 2, 18, 0));

        assertNotEquals(deadline, new Todo("submit report"));
    }
}
