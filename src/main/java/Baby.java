import java.util.Scanner;

/**
 * Runs the Baby command-line task manager.
 */
public class Baby {
    private static final int MAX_TASKS = 100;

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
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("Bye. I'll miss you.");
                break;
            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                int index = taskNumber - 1;

                tasks[index].markAsDone();

                System.out.println("Nice! I've marked this task as done:");
                System.out.println(" " + tasks[index]);
            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                int index = taskNumber - 1;

                tasks[index].markAsNotDone();

                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(" " + tasks[index]);
            } else if (input.startsWith("todo ")) {
                tasks[taskCount] = new Todo(input.substring(5));
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("deadline ")) {
                tasks[taskCount] = createDeadline(input);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("event ")) {
                tasks[taskCount] = createEvent(input);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            } else {
                tasks[taskCount] = new Todo(input);
                taskCount++;
                printTaskAdded(tasks[taskCount - 1], taskCount);
            }
        }
        scanner.close();
    }

    private static Deadline createDeadline(String input) {
        String details = input.substring(9);
        String[] parts = details.split(" /by ", 2);
        return new Deadline(parts[0], parts[1]);
    }

    private static Event createEvent(String input) {
        String details = input.substring(6);
        String[] descriptionAndFrom = details.split(" /from ", 2);
        String[] fromAndTo = descriptionAndFrom[1].split(" /to ", 2);
        return new Event(descriptionAndFrom[0], fromAndTo[0], fromAndTo[1]);
    }

    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}


