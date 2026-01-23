import java.util.ArrayList;

/**
 * Stores a list of tasks for the user to complete.
 */
public class TodoList {
    private final ArrayList<Task> tasks;

    public TodoList() {
        this.tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public void print() {
        for (int i = 0; i < this.tasks.size(); i++) {
            System.out.println((i + 1) + ". " + getTask(i));
        }
    }

    public Task getTask(int taskNumber) {
        return this.tasks.get(taskNumber);
    }

    public int size() {
        return this.tasks.size();
    }

    public Task deleteTask(int taskNumber) {
        return tasks.remove(taskNumber);
    }
}
