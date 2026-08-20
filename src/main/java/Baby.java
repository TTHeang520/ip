import java.util.Scanner;
public class Baby {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! I'm Baby.");
        System.out.println("What can I do for you, your highness.");

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }
            System.out.println(input);
        }

        System.out.println("Bye. I'll miss you.");
        scanner.close();
    }
}



