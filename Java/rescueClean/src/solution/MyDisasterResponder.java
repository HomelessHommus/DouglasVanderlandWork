package solution;
import org.jdom2.JDOMException;
import sim.Message;
import java.io.IOException;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class MyDisasterResponder extends DisasterResponder {

    String firstMessage;
    Graph graph = new Graph();
    String filename = "data/map.1000.graphml";
    @Override
    protected void handle(Message s) {

        String messageToUse = s.getMessage();

        String[] handlingMessage = messageToUse.split("\\|");

        switch (handlingMessage[0]) {
            case "RESCUE":
                break;
            case "ROAD":
                if (Objects.equals(handlingMessage[6], "BLOCKED")) {
                    graph.removeRoad
                            (parseInt(handlingMessage[2]), parseInt(handlingMessage[4]), handlingMessage[6]);
                }
                break;
            case "LOCATION":
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
