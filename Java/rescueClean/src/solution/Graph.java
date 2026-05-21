package solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Graph {

    private HashMap<Long, List<Road>> startingMap = new HashMap<>();

    public void addBuilding (long i) {
        startingMap.putIfAbsent(i, new ArrayList<>());
    }

    public void addRoad (Long i, Long d, double e) {
        startingMap.get(i).add(new Road(d, e));
    }

    public void removeBuilding (Long i) {
        startingMap.remove(i);
    }

    public HashMap<Long, List<Road>> getStartingMap() {
        return startingMap;
    }

    public void removeRoad (Long Building, Long Destination, String Access) {
        for (Road r : startingMap.get(Building)) {
            if (Objects.equals(r.getDestination(), Destination)) {
                if (Objects.equals(Access, "BLOCKED")) {
                    r.setAccess(Access);
                }
            }
        }
    }

    public String toString(Long i){
        return startingMap.get(i).toString();
    }
}