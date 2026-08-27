import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands into task details and task numbers.
 */
public class Parser {
    /**
     * Gets the description part of a todo command.
     *
     * @param input The full user command.
     * @return The trimmed todo description.
     */
    public static String getTodoDescription(String input) {
        return input.substring(Command.TODO.getCommandWord().length()).trim();
    }

    /**
     * Creates a deadline from the user's deadline command.
     *
     * @param input The full user command.
     * @param ui The UI used to show parsing errors.
     * @return The parsed deadline, or null if the input is invalid.
     */
    public static Deadline createDeadline(String input, Ui ui) {
        String details = input.substring(Command.DEADLINE.getCommandWord().length()).trim();
        String[] parts;
        if (details.startsWith("/by ")) {
            parts = new String[] { "", details.substring("/by ".length()) };
        } else {
            parts = details.split(" /by ", 2);
        }

        if (parts.length < 2) {
            ui.printError("OOPS! A deadline needs a description and /by. Try: deadline return book /by Sunday");
            return null;
        }

        String description = parts[0].trim();
        String byText = parts[1].trim();
        if (description.isEmpty()) {
            ui.printError("OOPS! A deadline needs a description before /by.");
            return null;
        } else if (byText.isEmpty()) {
            ui.printError("OOPS! A deadline needs a date or time after /by.");
            return null;
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime by = LocalDateTime.parse(byText, formatter);
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            ui.printError("OOPS! Please use the format yyyy-MM-dd HHmm.");
            return null;
        }
    }

    /**
     * Creates an event from the user's event command.
     *
     * @param input The full user command.
     * @param ui The UI used to show parsing errors.
     * @return The parsed event, or null if the input is invalid.
     */
    public static Event createEvent(String input, Ui ui) {
        String details = input.substring(Command.EVENT.getCommandWord().length()).trim();
        String[] descriptionAndTimes;
        if (details.startsWith("/from ")) {
            descriptionAndTimes = new String[] { "", details.substring("/from ".length()) };
        } else {
            descriptionAndTimes = details.split(" /from ", 2);
        }

        if (descriptionAndTimes.length < 2) {
            ui.printError("OOPS! An event needs a description, /from, and /to. Try: event meeting /from Mon 2pm /to 4pm");
            return null;
        }

        String description = descriptionAndTimes[0].trim();
        String times = descriptionAndTimes[1].trim();
        String[] fromAndTo;
        if (times.startsWith("/to ")) {
            fromAndTo = new String[] { "", times.substring("/to ".length()) };
        } else {
            fromAndTo = times.split(" /to ", 2);
        }

        if (fromAndTo.length < 2) {
            ui.printError("OOPS! An event needs an end time after /to.");
            return null;
        }

        String fromText = fromAndTo[0].trim();
        String toText = fromAndTo[1].trim();
        if (description.isEmpty()) {
            ui.printError("OOPS! An event needs a description before /from.");
            return null;
        } else if (fromText.isEmpty()) {
            ui.printError("OOPS! An event needs a start time after /from.");
            return null;
        } else if (toText.isEmpty()) {
            ui.printError("OOPS! An event needs an end time after /to.");
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime from = LocalDateTime.parse(fromText, formatter);
            LocalDateTime to = LocalDateTime.parse(toText, formatter);

            return new Event(description, from, to);
        } catch (DateTimeParseException e) {
            ui.printError("OOPS!Please use the format yyyy-MM-dd HHmm. My princess.");
            return null;
        }
    }

    /**
     * Converts a 1-based task number from user input into a 0-based list index.
     *
     * @param input The full user command.
     * @param command The command word at the start of the input.
     * @param taskCount The number of tasks currently stored.
     * @param ui The UI used to show parsing errors.
     * @return The valid 0-based task index, or -1 if the input is invalid.
     */
    public static int getTaskIndex(String input, String command, int taskCount, Ui ui) {
        String taskNumberText = input.substring(command.length()).trim();

        if (taskNumberText.isEmpty()) {
            ui.printError("OOPS! Please give me a task number to " + command + ". Try: " + command + " 1");
            return -1;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            ui.printError("OOPS! Task numbers must be whole numbers. Try: " + command + " 1");
            return -1;
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            ui.printError("OOPS! Task number " + taskNumber + " is not in your list.");
            return -1;
        }

        return taskNumber - 1;
    }
}
