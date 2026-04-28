import java.util.ArrayList;
import java.util.Collections;

public class MinMax {
    public static Integer sumSL(ArrayList<Integer> list) {
        return Collections.min(list) + Collections.max(list);
    }
}
