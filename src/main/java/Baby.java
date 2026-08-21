import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Baby command-line task manager.
 */
public class Baby {
    private static final String LINE = "____________________________________________________________";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";

    /**
     * Starts the command loop for the task manager.
     *
     * @param args Command-line arguments that are not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        printResponse("Hello! I'm Baby.", "What can I do for you, your highness.");

        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                printResponse("Bye. I'll miss you.");
                break;
            } else if (input.equals("list")) {
                printTaskList(tasks);
            } else if (isCommand(input, MARK_COMMAND)) {
                markTask(input, tasks, true);
            } else if (isCommand(input, UNMARK_COMMAND)) {
                markTask(input, tasks, false);
            } else if (isCommand(input, DELETE_COMMAND)) {
                deleteTask(input, tasks);
            } else if (isCommand(input, TODO_COMMAND)) {
                String description = getCommandArgument(input, TODO_COMMAND);
                if (description.isEmpty()) {
                    printError("OOPS! A todo needs a description. Try: todo read book");
                    continue;
                }

                Task task = new Todo(description);
                tasks.add(task);
                printTaskAdded(task, tasks.size());
            } else if (isCommand(input, DEADLINE_COMMAND)) {
                Deadline deadline = createDeadline(input);
                if (deadline == null) {
                    continue;
                }

                tasks.add(deadline);
                printTaskAdded(deadline, tasks.size());
            } else if (isCommand(input, EVENT_COMMAND)) {
                Event event = createEvent(input);
                if (event == null) {
                    continue;
                }

                tasks.add(event);
                printTaskAdded(event, tasks.size());
            } else if (input.isEmpty()) {
                printError("OOPS! Please enter a command.");
            } else {
                printError("OOPS! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
            }
        }
        scanner.close();
    }

    private static Deadline createDeadline(String input) {
        String details = getCommandArgument(input, DEADLINE_COMMAND);
        int byMarkerIndex = findMarkerIndex(details, "/by");

        if (byMarkerIndex == -1) {
            printError("OOPS! A deadline needs a description and /by. Try: deadline return book /by Sunday");
            return null;
        }

        String description = details.substring(0, byMarkerIndex).trim();
        String by = details.substring(byMarkerIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            printError("OOPS! A deadline needs a description before /by.");
            return null;
        } else if (by.isEmpty()) {
            printError("OOPS! A deadline needs a date or time after /by.");
            return null;
        }

        return new Deadline(description, by);
    }

    private static Event createEvent(String input) {
        String details = getCommandArgument(input, EVENT_COMMAND);
        int fromMarkerIndex = findMarkerIndex(details, "/from");

        if (fromMarkerIndex == -1) {
            printError("OOPS! An event needs a description, /from, and /to. Try: event meeting /from Mon 2pm /to 4pm");
            return null;
        }

        String description = details.substring(0, fromMarkerIndex).trim();
        String fromAndTo = details.substring(fromMarkerIndex + "/from".length()).trim();
        int toMarkerIndex = findMarkerIndex(fromAndTo, "/to");

        if (toMarkerIndex == -1) {
            printError("OOPS! An event needs an end time after /to.");
            return null;
        }

        String from = fromAndTo.substring(0, toMarkerIndex).trim();
        String to = fromAndTo.substring(toMarkerIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            printError("OOPS! An event needs a description before /from.");
            return null;
        } else if (from.isEmpty()) {
            printError("OOPS! An event needs a start time after /from.");
            return null;
        } else if (to.isEmpty()) {
            printError("OOPS! An event needs an end time after /to.");
            return null;
        }

        return new Event(description, from, to);
    }

    /**
     * Finds a marker such as /by only when it appears as a separate command marker.
     *
     * @param details The text after the command word.
     * @param marker The marker to find.
     * @return The marker's starting index, or -1 if it is missing.
     */
    private static int findMarkerIndex(String details, String marker) {
        if (details.startsWith(marker + " ")) {
            return 0;
        }

        int markerIndex = details.indexOf(" " + marker + " ");
        if (markerIndex == -1) {
            return -1;
        }

        return markerIndex + 1;
    }

    /**
     * Marks or unmarks a task after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     * @param isMarkingDone Whether to mark the task as done or not done.
     */
    private static void markTask(String input, ArrayList<Task> tasks, boolean isMarkingDone) {
        String command = isMarkingDone ? MARK_COMMAND : UNMARK_COMMAND;
        int index = getTaskIndex(input, command, tasks.size());
        if (index == -1) {
            return;
        }

        Task task = tasks.get(index);
        if (isMarkingDone) {
            task.markAsDone();
            printResponse("Nice! I've marked this task as done:", " " + task);
        } else {
            task.markAsNotDone();
            printResponse("OK, I've marked this task as not done yet:", " " + task);
        }
    }

    /**
     * Deletes a task after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void deleteTask(String input, ArrayList<Task> tasks) {
        int index = getTaskIndex(input, DELETE_COMMAND, tasks.size());
        if (index == -1) {
            return;
        }

        Task removedTask = tasks.remove(index);
        printResponse(
                "Noted. I've removed this task:",
                " " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Converts a 1-based task number from user input into a 0-based list index.
     *
     * @param input The full user command.
     * @param command The command word at the start of the input.
     * @param taskCount The number of tasks currently stored.
     * @return The valid 0-based task index, or -1 if the input is invalid.
     */
    private static int getTaskIndex(String input, String command, int taskCount) {
        String taskNumberText = getCommandArgument(input, command);

        if (taskNumberText.isEmpty()) {
            printError("OOPS! Please give me a task number to " + command + ". Try: " + command + " 1");
            return -1;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            printError("OOPS! Task numbers must be whole numbers. Try: " + command + " 1");
            return -1;
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            printError("OOPS! Task number " + taskNumber + " is not in your list.");
            return -1;
        }

        return taskNumber - 1;
    }

    /**
     * Gets the text after the command word.
     *
     * @param input The full user command.
     * @param command The command word at the start of the input.
     * @return The trimmed argument text, or an empty string if there is none.
     */
    private static String getCommandArgument(String input, String command) {
        if (input.length() == command.length()) {
            return "";
        }

        return input.substring(command.length() + 1).trim();
    }

    /**
     * Checks whether the input is exactly the command or starts with the command and a space.
     *
     * @param input The full user command.
     * @param command The command word to check.
     * @return True if the input uses the given command word.
     */
    private static boolean isCommand(String input, String command) {
        return input.equals(command) || input.startsWith(command + " ");
    }

    private static void printTaskAdded(Task task, int taskCount) {
        printResponse("Got it. I've added this task:", " " + task, "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints all stored tasks with their current 1-based task numbers.
     *
     * @param tasks The current task list.
     */
    private static void printTaskList(ArrayList<Task> tasks) {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }

        printResponse(lines);
    }

    private static void printError(String message) {
        printResponse(message);
    }

    /**
     * Prints one chatbot response wrapped in horizontal lines.
     *
     * @param lines Lines to show inside the response box.
     */
    private static void printResponse(String... lines) {
        System.out.println(LINE);
        for (String line : lines) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }
}
