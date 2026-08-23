import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Storage {
    private static final Path FILE_PATH = Paths.get("data", "minweeder.txt");

    private int skippedLineCount = 0;

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
                task = new Deadline(parts[2], parts[3]);
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
