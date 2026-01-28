/**
 * A type of task without any set deadline.
 */
public class TodoTask extends Task {
    public TodoTask(String description) {
        super(description);
    }

    @Override
    public String toFileFormat() {
        return "T | " + (super.isDone() ? "1" : "0") + " | " + super.getDescription();
    }
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
