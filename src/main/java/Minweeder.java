import java.util.Scanner;

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

        TaskList tasks = new TaskList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine().trim();
            if (command.isEmpty()) {
                continue;
            }
            try {
                String[] breakdown = command.split(" ", 2);

                if (command.equals("bye")) {
                    printBlock(GOODBYE);
                    break;
                } else if (command.equals("list")) {
                    String[] listing = new String[tasks.size() + 1];
                    listing[0] = "Here are your tasks:";
                    for (int i = 0; i < tasks.size(); i++) {
                        listing[i + 1] = (i + 1) + ". " + tasks.get(i);
                    }
                    printBlock(listing);
                } else if (breakdown[0].equals("mark")) {
                    int index = parseIndex(breakdown, tasks);
                    tasks.get(index).mark();
                    printBlock("Congrats! Task has been marked as completed:",
                            "  " + tasks.get(index));
                } else if (breakdown[0].equals("unmark")) {
                    int index = parseIndex(breakdown, tasks);
                    tasks.get(index).unmark();
                    printBlock("Done! Task has been marked as not done yet:",
                            "  " + tasks.get(index));
                } else if (breakdown[0].equals("todo")) {
                    String description = requireArguments(breakdown, "todo", "todo read book");
                    Todo todo = new Todo(description);
                    tasks.add(todo);
                    printBlock("Okay! TODO successfully added:",
                            "  " + todo,
                            "Now you have " + tasks.size() + " tasks in your list.");
                } else if (breakdown[0].equals("deadline")) {
                    String example = "deadline return book /by Sunday";
                    String arguments = requireArguments(breakdown, "deadline", example);
                    String[] parts = requireKeyword(arguments, "/by", example);
                    Deadline deadline = new Deadline(parts[0], parts[1]);
                    tasks.add(deadline);
                    printBlock("Okay! Deadline successfully added:",
                            "  " + deadline,
                            "Now you have " + tasks.size() + " tasks in your list.");
                } else if (breakdown[0].equals("event")) {
                    String example = "event project meeting /from Mon 2pm /to 4pm";
                    String arguments = requireArguments(breakdown, "event", example);
                    String[] fromParts = requireKeyword(arguments, "/from", example);
                    String[] toParts = requireKeyword(fromParts[1], "/to", example);
                    Event event = new Event(fromParts[0], toParts[0], toParts[1]);
                    tasks.add(event);
                    printBlock("Okay! Event successfully added:",
                            "  " + event,
                            "Now you have " + tasks.size() + " tasks in your list.");
                } else if (breakdown[0].equals("delete")) {
                    int index = parseIndex(breakdown, tasks);
                    Task deleted = tasks.delete(index);
                    printBlock("Task successfully removed: ",
                            " " + deleted,
                            "Now you have " + tasks.size() + " tasks in your list.");
                } else {
                    throw new MinweederException("That's not even a command? Theres todo, deadline, event, list, mark, unmark, bye.");
                }
            } catch (MinweederException e) {
                printBlock("Erm...you can't do that..." + e.getMessage());
            }
        }
        scanner.close();
    }
}
