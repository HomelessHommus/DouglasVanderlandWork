import java.util.*;

public class Directed extends Graph{

    Map<Vertex, List<Vertex>> DirList = new TreeMap<>();

    @Override
    void addVertex(Vertex v) {
        DirList.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    void addEdge(Vertex v, Vertex w) {
        addVertex(v);
        addVertex(w);

        if (!DirList.get(v).contains(w)){
            DirList.get(v).add(w);
        }

        Collections.sort(DirList.get(v));
    }

    @Override
    List<Vertex> adjacentTo(Vertex v) {
        return DirList.get(v);
    }

    @Override
    int degree(Vertex v) {
        if (!DirList.containsKey(v)) {
            return 0;
        }
        else return DirList.get(v).size();
    }

    @Override
    List<Vertex> getVertices() {
        return new ArrayList<>(DirList.keySet());
    }

    @Override
    boolean hasEdge(Vertex v, Vertex w) {
        return DirList.containsKey(v) && DirList.get(v).contains(w);
    }

    @Override
    boolean hasVertex(Vertex vertex) {
        return DirList.containsKey(vertex);
    }

    @Override
    Vertex getVertex(String v) {
        return null;
    }
}
