import java.util.*;

public class Directed extends Graph{

    Map<Vertex, List<Vertex>> DirList = new TreeMap<>();

    @Override
    void addVertex(Vertex v) {
        // add v to dirlist if it's not there
        DirList.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    void addEdge(Vertex v, Vertex w) {
        // check if already in map
        addVertex(v);
        addVertex(w);

        // if not already an edge, add w to v's list in its map
        if (!DirList.get(v).contains(w)){
            DirList.get(v).add(w);
        }

        // sort
        Collections.sort(DirList.get(v));
    }

    @Override
    List<Vertex> adjacentTo(Vertex v) {
        // return v vertex (has all it's edges it a list)
        return DirList.get(v);
    }

    @Override
    int degree(Vertex v) {
        // return the amount of edges (size of list)
        if (!DirList.containsKey(v)) {
            return 0;
        }
        else return DirList.get(v).size();
    }

    @Override
    List<Vertex> getVertices() {
        // return array of all keys in the map
        return new ArrayList<>(DirList.keySet());
    }

    @Override
    boolean hasEdge(Vertex v, Vertex w) {
        // return true is v is a key and it has w in its list (has an edge)
        return DirList.containsKey(v) && DirList.get(v).contains(w);
    }

    @Override
    boolean hasVertex(Vertex vertex) {
        // checks if list has key called vertex
        return DirList.containsKey(vertex);
    }

    @Override
    Vertex getVertex(String v) {
        // interates through all keys in set, returns the vertex where the label == v
        for (Vertex vertex : DirList.keySet()) {
            if (vertex.getLabel().equals(v)) {
                return vertex;
            }
        }
        return null;
    }
}
