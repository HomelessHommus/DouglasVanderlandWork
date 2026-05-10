package solution;
import org.jdom2.JDOMException;
import sim.Message;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.lang.Integer.parseInt;

public class MyDisasterResponder extends DisasterResponder {

    Graph graph = new Graph();
    String filename = "data/map.100000.graphml";
    BlockingQueue<String> RoadDeleteQueue = new LinkedBlockingQueue<>();
    BlockingQueue<String> BuildingDeleteQueue = new LinkedBlockingQueue<>();
    BlockingQueue<String> PathFindingQueue = new LinkedBlockingQueue<>();
    HashMap<Integer, Double> totalDistance = new HashMap<>();
    HashMap<Integer, Integer> previous = new HashMap<>();
    List<Integer> printingPath = new ArrayList<>();
    PriorityQueue<CurrentLocation> PQ = new PriorityQueue<>(Comparator.comparingInt(o -> (int) o.totalDistance));
    int Building;
    double Distance;
    double newDistance;
    int nextBuilding;
    Integer current;

    public ArrayList<Integer> PathFinding (int startBuilding, int endBuilding) {
        totalDistance.clear();
        previous.clear();
        printingPath.clear();
        PQ.clear();

        for(Integer key : graph.getStartingMap().keySet()) {
            totalDistance.put(key, 1000000.0);
        }

        totalDistance.put(startBuilding, (double) 0);
        PQ.add(new CurrentLocation(startBuilding, 0));

        while (!PQ.isEmpty()) {

            CurrentLocation current = PQ.poll();
            Building = current.Building;
            Distance = current.totalDistance;

            if (Building == endBuilding) {
                break;
            }

            if (Distance > totalDistance.get(Building)) {
                continue;
            }

            List<Road> roads = graph.getStartingMap().get(Building);

            if (roads == null) {continue;}

            for (Road road : roads) {
                nextBuilding = road.destination;
                newDistance = Distance + road.cost;

                if (newDistance < totalDistance.getOrDefault(nextBuilding, 1000000.0)) {
                    totalDistance.put(nextBuilding, newDistance);
                    previous.put(nextBuilding, Building);
                    PQ.add(new CurrentLocation(nextBuilding, newDistance));
                }
            }
        }

        printingPath.clear();
        current = endBuilding;
        while (current != null) {
            printingPath.add(current);
            current = previous.get(current);
        }
        Collections.reverse(printingPath);

        ArrayList<Integer> path = new ArrayList<>();

        for (Integer key : printingPath) {
            path.add(key);
        }
        return path;
    }



    public void ThreadsControl() {

        Thread RoadDeleteThread = new Thread(() -> {
            try {
                while (true) {
                    String deleteRoad = RoadDeleteQueue.take();
                    String[] messageInUse = deleteRoad.split("\\|");

                    if (Objects.equals(messageInUse[6], "BLOCKED")) {
                        if (graph.startingMap.containsKey(Integer.parseInt(messageInUse[2]))) {
                            graph.removeRoad (parseInt(messageInUse[2]), parseInt(messageInUse[4]), messageInUse[6]);
                            System.out.println("Road from " +  messageInUse[2] + " to " + messageInUse[4] + " has been blocked");
                        }
                    }
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread BuildingDeleteThread = new Thread(() -> {
            try {
                while (true) {
                    String deleteBuilding = BuildingDeleteQueue.take();
                    String[] messageInUse = deleteBuilding.split("\\|");

                    if (Objects.equals(messageInUse[2], "COLLAPSED")) {
                        if (graph.startingMap.containsKey(Integer.parseInt(messageInUse[1]))) {
                            graph.removeBuilding(parseInt(messageInUse[1]));
                            System.out.println("Building " + parseInt(messageInUse[1]) + " removed");
                        }
                    }
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread PathFinding = new Thread(() -> {
            try {
                while (true) {
                    String pathFinder = PathFindingQueue.take();
                    String[] messageInUse = pathFinder.split("\\|");
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp = PathFinding((1), Integer.parseInt(messageInUse[2]));
                    System.out.println(temp);
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        RoadDeleteThread.start();
        System.out.println("RoadDeleteThread Started");
        BuildingDeleteThread.start();
        System.out.println("BuildingDeleteThread Started");
        PathFinding.start();
        System.out.println("PathFindingThread Started");

    }
    @Override
    protected void handle(Message s) {

        String messageToUse = s.getMessage();

        String[] handlingMessage = messageToUse.split("\\|");

        switch (handlingMessage[0]) {
            case "RESCUE":
                PathFindingQueue.add(messageToUse);
                break;

            case "ROAD":
                if (handlingMessage[6].equals("BLOCKED")) {
                    RoadDeleteQueue.add(messageToUse);
                }

                break;

            case "LOCATION":
                BuildingDeleteQueue.add(messageToUse);
                break;

            case "PATH_INVALID":
                break;

            case "WAYPOINT_INVALID":
                break;

            case "VEHICLE":
                break;

            case "PEOPLE_TRANSFERRED":
                break;

            case "ERROR":
                break;

                default:
                    System.out.println("Unrecognized handling message");

        }
    }

    @Override
    protected void setup() throws IOException, JDOMException {
        graph = GraphBuilder.buildFromGraphML(filename);
        ThreadsControl();
    }
}
