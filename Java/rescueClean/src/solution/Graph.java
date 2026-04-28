package solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {

    public HashMap<Integer, List<Road>> startingMap = new HashMap<>();

    public void addBuilding (int i) {
        startingMap.putIfAbsent(i, new ArrayList<>());
    }

    public void addRoad (int i, int d, double e) {
        startingMap.get(i).add(new Road(d, e));
    }

    public void removeBuilding (int i) {
        startingMap.remove(i);
    }

    public void removeRoad (int i, int d) {}

    public String toString(int i){
        return startingMap.get(i).toString();
    }
}