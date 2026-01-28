/**
 * Represents a task that happens during a specific time period.
 */
public class EventTask extends Task {
    private final String start;
    private final String end;

    public EventTask(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a string representation of this EventTask to save to data file.
     * Format: E | 0/1 | description | start time | end time
     * where 0 means not done and 1 means done.
     *
     * @return formatted string to save to file
     */
    @Override
    public String toFileFormat() {
        return "E | " + (super.isDone() ? "1" : "0") + " | " + super.getDescription()
                + " | " + this.start + " | " + this.end;
    }

    /**
     * Returns a user-friendly string representation of the task to display in the todolist.
     *
     * @return formatted string for display in list
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }
}
