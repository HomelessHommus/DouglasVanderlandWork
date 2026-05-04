package solution;
import org.jdom2.JDOMException;
import sim.Message;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Integer.parseInt;

public class MyDisasterResponder extends DisasterResponder {

    String firstMessage;
    Graph graph = new Graph();
    String filename = "data/map.2000.graphml";
    BlockingQueue<String>  queue = new LinkedBlockingQueue<>();
    @Override
    protected void handle(Message s) {

//        change this so the handle message stores things in a que, then calls a seperate method for the swtich statement
        String messageToUse = s.getMessage();

        String[] handlingMessage = messageToUse.split("\\|");

        switch (handlingMessage[0]) {
            case "RESCUE":
                break;

            case "ROAD":
                if (Objects.equals(handlingMessage[6], "BLOCKED")) {
                    if (graph.startingMap.containsKey(Integer.parseInt(handlingMessage[2]))) {
                        graph.removeRoad (parseInt(handlingMessage[2]), parseInt(handlingMessage[4]), handlingMessage[6]);
                        System.out.println("Road from " +  handlingMessage[2] + " to " + handlingMessage[4] + " has been blocked");
                    }

                }
                break;

            case "LOCATION":
                if (Objects.equals(handlingMessage[2], "COLLAPSED")) {
                    if (graph.startingMap.containsKey(Integer.parseInt(handlingMessage[1]))) {
                        graph.removeBuilding(parseInt(handlingMessage[1]));
                        System.out.println("Building " + parseInt(handlingMessage[1]) + " removed");
                    }

                }
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

        }
    }

    @Override
    protected void setup() throws IOException, JDOMException {
        graph = GraphBuilder.buildFromGraphML(filename);
    }
}
