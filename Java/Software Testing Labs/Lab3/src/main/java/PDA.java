import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class PDA {
    private final ArrayList<Item> items = new ArrayList<>();

    public void createMemo(String message) {
        items.add(new Memo(message));
    }

    public void listMemo() {
        for (Item item : items) {
            if (item instanceof Memo)
                item.print();
        }
    }

    public void createPhone(String name, String number) {
        items.add(new Phone(name, number));
    }

    public void listPhone() {
        for (Item item : items) {
            if (item instanceof Phone)
                item.print();
        }
    }

    public void listPhone(String name) {
        for (Item item : items) {
            if (item instanceof Phone) {
                Phone p = (Phone) item;
                if (p.matchName(name)) {
                    p.print();
                }
            }
        }
    }

    public void listPhoneAlphabetically() {
        ArrayList<Phone> phoneItems = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Phone) {
                Phone p = (Phone) item;
                phoneItems.add(p);
            }
        }
        phoneItems.sort(Comparator.comparing(Phone::getName));
        for (Phone phone : phoneItems) {
            phone.print();
        }
    }

    public void createTodo(Date when, String what) {
        if (when != null) {
            items.add(new Todo(when, what));
        }
    }

    public void listTodo() {
        for (Item item : items) {
            if (item instanceof Todo)
                item.print();
        }
    }

    public void listTodo(Date when) {
        for (Item item : items) {
            if (item instanceof Todo) {
                Todo t = (Todo) item;
                if (t.matchDate(when)) {
                    t.print();
                }
            }
        }
    }

    public void listTodoByDate() {
        ArrayList<Todo> todoItems = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Todo) {
                Todo t = (Todo) item;
                todoItems.add(t);
            }
        }
        todoItems.sort(Comparator.comparing(Todo::getWhen));
        for (Todo td : todoItems) {
            td.print();
        }
    }

    public void list() {
        for (Item item : items) {
            item.print();
        }
    }

}
