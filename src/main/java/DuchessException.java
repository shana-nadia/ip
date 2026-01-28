/**
 * Exception for errors in the Duchess chatbot.
 */
public class DuchessException extends Exception {
    /**
     * Creates a new DuchessException with the given message.
     *
     * @param msg the error message
     */
    public DuchessException(String msg) {
        super(msg);
    }
}
