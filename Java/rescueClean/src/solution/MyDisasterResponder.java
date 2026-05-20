package solution;
import org.jdom2.JDOMException;
import sim.Message;
import util.ConfigurationInfo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class MyDisasterResponder extends DisasterResponder {

    // setup variables to refer back to
    String origin;
    Graph graph = new Graph();
    HashMap<Integer, VehicleTracker> availableVehicles = new HashMap<>();

    // queue for the path finding messages
    BlockingQueue<String> PathFindingQueue = new LinkedBlockingQueue<>();

    // Maps, Lists, and Variables to store data for the path finding
    HashMap<Long, Double> totalDistance = new HashMap<>();
    HashMap<Long, Long> previous = new HashMap<>();
    List<Long> printingPath = new ArrayList<>();
    PriorityQueue<CurrentLocation> PQ = new PriorityQueue<>(Comparator.comparingLong(o -> (int) o.totalDistance));
    Long Building;
    double Distance;
    double newDistance;
    Long nextBuilding;
    Long current;

    // Path finding algorithm, takes a start and end point
    public String PathFinding (Long startBuilding, Long endBuilding) {
        totalDistance.clear();
        previous.clear();
        printingPath.clear();
        PQ.clear();

        for(Long key : graph.getStartingMap().keySet()) {
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
                    if (graph.startingMap.containsKey(road.getDestination())) {

                        nextBuilding = road.getDestination();
                        newDistance = Distance + road.getCost();

                        if (newDistance < totalDistance.getOrDefault(nextBuilding, 1000000.0)) {
                            totalDistance.put(nextBuilding, newDistance);
                            previous.put(nextBuilding, Building);
                            PQ.add(new CurrentLocation(nextBuilding, newDistance));
                        }
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

        for (Long key : printingPath) {
            path.append(key).append(",");
        }
        path.deleteCharAt(path.length() - 1);
        return path.toString();
    }

    protected void handle(Message s) {

        String rescueMessage;

        String messageToUse = s.getMessage();

        String[] handlingMessage = messageToUse.split("\\|");

        switch (handlingMessage[0]) {
            case "RESCUE":
                Integer vehicleToUse = null;
                for (Integer key : availableVehicles.keySet()) {
                    if (!availableVehicles.get(key).getInUse()) {
                        vehicleToUse = key;
                        availableVehicles.get(key).setInUse(true);
                        break;
                    }
                }
                rescueMessage = origin + " " + handlingMessage[2] + " " + vehicleToUse;
                availableVehicles.get(vehicleToUse).setFinalDestination(handlingMessage[2]);
                PathFindingQueue.add(rescueMessage);
                break;

            case "ROAD":
                if (handlingMessage[6].equals("BLOCKED")){
                    if (graph.startingMap.containsKey(parseLong(handlingMessage[2]))) {
                        graph.removeRoad (parseLong(handlingMessage[2]), parseLong(handlingMessage[4]), handlingMessage[6]);
                        System.out.println("Road from " +  handlingMessage[2] + " to " + handlingMessage[4] + " has been blocked");
                    }
                    if (graph.startingMap.containsKey(parseLong(handlingMessage[4]))) {
                        graph.removeRoad(parseLong(handlingMessage[4]), parseLong(handlingMessage[2]), handlingMessage[6]);
                    }
                }
                break;

            case "LOCATION":
                if (graph.startingMap.containsKey(parseLong(handlingMessage[1]))) {
                    graph.removeBuilding(parseLong(handlingMessage[1]));
                    System.out.println("Building " + parseInt(handlingMessage[1]) + " removed");
                }
                break;

            case "PATH_INVALID":
                break;

            case "WAYPOINT_INVALID":
                graph.removeRoad(parseLong(handlingMessage[4]), parseLong(handlingMessage[6]), "BLOCKED");
                graph.removeRoad(parseLong(handlingMessage[6]), parseLong(handlingMessage[4]), "BLOCKED");
                System.out.println("Road from " +  handlingMessage[4] + " to " + handlingMessage[6] + " has been blocked");
                rescueMessage = handlingMessage[4] + " " + availableVehicles.get(parseInt(handlingMessage[2])).getFinalDestination() + " " + handlingMessage[2];
                PathFindingQueue.add(rescueMessage);
                break;

            case "VEHICLE":
                if (handlingMessage[2].equals("HALTED")) {
                    if (!handlingMessage[4].equals(origin)) {
                        rescueMessage = handlingMessage[4] + " " + origin + " " + handlingMessage[1];
                        PathFindingQueue.add(rescueMessage);
                    }
                }
                if (handlingMessage[2].equals("RETURNED")) {
                    availableVehicles.get(parseInt(handlingMessage[1])).setInUse(false);
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
        for (int i = 1; i <= ConfigurationInfo.NUMBER_OF_VEHICLES; i++) {
            availableVehicles.put(i, new VehicleTracker(i));
        }
        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String pathFinder = PathFindingQueue.take();
                    String[] messageInUse = pathFinder.split(" ");
                    String temp;
                    temp = PathFinding(parseLong(messageInUse[0]), parseLong(messageInUse[1]));
                    Message n = new Message("PATH|VEHICLE|" + messageInUse[2] + "|WAYPOINTS|" + temp);
                    outMessageQueue.add(n);
                }
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
