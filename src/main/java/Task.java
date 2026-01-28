/**
 * Represents one task for the user to complete.
 * Can be marked as done or not done.
 */
public abstract class Task {
    private boolean isDone;
    private final String description;

    /**
     * Creates a new task with the given description.
     * Initially, the task is marked as not done.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.isDone = false;
        this.description = description;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Marks this task as done.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Returns mark depending on whether task is done or not.
     * @return "X" if task is done, otherwise a blank space
     */
    public String getMark() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns a string representation of the task for saving to the data file.
     *
     * @return formatted string for file storage
     */
    public abstract String toFileFormat();

    /**
     * Returns a user-friendly string representation of the task to display in the todolist.
     *
     * @return formatted string for display in list
     */
    @Override
    public String toString() {
        return "[" + getMark() + "] " + this.description;
    }
}
