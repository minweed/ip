package minweeder.exception;

/**
 * Signals that a user command could not be understood or executed,
 * carrying a user-friendly message that explains what went wrong.
 */
public class MinweederException extends Exception {
    /**
     * Creates an exception with a message describing the problem to show the user.
     *
     * @param message the user-facing error message.
     */
    public MinweederException(String message) {
        super(message);
    }
}
