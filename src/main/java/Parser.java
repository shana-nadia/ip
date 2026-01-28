public class Parser {

    /**
     * Returns the command word of the input string given by the user.
     * @param input full input string
     * @return command word
     */
    public static String getCommand(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts[0];
    }

    /**
     * Returns the rest of the command, without the command word.
     * @param input full input string
     * @return description of command without command word.
     */
    public static String getRest(String input) {
        String[] parts = input.trim().split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }
}
