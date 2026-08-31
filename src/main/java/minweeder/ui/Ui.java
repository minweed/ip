package minweeder.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import minweeder.task.Task;
import minweeder.task.TaskList;

/**
 * Formats all user-facing messages, banners, and prompts, and handles console
 * input. Each {@code showXxx} method returns the formatted message as a
 * {@code String} rather than printing it directly, so the same messages can
 * be reused by both the console interface and the GUI.
 */
public class Ui {
    private static final String LINE =
            "────────────────────────────────────────────────────────────────\n";
    private static final String BANNER = " __  __ ___ _   ___        _______ _____ ____  _____ ____  \n"
            + "|  \\/  |_ _| \\ | \\ \\      / / ____| ____|  _ \\| ____|  _ \\ \n"
            + "| |\\/| || ||  \\| |\\ \\ /\\ / /|  _| |  _| | | | |  _| | |_) |\n"
            + "| |  | || || |\\  | \\ V  V / | |___| |___| |_| | |___|  _ < \n"
            + "|_|  |_|___|_| \\_|  \\_/\\_/  |_____|_____|____/|_____|_| \\_\\\n";
    private static final String GREETING = "Heyyo I'm Minweeder!\nLETS GET THINGS DONE RAHH";
    private static final String GOODBYE = "Goodbye! Hope you had a productive session :)";
    private static final DateTimeFormatter QUERY_DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Formats one or more messages, each on its own line, surrounded above and below
     * by a horizontal divider. Used by all the show* methods to keep output consistent.
     *
     * @param messages the lines to include, in order.
     * @return the formatted block of text.
     */
    private String formatBlock(String... messages) {
        StringBuilder block = new StringBuilder(LINE);
        for (String message : messages) {
            block.append(message).append("\n");
        }
        block.append(LINE);
        return block.toString();
    }

    /**
     * Formats the welcome banner and greeting shown when Minweeder starts.
     *
     * @return the formatted welcome message.
     */
    public String showWelcome() {
        return formatBlock(BANNER, GREETING);
    }

    /**
     * Formats the goodbye message shown when the user exits.
     *
     * @return the formatted goodbye message.
     */
    public String showGoodbye() {
        return formatBlock(GOODBYE);
    }

    /**
     * Reads and returns the next line of user input, with leading/trailing whitespace trimmed.
     *
     * @return the trimmed line of input.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Closes the input scanner. Should be called once, when the program is shutting down.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Formats a message explaining that the save file could not be read.
     *
     * @param message details of the underlying error.
     * @return the formatted error message.
     */
    public String showLoadingError(String message) {
        return formatBlock("I couldn't read your saved tasks, so let's start afresh. " + message);
    }

    /**
     * Formats a message informing the user that some lines in the save file could not be read.
     *
     * @param skippedLineCount the number of lines that were skipped.
     * @return the formatted message.
     */
    public String showSkippedLines(int skippedLineCount) {
        return formatBlock("BTW " + skippedLineCount
                + " line(s) of your save file were unreadable so some may be missing :(");
    }

    /**
     * Formats an error message in response to an invalid command.
     *
     * @param message the error message to show.
     * @return the formatted error message.
     */
    public String showError(String message) {
        return formatBlock("Erm...you can't do that..." + message);
    }

    /**
     * Confirms that a task was added to the list.
     *
     * @param label a human-readable name for the task type, e.g. "Todo".
     * @param task the task that was added.
     * @param totalTasks the total number of tasks now in the list.
     * @return the formatted confirmation message.
     */
    public String showTaskAdded(String label, Task task, int totalTasks) {
        return formatBlock("Okay! " + label + " successfully added:",
                "  " + task,
                "Now you have " + totalTasks + " tasks in your list.");
    }

    /**
     * Confirms that a task was removed from the list.
     *
     * @param task the task that was removed.
     * @param totalTasks the total number of tasks remaining in the list.
     * @return the formatted confirmation message.
     */
    public String showTaskDeleted(Task task, int totalTasks) {
        return formatBlock("Task successfully removed: ",
                " " + task,
                "Now you have " + totalTasks + " tasks in your list.");
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task the task that was marked.
     * @return the formatted confirmation message.
     */
    public String showTaskMarked(Task task) {
        return formatBlock("Congrats! Task has been marked as completed:",
                "  " + task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task the task that was unmarked.
     * @return the formatted confirmation message.
     */
    public String showTaskUnmarked(Task task) {
        return formatBlock("Done! Task has been marked as not done yet:",
                "  " + task);
    }

    /**
     * Formats every task currently in the list.
     *
     * @param tasks the list of tasks to display.
     * @return the formatted listing.
     */
    public String showList(TaskList tasks) {
        String[] listing = new String[tasks.size() + 1];
        listing[0] = "Here are your tasks:";
        for (int i = 0; i < tasks.size(); i++) {
            listing[i + 1] = (i + 1) + ". " + tasks.get(i);
        }
        return formatBlock(listing);
    }

    /**
     * Formats only the tasks that occur on a given date.
     *
     * @param date the date to filter tasks by.
     * @param tasks the list of tasks to search.
     * @return the formatted listing.
     */
    public String showTasksOn(LocalDate date, TaskList tasks) {
        ArrayList<String> matches = new ArrayList<>();
        matches.add("Tasks occurring on " + date.format(QUERY_DATE_DISPLAY_FORMAT) + ":");
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isOccurringOn(date)) {
                matches.add((i + 1) + ". " + tasks.get(i));
            }
        }
        return formatBlock(matches.toArray(new String[0]));
    }

    /**
     * Formats the tasks that matched a find query, numbered by their position in the
     * full list so that the numbers shown can be used with commands such as mark.
     *
     * @param matchingIndices the zero-based indices of the tasks to display.
     * @param tasks the full task list the indices refer to.
     * @return the formatted listing.
     */
    public String showFoundTasks(List<Integer> matchingIndices, TaskList tasks) {
        String[] listing = new String[matchingIndices.size() + 1];
        listing[0] = "Here are the matching tasks in your list:";
        for (int i = 0; i < matchingIndices.size(); i++) {
            int index = matchingIndices.get(i);
            listing[i + 1] = (index + 1) + ". " + tasks.get(index);
        }
        return formatBlock(listing);
    }
}
