package solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Graph {

    HashMap<Integer, List<Road>> startingMap = new HashMap<>();

    public void addBuilding (int i) {
        startingMap.putIfAbsent(i, new ArrayList<>());
    }

    public void addRoad (int i, int d, double e) {
        startingMap.get(i).add(new Road(d, e));
    }

    public void removeBuilding (int i) {
        startingMap.remove(i);
    }

    public void removeRoad (int i, int d, String a) {
        for (Road r : startingMap.get(i)) {
            if (r.getDestination() == d) {
                if (Objects.equals(a, "BLOCKED")) {
                    r.setAccess(a);
                }
            }
        }

    }

    public String toString(int i){
        return startingMap.get(i).toString();
    }
}