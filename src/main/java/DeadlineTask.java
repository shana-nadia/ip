/**
 * Represents a task to be completed by a certain deadline.
 */
public class DeadlineTask extends Task {
    private final String deadline;

    public DeadlineTask(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    /**
     * Returns a string representation of this DeadlineTask to save to data file.
     * Format: D | 0/1 | description | deadline
     * where 0 means not done and 1 means done.
     *
     * @return formatted string to save to file
     */
    @Override
    public String toFileFormat() {
        return "D | " + (super.isDone() ? "1" : "0") + " | " + super.getDescription() + " | " + this.deadline;
    }

    /**
     * Returns a user-friendly string representation of the task to display in the todolist.
     *
     * @return formatted string for display in list
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadline + ")";
    }
}
