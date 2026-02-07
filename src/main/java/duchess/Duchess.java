package duchess;

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
    private final TodoList todolist;
    private String commandType;

    public Duchess() {
        this.todolist = FileStorage.fetchTasks();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            String command = Parser.getCommand(input);
            String rest = Parser.getRest(input);

            switch (command) {
            case "bye":
                this.commandType = "DEFAULT";
                return "Bye! See you again next time!";

            case "list":
                this.commandType = "DEFAULT";
                return "Here is your todo list!\n" + todolist.toString();

            case "mark": {
                this.commandType = "MARK";
                if (rest.isEmpty()) {
                    throw new DuchessException("Please specify which task number to mark :(");
                }

                int taskNumber;
                try {
                    taskNumber = Integer.parseInt(rest) - 1;
                } catch (NumberFormatException e) {
                    throw new DuchessException("Please enter a valid task number :(");
                }

                if (taskNumber < 0 || taskNumber >= todolist.size()) {
                    throw new DuchessException("Oh no! That task number does not exist :(");
                }

                todolist.getTask(taskNumber).mark();
                FileStorage.writeTasks(todolist);

                return "Congratulations on finishing your task!\n"
                        + todolist.getTask(taskNumber);
            }

            case "unmark": {
                this.commandType = "MARK";
                if (rest.isEmpty()) {
                    throw new DuchessException("Please specify which task number to unmark :(");
                }

                int taskNumber;
                try {
                    taskNumber = Integer.parseInt(rest) - 1;
                } catch (NumberFormatException e) {
                    throw new DuchessException("Please enter a valid task number :(");
                }

                if (taskNumber < 0 || taskNumber >= todolist.size()) {
                    throw new DuchessException("Oh no! That task number does not exist :(");
                }

                todolist.getTask(taskNumber).unmark();
                FileStorage.writeTasks(todolist);

                return "Got it! Task has been unmarked:\n"
                        + todolist.getTask(taskNumber);
            }

            case "todo": {
                this.commandType = "ADD";
                if (rest.trim().isEmpty()) {
                    throw new DuchessException("Oh no! Can't have a todo task without something to do :(");
                }

                Task task = new TodoTask(rest);
                todolist.addTask(task);
                FileStorage.writeTasks(todolist);

                return "Sure! Task added:\n"
                        + task + "\n"
                        + "Now you have " + todolist.size() + " tasks left!";
            }

            case "deadline": {
                this.commandType = "ADD";
                if (!rest.contains(" /by ")) {
                    throw new DuchessException("Oh no! Can't have a deadline task without a deadline :(");
                }

                String[] parts = rest.split(" /by ", 2);
                Task task = new DeadlineTask(parts[0], parts[1]);
                todolist.addTask(task);
                FileStorage.writeTasks(todolist);

                return "Sure! Task added:\n"
                        + task + "\n"
                        + "Now you have " + todolist.size() + " tasks left!";
            }

            case "event": {
                this.commandType = "ADD";
                if (!rest.contains(" /from ") || !rest.contains(" /to ")) {
                    throw new DuchessException("Oh no! Can't have an event task without a start and end time :(");
                }

                String[] first = rest.split(" /from ", 2);
                String[] second = first[1].split(" /to ", 2);

                Task task = new EventTask(first[0], second[0], second[1]);
                todolist.addTask(task);
                FileStorage.writeTasks(todolist);

                return "Sure! Task added:\n"
                        + task + "\n"
                        + "Now you have " + todolist.size() + " tasks left!";
            }

            case "delete": {
                this.commandType = "DELETE";
                if (rest.trim().isEmpty()) {
                    throw new DuchessException("Please specify which task number to delete :(");
                }

                int taskNumber;
                try {
                    taskNumber = Integer.parseInt(rest) - 1;
                } catch (NumberFormatException e) {
                    throw new DuchessException("Please enter a valid task number :(");
                }

                if (taskNumber < 0 || taskNumber >= todolist.size()) {
                    throw new DuchessException("Oh no! That task number does not exist :(");
                }

                Task deleted = todolist.deleteTask(taskNumber);
                FileStorage.writeTasks(todolist);

                return "Okay! I have removed this task:\n"
                        + deleted + "\n"
                        + "Now you have " + todolist.size() + " tasks left!";
            }

            case "find": {
                this.commandType = "DEFAULT";
                if (rest.trim().isEmpty()) {
                    throw new DuchessException("Please specify a keyword to search for :(");
                }

                String keyword = rest.trim();
                StringBuilder res = new StringBuilder("Here are the matching tasks in your list:\n");
                int index = 1;

                for (Task task : todolist.getTasks()) {
                    if (task.getDescription().contains(keyword)) {
                        res.append(index).append(". ").append(task).append("\n");
                    }
                    index++;
                }

                return res.toString();
            }

            default:
                this.commandType = "DEFAULT";
                throw new DuchessException("Sorry! I don't know what that command means :(");
            }

        } catch (DuchessException e) {
            this.commandType = "DEFAULT";
            return e.getMessage();
        }
    }

    public String getCommandType() {
        return this.commandType;
    }
}
