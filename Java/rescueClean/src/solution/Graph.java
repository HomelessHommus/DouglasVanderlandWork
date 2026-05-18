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

    public HashMap<Integer, List<Road>> getStartingMap() {
        return startingMap;
    }

    public void removeRoad (int Building, int Destination, String Access) {
        for (Road r : startingMap.get(Building)) {
            if (r.getDestination() == Destination) {
                if (Objects.equals(Access, "BLOCKED")) {
                    r.setAccess(Access);
                }
            }
        }
    }

    public String toString(int i){
        return startingMap.get(i).toString();
    }
}