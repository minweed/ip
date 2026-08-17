import java.util.Scanner;

public class Minweeder {
    public static void main(String[] args) {
        String banner = " __  __ ___ _   ___        _______ _____ ____  _____ ____  \n"
                + "|  \\/  |_ _| \\ | \\ \\      / / ____| ____|  _ \\| ____|  _ \\ \n"
                + "| |\\/| || ||  \\| |\\ \\ /\\ / /|  _| |  _| | | | |  _| | |_) |\n"
                + "| |  | || || |\\  | \\ V  V / | |___| |___| |_| | |___|  _ < \n"
                + "|_|  |_|___|_| \\_|  \\_/\\_/  |_____|_____|____/|_____|_| \\_\\\n";
        String line = "────────────────────────────────────────────────────────────────\n";
        String greeting = "Heyyo I'm Minweeder!\nLETS GET THINGS DONE RAHH\n";
        String goodbye = "Goodbye! Hope you had a productive session :)\n";

        System.out.print(line);
        System.out.println(banner);
        System.out.print(greeting);
        System.out.print(line);

        Task[] storage = new Task[100];
        int counter = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();
            String[] breakdown = command.split(" ");

            if (command.equals("bye")) {
                System.out.print(line);
                System.out.print(goodbye);
                System.out.print(line);
                break;
            } else if (command.equals("list")) {
                System.out.print(line);
                System.out.println("Here are your tasks:");
                for (int i = 0; i < counter; i++) {
                    System.out.println((i + 1) + ". " + storage[i]);
                }
                System.out.print(line);
            } else if (breakdown[0].equals("mark")) {
                int taskNumber = Integer.parseInt(breakdown[1]);
                int index = taskNumber - 1;
                storage[index].mark();
                System.out.print(line);
                System.out.println("Congrats! Task has been marked as completed:");
                System.out.println("  " + storage[index]);
                System.out.println(line);
            } else if (breakdown[0].equals("unmark")) {
                int taskNumber = Integer.parseInt(breakdown[1]);
                int index = taskNumber - 1;
                storage[index].unmark();
                System.out.print(line);
                System.out.println("Done! Task has been marked as not done yet:");
                System.out.println(" " + storage[index]);
                System.out.println(line);
            }
            else {
                System.out.print(line);
                Task task = new Task(command);
                storage[counter] = task;
                counter++;
                System.out.println("added: " + command);
                System.out.print(line);
            }
        }
        scanner.close();
    }
}
