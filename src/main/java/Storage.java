import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Storage {
    private static final Path FILE_PATH = Paths.get("data", "minweeder.txt");

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
}
