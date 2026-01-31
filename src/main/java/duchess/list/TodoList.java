package duchess.list;

import java.util.ArrayList;

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
     * Prints all tasks in the list in order.
     */
    public void print() {
        for (int i = 0; i < this.tasks.size(); i++) {
            System.out.println((i + 1) + ". " + getTask(i));
        }
    }

    /**
     * Retrieves the task with the given task number.
     * @param taskNumber the number of the task to retrieve
     * @return the task with the given number
     */
    public Task getTask(int taskNumber) {
        return this.tasks.get(taskNumber);
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
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
        return tasks.remove(taskNumber);
    }
}
