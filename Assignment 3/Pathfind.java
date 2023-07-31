// Omar Alshafei
// Assignment 3
// 7/27/2023

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// this program creates a graph and then implements multiple pathfinding algorithms on the graph
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
    
    // performs Dijkstra's algorithm
    public void dijkstraAlgorithm() {
        // set every node's distance to infinity
        Arrays.fill(distance, INF);
        // set every node's parent as -1
        Arrays.fill(parent, -1);
        // set the source node's distance to zero
        distance[sourceVertex] = -1;
        // create a set to track unvisted verticies
        Set<Integer> unvisitedVertices = new HashSet<>(vertices);
        
        // loop to find the shortest path from the source vertex to other vertices
        while (!unvisitedVertices.isEmpty()) {
            
            // calls method to find the vertex with the minimum distance that is unvisited
            int current = findMinDistance(unvisitedVertices);
            
            if (current == -1) 
                break;

            unvisitedVertices.remove(current);
            
            // loop through neighbors of the current vertex and their update distance
            for (int neighbor : vertices) {
                
                // check incase neighbor is visited or edge doesnt exist
                if (!unvisitedVertices.contains(neighbor) || adjacencyMatrix[current][neighbor] == 0)
                    continue;

                int newDistance;
                if (distance[current] == -1) {
                    // if current is the source vertex the new distance is the distance of the edge from current to neighbor
                    newDistance = adjacencyMatrix[current][neighbor];
                }
                else {
                    // calculate the new distance by adding the current distance and the edge distance
                    newDistance = distance[current] + adjacencyMatrix[current][neighbor];
                }
                // updates distance and parent values if the new distance is smaller than the current distance
                if (newDistance < distance[neighbor]) {
                    distance[neighbor] = newDistance;
                    parent[neighbor] = current;
                }
            }
        }
    }

    // finds the vertex with the lowest weight
    private int findMinDistance(Set<Integer> unvisitedVertices) {
        int minDistance = INF;
        int minVertex = -1;

        for (int vertex : unvisitedVertices) {
            if (distance[vertex] < minDistance) {
                minDistance = distance[vertex];
                minVertex = vertex;
            }
        }
        // returns vertex with the lowest distance
        return minVertex;
    }

    // performs Bellman Ford's algorithm
    public void bellmanFordAlgorithm() {
        // set every node's distance to infinity
        Arrays.fill(distance, INF);
        // set every node's parent as zero
        Arrays.fill(parent, 0);
        // set the source node's distance to zero
        distance[sourceVertex] = 0;

        // iterate through the graph |V| - 1 times
        for (int i = 0; i < numVertices - 1; i++) {
            for (int source = 1; source <= numVertices; source++) {
                for (int destination = 1; destination <= numVertices; destination++) {
                    if (adjacencyMatrix[source][destination] != 0) {
                        // check if the distance from the source to the destination from this edge is shorter than the current distance
                        if (distance[source] != INF && distance[source] + adjacencyMatrix[source][destination] < distance[destination]) {
                            distance[destination] = distance[source] + adjacencyMatrix[source][destination]; // update the distance to the destination vertex
                            parent[destination] = source; // set its parent to the source vertex

                        }
                    }
                }
            }
        }

        // check for negative-weight cycles
        for (int src = 1; src <= numVertices; src++) {
            for (int dest = 1; dest <= numVertices; dest++) {
                if (adjacencyMatrix[src][dest] != 0) {
                    if (distance[src] != INF && distance[src] + adjacencyMatrix[src][dest] < distance[dest]) {
                        throw new RuntimeException("Error: Negative-weight cycle!");
                    }
                }
            }
        }
    }
    
    // performs Floyd Warshall's algorithm
    public void floydWarshallAlgorithm() {
    
        // fill in 2D array distance's to infinity
        for(int[] row : fwDistance)
            Arrays.fill(row, INF);

        // Initialize the distance matrix with the adjacency matrix
        for (int i = 1; i <= numVertices; i++) {
            fwDistance[i][i] = 0;            
            for (int j = 1; j <= numVertices; j++) {
                // set the weight if edge weight is valid
                if (adjacencyMatrix[i][j] != 0) 
                    fwDistance[i][j] = adjacencyMatrix[i][j];
            }
        }
    
        // iterate through and find the shortest paths
        for (int k = 1; k <= numVertices; k++)
            for (int i = 1; i <= numVertices; i++)
                for (int j = 1; j <= numVertices; j++)
                    if (fwDistance[i][k] != INF && fwDistance[k][j] != INF) // continue if paths are not infinity
                        if (fwDistance[i][k] + fwDistance[k][j] < fwDistance[i][j]) // if i to k plus k to j is less then i to j
                            fwDistance[i][j] = fwDistance[i][k] + fwDistance[k][j]; // then set it as i to j 
    }
        
    // writes the algorithm output to a file
    private void writeToFile(FileWriter file) throws IOException {
        file.write(numVertices + "\n");
        for (int vertex : vertices)
            file.write(vertex + " " + distance[vertex] + " " + parent[vertex] + "\n");
    }
    
    // write the Ford-Warshall algorithm output to file
    private void outputFW(FileWriter fw) throws IOException {
        fw.write(numVertices + "\n");
        for (int source : vertices){
            for(int destination : vertices){
                fw.write(fwDistance[source][destination] + " ");
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
        
        // create file for Dijkstra's algorithm
        FileWriter dj = new FileWriter("cop3503-asn2-output-alshafei-omar-dj.txt");
        // call method to perform Dijkstra's algorithm
        graph.dijkstraAlgorithm();
        // write the output to file
        dj.write("Dijkstra's Algorithm:\n");
        graph.writeToFile(dj);
        // close file for Dijkstra's algorithm
        dj.close();
        
        // create file for Bellman Ford's algorithm
        FileWriter bf = new FileWriter("cop3503-asn2-output-alshafei-omar-bf.txt");
        // call method to perform Bellman Ford's algorithm
        graph.bellmanFordAlgorithm();
        // write the output to file
        bf.write("Bellman Ford's Algorithm:\n");
        graph.writeToFile(bf);
        // close file for Bellman Ford's algorithm
        bf.close();
        
        // create file for Floyd Warshall's algorithm
        FileWriter fw = new FileWriter("cop3503-asn2-output-alshafei-omar-fw.txt");
        // call method to perform Floyd Warshall's algorithm
        graph.floydWarshallAlgorithm();
        // write the output to file
        fw.write("Floyd Warshall's Algorithm:\n");
        graph.outputFW(fw);
        // close file for Floyd Warshall's algorithm
        fw.close();
    }
}
