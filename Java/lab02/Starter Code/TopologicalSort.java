import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class topologicalSort{

    public void TopologicalSort(Graph g){
        topoOrder = new LinkedList<>();
        visited = new HashSet<>();
        List<Vertex> starts = findZeroInDegree(g);
        Collections.sort(starts);

        for (Vertex v : starts) {
            if (!visited.contains(v)) {
                dfs(g, v);
            }
        }
        Collections.reverse(topoOrder);
    }
}
