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

    public static void main(String[] args) {
        printBlock(BANNER, GREETING);

        TaskList tasks = new TaskList();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();
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
                int index = Integer.parseInt(breakdown[1]) - 1;
                tasks.get(index).mark();
                printBlock("Congrats! Task has been marked as completed:",
                        "  " + tasks.get(index));
            } else if (breakdown[0].equals("unmark")) {
                int index = Integer.parseInt(breakdown[1]) - 1;
                tasks.get(index).unmark();
                printBlock("Done! Task has been marked as not done yet:",
                        "  " + tasks.get(index));
            } else if (breakdown[0].equals("todo")) {
                Todo todo = new Todo(breakdown[1]);
                tasks.add(todo);
                printBlock("Okay! TODO successfully added:",
                        "  " + todo,
                        "Now you have " + tasks.size() + " tasks in your list.");
            } else if (breakdown[0].equals("deadline")) {
                String[] deadlineArgs = breakdown[1].split(" /by ", 2);
                Deadline deadline = new Deadline(deadlineArgs[0], deadlineArgs[1]);
                tasks.add(deadline);
                printBlock("Okay! Deadline successfully added:",
                        "  " + deadline,
                        "Now you have " + tasks.size() + " tasks in your list.");
            } else if (breakdown[0].equals("event")) {
                String[] eventArgs = breakdown[1].split(" /from ", 2);
                String description = eventArgs[0];
                String[] fromTo = eventArgs[1].split(" /to ", 2);
                Event event = new Event(description, fromTo[0], fromTo[1]);
                tasks.add(event);
                printBlock("Okay! Event successfully added:",
                        "  " + event,
                        "Now you have " + tasks.size() + " tasks in your list.");
            } else {
                tasks.add(new Task(command));
                printBlock("added: " + command);
            }
        }
        scanner.close();
    }
}
