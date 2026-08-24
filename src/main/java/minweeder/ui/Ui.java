package minweeder.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import minweeder.task.Task;
import minweeder.task.TaskList;

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

    private void printBlock(String... messages) {
        System.out.print(LINE);
        for (String message : messages) {
            System.out.println(message);
        }
        System.out.print(LINE);
    }

    public void showWelcome() {
        printBlock(BANNER, GREETING);
    }

    public void showGoodbye() {
        printBlock(GOODBYE);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }

    public void showLoadingError(String message) {
        printBlock("I couldn't read your saved tasks, so let's start afresh. " + message);
    }

    public void showSkippedLines(int skippedLineCount) {
        printBlock("BTW " + skippedLineCount
                + " line(s) of your save file were unreadable so some may be missing :(");
    }

    public void showError(String message) {
        printBlock("Erm...you can't do that..." + message);
    }

    public void showTaskAdded(String label, Task task, int totalTasks) {
        printBlock("Okay! " + label + " successfully added:",
                "  " + task,
                "Now you have " + totalTasks + " tasks in your list.");
    }

    public void showTaskDeleted(Task task, int totalTasks) {
        printBlock("Task successfully removed: ",
                " " + task,
                "Now you have " + totalTasks + " tasks in your list.");
    }

    public void showTaskMarked(Task task) {
        printBlock("Congrats! Task has been marked as completed:",
                "  " + task);
    }

    public void showTaskUnmarked(Task task) {
        printBlock("Done! Task has been marked as not done yet:",
                "  " + task);
    }

    public void showList(TaskList tasks) {
        String[] listing = new String[tasks.size() + 1];
        listing[0] = "Here are your tasks:";
        for (int i = 0; i < tasks.size(); i++) {
            listing[i + 1] = (i + 1) + ". " + tasks.get(i);
        }
        printBlock(listing);
    }

    public void showTasksOn(LocalDate date, TaskList tasks) {
        ArrayList<String> matches = new ArrayList<>();
        matches.add("Tasks occurring on " + date.format(QUERY_DATE_DISPLAY_FORMAT) + ":");
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).isOccurringOn(date)) {
                matches.add((i + 1) + ". " + tasks.get(i));
            }
        }
        printBlock(matches.toArray(new String[0]));
    }
}
