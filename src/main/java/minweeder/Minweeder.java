package minweeder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import minweeder.command.CommandWord;
import minweeder.exception.MinweederException;
import minweeder.parser.Parser;
import minweeder.storage.Storage;
import minweeder.task.Deadline;
import minweeder.task.Event;
import minweeder.task.Task;
import minweeder.task.TaskList;
import minweeder.task.Todo;
import minweeder.ui.Ui;

/**
 * Core of the Minweeder task-tracking application. Loads any saved tasks on
 * construction, then executes one user command at a time via
 * {@link #getResponse(String)}, returning the resulting message so that both
 * a console interface ({@link #main(String[])}) and a GUI can display it.
 */
public class Minweeder {
    private final Ui ui = new Ui();
    private final Storage storage = new Storage();
    private final TaskList tasks;
    private final String startupMessage;
    private boolean isExit = false;

    /**
     * Creates a Minweeder instance, loading any previously saved tasks.
     * If loading fails or some lines could not be read, the details are kept
     * to be shown alongside the welcome message.
     */
    public Minweeder() {
        TaskList loadedTasks;
        StringBuilder startup = new StringBuilder();
        try {
            loadedTasks = storage.load();
            if (storage.getSkippedLineCount() > 0) {
                startup.append(ui.showSkippedLines(storage.getSkippedLineCount()));
            }
        } catch (MinweederException e) {
            loadedTasks = new TaskList();
            startup.append(ui.showLoadingError(e.getMessage()));
        }
        this.tasks = loadedTasks;
        this.startupMessage = startup.toString();
    }

    /**
     * Returns the welcome banner, followed by any messages about problems
     * encountered while loading saved tasks.
     *
     * @return the formatted welcome message.
     */
    public String getWelcomeMessage() {
        return startupMessage.isEmpty() ? ui.showWelcome() : ui.showWelcome() + "\n" + startupMessage;
    }

    /**
     * Returns whether the last command executed via {@link #getResponse(String)}
     * was a request to exit the application.
     *
     * @return true if the application should now exit.
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Adds a task to the list, persists the updated list to storage, and returns
     * a confirmation message. Shared by the todo/deadline/event command handlers.
     *
     * @param label a human-readable name for the task type, e.g. "TODO".
     * @param task the task to add.
     * @return the formatted confirmation message.
     * @throws MinweederException if saving the updated list fails.
     */
    private String addTask(String label, Task task) throws MinweederException {
        tasks.add(task);
        storage.save(tasks);
        return ui.showTaskAdded(label, task, tasks.size());
    }

    /**
     * Executes a single user command and returns the resulting message.
     *
     * @param command the raw command text entered by the user.
     * @return the formatted response, or an empty string if the command was blank.
     */
    public String getResponse(String command) {
        if (command.isEmpty()) {
            return "";
        }
        try {
            String[] breakdown = Parser.splitCommand(command);
            CommandWord commandWord = Parser.parseCommandWord(breakdown);

            switch (commandWord) {
                case BYE:
                    isExit = true;
                    return ui.showGoodbye();
                case LIST:
                    return ui.showList(tasks);
                case MARK: {
                    int index = Parser.parseIndex(breakdown, tasks);
                    tasks.get(index).mark();
                    storage.save(tasks);
                    return ui.showTaskMarked(tasks.get(index));
                }
                case UNMARK: {
                    int index = Parser.parseIndex(breakdown, tasks);
                    tasks.get(index).unmark();
                    storage.save(tasks);
                    return ui.showTaskUnmarked(tasks.get(index));
                }
                case TODO: {
                    String description = Parser.requireArguments(breakdown, "todo", "todo read book");
                    Todo todo = new Todo(description);
                    return addTask("TODO", todo);
                }
                case DEADLINE: {
                    String example = "deadline return book /by 2/12/2019 1800";
                    String arguments = Parser.requireArguments(breakdown, "deadline", example);
                    String[] parts = Parser.requireKeyword(arguments, "/by", example);
                    LocalDateTime by = Parser.parseDeadlineBy(parts[1], example);
                    Deadline deadline = new Deadline(parts[0], by);
                    return addTask("Deadline", deadline);
                }
                case EVENT: {
                    String example = "event project meeting /from Mon 2pm /to 4pm";
                    String arguments = Parser.requireArguments(breakdown, "event", example);
                    String[] fromParts = Parser.requireKeyword(arguments, "/from", example);
                    String[] toParts = Parser.requireKeyword(fromParts[1], "/to", example);
                    Event event = new Event(fromParts[0], toParts[0], toParts[1]);
                    return addTask("Event", event);
                }
                case DELETE: {
                    int index = Parser.parseIndex(breakdown, tasks);
                    Task deleted = tasks.delete(index);
                    storage.save(tasks);
                    return ui.showTaskDeleted(deleted, tasks.size());
                }
                case ON: {
                    String example = "on 2/12/2019";
                    String argument = Parser.requireArguments(breakdown, "on", example);
                    LocalDate date = Parser.parseOnDate(argument, example);
                    return ui.showTasksOn(date, tasks);
                }
                case FIND: {
                    String example = "find book";
                    String keyword = Parser.requireArguments(breakdown, "find", example);
                    return ui.showFoundTasks(tasks.findIndices(keyword), tasks);
                }
                default:
                    assert false : "unhandled command word: " + commandWord;
                    return "";
            }
        } catch (MinweederException e) {
            return ui.showError(e.getMessage());
        }
    }

    /**
     * Starts Minweeder as a console application: prints the welcome message, then
     * repeatedly reads and executes user commands until told to exit.
     *
     * @param args unused command-line arguments.
     */
    public static void main(String[] args) {
        Minweeder minweeder = new Minweeder();
        System.out.print(minweeder.ui.decorate(minweeder.getWelcomeMessage()));

        while (!minweeder.isExit()) {
            String command = minweeder.ui.readCommand();
            String response = minweeder.getResponse(command);
            if (!response.isEmpty()) {
                System.out.print(minweeder.ui.decorate(response));
            }
        }
        minweeder.ui.close();
    }
}
