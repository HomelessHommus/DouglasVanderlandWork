import java.util.Comparator;

public class TodoDateComparator implements Comparator<Todo> {
    @Override
    public int compare(Todo o1, Todo o2) {
        return o1.getWhen().compareTo(o2.getWhen());
    }
}
