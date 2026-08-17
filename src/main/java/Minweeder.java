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

        String[] storage = new String[100];
        int counter = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.print(line);
                System.out.print(goodbye);
                System.out.print(line);
                break;
            } else if (command.equals("list")) {
                System.out.print(line);
                for (int i = 0; i < counter; i++) {
                    System.out.println((i + 1) + ". " + storage[i]);
                }
                System.out.print(line);
            } else {
                System.out.print(line);
                storage[counter] = command;
                counter++;
                System.out.println("added: " + command);
                System.out.print(line);
            }
        }
        scanner.close();
    }
}
