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
            String command = words[0];

            if (command.equals("bye")) {
                System.out.println("Bye! See you again next time!");
                break;
            }
            if (command.equals("list")) {
                System.out.println("Here is your todo list!");
                todolist.print();
            } else if (command.equals("mark")) {
                int taskNumber = Integer.parseInt(words[1]) - 1;
                todolist.getTask(taskNumber).mark();
                System.out.println("Congratulations on finishing your task! Task has been marked accordingly:\n" + todolist.getTask(taskNumber));
            } else if (command.equals("unmark")) {
                int taskNumber = Integer.parseInt(words[1]) - 1;
                todolist.getTask(taskNumber).unmark();
                System.out.println("Got it! Task has been unmarked accordingly:\n" + todolist.getTask(taskNumber));
            } else if (command.equals("todo")) {
                Task task = new TodoTask(words[1]);
                todolist.addTask(task);
                System.out.println("Sure! Task added:\n" + task);
            } else if (command.equals("deadline")) {
                String[] parts = words[1].split(" /by ", 2);
                String description = parts[0];
                String deadline = parts[1];

                Task task = new DeadlineTask(description, deadline);
                todolist.addTask(task);

                System.out.println("Sure! Task added:\n" + task);
            } else if (command.equals("event")) {
                String[] first_parts = words[1].split(" /from ", 2);
                String[] second_parts = first_parts[1].split(" /to ", 2);
                String description = first_parts[0];
                String start = second_parts[0];
                String end = second_parts[1];

                Task task = new EventTask(description, start, end);
                todolist.addTask(task);

                System.out.println("Sure! Task added:\n" + task);
            } else {
                    System.out.println("Sorry!");
                }
            }

        scanner.close();
    }
}
