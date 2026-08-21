import java.util.Scanner;

/**
 * Runs the Baby command-line task manager.
 */
public class Baby {
    private static final int MAX_TASKS = 100;
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";

    /**
     * Starts the command loop for the task manager.
     *
     * @param args Command-line arguments that are not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];

        int taskCount = 0;

        System.out.println("Hello! I'm Baby.");
        System.out.println("What can I do for you, your highness.");

        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }

            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye. I'll miss you.");
                break;
            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (isCommand(input, MARK_COMMAND)) {
                markTask(input, tasks, taskCount, true);
            } else if (isCommand(input, UNMARK_COMMAND)) {
                markTask(input, tasks, taskCount, false);
            } else if (isCommand(input, TODO_COMMAND)) {
                if (isTaskListFull(taskCount)) {
                    printError("OOPS! Your task list is full. Please finish something before adding more.");
                    continue;
                }

                String description = getCommandArgument(input, TODO_COMMAND);
                if (description.isEmpty()) {
                    printError("OOPS! A todo needs a description. Try: todo read book");
                    continue;
                }

                tasks[taskCount] = new Todo(description);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (isCommand(input, DEADLINE_COMMAND)) {
                if (isTaskListFull(taskCount)) {
                    printError("AHA! Your task list is full. Please finish something before adding more.");
                    continue;
                }

                Deadline deadline = createDeadline(input);
                if (deadline == null) {
                    continue;
                }

                tasks[taskCount] = deadline;
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (isCommand(input, EVENT_COMMAND)) {
                if (isTaskListFull(taskCount)) {
                    printError("OOPS! Your task list is full. Please finish something before adding more.");
                    continue;
                }

                Event event = createEvent(input);
                if (event == null) {
                    continue;
                }

                tasks[taskCount] = event;
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (input.isEmpty()) {
                printError("KNOCK KNOCK.Please enter a command.");
            } else {
                printError("OHNO! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, or bye.");
            }
        }
        scanner.close();
    }

    private static Deadline createDeadline(String input) {
        String details = getCommandArgument(input, DEADLINE_COMMAND);
        int byMarkerIndex = findMarkerIndex(details, "/by");

        if (byMarkerIndex == -1) {
            printError("UHOH! A deadline needs a description and /by. Try: deadline return book /by Sunday");
            return null;
        }

        String description = details.substring(0, byMarkerIndex).trim();
        String by = details.substring(byMarkerIndex + "/by".length()).trim();
        if (description.isEmpty()) {
            printError("UHOH! A deadline needs a description before /by.");
            return null;
        } else if (by.isEmpty()) {
            printError("UHOH! A deadline needs a date or time after /by.");
            return null;
        }

        return new Deadline(description, by);
    }

    private static Event createEvent(String input) {
        String details = getCommandArgument(input, EVENT_COMMAND);
        int fromMarkerIndex = findMarkerIndex(details, "/from");

        if (fromMarkerIndex == -1) {
            printError("An event needs a description, /from, and /to. Try: event meeting /from Mon 2pm /to 4pm");
            return null;
        }

        String description = details.substring(0, fromMarkerIndex).trim();
        String fromAndTo = details.substring(fromMarkerIndex + "/from".length()).trim();
        int toMarkerIndex = findMarkerIndex(fromAndTo, "/to");

        if (toMarkerIndex == -1) {
            printError("OH NO! An event needs an end time after /to.");
            return null;
        }

        String from = fromAndTo.substring(0, toMarkerIndex).trim();
        String to = fromAndTo.substring(toMarkerIndex + "/to".length()).trim();
        if (description.isEmpty()) {
            printError("OH NO! An event needs a description before /from.");
            return null;
        } else if (from.isEmpty()) {
            printError("OH NO! An event needs a start time after /from.");
            return null;
        } else if (to.isEmpty()) {
            printError("OH NO! An event needs an end time after /to.");
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
     * @param taskCount The number of tasks currently stored.
     * @param isMarkingDone Whether to mark the task as done or not done.
     */
    private static void markTask(String input, Task[] tasks, int taskCount, boolean isMarkingDone) {
        String command = isMarkingDone ? MARK_COMMAND : UNMARK_COMMAND;
        String taskNumberText = getCommandArgument(input, command);

        if (taskNumberText.isEmpty()) {
            printError("Please please give me a task number to " + command + ". Try: " + command + " 1");
            return;
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            printError("OOPSIE! Task numbers must be whole numbers. Try: " + command + " 1");
            return;
        }

        if (taskNumber < 1 || taskNumber > taskCount) {
            printError("OOPSIEE! Task number " + taskNumber + " is not in your list.");
            return;
        }

        int index = taskNumber - 1;
        if (isMarkingDone) {
            tasks[index].markAsDone();
            System.out.println("Nice! I've marked this task as done:");
        } else {
            tasks[index].markAsNotDone();
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println(" " + tasks[index]);
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

    /**
     * Checks whether the fixed-size task array has no free slots left.
     *
     * @param taskCount The number of tasks currently stored.
     * @return True if no more tasks can be added.
     */
    private static boolean isTaskListFull(int taskCount) {
        return taskCount >= MAX_TASKS;
    }

    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    private static void printError(String message) {
        System.out.println(message);
    }
}
