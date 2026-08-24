package minweeder.command;

import minweeder.exception.MinweederException;

public enum CommandWord {
    LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, ON, BYE;

    public static CommandWord getCommandWord(String word) throws MinweederException {
        try {
            return CommandWord.valueOf(word.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new MinweederException("That's not even a command? Theres todo, deadline, event, "
                    + "list, mark, unmark, delete, on, bye.");
        }
    }
}
