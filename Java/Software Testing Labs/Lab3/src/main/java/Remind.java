import java.util.Scanner;

public class Remind {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 'm' for Memo or 't' for Todo:");
        String type = scanner.nextLine();

        if (type.equals("m")) {
            System.out.println("Enter a message:");
            String message = scanner.nextLine();
            Memo memo = new Memo(message);
            memo.print();
        }
        else {
            System.out.println("Enter when:");
            String when = scanner.nextLine();
            System.out.println("Enter what:");
            String what = scanner.nextLine();
            Todo todo = new Todo(Todo.dateParse(when), what);
            todo.print();
        }
    }
}
