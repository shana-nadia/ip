package duchess.parser;

/**
 * Handles parsing of commands inputted by user.
 */
public class Parser {

    /**
     * Returns the command word of the input string given by the user.
     * @param input full input string
     * @return command word
     */
    public static String getCommand(String input) {
        String[] parts = input.trim().split(" ", 2);
        assert parts.length >= 1 : "Input should always have at least one part for command";
        assert parts[0] != null && !parts[0].isEmpty() : "Command should not be null or empty";
        return parts[0];
    }

    /**
     * Returns the rest of the command, without the command word.
     * @param input full input string
     * @return description of command without command word.
     */
    public static String getRest(String input) {
        String[] parts = input.trim().split(" ", 2);
        assert parts != null : "Split parts should not be null";
        return parts.length > 1 ? parts[1] : "";
    }
}
