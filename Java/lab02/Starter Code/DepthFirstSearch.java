import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class DepthFirstSearch {

    /**
     * Maintain the previous Vertex for each Vertex to discover paths through the graph.
     */
    Map<Vertex,Vertex> prev;

    /**
     * Maintain the distances from the source Vertex.
     */
    Map<Vertex,Integer> dist;

    /**
     * The list of Vertex objects in traversal order
     */
    List<Vertex> traversalOrder;

    public DepthFirstSearch(Graph g, Vertex source) {
        prev = new HashMap<>();
        dist = new HashMap<>();
        traversalOrder = new LinkedList<>();
        List<Vertex> vs = g.getVertices();

        for (Vertex v : vs) {
            if (v.equals(source)) {
                source = v;
            }
            g.clearState();
            g.setState(v, Vertex.VertexState.UNVISITED);
        }

        dfs(source, g);
    }

    private void dfs(Vertex v, Graph g) {
        g.setState(v, Vertex.VertexState.DISCOVERED);
        traversalOrder.add(v);
        List<Vertex> neighbours = g.adjacentTo(v);

        if (neighbours != null) {
            for (Vertex next : neighbours) {
                if (g.getState(next) == Vertex.VertexState.UNVISITED) {

                    prev.put(next, v);
                    dist.put(next, dist.get(v) + 1);
                    dfs(next, g);
                }
            }
        }
        g.setState(v, Vertex.VertexState.FINISHED);
    }

    public List<Vertex> getDepthFirstTraversalList()
    {
        return traversalOrder;
    }

    public int getDistanceTo(Vertex to)
    {
        if (dist.get(to) != null) {
            return dist.get(to);
        }
        else {
            return -1;
        }
    }

    public List<Vertex> pathTo(Vertex to)
    {
        Stack<Vertex> path = new Stack<>();
        while (dist.containsKey(to))
        {
            path.push(to);
            to = prev.get(to);
        }
        return path;
    }
}
