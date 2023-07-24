// Omar Alshafei
// Assignment 2

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// this program creates a graph and then implements dijkstras algorithm on the graph
public class Dijkstra {
    private Set<Integer> vertices;
    private int[][] adjacencyMatrix;
    private int[] weight;
    private int[] parent;
    public int sourceVertex;
    public int numVertices;

    // graph constructor
    public Dijkstra(int sourceVertex, int numVertices) {
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
    public void dijkstraAlgorithm() {
        // initialize the weight and parent arrays
        Arrays.fill(weight, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        // set the source vertex weight to -1
        weight[sourceVertex] = -1;
        // create a set to track unvisted verticies
        Set<Integer> unvisitedVertices = new HashSet<>(vertices);
        
        // loop to find the shortest path from the source vertex to other vertices
        while (!unvisitedVertices.isEmpty()) {
            
            // calls method to find the vertex with the minimum weight that is unvisited
            int current = findMinWeight(unvisitedVertices);
            
            if (current == -1) 
                break;

            unvisitedVertices.remove(current);
            
            // loop through neighbors of the current vertex and their update weights
            for (int neighbor : vertices) {
                
                // check incase neighbor is visited or edge doesnt exist
                if (!unvisitedVertices.contains(neighbor) || adjacencyMatrix[current][neighbor] == 0)
                    continue;

                int newWeight;
                if (weight[current] == -1) {
                    // if current is the source vertex the new weight is the weight of the edge from current to neighbor
                    newWeight = adjacencyMatrix[current][neighbor];
                }
                else {
                    // calculate the new weight by adding the current weight and the edge weight
                    newWeight = weight[current] + adjacencyMatrix[current][neighbor];
                }
                // updates weight and parent values if the new weight is smaller than the current weight
                if (newWeight < weight[neighbor]) {
                    weight[neighbor] = newWeight;
                    parent[neighbor] = current;
                }
            }
        }
    }

    // finds the vertex with the lowest weight
    private int findMinWeight(Set<Integer> unvisitedVertices) {
        int minWeight = Integer.MAX_VALUE;
        int minVertex = -1;

        for (int vertex : unvisitedVertices) {
            if (weight[vertex] < minWeight) {
                minWeight = weight[vertex];
                minVertex = vertex;
            }
        }
        // returns vertex with the lowest weight
        return minVertex;
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
        Dijkstra graph = new Dijkstra(sourceVertex, numVertices);
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
        graph.dijkstraAlgorithm();
        // write the output to file
        graph.writeToFile();
    }
}
