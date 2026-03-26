import com.sun.source.tree.Tree;

import java.util.*;

public class Undirected extends Graph{

    Map<Vertex, List<Vertex>> UndirList = new TreeMap<>();

    @Override
    void addVertex(Vertex v) {
        UndirList.putIfAbsent(v, new ArrayList<>());
    }

    @Override
    void addEdge(Vertex v, Vertex w) {

        addVertex(v);
        addVertex(w);

        if (!UndirList.get(v).contains(w)){
            UndirList.get(v).add(w);
        }
        if (!UndirList.get(w).contains(v)){
            UndirList.get(w).add(v);
        }

        Collections.sort(UndirList.get(v));
        Collections.sort(UndirList.get(w));
    }

    @Override
    List<Vertex> adjacentTo(Vertex v) {
        return UndirList.get(v);
    }

    @Override
    int degree(Vertex v) {
        if (!UndirList.containsKey(v)) {
            return 0;
        }
        else return UndirList.get(v).size();
    }

    @Override
    List<Vertex> getVertices() {
        return new ArrayList<>(UndirList.keySet());
    }

    @Override
    boolean hasEdge(Vertex v, Vertex w) {
        return UndirList.containsKey(v) && UndirList.get(v).contains(w);
    }

    @Override
    boolean hasVertex(Vertex vertex) {
        return UndirList.containsKey(vertex);
    }

    @Override
    Vertex getVertex(String v) {
        return null;
    }
}
