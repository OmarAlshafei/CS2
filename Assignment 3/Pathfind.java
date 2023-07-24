// Omar Alshafei
// Assignment 2

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// this program creates a graph and then implements dijkstras algorithm on the graph
public class Pathfind {
    private Set<Integer> vertices;
    private int[][] adjacencyMatrix;
    private int[] weight;
    private int[] parent;
    public int sourceVertex;
    public int numVertices;
    public static final int INF = Integer.MAX_VALUE;

    // graph constructor
    public Pathfind(int sourceVertex, int numVertices) {
        this.sourceVertex = sourceVertex;
        this.numVertices = numVertices;
        vertices = new HashSet<>();
        adjacencyMatrix = new int[numVertices + 1][numVertices + 1];
        weight = new int[numVertices + 1];
        parent = new int[numVertices + 1];

        for (int[] arr : adjacencyMatrix) {
            Arrays.fill(arr, 0);
        }
    }

    // adds vertex to the graph
    public void addVertex(int vertex) {
        vertices.add(vertex);
    }

    // adds edge and its weight to the graph
    public void addEdge(int source, int destination, int weight) {
        if (!vertices.contains(source))
            vertices.add(source);

        if (!vertices.contains(destination)) {
            vertices.add(destination);
        }
        // set edge weight
        adjacencyMatrix[source][destination] = weight;
        adjacencyMatrix[destination][source] = weight;
    }

    // performs Dijkstra's algorithm
    public void bellmanFordAlgorithm() {
        Arrays.fill(weight, INF);
        Arrays.fill(parent, 0);
        weight[sourceVertex] = 0;

        // Relax edges repeatedly
        for (int i = 0; i < numVertices - 1; i++) {
            for (int src = 1; src <= numVertices; src++) {
                for (int dest = 1; dest <= numVertices; dest++) {
                    if (adjacencyMatrix[src][dest] != 0) {
                        if (weight[src] != INF && weight[src] + adjacencyMatrix[src][dest] < weight[dest]) {
                            weight[dest] = weight[src] + adjacencyMatrix[src][dest];
                            parent[dest] = src;
                        }
                    }
                }
            }
        }

        // Check for negative-weight cycles
        for (int src = 1; src <= numVertices; src++) {
            for (int dest = 1; dest <= numVertices; dest++) {
                if (adjacencyMatrix[src][dest] != 0) {
                    if (weight[src] != INF && weight[src] + adjacencyMatrix[src][dest] < weight[dest]) {
                        return;
                    }
                }
            }
        }
    }

    // writes out the output to a file
    private void writeToFile() throws IOException {
        FileWriter fw = new FileWriter("cop3503-asn2-output-alshafei-omar.txt");
        fw.write(numVertices + "\n");

        for (int vertex : vertices)
            fw.write(vertex + " " + weight[vertex] + " " + parent[vertex] + "\n");

        fw.close();
    }

    // driver method
    public static void main(String[] args) throws IOException {
        // scan input file
        Scanner sc = new Scanner(new FileReader("cop3503-asn2-input.txt"));
        // create a list to store the input from the file
        List<Integer> input = new ArrayList<>();

        int vertex = -1;
        int destinationVertex = -1;
        int weight = -1;

        // scan in and store the input from the file
        while (sc.hasNext()) {
            if (sc.hasNext("#"))
                sc.nextLine();
            if (sc.hasNextInt())
                input.add(sc.nextInt());
            else
                sc.next();
        }

        // store the number of vertices
        int numVertices = input.get(0);
        input.remove(0);
        // store the source vertex
        int sourceVertex = input.get(0);
        input.remove(0);
        // store the number of edges
        int numEdges = input.get(0);
        input.remove(0);

        // create the graph
        Pathfind graph = new Pathfind(sourceVertex, numVertices);
        // fill in the graph with the input
        while (!input.isEmpty()) {
            vertex = input.get(0);
            input.remove(0);
            destinationVertex = input.get(0);
            input.remove(0);
            weight = input.get(0);
            input.remove(0);
            graph.addVertex(vertex);
            graph.addVertex(destinationVertex);
            graph.addEdge(vertex, destinationVertex, weight);
        }
        // close scanner
        sc.close();
        // call method to perform Dijkstra's algorithm on the graph
        graph.bellmanFordAlgorithm();
        // write the output to file
        graph.writeToFile();
    }
}
