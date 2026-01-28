import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that happens during a specific time period.
 */
public class EventTask extends Task {
    private final LocalDateTime start;
    private final LocalDateTime end;
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy, h a");

    public EventTask(String description, String start, String end) {
        super(description);
        this.start = LocalDateTime.parse(start, INPUT_FORMAT);
        this.end = LocalDateTime.parse(end, INPUT_FORMAT);
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
                + " | " + this.start.format(INPUT_FORMAT) + " | " + this.end.format(INPUT_FORMAT);
    }

    /**
     * Returns a user-friendly string representation of the task to display in the todolist.
     *
     * @return formatted string for display in list
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start.format(PRINT_FORMAT)
                + " to: " + this.end.format(PRINT_FORMAT) + ")";
    }
}
