package minweeder.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import minweeder.exception.MinweederException;
import minweeder.task.Deadline;
import minweeder.task.TaskList;
import minweeder.task.Todo;

public class StorageTest {
    private static final Path FILE_PATH = Paths.get("data", "minweeder.txt");
    private byte[] backup;
    private boolean fileExisted;

    @BeforeEach
    public void backUpExistingSaveFile() throws IOException {
        fileExisted = Files.exists(FILE_PATH);
        if (fileExisted) {
            backup = Files.readAllBytes(FILE_PATH);
        }
    }

    @AfterEach
    public void restoreExistingSaveFile() throws IOException {
        if (fileExisted) {
            Files.write(FILE_PATH, backup);
        } else {
            Files.deleteIfExists(FILE_PATH);
        }
    }

    @Test
    public void saveThenLoad_roundTrip_restoresEquivalentTasks() throws MinweederException {
        Storage storage = new Storage();
        TaskList original = new TaskList();
        original.add(new Todo("read book"));
        Deadline deadline = new Deadline("return book", LocalDateTime.of(2024, 12, 2, 18, 0));
        deadline.mark();
        original.add(deadline);

        storage.save(original);
        TaskList loaded = storage.load();

        assertEquals(2, loaded.size());
        assertEquals(original.get(0).toFileString(), loaded.get(0).toFileString());
        assertEquals(original.get(1).toFileString(), loaded.get(1).toFileString());
    }

    @Test
    public void load_lineWithUnknownTaskType_isSkippedAndCounted() throws IOException, MinweederException {
        Files.createDirectories(FILE_PATH.getParent());
        Files.write(FILE_PATH, "X | 0 | mystery task\nT | 0 | read book\n".getBytes());

        Storage storage = new Storage();
        TaskList loaded = storage.load();

        assertEquals(1, loaded.size());
        assertEquals("T | 0 | read book", loaded.get(0).toFileString());
        assertEquals(1, storage.getSkippedLineCount());
    }

    @Test
    public void load_missingFile_returnsEmptyTaskList() throws IOException, MinweederException {
        Files.deleteIfExists(FILE_PATH);

        Storage storage = new Storage();
        TaskList loaded = storage.load();

        assertEquals(0, loaded.size());
    }
}
