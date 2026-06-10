package solution;
import org.jdom2.JDOMException;
import sim.Message;
import util.ConfigurationInfo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

public class MyDisasterResponder extends DisasterResponder {

    // setup variables to refer back to over the whole program
    String origin;
    Graph graph = new Graph();
    HashMap<Integer, VehicleTracker> availableVehicles = new HashMap<>();
    Integer vehicleToUse = null;

    // queue for the path finding messages
    // the thread assigned to path finding polls from this
    BlockingQueue<String> PathFindingQueue = new LinkedBlockingQueue<>();

    // rescue message used to direct the path finding algorithms on where to go
    // this is out here because multiple things use it across different methods
    String rescueMessage;

    // BELOW ARE THE 2 SOLUTIONS, TO CHANGE SOLUTION GO TO threadControl() JUST BELOW

    // Bidirectional Dijkstra path finding algorithm
    // takes in a start and end building as a parameter
    // THIS IS SOLUTION 1 FOR THE ASSIGNMENT
    public String BDD (Long startBuilding, Long endBuilding) {


        System.out.println("Path finding started from " + startBuilding + " to " + endBuilding);

        ConcurrentHashMap<Long, Double> distanceForward = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Double> distanceBackward = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Long> previousNodeForward = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Long> previousNodeBackward = new ConcurrentHashMap<>();
        Set<Long> forwardVisited = new HashSet<>();
        Set<Long> backwardVisited = new HashSet<>();
        PriorityQueue<CurrentLocation> forwardPriQ = new PriorityQueue<>(Comparator.comparingDouble(o -> o.totalDistance));
        PriorityQueue<CurrentLocation> backwardPriQ = new PriorityQueue<>(Comparator.comparingDouble(o -> o.totalDistance));

        distanceForward.put(startBuilding, 0.0);
        distanceBackward.put(endBuilding, 0.0);
        forwardPriQ.add(new CurrentLocation(startBuilding, 0));
        backwardPriQ.add(new CurrentLocation(endBuilding, 0));

        double closestDistance = Double.MAX_VALUE;
        Long meetingPoint = null;

        while (!forwardPriQ.isEmpty() && !backwardPriQ.isEmpty()) {

            CurrentLocation forward = forwardPriQ.poll();
            Long forwardNode = forward.Building;

            if (!forwardVisited.contains(forwardNode)) {
                forwardVisited.add(forwardNode);

                if (backwardVisited.contains(forwardNode)) {
                    double possibleForward = distanceForward.getOrDefault(forwardNode, Double.MAX_VALUE) + distanceBackward.getOrDefault(forwardNode, Double.MAX_VALUE);
                    if (possibleForward < closestDistance) {
                        closestDistance = possibleForward;
                        meetingPoint = forwardNode;
                    }
                }

                List<Road> fordwardRoadsList = graph.getStartingMap().get(forwardNode);
                if (fordwardRoadsList != null) {
                    for (Road road : fordwardRoadsList) {
                        if (road.getAccess().equals("open")) {
                            Long nextBuildingForward = road.getDestination();
                            if (!forwardVisited.contains(nextBuildingForward) && graph.getStartingMap().containsKey(nextBuildingForward)) {
                                double newDist = distanceForward.getOrDefault(forwardNode, Double.MAX_VALUE) + road.getCost();
                                if (newDist < distanceForward.getOrDefault(nextBuildingForward, Double.MAX_VALUE)) {
                                    distanceForward.put(nextBuildingForward, newDist);
                                    previousNodeForward.put(nextBuildingForward, forwardNode);
                                    forwardPriQ.add(new CurrentLocation(nextBuildingForward, newDist));
                                }
                            }
                        }
                    }
                }
            }

            CurrentLocation backward = backwardPriQ.poll();
            Long backwardNode = backward.Building;

            if (!backwardVisited.contains(backwardNode)) {
                backwardVisited.add(backwardNode);

                if (forwardVisited.contains(backwardNode)) {
                    double possibleBackwards = distanceForward.getOrDefault(backwardNode, Double.MAX_VALUE) + distanceBackward.getOrDefault(backwardNode, Double.MAX_VALUE);
                    if (possibleBackwards < closestDistance) {
                        closestDistance = possibleBackwards;
                        meetingPoint = backwardNode;
                    }
                }

                List<Road> backwardsRoadsList = graph.getStartingMap().get(backwardNode);
                if (backwardsRoadsList != null) {
                    for (Road road : backwardsRoadsList) {
                        if (road.getAccess().equals("open")) {
                            Long nextBuildingBackward = road.getDestination();
                            if (!backwardVisited.contains(nextBuildingBackward) && graph.getStartingMap().containsKey(nextBuildingBackward)) {
                                double newDist = distanceBackward.getOrDefault(backwardNode, Double.MAX_VALUE) + road.getCost();
                                if (newDist < distanceBackward.getOrDefault(nextBuildingBackward, Double.MAX_VALUE)) {
                                    distanceBackward.put(nextBuildingBackward, newDist);
                                    previousNodeBackward.put(nextBuildingBackward, backwardNode);
                                    backwardPriQ.add(new CurrentLocation(nextBuildingBackward, newDist));
                                }
                            }
                        }
                    }
                }
            }
            double forwardMinimum = forwardPriQ.isEmpty() ? Double.MAX_VALUE : forwardPriQ.peek().totalDistance;
            double backwardMinimum = backwardPriQ.isEmpty() ? Double.MAX_VALUE : backwardPriQ.peek().totalDistance;
            if (forwardMinimum + backwardMinimum >= closestDistance) {
                break;
            }
        }

        double bestDistance = Double.MAX_VALUE;
        for (Long building : graph.getStartingMap().keySet()) {
            if(backwardVisited.contains(building)){
                double totalDistance = distanceForward.getOrDefault(building, Double.MAX_VALUE) + distanceBackward.getOrDefault(building, Double.MAX_VALUE);
                if (totalDistance < bestDistance) {
                    bestDistance = totalDistance;
                    meetingPoint = building;
                }
            }
        }

        if (meetingPoint == null) {
            return null;
        }

        List<Long> totalPath = new ArrayList<>();

        Long currentBuilding = meetingPoint;
        while (currentBuilding != null) {
            totalPath.add(currentBuilding);
            currentBuilding = previousNodeForward.get(currentBuilding);
        }

        Collections.reverse(totalPath);

        currentBuilding = previousNodeBackward.get(meetingPoint);
        while (currentBuilding != null) {
            totalPath.add(currentBuilding);
            currentBuilding = previousNodeBackward.get(currentBuilding);
        }

        if (totalPath.isEmpty()) {
            return null;
        }

        StringBuilder toSend = new StringBuilder();
        for (Long node : totalPath) {
            toSend.append(node).append(",");
        }
        toSend.deleteCharAt(toSend.length() - 1);
        System.out.println("Path found from " + startBuilding + " to " + endBuilding);
        return toSend.toString();
    }

    // BFS path finding algorithm
    // takes in a start and end building as a parameter
    // THIS IS SOLUTION 2 FOR THE ASSIGNMENT
    public String BFS (Long startBuilding, Long endBuilding) {

        System.out.println("Path finding started from " + startBuilding + " to " + endBuilding);

        if (startBuilding.equals(endBuilding)) {
            return startBuilding.toString();
        }

        HashMap<Long, Long> previousBuilding = new HashMap<>();
        Set<Long> visitedBuildings = new HashSet<>();
        Queue<Long> FIFOQueue = new LinkedList<>();

        visitedBuildings.add(startBuilding);
        FIFOQueue.add(startBuilding);
        previousBuilding.put(startBuilding, null);

        while (!FIFOQueue.isEmpty()) {
            Long currentBuilding = FIFOQueue.poll();

            if (currentBuilding.equals(endBuilding)) {
                break;
            }

            List<Road> roads = graph.getStartingMap().get(currentBuilding);

            if (roads == null) {
                continue;
            }

            for (Road road : roads) {
                if (road.getAccess().equals("open")) {
                    Long nextBuilding = road.getDestination();

                    if (!visitedBuildings.contains(nextBuilding) && graph.getStartingMap().containsKey(nextBuilding)) {
                        visitedBuildings.add(nextBuilding);
                        previousBuilding.put(nextBuilding, currentBuilding);
                        FIFOQueue.add(nextBuilding);
                    }
                }
            }
        }

        if (!previousBuilding.containsKey(endBuilding)){
            return null;
        }

        List<Long> pathList = new ArrayList<>();
        Long currentBuilding = endBuilding;
        while (currentBuilding != null) {
            pathList.add(currentBuilding);
            currentBuilding = previousBuilding.get(currentBuilding);
        }
        Collections.reverse(pathList);

        if (pathList.isEmpty()) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        for (Long node : pathList) {
            result.append(node).append(",");
        }
        result.deleteCharAt(result.length() - 1);
        System.out.println("Path found from " + startBuilding + " to " + endBuilding);
        return result.toString();
    }

    // thread control for the path finding
    // while loop that is constantly trying to poll the BlockingQueue above
    public void threadControl() {

        executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String pathFinder;
                    try {
                        pathFinder = PathFindingQueue.take();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("PathFinding thread interrupted");
                        break;
                    }

                    String[] messageInUse = pathFinder.split(" ");

                    try {
                        // TO CHANGE THE SOLUTION CHANGE ↓ THIS ↓ TO EITHER "BFS" OR "BDD". NOTHING ELSE REQUIRED
                        String temp =                      BDD
                                (parseLong(messageInUse[0]), parseLong(messageInUse[1]));
                        if (temp != null) {
                            Message m = new Message("PATH|VEHICLE|" + messageInUse[2] + "|WAYPOINTS|" + temp);
                            outMessageQueue.add(m);

                        } else {
                            System.out.println("Path not found from " + messageInUse[0] + " to " + messageInUse[1]);

                            if (!messageInUse[1].equals(origin)) {
                                VehicleTracker vehicle = availableVehicles.get(parseInt(messageInUse[2]));
                                if (vehicle != null) {
                                    String position = vehicle.getPosition();
                                    if (position != null && !position.equals(origin)) {
                                        String returnMessage = position + " " + origin + " " + messageInUse[2];
                                        System.out.println("Rerouting vehicle " + messageInUse[2] + " home");
                                        PathFindingQueue.add(returnMessage);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error during pathfinding");
                    }
                }
            } catch (Exception e) {
                System.out.println("PathFinding thread dead");
            }
        });
    }

    // the main handle method for incoming messages
    protected void handle(Message s) {

        String messageToUse = s.text;

        String[] handlingMessage = messageToUse.split("\\|");

        switch (handlingMessage[0]) {
            // gets info from the message and adds that to the path finding queue
            // changes variables in the available vehicles map to keep track of vehicle
            case "RESCUE":
                for (Integer key : availableVehicles.keySet()) {
                    if (!availableVehicles.get(key).getInUse()) {
                        if (!availableVehicles.get(key).getDestroyed()) {
                            vehicleToUse = key;
                            availableVehicles.get(key).setInUse(true);
                            break;
                        }
                    }
                }
                rescueMessage = origin + " " + handlingMessage[2] + " " + vehicleToUse;
                availableVehicles.get(vehicleToUse).setFinalDestination(handlingMessage[2]);
                availableVehicles.get(vehicleToUse).setStartDestination(origin);
                availableVehicles.get(vehicleToUse).setRescuePosition(handlingMessage[2]);
                PathFindingQueue.add(rescueMessage);
                break;

                // deletes a road from the hashmap
            case "ROAD":
                if (handlingMessage[6].equals("BLOCKED")){
                    if (graph.getStartingMap().containsKey(parseLong(handlingMessage[2]))) {
                        graph.removeRoad (parseLong(handlingMessage[2]), parseLong(handlingMessage[4]), handlingMessage[6]);
                        System.out.println("Road from " +  handlingMessage[2] + " to " + handlingMessage[4] + " has been blocked");
                    }
                }
                break;

                // deletes a location from the hashmap
            case "LOCATION":
                if (graph.getStartingMap().containsKey(parseLong(handlingMessage[1]))) {
                    graph.removeBuilding(parseLong(handlingMessage[1]));
                    System.out.println("Building " + parseLong(handlingMessage[1]) + " removed");
                }
                break;

                // reroutes the vehicle home if this message is seen
            case "PATH_INVALID":
                if (!handlingMessage[3].equals("INVALID_STARTING_POINT") ) {
                    if (!handlingMessage[3].equals("DESTROYED")) {
                        rescueMessage = availableVehicles.get(parseInt(handlingMessage[2])).getPosition() + " " + origin + " " + parseInt(handlingMessage[2]);
                        availableVehicles.get(parseInt(handlingMessage[2])).setFinalDestination(origin);
                        availableVehicles.get(parseInt(handlingMessage[2])).setStartDestination(availableVehicles.get(parseInt(handlingMessage[2])).getPosition());
                        PathFindingQueue.add(rescueMessage);
                        System.out.println(availableVehicles.get(parseInt(handlingMessage[2])).getPosition());

                        for (Integer key : availableVehicles.keySet()) {
                            if (!availableVehicles.get(key).getInUse()) {
                                vehicleToUse = key;
                                availableVehicles.get(key).setInUse(true);
                                break;
                            }
                        }
                        rescueMessage = origin + " " + availableVehicles.get(parseInt(handlingMessage[2])).getRescuePosition() + " " + vehicleToUse;
                        availableVehicles.get(vehicleToUse).setFinalDestination(availableVehicles.get(parseInt(handlingMessage[2])).getRescuePosition());
                        availableVehicles.get(vehicleToUse).setStartDestination(origin);
                        PathFindingQueue.add(rescueMessage);
                        break;
                    }

                }
                break;

                // used to reroute the path. deletes the road that was bad midway through the simulation
            case "WAYPOINT_INVALID":
                graph.removeRoad(parseLong(handlingMessage[4]), parseLong(handlingMessage[6]), "BLOCKED");
                System.out.println("Road from " +  handlingMessage[4] + " to " + handlingMessage[6] + " has been blocked");
                rescueMessage = handlingMessage[4] + " " + availableVehicles.get(parseInt(handlingMessage[2])).getFinalDestination() + " " + handlingMessage[2];
                PathFindingQueue.add(rescueMessage);
                break;

            case "VEHICLE":

                //send vehicle back home because it's at rescue location
                if (handlingMessage[2].equals("HALTED")) {
                    if (!handlingMessage[4].equals(origin)) {
                        rescueMessage = handlingMessage[4] + " " + origin + " " + handlingMessage[1];
                        availableVehicles.get(parseInt(handlingMessage[1])).setFinalDestination(origin);
                        availableVehicles.get(parseInt(handlingMessage[1])).setStartDestination(handlingMessage[4]);
                        PathFindingQueue.add(rescueMessage);
                    }
                }

                // changes the bool variable so vehicle can be used again
                if (handlingMessage[2].equals("RETURNED")) {
                    availableVehicles.get(parseInt(handlingMessage[1])).setInUse(false);
                }

                // changes variable to keep track of vehicle as it travels
                if (handlingMessage[2].equals("ARRIVED")) {
                    availableVehicles.get(parseInt(handlingMessage[1])).setPosition(handlingMessage[4]);
                }

                // removes vehicle so it can't be used again
                if (handlingMessage[2].equals("DESTROYED")) {
                    availableVehicles.remove(parseInt(handlingMessage[1]));
                    System.out.println("Vehicle has been destroyed");
                }
                break;

                // changes vehicle variables for rerouting purposes
            case "PEOPLE_TRANSFERRED":
                availableVehicles.get(parseInt(handlingMessage[4])).setStartDestination(handlingMessage[2]);
                availableVehicles.get(parseInt(handlingMessage[4])).setFinalDestination(origin);
                break;

                // prints error
            case "ERROR":
                System.out.println("ERROR");
                break;

                default:
                    System.out.println("Unrecognized handling message");
        }
    }

    // setup method done before simulation starts
    @Override
    protected void setup() {

        // calls the methods to build the graph from the config file name
        try {
            String filename = ConfigurationInfo.getMapFile(configFile);
            origin = ConfigurationInfo.getOrigin(configFile);
            graph = GraphBuilder.buildFromGraphML(filename);
        }
        catch (IOException | JDOMException e) {
            e.printStackTrace();
        }

        // adds the availableVehicles to the hashmap to be used later
        for (int i = 0; i < ConfigurationInfo.NUMBER_OF_VEHICLES; i++) {
            availableVehicles.putIfAbsent(i, new VehicleTracker(i));
        }

        // starts the path finding thread method
        threadControl();
    }
}