package minweeder.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import minweeder.exception.MinweederException;
import minweeder.task.Deadline;
import minweeder.task.Event;
import minweeder.task.Task;
import minweeder.task.TaskList;
import minweeder.task.Todo;

/**
 * Handles loading tasks from and saving tasks to the storage file on disk.
 */
public class Storage {
    private static final Path FILE_PATH = Paths.get("data", "minweeder.txt");

    private int skippedLineCount = 0;

    /**
     * Saves the given task list to the storage file, overwriting any existing contents.
     *
     * @param tasks the tasks to save.
     * @throws MinweederException if the file could not be written.
     */
    public void save(TaskList tasks) throws MinweederException {
        try {
            Files.createDirectories(FILE_PATH.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH)) {
                for (int i = 0; i < tasks.size(); i++) {
                    writer.write(tasks.get(i).toFileString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new MinweederException("I couldn't save your tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the storage file, if it exists. Lines that cannot be parsed are
     * skipped and counted; see {@link #getSkippedLineCount()}.
     *
     * @return the loaded task list, empty if the file does not exist.
     * @throws MinweederException if the file exists but could not be read.
     */
    public TaskList load() throws MinweederException {
        TaskList tasks = new TaskList();
        skippedLineCount = 0;
        if (!Files.exists(FILE_PATH)) {
            return tasks;
        }
        try {
            for (String line : Files.readAllLines(FILE_PATH)) {
                if (line.isBlank()) {
                    continue;
                }
                Task task = parseTask(line);
                if (task == null) {
                    skippedLineCount++;
                } else {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new MinweederException("I couldn't read your saved tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Returns the number of lines skipped during the most recent {@link #load()}
     * because they could not be parsed as a valid task.
     *
     * @return the number of skipped lines.
     */
    public int getSkippedLineCount() {
        return skippedLineCount;
    }

    private static Task parseTask(String line) {
        String[] parts = line.split("\\|");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        if (parts.length < 3 || parts[2].isEmpty()) {
            return null;
        }
        Task task;
        switch (parts[0]) {
            case "T":
                task = new Todo(parts[2]);
                break;
            case "D":
                if (parts.length < 4 || parts[3].isEmpty()) {
                    return null;
                }
                LocalDateTime by;
                try {
                    by = LocalDateTime.parse(parts[3]);
                } catch (DateTimeParseException e) {
                    return null;
                }
                task = new Deadline(parts[2], by);
                break;
            case "E":
                if (parts.length < 5 || parts[3].isEmpty() || parts[4].isEmpty()) {
                    return null;
                }
                task = new Event(parts[2], parts[3], parts[4]);
                break;
            default:
                return null;
        }
        if (parts[1].equals("1")) {
            task.mark();
        } else if (!parts[1].equals("0")) {
            return null;
        }
        return task;
    }
}
