package duchess.list;

import java.time.LocalDateTime;
import java.util.ArrayList;

import duchess.task.DeadlineTask;
import duchess.task.EventTask;
import duchess.task.Task;

/**
 * Stores a list of tasks for the user to complete in an ArrayList.
 */
public class TodoList {
    private final ArrayList<Task> tasks;

    public TodoList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the todolist.
     * @param task the task to be added
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Retrieves the task with the given task number.
     * @param taskNumber the number of the task to retrieve
     * @return the task with the given number
     */
    public Task getTask(int taskNumber) {
        assert taskNumber >= 0 && taskNumber < tasks.size() : "Task index out of bounds in getTask";
        return this.tasks.get(taskNumber);
    }

    /**
     * Returns a new TodoList containing tasks whose string representation
     * contains the given keyword.
     *
     * @param keyword the keyword to search for
     * @return a TodoList of matching tasks
     */
    public TodoList findTasks(String keyword) {
        TodoList matched = new TodoList();

        tasks.stream()
                .filter(task -> task.toString().contains(keyword))
                .forEach(matched::addTask);

        return matched;
    }

    /**
     * Returns the number of tasks currently in the todolist.
     * @return the size of the todolist.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Removes and returns the task with the given number.
     * @param taskNumber the number of the task to remove
     * @return the deleted task
     */
    public Task deleteTask(int taskNumber) {
        assert taskNumber >= 0 && taskNumber < tasks.size() : "Task index out of bounds in deleteTask";
        return tasks.remove(taskNumber);
    }

    /**
     * Sorts tasks chronologically.
     * - DeadlineTask sorted by deadline date
     * - EventTask sorted by start time
     * - Other tasks (e.g. TodoTask) placed at the end
     */
    public void sortByTime() {
        tasks.sort((t1, t2) -> {
            LocalDateTime time1 = getTaskTime(t1);
            LocalDateTime time2 = getTaskTime(t2);
            return time1.compareTo(time2);
        });
    }

    /**
     * Returns the time used for sorting a task.
     */
    private LocalDateTime getTaskTime(Task task) {
        if (task instanceof DeadlineTask) {
            return ((DeadlineTask) task).getDeadline().atStartOfDay();
        } else if (task instanceof EventTask) {
            return ((EventTask) task).getStart();
        } else {
            return LocalDateTime.MAX;
        }
    }

    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "Your todo list is empty!";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            stringBuilder.append(i + 1)
                    .append(". ")
                    .append(tasks.get(i))
                    .append("\n");
        }
        return stringBuilder.toString();
    }
}
