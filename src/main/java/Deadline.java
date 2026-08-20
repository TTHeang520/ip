/**
 * Represents a task that should be completed by a specific date or time.
 */
public class Deadline extends Task {
    private String by;

    /**
     * Creates a deadline task with the given description and deadline.
     *
     * @param description Description of the deadline task.
     * @param by Deadline text.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getTypeIcon() {
        return "D";
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
