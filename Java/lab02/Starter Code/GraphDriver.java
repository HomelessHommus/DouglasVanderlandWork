import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
/**
 *
 * @author lewi0146
 */
public class GraphDriver {

    /**
     * <p>
     * The GraphDriver class expect no command line arguments but process one
     * line from standard input.</p>
     * <p>
     * The format is</p>
     * <code>level_test additional_args</code>
     * <p>
     * The level_test is a number between 1 and 5. <p>
     * <p>Example usage is print graph
     * of undirected ("U") or directed ("D") graph</p>
     * <p>
     * <code>1 U</code></p>
     * <p>
     * <code>1 D</code></p>
     * <p>
     * print graph, the traversal order and distance from a starting node of
     * breadth first traversal of undirected ("U") or directed ("D") graph</p>
     * <p>
     * <code>2 U</code></p>
     * <p>
     * <code>2 D</code></p>
     * <p>
     * Do the Bacon!</p>
     * <p>
     * <code>5</code></p>
     * <p>
     * print graph, the traversal order and distance from a starting node of
     * depth first traversal of undirected ("U") or directed ("D") graph </p>
     * <p>
     * <code>3 U</code></p>
     * <p>
     * <code>3 D</code></p>
     * <p>
     * Process the topological sort for Bob's language topics. "Y" to print the
     * graph and "N" to only print the topic list.</p>
     * <p>
     * <code>4 Y</code></p>
     * <p>
     * <code>4 N</code></p>
     *
     * <p>
     * For 2 and 3 you can also specify a start vertex to traverse from. The
     * default value is "a", for example</p>
     * <p>
     * <code>2 U a</code></p>
     * <p>
     * will be an undirected graph doing a breadth first traversal start from
     * the node labelled with an "a".</p>
     * <p>
     * <code>3 D c</code></p>
     * <p>
     * will be an directed graph doing a depth first traversal start from the
     * node labelled with an "c".</p>
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String line = in.nextLine();
        String[] input = line.split(" ");

        int testLevel = Integer.parseInt(input[0]);

        Graph g = null;
        if (input.length > 1 && testLevel < 4) {
            String graphType = input[1];
            switch (graphType) {
                case "U":
                    g = null; // your new Adjacency List Undirected Graph
                    break;
                case "D":
                    g = null; // your new Adjacency List Directed Graph
                    break;

            }
        }

        String startFrom = "a";
        if (input.length > 2 && testLevel < 4) {
            startFrom = input[2];
        }

        switch (testLevel) {
            case 1:
                test1(g); // build and print graph
                break;
            case 2:
                test2(g, startFrom); // breadth first search
                break;
            case 3:
                test3(g, startFrom); // depth first seach
                break;
            case 4:
                String printGraph = (input.length > 1) ? input[1] : "N";
                test4(printGraph); // topological sort uses directed graph only
                break;
            case 5:
                // just for fun!
                bacon(); // uses undirected graph only
                break;
        }

    }

    public static void printGraph(Graph g) {

        List<Vertex> vs = g.getVertices();
        System.out.println("Vertices: " + vs);
        for (Vertex v : vs) {
            System.out.print(v + ": ");
            List<Vertex> adjVList = g.adjacentTo(v);
            for (Vertex av : adjVList) {
                System.out.print(av + " ");
            }
            System.out.println();

        }
    }

    public static void test1(Graph g) {

        System.out.println("test 1: Build a graph");

        // build a sample graph and print it
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("b", "d");
        g.addEdge("c", "d");
        g.addEdge("c", "e");
        g.addEdge("d", "a");

        printGraph(g);

    }

    public static void test2(Graph g, String startFrom) {

        System.out.println("test 2: Breadth First Search");

        // build a sample graph and print it
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("b", "d");
        g.addEdge("c", "d");
        g.addEdge("c", "e");
        g.addEdge("d", "a");

        printGraph(g);

        // perform a breadth first traversal from vertex in startFrom
        BreadthFirstSearch bfs = new BreadthFirstSearch(g, g.getVertex(startFrom));
        System.out.println(bfs.getBreadFirstTraversalList());

        // print out the distances/frontiers from vertex in startFrom
        List<Vertex> vit = g.getVertices();
        Vertex source = g.getVertex(startFrom);
        for (Vertex dest : vit) {
            System.out.println("dist from " + source.getLabel() + " to " + dest + ": " + bfs.getDistanceTo(dest));
        }

    }

    public static void bacon() {
        String castFile = "data/cast.rated.txt";
        try {
            Scanner fs = new Scanner(new FileInputStream(castFile));

            Graph g = null; // you new Adjacency List Undirected Graph();

            System.out.println("reading in file: " + castFile);
            while (fs.hasNext()) {
                String line = fs.nextLine();
                Scanner movie = new Scanner(line);
                movie.useDelimiter("/");
                String movieName = movie.next();
                while (movie.hasNext()) {
                    // add each actor
                    g.addEdge(movieName, movie.next());
                }
            }
            //System.out.println(g.getVertices());

            System.out.println("calculating bfs for Bacon, Kevin");
            Vertex source = g.getVertex("Bacon, Kevin");
            BreadthFirstSearch bfs = new BreadthFirstSearch(g, source);

            Scanner in = new Scanner(System.in);
            System.out.print("Enter an actor <lastname, firstname>: ");

            String actor = in.nextLine();
            while (!actor.isEmpty()) {

                Vertex dest = g.getVertex(actor);
                System.out.println("dist from " + source.getLabel() + " to " + dest + ": " + bfs.getDistanceTo(dest) / 2);

                for (Vertex v : bfs.pathTo(dest)) {
                    System.out.println("  " + v.getLabel());
                }

                System.out.print("Enter an actor <lastname, firstname>: ");
                actor = in.nextLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find the cast file: " + castFile);
        }

    }

    public static void test3(Graph g, String startFrom) {

        System.out.println("test 3: Depth First Search");
        // build a sample graph and print it
        g.addEdge("a", "b");
        g.addEdge("b", "c");
        g.addEdge("b", "d");
        g.addEdge("c", "d");
        g.addEdge("c", "e");
        g.addEdge("d", "a");

        printGraph(g);

        System.out.println("Depth First");
        List<Vertex> ld = null;
        // this list is from your post-order Depth First Traversal starting
        // at startFrom (defaults to "a");
        ld = null;
        System.out.println(ld);

    }

    public static void test4(String printGraph) {

        System.out.println("test 4: Topological Sort");

        // build a sample graph and print it - only need directed graph.
        Graph g = null; // your new Adjacency List Directed Graph();

        if (printGraph.equals("Y")) {
            printGraph(g);
        }

        System.out.println("Topological Sort");
        List<Vertex> lt = null; // this list from your Topological Sort of graph g;
        System.out.println(lt);

    }
}
