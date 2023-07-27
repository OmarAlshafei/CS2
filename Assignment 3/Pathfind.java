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
    private int[] distance;
    private int[] parent;
    public int sourceVertex;
    public int numVertices;
    int[][] fwDistance;
    public static final int INF = Integer.MAX_VALUE;

    // graph constructor
    public Pathfind(int sourceVertex, int numVertices) {
        vertices = new HashSet<>();
        adjacencyMatrix = new int[numVertices + 1][numVertices + 1];
        distance = new int[numVertices + 1];
        parent = new int[numVertices + 1];
        this.sourceVertex = sourceVertex;
        this.numVertices = numVertices;
        fwDistance = new int[numVertices + 1][numVertices + 1];

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

    // performs Bellman Ford's algorithm
    public void bellmanFordAlgorithm() {
        Arrays.fill(distance, INF);
        Arrays.fill(parent, 0);
        distance[sourceVertex] = 0;

        // Relax edges repeatedly
        for (int i = 0; i < numVertices - 1; i++) {
            for (int source = 1; source <= numVertices; source++) {
                for (int destination = 1; destination <= numVertices; destination++) {
                    if (adjacencyMatrix[source][destination] != 0) {
                        if (distance[source] != INF && distance[source] + adjacencyMatrix[source][destination] < distance[destination]) {
                            distance[destination] = distance[source] + adjacencyMatrix[source][destination];
                            parent[destination] = source;
                        }
                    }
                }
            }
        }

        // Check for negative-distance cycles
        for (int src = 1; src <= numVertices; src++) {
            for (int dest = 1; dest <= numVertices; dest++) {
                if (adjacencyMatrix[src][dest] != 0) {
                    if (distance[src] != INF && distance[src] + adjacencyMatrix[src][dest] < distance[dest]) {
                        return;
                    }
                }
            }
        }
    }
    
    // performs Floyd Warshall's algorithm
    public void floydWarshallAlgorithm() {
        // Initialize the distance matrix with the adjacency matrix
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                // if the points are the same set weight to zero
                if (i == j) 
                    fwDistance[i][j] = 0;
                // set the weight if edge is valid
                else if (adjacencyMatrix[i][j] != 0) 
                    fwDistance[i][j] = adjacencyMatrix[i][j];
                // else set to infinity
                else
                    fwDistance[i][j] = INF;
            }
        }
    
        // Compute all-pairs shortest paths
        for (int k = 1; k <= numVertices; k++)
            for (int i = 1; i <= numVertices; i++)
                for (int j = 1; j <= numVertices; j++)
                    if (fwDistance[i][k] != INF && fwDistance[k][j] != INF) // continue if paths are not infinity
                        if (fwDistance[i][k] + fwDistance[k][j] < fwDistance[i][j]) // if i to k plus k to j is less then i to j
                            fwDistance[i][j] = fwDistance[i][k] + fwDistance[k][j]; // then set it as i to j 
                                
        // Update the distance and parent arrays with the results
        for (int i = 1; i <= numVertices; i++) {
            for (int j = 1; j <= numVertices; j++) {
                if (fwDistance[i][j] != INF) {
                    distance[j] = fwDistance[sourceVertex][j];
                    parent[j] = i;
                }
            }
        }
    }
        
    // writes out the output to a file
    private void outputBF(FileWriter bf) throws IOException {
        bf.write("Bellman Ford's Output:\n");
        bf.write(numVertices + "\n");
        for (int vertex : vertices)
            bf.write(vertex + " " + distance[vertex] + " " + parent[vertex] + "\n");
    }
    
    // write out the Ford-Warshall algorithm to file
    private void outputFW(FileWriter fw) throws IOException {
        fw.write("Floyd Warshall's Output:\n\n");
        String x = "   ";
        fw.write(x);
        for (int vertex : vertices){
            fw.write(String.format("%3d", vertex));
        }
        
        fw.write("\n\n");
    
        for (int vertex : vertices) {
            fw.write(String.format(vertex + "  "));
            for (int j = 1; j <= numVertices; j++) {
                fw.write(String.format("%3d", fwDistance[vertex][j]));
            }
            fw.write("\n");
        }
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
        
        // create file for Bellman Ford's algorithm
        FileWriter bf = new FileWriter("cop3503-asn2-output-alshafei-omar-bf.txt");
        // call method to perform Bellman Ford's algorithm
        graph.bellmanFordAlgorithm();
        // write the output to file
        graph.outputBF(bf);
        // close file for Bellman Ford's algorithm
        bf.close();
        
        // create file for Floyd Warshall's algorithm
        FileWriter fw = new FileWriter("cop3503-asn2-output-alshafei-omar-fw.txt");
        // call method to perform Floyd Warshall's algorithm
        graph.floydWarshallAlgorithm();
        // write the output to file
        graph.outputFW(fw);
        // close file for Floyd Warshall's algorithm
        fw.close();
    }
}
