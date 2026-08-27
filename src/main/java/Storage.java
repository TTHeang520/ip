import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles saving tasks to the data file and loading tasks from it.
 */
public class Storage {
    private static final String LINE = "____________________________________________________________";
    private static final String FILE_PATH = System.getProperty("baby.filePath", "./data/baby.txt");

    /**
     * Saves the current tasks to the data file.
     *
     * @param tasks The tasks to save.
     */
    public static void saveTasks(ArrayList<Task> tasks) {
        try {
            File dataFile = new File(FILE_PATH);
            File dataFolder = dataFile.getParentFile();
            if (dataFolder != null) {
                dataFolder.mkdirs();
            }

            FileWriter writer = new FileWriter(dataFile);

            for (Task task : tasks) {
                writer.write(task.toFileString() + System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            printError("Sorry My Princess, I couldn't save your tasks.");
        }
    }

    /**
     * Loads saved tasks from the data file.
     *
     * @return The saved tasks, or an empty list if the data file does not exist.
     */
    public static ArrayList<Task> loadTasks() {
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
                    DateTimeFormatter formatter =
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

                    LocalDateTime by =
                            LocalDateTime.parse(parts[3], formatter);

                    task = new Deadline(parts[2], by);
                } else {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

                    LocalDateTime from = LocalDateTime.parse(parts[3], formatter);

                    LocalDateTime to =  LocalDateTime.parse(parts[4], formatter);

                    task = new Event(parts[2], from, to);
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

    private static void printError(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }
}
