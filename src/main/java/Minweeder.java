import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Minweeder {
    private static final String LINE =
            "────────────────────────────────────────────────────────────────\n";
    private static final String BANNER = " __  __ ___ _   ___        _______ _____ ____  _____ ____  \n"
            + "|  \\/  |_ _| \\ | \\ \\      / / ____| ____|  _ \\| ____|  _ \\ \n"
            + "| |\\/| || ||  \\| |\\ \\ /\\ / /|  _| |  _| | | | |  _| | |_) |\n"
            + "| |  | || || |\\  | \\ V  V / | |___| |___| |_| | |___|  _ < \n"
            + "|_|  |_|___|_| \\_|  \\_/\\_/  |_____|_____|____/|_____|_| \\_\\\n";
    private static final String GREETING = "Heyyo I'm Minweeder!\nLETS GET THINGS DONE RAHH";
    private static final String GOODBYE = "Goodbye! Hope you had a productive session :)";
    private static final DateTimeFormatter DEADLINE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter QUERY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy");
    private static final DateTimeFormatter QUERY_DATE_DISPLAY_FORMAT =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private static void printBlock(String... messages) {
        System.out.print(LINE);
        for (String message : messages) {
            System.out.println(message);
        }
        System.out.print(LINE);
    }

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

    private static void addTask(TaskList tasks, Storage storage, String label, Task task)
            throws MinweederException {
        tasks.add(task);
        storage.save(tasks);
        printBlock("Okay! " + label + " successfully added:",
                "  " + task,
                "Now you have " + tasks.size() + " tasks in your list.");
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
        printBlock(BANNER, GREETING);

        Storage storage = new Storage();
        TaskList tasks = new TaskList();
        try {
            tasks = storage.load();
            if (storage.getSkippedLineCount() > 0) {
                printBlock("BTW " + storage.getSkippedLineCount()
                        + " line(s) of your save file were unreadable so some may be missing :(");
            }
        } catch (MinweederException e) {
            printBlock("I couldn't read your saved tasks, so let's start afresh. "
                    + e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;
        while (isRunning) {
            String command = scanner.nextLine().trim();
            if (command.isEmpty()) {
                continue;
            }
            try {
                String[] breakdown = command.split(" ", 2);
                CommandWord commandWord = CommandWord.getCommandWord(breakdown[0]);

                switch (commandWord) {
                    case BYE:
                        printBlock(GOODBYE);
                        isRunning = false;
                        break;
                    case LIST:
                        String[] listing = new String[tasks.size() + 1];
                        listing[0] = "Here are your tasks:";
                        for (int i = 0; i < tasks.size(); i++) {
                            listing[i + 1] = (i + 1) + ". " + tasks.get(i);
                        }
                        printBlock(listing);
                        break;
                    case MARK: {
                        int index = parseIndex(breakdown, tasks);
                        tasks.get(index).mark();
                        storage.save(tasks);
                        printBlock("Congrats! Task has been marked as completed:",
                                "  " + tasks.get(index));
                        break;
                    }
                    case UNMARK: {
                        int index = parseIndex(breakdown, tasks);
                        tasks.get(index).unmark();
                        storage.save(tasks);
                        printBlock("Done! Task has been marked as not done yet:",
                                "  " + tasks.get(index));
                        break;
                    }
                    case TODO: {
                        String description = requireArguments(breakdown, "todo", "todo read book");
                        Todo todo = new Todo(description);
                        addTask(tasks, storage, "TODO", todo);
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
                        addTask(tasks, storage, "Deadline", deadline);
                        break;
                    }
                    case EVENT: {
                        String example = "event project meeting /from Mon 2pm /to 4pm";
                        String arguments = requireArguments(breakdown, "event", example);
                        String[] fromParts = requireKeyword(arguments, "/from", example);
                        String[] toParts = requireKeyword(fromParts[1], "/to", example);
                        Event event = new Event(fromParts[0], toParts[0], toParts[1]);
                        addTask(tasks, storage, "Event", event);
                        break;
                    }
                    case DELETE: {
                        int index = parseIndex(breakdown, tasks);
                        Task deleted = tasks.delete(index);
                        storage.save(tasks);
                        printBlock("Task successfully removed: ",
                                " " + deleted,
                                "Now you have " + tasks.size() + " tasks in your list.");
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
                        ArrayList<String> matches = new ArrayList<>();
                        matches.add("Tasks occurring on " + date.format(QUERY_DATE_DISPLAY_FORMAT) + ":");
                        for (int i = 0; i < tasks.size(); i++) {
                            if (tasks.get(i).isOccurringOn(date)) {
                                matches.add((i + 1) + ". " + tasks.get(i));
                            }
                        }
                        printBlock(matches.toArray(new String[0]));
                        break;
                    }
                }
            } catch (MinweederException e) {
                printBlock("Erm...you can't do that..." + e.getMessage());
            }
        }
        scanner.close();
    }
}
