package minweeder.exception;

/**
 * Signals a user-facing error, such as invalid input or a failed file operation.
 */
public class MinweederException extends Exception {
    public MinweederException(String message) {
        super(message);
    }
}
