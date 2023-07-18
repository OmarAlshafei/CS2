// Omar Alshafei
// Assignment 2

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// graph class
public class Dijkstra {
    private Set<Integer> vertices;
    private int[][] adjacencyMatrix;
    public int sourceVertex;
    public int numVertices;
    // graph constructor
    public Dijkstra(int sourceVertex, int numVertices) {
        this.sourceVertex = sourceVertex;
        this.numVertices = numVertices;
        vertices = new HashSet<>();
        adjacencyMatrix = new int[numVertices + 1][numVertices + 1];
        
        for(int [] arr : adjacencyMatrix){
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
            
        if(!vertices.contains(destination)){
            vertices.add(destination);
        }
        // set edge weight
        adjacencyMatrix[source][destination] = weight;
        adjacencyMatrix[destination][source] = weight;
    }
    
    public void visitVerticies(Set<Integer> visitedVerticies, int[] distance, int[] parent){
        // loop until all the verticies have been visited
        while (visitedVerticies.size() < vertices.size()) {
            int curVertex = -1;
            int minDistance = Integer.MAX_VALUE;
    
            for (int vertex : vertices) {
                if (!visitedVerticies.contains(vertex) && distance[vertex] < minDistance) {
                    curVertex = vertex;
                    minDistance = distance[vertex];
                }
            }
            // set the current vertex as visited
            visitedVerticies.add(curVertex);
            // set distances of the neighbors of the current vertex
            for (int neighbor : vertices) {
                if (!visitedVerticies.contains(neighbor) && adjacencyMatrix[curVertex][neighbor] != 0) {
                    // calculate the new distance
                    int newDistance = distance[curVertex] + adjacencyMatrix[curVertex][neighbor];
                    // if the new distance is smaller then the neighbor's distance
                    if (newDistance < distance[neighbor]) {
                        // update the neighbor's distance
                        distance[neighbor] = newDistance;
                        // set current vertex as the parent
                        parent[neighbor] = curVertex;
                    }
                }
            }
        }
    }
    
    // perform dijkstra's algorithm
    public void dijkstraAlgorithm() {
        // initialize the distance array
        int[] distance = new int[numVertices + 1];
        // set the distance to max
        Arrays.fill(distance, Integer.MAX_VALUE);
        // initalize the parent array
        int[] parent = new int[numVertices + 1];
        // set parents to -1
        Arrays.fill(parent, -1);
        // create a set to track visited verticies
        Set<Integer> visitedVerticies = new HashSet<>();
        // set source vertex distance to 0
        distance[sourceVertex] = 0;
    
        // call method to visit all verticies
        visitVerticies(visitedVerticies, distance, parent);
        
        distance[sourceVertex] = -1;
        System.out.println(numVertices);
        // print out the vertex, its distance, and its parent
        for (int vertex : vertices)
            System.out.println(vertex + " " +distance[vertex]+ " " + parent[vertex]);
    }
    // driver method
    public static void main(String[] args) throws IOException {
        // input file
        String file = "cop3503-asn2-input.txt";
        // open scanner
        Scanner sc = new Scanner(new FileReader(file));
        // create list to store input from the file
        List<Integer> input  = new ArrayList<>();
        
        int vertex = -1;
        int destinationVertex = -1;
        int weight = -1;
        
        // scan in and store the input from the file
        while(sc.hasNext()){
        if(sc.hasNext("#"))
            sc.nextLine();
        if (sc.hasNextInt())
            input.add(sc.nextInt());
        else
            sc.next();
        }        
        
        int i = 0;
        // store the number of verticies
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
        while (!input.isEmpty()){
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
        // call method to perform dijksta's algorithm on the graph
        graph.dijkstraAlgorithm();

    }
}
