import java.util.Scanner;

public class Duchess {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TodoList todolist = new TodoList();

        System.out.println("Good day to you! I'm Duchess.");
        System.out.println("What can I do for you today?");

        while (true) {
            String input = scanner.nextLine();
            String[] words = input.split(" ", 2);
            if (input.equals("bye")) {
                System.out.println("Bye! See you again next time!");
                break;
            }
            if (input.equals("list")) {
                System.out.println("Here is your todo list!");
                todolist.print();
            } else if (words[0].equals("mark")) {
                int taskNumber = Integer.parseInt(words[1]) - 1;
                todolist.getTask(taskNumber).mark();
                System.out.println("Congratulations on finishing your task! Task has been marked accordingly:\n" + todolist.getTask(taskNumber));
            } else if (words[0].equals("unmark")) {
                int taskNumber = Integer.parseInt(words[1]) - 1;
                todolist.getTask(taskNumber).unmark();
                System.out.println("Got it! Task has been unmarked accordingly:\n" + todolist.getTask(taskNumber));
            } else {
                Task task = new Task(input);
                todolist.addTask(task);
                System.out.println("added: " + input);
            }
        }

        scanner.close();
    }
}
