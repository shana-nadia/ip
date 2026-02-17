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
    private static final String COMMAND_SORT = "sort";

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
        input = normalizeInput(input); // ChatGPT: normalise spaces
        try {
            String command = Parser.getCommand(input);
            assert command != null : "Parsed command should not be null";
            assert !command.isEmpty() : "Parsed command should not be empty";
            String rest = Parser.getRest(input);

            switch (command) {
            case COMMAND_BYE: {
                commandType = TYPE_DEFAULT;
                return getFarewell();
            }

            case COMMAND_LIST: {
                commandType = TYPE_DEFAULT;
                return "Very well. Behold your royal obligations, peasant:\n" + todoList;
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

            case COMMAND_SORT: {
                commandType = TYPE_DEFAULT;
                todoList.sortByTime();
                return "Very well. I have arranged your obligations in proper chronological order, peasant:\n"
                        + todoList;
            }

            default: {
                commandType = TYPE_DEFAULT;
                throw new DuchessException(
                        "Foolish peasant. That command is beneath my understanding. "
                                + "Try: todo, deadline, event, list, mark, unmark, delete, "
                                + "find, sort, bye" // Written by ChatGPT
                );
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

    // Written by ChatGPT
    private String normalizeInput(String input) {
        return input.trim().replaceAll("\\s+", " ");
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
            throw new DuchessException("Speak clearly, peasant. Which task number do you mean?");
        }

        int index;
        try {
            index = Integer.parseInt(rest) - 1;
        } catch (NumberFormatException e) {
            throw new DuchessException("That number makes no sense in my kingdom.");
        }

        if (index < 0 || index >= todoList.size()) {
            throw new DuchessException(
                    "Such a task number does not exist in my kingdom, peasant. Task number must be between 1 and "
                            + todoList.size() // Written by ChatGPT
            );
        }

        assert index >= 0 && index < todoList.size() : "Task number out of bounds";

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
                "Impressive, peasant. Even I am mildly pleased.\n");
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
                "A shame, peasant. I expected better discipline. I shall mark this task as unfinished:\n");
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

    // Written by ChatGPT
    private String addTaskAndConfirm(Task task) throws DuchessException {
        todoList.addTask(task);
        FileStorage.writeTasks(todoList);
        return "As you command, peasant. I have inscribed this task into the royal ledger:\n"
                + task
                + "\nYou now possess " + todoList.size() + " obligations under my watch.";
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

        return "So be it. This task shall be erased from existence:\n"
                + removedTask
                + "\nYou now possess " + todoList.size() + " obligations under my watch.";
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
            throw new DuchessException("You dare present me with an incomplete decree?");
        }

        Task task = new TodoTask(rest);
        return addTaskAndConfirm(task);
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
                    "You must declare the deadline using /by.\n"
                            + "Example: deadline buy porridge /by 2026-02-20 1800.\n" // Written by ChatGPT
                            + "Even royalty requires proper format, peasant.");
        }

        String[] parts = rest.split("/by", 2);
        assert parts.length == 2 : "Deadline command should contain /by";

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty() || by.isEmpty()) {
            throw new DuchessException("You dare submit an incomplete decree to the throne?");
        }

        validateDateTime(by); // Written by ChatGPT

        Task task = new DeadlineTask(description, by);
        return addTaskAndConfirm(task);
    }

    /**
     * Creates and adds an event task to the todo list.
     * @param rest the user input containing the task description, start, and end time
     * @return a confirmation message showing the added task
     * @throws DuchessException if the input format is invalid or incomplete
     */
    private String handleEvent(String rest) throws DuchessException {
        commandType = TYPE_ADD;

        if (!rest.contains("/from") || !rest.contains("/to")) {
            throw new DuchessException(
                    "An event without a start and end time? Even chaos has structure, peasant.\n"
                            + "Event tasks require /from and /to times.\n"
                            + "Example: event project meeting /from 2026-02-16 1400 "
                            + "/to 2026-02-16 1500" // Written by ChatGPT
            );
        }

        String[] first = rest.split("/from", 2);
        String description = first[0].trim();
        assert first.length == 2 : "Event should contain /from";

        String[] second = first[1].split("/to", 2);
        String from = second[0].trim();
        String to = second[1].trim();

        assert second.length == 2 : "Event should contain /to";

        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new DuchessException("An event without proper detail? Explain yourself, peasant.");
        }

        // Written by ChatGPT
        validateDateTime(from);
        validateDateTime(to);

        // Written by ChatGPT
        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        java.time.LocalDateTime start =
                java.time.LocalDateTime.parse(from, formatter);
        java.time.LocalDateTime end =
                java.time.LocalDateTime.parse(to, formatter);

        if (start.isAfter(end)) {
            throw new DuchessException("The beginning of an event cannot occur after its end, peasant. "
                    + "Even time obeys order.");
        }

        Task task = new EventTask(description, from, to);
        return addTaskAndConfirm(task);
    }

    // Written by ChatGPT
    private void validateDateTime(String dateTime) throws DuchessException {
        try {
            // Using yyyy-MM-dd HHmm format for simplicity
            java.time.LocalDateTime.parse(dateTime, java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        } catch (java.time.format.DateTimeParseException e) {
            throw new DuchessException(
                    "You must declare time in the sacred format yyyy-MM-dd HHmm.\n"
                            + "Example: 2026-02-16 1400"
            );
        }
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
            throw new DuchessException("Speak your keyword clearly, peasant. Example: find farmwork");
        }

        TodoList matchedTasks = todoList.findTasks(rest);

        return "These are the tasks that match your feeble search, peasant:\n" + matchedTasks;
    }

    /**
     * Returns Duchess's royal greeting.
     * @return greeting
     */
    public String getGreeting() {
        return "Bow before Duchess. State your business, peasant.";
    }

    /**
     * Returns Duchess's royal farewell
     * @return farewell
     */
    public String getFarewell() {
        return "You are dismissed, peasant. Return when you require my wisdom again.";
    }
}
