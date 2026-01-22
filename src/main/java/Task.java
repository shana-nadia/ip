/**
 * Represents one task for the user to complete.
 */
public class Task {
    private boolean isDone;
    private final String description;

    public Task(String description) {
        this.isDone = false;
        this.description = description;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    public String getMark() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getMark() + "] " + this.description;
    }
}
