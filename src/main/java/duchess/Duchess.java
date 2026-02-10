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
    private final TodoList todoList;
    private String commandType;

    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_FIND = "find";

    private static final String TYPE_DEFAULT = "DEFAULT";
    private static final String TYPE_ADD = "ADD";
    private static final String TYPE_MARK = "MARK";
    private static final String TYPE_DELETE = "DELETE";

    public Duchess() {
        this.todoList = FileStorage.fetchTasks();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        try {
            String command = Parser.getCommand(input);
            String rest = Parser.getRest(input);

            switch (command) {
            case COMMAND_BYE: {
                commandType = TYPE_DEFAULT;
                return "Bye! See you again next time!";
            }

            case COMMAND_LIST: {
                commandType = TYPE_DEFAULT;
                return "Here is your todo list!\n" + todoList;
            }

            case COMMAND_MARK: {
                return handleMark(rest);
            }

            case COMMAND_UNMARK: {
                return handleUnmark(rest);
            }

            case COMMAND_DELETE: {
                return handleDelete(rest);
            }

            case COMMAND_TODO: {
                return handleTodo(rest);
            }

            case COMMAND_DEADLINE: {
                return handleDeadline(rest);
            }

            case COMMAND_EVENT: {
                return handleEvent(rest);
            }

            case COMMAND_FIND: {
                return handleFind(rest);
            }

            default: {
                commandType = TYPE_DEFAULT;
                throw new DuchessException("Sorry! I don't know what that command means :(");
            }
            }
        } catch (DuchessException e) {
            commandType = TYPE_DEFAULT;
            return e.getMessage();
        }
    }

    /**
     * Retrieves the type of command the user has inputted.
     * @return type of user input
     */
    public String getCommandType() {
        return this.commandType;
    }

    /**
     * Parses the task index from user input and checks that it refers
     * to an existing task in the todo list.
     *
     * @param rest the remaining user input excluding the command word, containing the task number
     * @return the zero-based index of the task
     * @throws DuchessException if the input is empty, not a number,
     *                          or refers to a non-existent task
     */
    private int parseTaskIndex(String rest) throws DuchessException {
        if (rest.trim().isEmpty()) {
            throw new DuchessException("Please specify a task number :(");
        }

        int index;
        try {
            index = Integer.parseInt(rest) - 1;
        } catch (NumberFormatException e) {
            throw new DuchessException("Please enter a valid task number :(");
        }

        if (index < 0 || index >= todoList.size()) {
            throw new DuchessException("Oh no! That task number does not exist :(");
        }

        return index;
    }

    /**
     * Marks a task in the todo list as completed.
     *
     * @param rest the remaining user input excluding the command word, containing the task number
     * @return a confirmation message indicating the task was marked
     * @throws DuchessException if the task number is invalid
     */
    private String handleMark(String rest) throws DuchessException {
        return updateTaskMarkStatus(rest, true,
                "Congratulations on finishing your task!\n");
    }

    /**
     * Marks a task in the todo list as not completed.
     *
     * @param rest the remaining user input excluding the command word, containing the task number
     * @return a confirmation message indicating the task was unmarked
     * @throws DuchessException if the task number is invalid
     */
    private String handleUnmark(String rest) throws DuchessException {
        return updateTaskMarkStatus(rest, false,
                "Got it! Task has been unmarked:\n");
    }

    /**
     * Updates the marked status of a task and writes the change to storage.
     *
     * @param rest the remaining user input excluding the command word, containing the task number
     * @param isMark true to mark the task, false to unmark it
     * @param message the message to display after updating the task
     * @return a formatted confirmation message
     * @throws DuchessException if the task number is invalid
     */
    private String updateTaskMarkStatus(String rest, boolean isMark, String message)
            throws DuchessException {

        commandType = TYPE_MARK;

        int index = parseTaskIndex(rest);
        Task task = todoList.getTask(index);

        if (isMark) {
            task.mark();
        } else {
            task.unmark();
        }

        FileStorage.writeTasks(todoList);
        return message + task;
    }

    /**
     * Deletes a task from the todo list.
     *
     * @param rest the remaining user input excluding the command word, containing the task number
     * @return a confirmation message showing the removed task
     * @throws DuchessException if the task number is invalid
     */
    private String handleDelete(String rest) throws DuchessException {
        commandType = TYPE_DELETE;

        int index = parseTaskIndex(rest);
        Task removedTask = todoList.deleteTask(index);

        FileStorage.writeTasks(todoList);

        return "Noted. I've removed this task:\n"
                + removedTask
                + "\nNow you have " + todoList.size() + " tasks in the list.";
    }

    /**
     * Creates and adds a todo task to the todo list.
     *
     * @param rest the description of the todo task
     * @return a confirmation message showing the added task
     * @throws DuchessException if the description is empty
     */
    private String handleTodo(String rest) throws DuchessException {
        commandType = TYPE_ADD;

        if (rest.trim().isEmpty()) {
            throw new DuchessException("The description of a todo cannot be empty :(");
        }

        Task task = new TodoTask(rest);
        todoList.addTask(task);

        FileStorage.writeTasks(todoList);

        return "Got it. I've added this task:\n"
                + task
                + "\nNow you have " + todoList.size() + " tasks in the list.";
    }

    /**
     * Creates and adds a deadline task to the todo list.
     *
     * @param rest the user input containing the task description and deadline
     * @return a confirmation message showing the added task
     * @throws DuchessException if the input format is invalid or incomplete
     */
    private String handleDeadline(String rest) throws DuchessException {
        commandType = TYPE_ADD;

        if (!rest.contains("/by")) {
            throw new DuchessException(
                    "Please specify a deadline using /by\n"
                            + "Example: deadline return book /by Sunday");
        }

        String[] parts = rest.split("/by", 2);
        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new DuchessException("Deadline description or time cannot be empty :(");
        }

        Task task = new DeadlineTask(description, by);
        todoList.addTask(task);

        FileStorage.writeTasks(todoList);

        return "Got it. I've added this task:\n"
                + task
                + "\nNow you have " + todoList.size() + " tasks in the list.";
    }

    /**
     * Creates and adds an event task to the todo list.
     *
     * @param rest the user input containing the task description, start, and end time
     * @return a confirmation message showing the added task
     * @throws DuchessException if the input format is invalid or incomplete
     */
    private String handleEvent(String rest) throws DuchessException {
        commandType = TYPE_ADD;

        if (!rest.contains("/from") || !rest.contains("/to")) {
            throw new DuchessException(
                    "Please specify an event using /from and /to\n"
                            + "Example: event project meeting /from Mon 2pm /to 4pm");
        }

        String[] fromSplit = rest.split("/from", 2);
        String description = fromSplit[0].trim();

        String[] toSplit = fromSplit[1].split("/to", 2);
        String from = toSplit[0].trim();
        String to = toSplit[1].trim();

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DuchessException("Event description or time cannot be empty :(");
        }

        Task task = new EventTask(description, from, to);
        todoList.addTask(task);

        FileStorage.writeTasks(todoList);

        return "Got it. I've added this task:\n"
                + task
                + "\nNow you have " + todoList.size() + " tasks in the list.";
    }

    /**
     * Finds and displays tasks that contain the given keyword.
     *
     * @param rest the keyword used to search for matching tasks
     * @return a list of tasks that match the keyword
     * @throws DuchessException if the keyword is empty
     */
    private String handleFind(String rest) throws DuchessException {
        commandType = TYPE_DEFAULT;

        if (rest.trim().isEmpty()) {
            throw new DuchessException("Please specify a keyword to search for :(");
        }

        TodoList matchedTasks = todoList.findTasks(rest);

        return "Here are the matching tasks in your list:\n" + matchedTasks;
    }
}
