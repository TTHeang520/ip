import java.util.Scanner;
public class Baby {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];

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
            }else if (input.startsWith("mark ")) {
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
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
            }
        }
        scanner.close();
    }
}



