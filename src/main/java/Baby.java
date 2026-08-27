import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Runs the Baby command-line task manager.
 */
public class Baby {
    private static final String LINE = "____________________________________________________________";
    private static final String FILE_PATH = "./data/baby.txt";
    /**
     * Starts the command loop for the task manager.
     *
     * @param args Command-line arguments that are not used.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = loadTasks();

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
            } else if (input.equals(Command.MARK.getCommandWord())
                    || input.startsWith(Command.MARK.getCommandWord() + " ")) {
                markTask(input, tasks);
            } else if (input.equals(Command.UNMARK.getCommandWord())
                    || input.startsWith(Command.UNMARK.getCommandWord() + " ")) {
                unmarkTask(input, tasks);
            } else if (input.equals(Command.DELETE.getCommandWord())
                    || input.startsWith(Command.DELETE.getCommandWord() + " ")) {
                deleteTask(input, tasks);
            } else if (input.equals(Command.TODO.getCommandWord())
                    || input.startsWith(Command.TODO.getCommandWord() + " ")) {
                String description = input.substring(Command.TODO.getCommandWord().length()).trim();
                if (description.isEmpty()) {
                    printError("OOPS! A todo needs a description. Try: todo read book");
                    continue;
                }

                Task task = new Todo(description);
                tasks.add(task);
                saveTasks(tasks);
                printTaskAdded(task, tasks.size());
            } else if (input.equals(Command.DEADLINE.getCommandWord())
                    || input.startsWith(Command.DEADLINE.getCommandWord() + " ")) {
                Deadline deadline = createDeadline(input);
                if (deadline == null) {
                    continue;
                }

                tasks.add(deadline);
                saveTasks(tasks);
                printTaskAdded(deadline, tasks.size());
            } else if (input.equals(Command.EVENT.getCommandWord())
                    || input.startsWith(Command.EVENT.getCommandWord() + " ")) {
                Event event = createEvent(input);
                if (event == null) {
                    continue;
                }

                tasks.add(event);
                saveTasks(tasks);
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
        String details = input.substring(Command.DEADLINE.getCommandWord().length()).trim();
        String[] parts;
        if (details.startsWith("/by ")) {
            parts = new String[] { "", details.substring("/by ".length()) };
        } else {
            parts = details.split(" /by ", 2);
        }

        if (parts.length < 2) {
            printError("OOPS! A deadline needs a description and /by. Try: deadline return book /by Sunday");
            return null;
        }

        String description = parts[0].trim();
        String by = parts[1].trim();
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
        String details = input.substring(Command.EVENT.getCommandWord().length()).trim();
        String[] descriptionAndTimes;
        if (details.startsWith("/from ")) {
            descriptionAndTimes = new String[] { "", details.substring("/from ".length()) };
        } else {
            descriptionAndTimes = details.split(" /from ", 2);
        }

        if (descriptionAndTimes.length < 2) {
            printError("OOPS! An event needs a description, /from, and /to. Try: event meeting /from Mon 2pm /to 4pm");
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
            printError("OOPS! An event needs an end time after /to.");
            return null;
        }

        String from = fromAndTo[0].trim();
        String to = fromAndTo[1].trim();
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
     * Marks a task as done after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void markTask(String input, ArrayList<Task> tasks) {
        int index = getTaskIndex(input, Command.MARK.getCommandWord(), tasks.size());
        if (index == -1) {
            return;
        }

        Task task = tasks.get(index);
        task.markAsDone();
        saveTasks(tasks);
        printResponse("Nice! I've marked this task as done:", " " + task);
    }

    /**
     * Marks a task as not done after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void unmarkTask(String input, ArrayList<Task> tasks) {
        int index = getTaskIndex(input, Command.UNMARK.getCommandWord(), tasks.size());
        if (index == -1) {
            return;
        }

        Task task = tasks.get(index);
        task.markAsNotDone();
        saveTasks(tasks);
        printResponse("OK, I've marked this task as not done yet:", " " + task);
    }

    /**
     * Deletes a task after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void deleteTask(String input, ArrayList<Task> tasks) {
        int index = getTaskIndex(input, Command.DELETE.getCommandWord(), tasks.size());
        if (index == -1) {
            return;
        }

        Task removedTask = tasks.remove(index);
        saveTasks(tasks);
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
        String taskNumberText = input.substring(command.length()).trim();

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

    private static void saveTasks (ArrayList<Task> tasks) {
        try {
            File dataFolder = new File("./data"); //create new file object
            dataFolder.mkdirs();

            FileWriter writer = new FileWriter(FILE_PATH);

            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            printError("Sorry My Princess, I couldn't save your tasks.");
        }
    }

    private static ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return tasks;
        }

        try {
            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");

                String type = parts[0];
                boolean isDone = parts[1].equals("1");

                Task task;

                if (type.equals("T")) {
                    task = new Todo(parts[2]);
                } else if (type.equals("D")) {
                    task = new Deadline(parts[2], parts[3]);
                } else {
                    task = new Event(parts[2], parts[3], parts[4]);
                }

                if (isDone) {
                    task.markAsDone();
                }

                tasks.add(task);
            }

            fileScanner.close();
        } catch (IOException e) {
            printError("Sorry My Princess, I couldn't load your tasks.");
        }

        return tasks;
    }
}


