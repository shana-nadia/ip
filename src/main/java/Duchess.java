import java.util.Scanner;

/**
 * Starting point of the Duchess chatbot.
 * Handles user commands and input and responds accordingly.
 */
public class Duchess {
    /**
     * Runs the Duchess chatbot.
     * @param args Command-line arguments, not used
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TodoList todolist = FileStorage.fetchTasks();

        System.out.println("Good day to you! I'm Duchess.");
        System.out.println("What can I do for you today?");

        label:
        while (true) {
            try {
                String input = scanner.nextLine();
                String[] words = input.split(" ", 2);
                String command = words[0];

                switch (command) {
                case "bye":
                    System.out.println("Bye! See you again next time!");
                    break label;
                case "list":
                    System.out.println("Here is your todo list!");
                    todolist.print();
                    break;
                case "mark": {
                    if (words.length < 2) {
                        throw new DuchessException("Please specify which task number to mark :(");
                    }

                    int taskNumber = Integer.parseInt(words[1]) - 1;

                    if (taskNumber < 0 || taskNumber >= todolist.size()) {
                        throw new DuchessException("Oh no! That task number does not exist :(");
                    }

                    todolist.getTask(taskNumber).mark();
                    FileStorage.writeTasks(todolist);
                    System.out.println("Congratulations on finishing your task! Task has been marked accordingly:\n" + todolist.getTask(taskNumber));
                    break;
                }
                case "unmark": {
                    if (words.length < 2) {
                        throw new DuchessException("Please specify which task number to unmark :(");
                    }
                    int taskNumber = Integer.parseInt(words[1]) - 1;

                    if (taskNumber < 0 || taskNumber >= todolist.size()) {
                        throw new DuchessException("Oh no! That task number does not exist :(");
                    }

                    todolist.getTask(taskNumber).unmark();
                    FileStorage.writeTasks(todolist);
                    System.out.println("Got it! Task has been unmarked accordingly:\n" + todolist.getTask(taskNumber));
                    break;
                }
                case "todo": {
                    if (words.length < 2 || words[1].trim().isEmpty()) {
                        throw new DuchessException("Oh no! Can't have a todo task without something to do :(");
                    }
                    Task task = new TodoTask(words[1]);
                    todolist.addTask(task);
                    System.out.println(
                            "Sure! Task added:\n"
                                    + task + "\n"
                                    + "Now you have " + todolist.size() + " tasks left!"
                    );
                    break;
                }
                case "deadline": {
                    if (words.length < 2) {
                        throw new DuchessException("Oh no! Can't have a deadline task without something to do :");
                    }

                    if (!words[1].contains(" /by ")) {
                        throw new DuchessException("Oh no! Can't have a deadline task without a deadline :(");
                    }

                    String[] parts = words[1].split(" /by ", 2);
                    String description = parts[0];
                    String deadline = parts[1];

                    Task task = new DeadlineTask(description, deadline);
                    todolist.addTask(task);
                    FileStorage.writeTasks(todolist);

                    System.out.println(
                            "Sure! Task added:\n"
                                    + task + "\n"
                                    + "Now you have " + todolist.size() + " tasks left!"
                    );
                    break;
                }
                case "event": {
                    if (words.length < 2) {
                        throw new DuchessException("Oh no! Can't have an event task without an event :(");
                    }

                    if (!words[1].contains(" /from ") || !words[1].contains(" /to ")) {
                        throw new DuchessException("Oh no! Can't have an event task without a start and end time :(");
                    }

                    String[] first_parts = words[1].split(" /from ", 2);
                    String[] second_parts = first_parts[1].split(" /to ", 2);
                    String description = first_parts[0];
                    String start = second_parts[0];
                    String end = second_parts[1];

                    Task task = new EventTask(description, start, end);
                    todolist.addTask(task);
                    FileStorage.writeTasks(todolist);

                    System.out.println(
                            "Sure! Task added:\n"
                                    + task + "\n"
                                    + "Now you have " + todolist.size() + " tasks left!"
                    );
                    break;
                }
                case "delete": {
                    if (words.length < 2) {
                        throw new DuchessException("Please specify which task number to delete :(");
                    }

                    int taskNumber = Integer.parseInt(words[1]) - 1;

                    if (taskNumber < 0 || taskNumber >= todolist.size()) {
                        throw new DuchessException("Oh no! That task number does not exist :(");
                    }

                    Task deleted = todolist.deleteTask(taskNumber);
                    FileStorage.writeTasks(todolist);
                    System.out.println(
                            "Okay! I have removed this task:\n"
                                    + deleted + "\n"
                                    + "Now you have " + todolist.size() + " tasks left!"
                    );
                    break;
                }
                default:
                    throw new DuchessException("Sorry! I don't know what that command means :(");
                }
            } catch (DuchessException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.close();
    }
}
