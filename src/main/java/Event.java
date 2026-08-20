/**
 * Represents a task that happens from a start date or time to an end date or time.
 */
public class Event extends Task {
    private String from;
    private String to;

    /**
     * Creates an event task with the given description, start, and end.
     *
     * @param description Description of the event task.
     * @param from Start date or time text.
     * @param to End date or time text.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String getTypeIcon() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
