import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author lewi0146
 */
public class BreadthFirstSearch {

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

    public BreadthFirstSearch(Graph g, Vertex source)
    {
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

        bfs(source, g);

    }

    private void bfs(Vertex v, Graph g) {

        Queue<Vertex> q = new LinkedList<>();


        q.offer(v);
        dist.put(v, 0);

        while (!q.isEmpty()) {

            Vertex v2 = q.poll();
            g.setState(v2, Vertex.VertexState.DISCOVERED);

            List<Vertex> vs = g.adjacentTo(v2);
            if (vs != null) {
                for (Vertex child : vs) {

                    if (g.getState(child) == Vertex.VertexState.UNVISITED) {
                        g.setState(child, Vertex.VertexState.DISCOVERED);
                        q.offer(child);
                        // distance is the distance so far (dist.get(v2)) plus 1
                        dist.put(child, 1 + dist.get(v2));
                        prev.put(child, v2);
                    }
                }

            }
            g.setState(v2, Vertex.VertexState.FINISHED);

            // add to the
            traversalOrder.add(v2);

        }

    }

    public List<Vertex> getBreadFirstTraversalList()
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
