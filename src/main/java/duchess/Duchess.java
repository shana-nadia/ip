package duchess;

import duchess.ui.Ui;
import duchess.list.TodoList;
import duchess.storage.FileStorage;
import duchess.parser.Parser;
import duchess.exception.DuchessException;
import duchess.task.Task;
import duchess.task.TodoTask;
import duchess.task.DeadlineTask;
import duchess.task.EventTask;

/**
 * Starting point of the Duchess chatbot.
 * Handles user commands and input and responds accordingly.
 */
public class Duchess {
    private final Ui ui;
    private final TodoList todolist;

    public Duchess() {
        this.ui = new Ui();
        this.todolist = FileStorage.fetchTasks();
    }

    /**
     * Runs main loop of Duchess chatbot.
     * Reads user input, parses and executes commands and displays responses until user exits the chatbot.
     */
    public void run() {
        this.ui.greet();

        boolean isExit = false;
        while (!isExit) {
            try {
                String input = ui.readInput();
                String command = Parser.getCommand(input);
                String rest = Parser.getRest(input);

                switch (command) {
                case "bye":
                    this.ui.reply("Bye! See you again next time!");
                    isExit = true;
                    break;

                case "list":
                    this.ui.reply("Here is your todo list!");
                    this.todolist.print();
                    break;

                case "mark": {
                    if (rest.isEmpty()) {
                        throw new DuchessException("Please specify which task number to mark :(");
                    }

                    int taskNumber = Integer.parseInt(rest) - 1;

                    if (taskNumber < 0 || taskNumber >= this.todolist.size()) {
                        throw new DuchessException("Oh no! That task number does not exist :(");
                    }

                    this.todolist.getTask(taskNumber).mark();
                    FileStorage.writeTasks(this.todolist);
                    this.ui.reply("Congratulations on finishing your task! Task has been marked accordingly:\n"
                            + this.todolist.getTask(taskNumber));
                    break;
                }

                case "unmark": {
                    if (rest.isEmpty()) {
                        throw new DuchessException("Please specify which task number to unmark :(");
                    }

                    int taskNumber = Integer.parseInt(rest) - 1;
                    if (taskNumber < 0 || taskNumber >= this.todolist.size()) {
                        throw new DuchessException("Oh no! That task number does not exist :(");
                    }

                    this.todolist.getTask(taskNumber).unmark();
                    FileStorage.writeTasks(this.todolist);

                    ui.reply(
                            "Got it! Task has been unmarked accordingly:\n"
                                    + this.todolist.getTask(taskNumber)
                    );
                    break;
                }

                case "todo": {
                    if (rest.trim().isEmpty()) {
                        throw new DuchessException("Oh no! Can't have a todo task without something to do :(");
                    }

                    Task task = new TodoTask(rest);
                    this.todolist.addTask(task);

                    ui.reply(
                            "Sure! Task added:\n"
                                    + task + "\n"
                                    + "Now you have " + this.todolist.size() + " tasks left!"
                    );
                    break;
                }

                case "deadline": {
                    if (!rest.contains(" /by ")) {
                        throw new DuchessException("Oh no! Can't have a deadline task without a deadline :(");
                    }

                    String[] parts = rest.split(" /by ", 2);
                    Task task = new DeadlineTask(parts[0], parts[1]);
                    todolist.addTask(task);
                    FileStorage.writeTasks(todolist);

                    ui.reply(
                            "Sure! Task added:\n"
                                    + task + "\n"
                                    + "Now you have " + this.todolist.size() + " tasks left!"
                    );
                    break;
                }

                case "event": {
                    if (!rest.contains(" /from ") || !rest.contains(" /to ")) {
                        throw new DuchessException("Oh no! Can't have an event task without a start and end time :(");
                    }

                    String[] first = rest.split(" /from ", 2);
                    String[] second = first[1].split(" /to ", 2);

                    Task task = new EventTask(first[0], second[0], second[1]);
                    this.todolist.addTask(task);
                    FileStorage.writeTasks(todolist);

                    this.ui.reply(
                            "Sure! Task added:\n"
                                    + task + "\n"
                                    + "Now you have " + this.todolist.size() + " tasks left!"
                    );
                    break;
                }

                case "delete": {
                    if (rest.trim().isEmpty()) {
                        throw new DuchessException("Please specify which task number to delete :(");
                    }

                    int taskNumber = Integer.parseInt(rest) - 1;
                    if (taskNumber < 0 || taskNumber >= this.todolist.size()) {
                        throw new DuchessException("Oh no! That task number does not exist :(");
                    }

                    Task deleted = this.todolist.deleteTask(taskNumber);
                    FileStorage.writeTasks(this.todolist);

                    this.ui.reply(
                            "Okay! I have removed this task:\n"
                                    + deleted + "\n"
                                    + "Now you have " + this.todolist.size() + " tasks left!"
                    );
                    break;
                }

                case "find": {
                    if (rest.trim().isEmpty()) {
                        throw new DuchessException("Please specify a keyword to search for :(");
                    }

                    String keyword = rest.trim();
                    StringBuilder res = new StringBuilder("Here are the matching tasks in your list:\n");
                    int index = 1;

                    for (Task task : this.todolist.getTasks()) {
                        if (task.getDescription().contains(keyword)) {
                            res.append(index).append(". ").append(task).append("\n");
                        }
                        index++;
                    }

                    ui.reply(res.toString());
                    break;
                }

                default:
                    throw new DuchessException("Sorry! I don't know what that command means :(");
                }

                this.ui.separateWithLine();

            } catch (DuchessException e) {
                ui.reply(e.getMessage());
                ui.separateWithLine();
            }
        }

        ui.closeScanner();
    }

    /**
     * Runs the Duchess chatbot.
     * @param args Command-line arguments, not used
     */
    public static void main(String[] args) {
        new Duchess().run();
    }
}
