package duchess.ui;

import java.util.Scanner;

/**
 * Handles the Ui interaction between the chatbot and the user.
 */
public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Greets the user upon loading the chatbot.
     */
    public void greet() {
        System.out.println("Good day to you! I'm Duchess.");
        System.out.println("What can I do for you today?");
    }

    /**
     * Adds a dividing line between dialogue.
     */
    public void separateWithLine() {
        System.out.println("______________________________________");
    }

    /**
     * Reads input command from user.
     * @return input from user
     */
    public String readInput() {
        return this.scanner.nextLine();
    }

    /**
     * Replies to user with given message.
     */
    public void reply(String msg) {
        System.out.println(msg);
    }

    /**
     * Closes scanner at end of program.
     */
    public void closeScanner() {
        this.scanner.close();
    }
}
