package minweeder.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
