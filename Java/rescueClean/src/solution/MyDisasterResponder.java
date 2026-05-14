package solution;
import org.jdom2.JDOMException;
import sim.Message;
import util.ConfigurationInfo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.lang.Integer.parseInt;

public class MyDisasterResponder extends DisasterResponder {

    // setup variables to refer back to
    String origin;
    Graph graph = new Graph();

    // queue for the path finding messages
    BlockingQueue<String> PathFindingQueue = new LinkedBlockingQueue<>();

    // Maps, Lists, and Variables to store data for the path finding
    HashMap<Integer, Double> totalDistance = new HashMap<>();
    HashMap<Integer, Integer> previous = new HashMap<>();
    List<Integer> printingPath = new ArrayList<>();
    PriorityQueue<CurrentLocation> PQ = new PriorityQueue<>(Comparator.comparingInt(o -> (int) o.totalDistance));
    int Building;
    double Distance;
    double newDistance;
    int nextBuilding;
    Integer current;

    // Path finding algorithm, takes a start and end point
    public String PathFinding (int startBuilding, int endBuilding) {
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

            if (roads == null) {
                continue;
            }

            for (Road road : roads) {
                if (road.getAccess().equals("open")) {
                    nextBuilding = road.destination;
                    newDistance = Distance + road.cost;

                    if (newDistance < totalDistance.getOrDefault(nextBuilding, 1000000.0)) {
                        totalDistance.put(nextBuilding, newDistance);
                        previous.put(nextBuilding, Building);
                        PQ.add(new CurrentLocation(nextBuilding, newDistance));
                    }
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

        StringBuilder path = new StringBuilder();

        for (Integer key : printingPath) {
            path.append(key).append(",");
        }
        path.deleteCharAt(path.length() - 1);
        return path.toString();
    }

    public void ThreadsControl() {
        executor.submit(new Runnable() {
            @Override
            public void run() {



            }
        });

//        Thread RoadDeleteThread = new Thread(() -> {
//            try {
//                while (true) {
//                    String deleteRoad = RoadDeleteQueue.take();
//                    String[] messageInUse = deleteRoad.split("\\|");
//
//                    if (Objects.equals(messageInUse[6], "BLOCKED")) {
//                        if (graph.startingMap.containsKey(Integer.parseInt(messageInUse[2]))) {
//                            graph.removeRoad (parseInt(messageInUse[2]), parseInt(messageInUse[4]), messageInUse[6]);
//                            System.out.println("Road from " +  messageInUse[2] + " to " + messageInUse[4] + " has been blocked");
//                        }
//                    }
//                }
//            }
//            catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });
//
//        Thread BuildingDeleteThread = new Thread(() -> {
//            try {
//                while (true) {
//                    String deleteBuilding = BuildingDeleteQueue.take();
//                    String[] messageInUse = deleteBuilding.split("\\|");
//
//                    if (Objects.equals(messageInUse[2], "COLLAPSED")) {
//                        if (graph.startingMap.containsKey(Integer.parseInt(messageInUse[1]))) {
//                            graph.removeBuilding(parseInt(messageInUse[1]));
//                            System.out.println("Building " + parseInt(messageInUse[1]) + " removed");
//                        }
//                    }
//                }
//            }
//            catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });

        Thread PathFinding = new Thread(() -> {
            try {
                while (true) {
                    String pathFinder = PathFindingQueue.take();
                    String[] messageInUse = pathFinder.split(" ");
                    String temp;
                    temp = PathFinding(Integer.parseInt(messageInUse[0]), Integer.parseInt(messageInUse[1]));
                    System.out.println(temp);
                    Message n = new Message("PATH|VEHICLE|1|WAYPOINTS|" + temp);
                    outMessageQueue.add(n);

                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

//        RoadDeleteThread.start();
//        System.out.println("RoadDeleteThread Started");
//        BuildingDeleteThread.start();
//        System.out.println("BuildingDeleteThread Started");
        PathFinding.start();
        System.out.println("PathFindingThread Started");

    }
    @Override
    protected void handle(Message s) {

        String rescueMessage;

        String messageToUse = s.getMessage();

        String[] handlingMessage = messageToUse.split("\\|");

        switch (handlingMessage[0]) {
            case "RESCUE":
                rescueMessage = origin + " " + handlingMessage[2];
                System.out.println(rescueMessage);
                PathFindingQueue.add(rescueMessage);
                break;

            case "ROAD":
                if (handlingMessage[6].equals("BLOCKED")){
                    if (graph.startingMap.containsKey(Integer.parseInt(handlingMessage[2]))) {
                        graph.removeRoad (parseInt(handlingMessage[2]), parseInt(handlingMessage[4]), handlingMessage[6]);
                        System.out.println("Road from " +  handlingMessage[2] + " to " + handlingMessage[4] + " has been blocked");
                    }
                }
                break;

            case "LOCATION":
                if (graph.startingMap.containsKey(Integer.parseInt(handlingMessage[1]))) {
                    graph.removeBuilding(parseInt(handlingMessage[1]));
                    System.out.println("Building " + parseInt(handlingMessage[1]) + " removed");
                }
                break;

            case "PATH_INVALID":
                break;

            case "WAYPOINT_INVALID":
                break;

            case "VEHICLE":
                if (handlingMessage[2].equals("HALTED")) {
                    if (!handlingMessage[4].equals(origin)) {
                        rescueMessage = handlingMessage[4] + " " + origin;
                        PathFindingQueue.add(rescueMessage);
                    }
                }
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
        String filename = ConfigurationInfo.getMapFile(configFile);
        origin = ConfigurationInfo.getOrigin(configFile);
        graph = GraphBuilder.buildFromGraphML(filename);
        ThreadsControl();
    }
}
