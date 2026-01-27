public class DeadlineTask extends Task {
    private final String deadline;

    public DeadlineTask(String description, String deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    public String toFileFormat() {
        return "D | " + (super.isDone() ? "1" : "0") + " | " + super.getDescription() + " | " + this.deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadline + ")";
    }
}
