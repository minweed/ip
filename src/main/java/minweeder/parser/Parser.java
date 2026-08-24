package minweeder.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import minweeder.command.CommandWord;
import minweeder.exception.MinweederException;
import minweeder.task.TaskList;

/**
 * Provides static helper methods for parsing raw user input into commands and arguments.
 */
public class Parser {
    private static final DateTimeFormatter DEADLINE_INPUT_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter QUERY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("d/M/yyyy");

    /**
     * Splits raw user input into the command word and the rest of the arguments.
     *
     * @param command the full line of user input.
     * @return an array of at most 2 elements: the command word, and (if present) the remaining arguments.
     */
    public static String[] splitCommand(String command) {
        return command.split(" ", 2);
    }

    /**
     * Parses the command word portion of a split command.
     *
     * @param breakdown the result of {@link #splitCommand(String)}.
     * @return the matching {@link CommandWord}.
     * @throws MinweederException if the command word is not recognized.
     */
    public static CommandWord parseCommandWord(String[] breakdown) throws MinweederException {
        return CommandWord.getCommandWord(breakdown[0]);
    }

    /**
     * Extracts and validates the argument text following a command word.
     *
     * @param breakdown the result of {@link #splitCommand(String)}.
     * @param commandWord the name of the command, used in the error message.
     * @param example an example of valid usage, used in the error message.
     * @return the trimmed argument text.
     * @throws MinweederException if no argument text was supplied.
     */
    public static String requireArguments(String[] breakdown, String commandWord, String example)
            throws MinweederException {
        if (breakdown.length < 2 || breakdown[1].isBlank()) {
            throw new MinweederException("a " + commandWord + " needs a description. e.g. "
                    + example);
        }
        return breakdown[1].trim();
    }

    /**
     * Splits text on a required keyword, ensuring both sides are non-blank.
     *
     * @param text the text to split, e.g. "return book /by 2/12/2019 1800".
     * @param keyword the keyword to split on, e.g. "/by".
     * @param example an example of valid usage, used in the error message.
     * @return a 2-element array: the text before the keyword, and the text after it, both trimmed.
     * @throws MinweederException if the keyword is missing or either side is blank.
     */
    public static String[] requireKeyword(String text, String keyword, String example)
            throws MinweederException {
        String[] parts = text.split(" " + keyword + " ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new MinweederException("You need something on either side of " + keyword
                    + ". e.g. " + example);
        }
        return new String[] {parts[0].trim(), parts[1].trim()};
    }

    /**
     * Parses and validates a 1-based task index supplied by the user, converting it to 0-based.
     *
     * @param breakdown the result of {@link #splitCommand(String)}.
     * @param tasks the current task list, used to validate the index is in range.
     * @return the 0-based index of the task.
     * @throws MinweederException if no number was supplied, it isn't a number, or it's out of range.
     */
    public static int parseIndex(String[] breakdown, TaskList tasks) throws MinweederException {
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

    /**
     * Parses deadline "by" text into a {@link LocalDateTime}.
     *
     * @param text the date/time text, expected in "d/M/yyyy HHmm" format.
     * @param example an example of valid usage, used in the error message.
     * @return the parsed date and time.
     * @throws MinweederException if the text does not match the expected format.
     */
    public static LocalDateTime parseDeadlineBy(String text, String example) throws MinweederException {
        try {
            return LocalDateTime.parse(text, DEADLINE_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MinweederException("please use d/M/yyyy HHmm for the date, e.g. " + example);
        }
    }

    /**
     * Parses "on" query text into a {@link LocalDate}.
     *
     * @param text the date text, expected in "d/M/yyyy" format.
     * @param example an example of valid usage, used in the error message.
     * @return the parsed date.
     * @throws MinweederException if the text does not match the expected format.
     */
    public static LocalDate parseOnDate(String text, String example) throws MinweederException {
        try {
            return LocalDate.parse(text, QUERY_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new MinweederException("please use d/M/yyyy for the date, e.g. " + example);
        }
    }
}
