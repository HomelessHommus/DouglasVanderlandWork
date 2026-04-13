public class Edge {

    int destination;
    int weight;

    Edge(int destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Destination: " + destination + " " + "Weight: " + weight;
    }
}

