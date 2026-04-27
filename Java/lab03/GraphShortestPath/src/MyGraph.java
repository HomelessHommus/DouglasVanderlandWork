import java.util.*;

public class MyGraph implements  Graph {

    HashMap<Integer, List<Edge>> graph = GraphBuilder.mainMap;
    HashMap<Integer, Integer> totalDist = new HashMap<>();
    HashMap<Integer, Integer> prev = new HashMap<>();
    List<Integer> printingPath = new ArrayList<>();
    PriorityQueue<CurrentLocation> priQue = new PriorityQueue<>(Comparator.comparingInt(o -> o.totalDistance));
    int vertex;
    int distance;
    int newDistance;
    int neighbour;
    Integer current;



    public void getEdge(int i) {
    }

    public void printGraph() {
        for (int key : graph.keySet()) {
            System.out.println(key + ": " + graph.get(key) + " Edges: " + graph.get(key).size());
        }
        shortestPath(0);
    }

    public void shortestPath(int source) {

        for(Integer key : graph.keySet()) {
            totalDist.put(key, 1000000);
        }

        totalDist.put(source, 0);
        priQue.add(new CurrentLocation(source, 0));

        while (!priQue.isEmpty()) {

            CurrentLocation current = priQue.poll();
            vertex = current.vertex;
            distance = current.totalDistance;


            if (distance < totalDist.get(vertex)) {
                continue;
            }
            for (Edge edge : graph.get(vertex)) {
                neighbour = edge.destination;
                newDistance = distance + edge.weight;

                if (newDistance < totalDist.get(neighbour)) {
                    totalDist.put(neighbour, newDistance);
                    prev.put(neighbour, vertex);
                    priQue.add(new CurrentLocation(neighbour, newDistance));
                }
            }
        }

        for (Integer key : graph.keySet()) {
            printingPath.clear();
            current = key;
            while (current != null) {
                printingPath.add(current);
                current = prev.get(current);
            }
            Collections.reverse(printingPath);

            System.out.print("shortest path to " + key + ":");
            for (Integer i: printingPath) {
                System.out.print(" " + i);
            }
            System.out.println(": " + "cost = " + totalDist.get(key));
        }

//      implement check for sourceVertex to make sure it's not over mainMap length
//      check for when a path cant reach a vertex

    }
}
