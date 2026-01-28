/**
 * A type of task without any set deadline.
 */
public class TodoTask extends Task {
    public TodoTask(String description) {
        super(description);
    }

    /**
     * Returns a string representation of this TodoTask to save to data file.
     * Format: T | 0/1 | description
     * where 0 means not done and 1 means done.
     *
     * @return formatted string to save to file
     */
    @Override
    public String toFileFormat() {
        return "T | " + (super.isDone() ? "1" : "0") + " | " + super.getDescription();
    }

    /**
     * Returns a user-friendly string representation of the task to display in the todolist.
     *
     * @return formatted string for display in list
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
