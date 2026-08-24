import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Minweeder {
    private static final DateTimeFormatter DEADLINE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter QUERY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy");

    private static String requireArguments(String[] breakdown, String commandWord, String example)
            throws MinweederException {
        if (breakdown.length < 2 || breakdown[1].isBlank()) {
            throw new MinweederException("a " + commandWord + " needs a description. e.g. "
                    + example);
        }
        return breakdown[1].trim();
    }

    private static String[] requireKeyword(String text, String keyword, String example)
            throws MinweederException {
        String[] parts = text.split(" " + keyword + " ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new MinweederException("You need something on either side of " + keyword
                    + ". e.g. " + example);
        }
        return new String[] {parts[0].trim(), parts[1].trim()};
    }

    private static void addTask(TaskList tasks, Storage storage, Ui ui, String label, Task task)
            throws MinweederException {
        tasks.add(task);
        storage.save(tasks);
        ui.showTaskAdded(label, task, tasks.size());
    }

    private static int parseIndex(String[] breakdown, TaskList tasks) throws MinweederException {
        if (breakdown.length < 2 || breakdown[1].isBlank()) {
            throw new MinweederException("Which task? Choose a number, e.g. "
                    + breakdown[0] + " 2");
        }
        String argument = breakdown[1].trim();
        int taskNumber;
        try {
            taskNumber = Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new MinweederException("'" + argument + "' is not a number!");
        }
        if (tasks.size() == 0) {
            throw new MinweederException("your list is empty, so there is nothing to "
                    + breakdown[0] + " yet.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new MinweederException("you have " + tasks.size()
                    + " tasks, so pick a number from 1 to " + tasks.size() + ".");
        }
        return taskNumber - 1;
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage();
        TaskList tasks = new TaskList();
        try {
            tasks = storage.load();
            if (storage.getSkippedLineCount() > 0) {
                ui.showSkippedLines(storage.getSkippedLineCount());
            }
        } catch (MinweederException e) {
            ui.showLoadingError(e.getMessage());
        }

        boolean isRunning = true;
        while (isRunning) {
            String command = ui.readCommand();
            if (command.isEmpty()) {
                continue;
            }
            try {
                String[] breakdown = command.split(" ", 2);
                CommandWord commandWord = CommandWord.getCommandWord(breakdown[0]);

                switch (commandWord) {
                    case BYE:
                        ui.showGoodbye();
                        isRunning = false;
                        break;
                    case LIST:
                        ui.showList(tasks);
                        break;
                    case MARK: {
                        int index = parseIndex(breakdown, tasks);
                        tasks.get(index).mark();
                        storage.save(tasks);
                        ui.showTaskMarked(tasks.get(index));
                        break;
                    }
                    case UNMARK: {
                        int index = parseIndex(breakdown, tasks);
                        tasks.get(index).unmark();
                        storage.save(tasks);
                        ui.showTaskUnmarked(tasks.get(index));
                        break;
                    }
                    case TODO: {
                        String description = requireArguments(breakdown, "todo", "todo read book");
                        Todo todo = new Todo(description);
                        addTask(tasks, storage, ui, "TODO", todo);
                        break;
                    }
                    case DEADLINE: {
                        String example = "deadline return book /by 2/12/2019 1800";
                        String arguments = requireArguments(breakdown, "deadline", example);
                        String[] parts = requireKeyword(arguments, "/by", example);
                        LocalDateTime by;
                        try {
                            by = LocalDateTime.parse(parts[1], DEADLINE_INPUT_FORMAT);
                        } catch (DateTimeParseException e) {
                            throw new MinweederException("please use d/M/yyyy HHmm for the date, e.g. " + example);
                        }
                        Deadline deadline = new Deadline(parts[0], by);
                        addTask(tasks, storage, ui, "Deadline", deadline);
                        break;
                    }
                    case EVENT: {
                        String example = "event project meeting /from Mon 2pm /to 4pm";
                        String arguments = requireArguments(breakdown, "event", example);
                        String[] fromParts = requireKeyword(arguments, "/from", example);
                        String[] toParts = requireKeyword(fromParts[1], "/to", example);
                        Event event = new Event(fromParts[0], toParts[0], toParts[1]);
                        addTask(tasks, storage, ui, "Event", event);
                        break;
                    }
                    case DELETE: {
                        int index = parseIndex(breakdown, tasks);
                        Task deleted = tasks.delete(index);
                        storage.save(tasks);
                        ui.showTaskDeleted(deleted, tasks.size());
                        break;
                    }
                    case ON: {
                        String example = "on 2/12/2019";
                        String argument = requireArguments(breakdown, "on", example);
                        LocalDate date;
                        try {
                            date = LocalDate.parse(argument, QUERY_DATE_FORMAT);
                        } catch (DateTimeParseException e) {
                            throw new MinweederException("please use d/M/yyyy for the date, e.g. " + example);
                        }
                        ui.showTasksOn(date, tasks);
                        break;
                    }
                }
            } catch (MinweederException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
    }
}
