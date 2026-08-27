package baby;

import java.util.ArrayList;

/**
 * Runs the Baby command-line task manager.
 */
public class Baby {
    /**
     * Starts the command loop for the task manager.
     *
     * @param args Command-line arguments that are not used.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        TaskList tasks = new TaskList(Storage.loadTasks());

        ui.showWelcome();

        while (true) {
            if (!ui.hasNextLine()) {
                break;
            }

            String input = ui.readCommand();

            if (input.equals("bye")) {
                ui.showGoodbye();
                break;
            } else if (input.equals("list")) {
                ui.printTaskList(tasks.getTasks());
            } else if (input.equals(Command.MARK.getCommandWord())
                    || input.startsWith(Command.MARK.getCommandWord() + " ")) {
                markTask(input, tasks, ui);
            } else if (input.equals(Command.UNMARK.getCommandWord())
                    || input.startsWith(Command.UNMARK.getCommandWord() + " ")) {
                unmarkTask(input, tasks, ui);
            } else if (input.equals(Command.DELETE.getCommandWord())
                    || input.startsWith(Command.DELETE.getCommandWord() + " ")) {
                deleteTask(input, tasks, ui);
            } else if (input.equals(Command.TODO.getCommandWord())
                    || input.startsWith(Command.TODO.getCommandWord() + " ")) {
                String description = Parser.getTodoDescription(input);
                if (description.isEmpty()) {
                    ui.printError("OOPS! A todo needs a description. Try: todo read book");
                    continue;
                }

                Task task = new Todo(description);
                tasks.add(task);
                Storage.saveTasks(tasks.getTasks());
                ui.printTaskAdded(task, tasks.size());
            } else if (input.equals(Command.DEADLINE.getCommandWord())
                    || input.startsWith(Command.DEADLINE.getCommandWord() + " ")) {
                Deadline deadline = Parser.createDeadline(input, ui);
                if (deadline == null) {
                    continue;
                }

                tasks.add(deadline);
                Storage.saveTasks(tasks.getTasks());
                ui.printTaskAdded(deadline, tasks.size());
            } else if (input.equals(Command.EVENT.getCommandWord())
                    || input.startsWith(Command.EVENT.getCommandWord() + " ")) {
                Event event = Parser.createEvent(input, ui);
                if (event == null) {
                    continue;
                }

                tasks.add(event);
                Storage.saveTasks(tasks.getTasks());
                ui.printTaskAdded(event, tasks.size());
            } else if (input.equals(Command.FIND.getCommandWord())
                    || input.startsWith(Command.FIND.getCommandWord() + " ")) {
                findTasks(input, tasks, ui);
            } else if (input.isEmpty()) {
                ui.printError("OOPS! Please enter a command.");
            } else {
                ui.printError("OOPS! I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
            }
        }
        ui.close();
    }

    /**
     * Marks a task as done after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void markTask(String input, TaskList tasks, Ui ui) {
        int index = Parser.getTaskIndex(input, Command.MARK.getCommandWord(), tasks.size(), ui);
        if (index == -1) {
            return;
        }

        Task task = tasks.get(index);
        task.markAsDone();
        Storage.saveTasks(tasks.getTasks());
        ui.printResponse("Nice! I've marked this task as done:", " " + task);
    }

    /**
     * Marks a task as not done after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void unmarkTask(String input, TaskList tasks, Ui ui) {
        int index = Parser.getTaskIndex(input, Command.UNMARK.getCommandWord(), tasks.size(), ui);
        if (index == -1) {
            return;
        }

        Task task = tasks.get(index);
        task.markAsNotDone();
        Storage.saveTasks(tasks.getTasks());
        ui.printResponse("OK, I've marked this task as not done yet:", " " + task);
    }

    /**
     * Deletes a task after checking that the task number is valid.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     */
    private static void deleteTask(String input, TaskList tasks, Ui ui) {
        int index = Parser.getTaskIndex(input, Command.DELETE.getCommandWord(), tasks.size(), ui);
        if (index == -1) {
            return;
        }

        Task removedTask = tasks.remove(index);
        Storage.saveTasks(tasks.getTasks());
        ui.printResponse(
                "Noted. I've removed this task:",
                " " + removedTask,
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Finds tasks whose description contains the given keyword.
     *
     * @param input The full user command.
     * @param tasks The current task list.
     * @param ui The UI used to show results and errors.
     */
    private static void findTasks(String input, TaskList tasks, Ui ui) {
        String keyword = input.substring(Command.FIND.getCommandWord().length()).trim();

        if (keyword.isEmpty()) {
            ui.printError("OOPS! Please give me a keyword to find.");
            return;
        }

        ArrayList<Task> matchingTasks = new ArrayList<>();

        for (Task task : tasks.getTasks()) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }

        ui.printMatchingTasks(matchingTasks);
    }
}
