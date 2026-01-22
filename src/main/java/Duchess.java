import java.util.Scanner;

public class Duchess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Good day to you! I'm Duchess.");
        System.out.println("What can I do for you today?");
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("Bye! See you again next time!");
                break;
            }
            System.out.println(input);
        }
    }
}
