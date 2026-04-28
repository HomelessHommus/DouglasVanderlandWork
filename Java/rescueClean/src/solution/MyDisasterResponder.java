package solution;

import org.jdom2.JDOMException;
import sim.Message;

import java.io.IOException;

public class MyDisasterResponder extends DisasterResponder {

    String filename = "data/map.10.graphml";
    @Override
    protected void handle(Message s) {

    }

    @Override
    protected void setup() throws IOException, JDOMException {
        GraphBuilder.buildFromGraphML("data/map.10.graphml");
    }
}
