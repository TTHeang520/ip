package baby;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles command-line input and output for Baby.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    private Scanner scanner;

    /**
     * Creates a UI that reads commands from the command line.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Checks whether there is another line of user input.
     *
     * @return True if another command can be read.
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next user command.
     *
     * @return The trimmed command.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints the welcome message.
     */
    public void showWelcome() {
        printResponse("Hello! I'm Baby.", "What can I do for you, your highness.");
    }

    /**
     * Prints the goodbye message.
     */
    public void showGoodbye() {
        printResponse("Bye. I'll miss you.");
    }

    /**
     * Prints the message shown after adding a task.
     *
     * @param task The task that was added.
     * @param taskCount The number of tasks after adding.
     */
    public void printTaskAdded(Task task, int taskCount) {
        printResponse("Got it. I've added this task:", " " + task, "Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints all stored tasks with their current 1-based task numbers.
     *
     * @param tasks The current task list.
     */
    public void printTaskList(ArrayList<Task> tasks) {
        String[] lines = new String[tasks.size() + 1];
        lines[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.size(); i++) {
            lines[i + 1] = (i + 1) + "." + tasks.get(i);
        }

        printResponse(lines);
    }

    /**
     * Prints an error message.
     *
     * @param message The error message to print.
     */
    public void printError(String message) {
        printResponse(message);
    }

    /**
     * Prints one chatbot response wrapped in horizontal lines.
     *
     * @param lines Lines to show inside the response box.
     */
    public void printResponse(String... lines) {
        System.out.println(LINE);
        for (String line : lines) {
            System.out.println(" " + line);
        }
        System.out.println(LINE);
    }
}
