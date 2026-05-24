package solution;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PathFinding {

    Graph graph;

    public PathFinding(Graph graph) {
        this.graph = graph;
    }

    public String PathFinding2 (long startBuilding, Long endBuilding)
    {
        ConcurrentHashMap<Long, Double> forwardDistance = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Double> backwardsDistance = new ConcurrentHashMap<>();

        ConcurrentHashMap<Long, Long> forwardPrevious = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Long> backwardPrevious = new ConcurrentHashMap<>();

        PriorityQueue<double[]> PQ = new PriorityQueue<>(Comparator.comparingDouble(o -> o[0]));

        forwardDistance.put(startBuilding, 0.0);

        PQ.add(new double[]{startBuilding, 0.0});

        while (!PQ.isEmpty()) {
            double[] current = PQ.poll();
            double currentCost = current[0];
            long currentBuilding = (long) current[1];

            if (currentBuilding == endBuilding){
                break;
            }

            if (currentCost > forwardDistance.getOrDefault(currentBuilding, Double.MAX_VALUE)){
                continue;
            }

            List<Road> roads = graph.getStartingMap().getOrDefault(currentBuilding, new ArrayList<>());
            for (Road road : roads) {
                if (!(Objects.equals(road.getAccess(), "open"))) {
                    long neighbourBuilding = road.getDestination();
                    double newCost = currentCost + road.getCost();

                    if (newCost < forwardDistance.getOrDefault(neighbourBuilding, Double.MAX_VALUE)) {
                        forwardDistance.put(neighbourBuilding, newCost);
                        forwardPrevious.put(neighbourBuilding, currentBuilding);
                        PQ.offer(new double[]{newCost, neighbourBuilding});
                    }
                }
            }
        }

        List<Long> PathToPrint = new ArrayList<>();

        if (forwardPrevious.containsKey(endBuilding) && startBuilding != endBuilding) {
            return PathToPrint.toString();
        }

        for (Long building = endBuilding; building != null; building = forwardPrevious.get(building)) {
            PathToPrint.add(building);
            if (building.equals(startBuilding)) {
                break;
            }
        }
        Collections.reverse(PathToPrint);

        return PathToPrint.toString();
    }

}
