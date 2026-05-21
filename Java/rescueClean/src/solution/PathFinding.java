package solution;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class PathFinding {

    Graph graph;

    public PathFinding(Graph graph) {
        this.graph = graph;
    }

    ConcurrentHashMap<Long, Double> forwardDistance = new ConcurrentHashMap<>();
    ConcurrentHashMap<Long, Double> backwardsDistance = new ConcurrentHashMap<>();

    ConcurrentHashMap<Long, Long> forwardPrevious = new ConcurrentHashMap<>();
    ConcurrentHashMap<Long, Long> backwardPrevious = new ConcurrentHashMap<>();

}
