import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dijkstra {
    private Set<Integer> vertices;
    private int[][] adjacencyMatrix;
    public int sourceVertex;
    public int numVertices;


    public Dijkstra(int sourceVertex, int numVertices) {
        this.sourceVertex = sourceVertex;
        this.numVertices = numVertices;
        vertices = new HashSet<>();
        adjacencyMatrix = new int[numVertices + 1][numVertices + 1];
        
        for(int [] arr : adjacencyMatrix){
            Arrays.fill(arr, 0);
        }
    }

    public void addVertex(int vertex) {
        vertices.add(vertex);
    }

    public void addEdge(int source, int destination, int weight) {
        if (!vertices.contains(source))
            vertices.add(source);
            
        if(!vertices.contains(destination)){
            vertices.add(destination);
        }
            
        adjacencyMatrix[source][destination] = weight;
        adjacencyMatrix[destination][source] = weight;
    }

    public void resizeMatrix() {
        int size = vertices.size();
        int[][] newMatrix = new int[size][size];

        for (int i = 0; i < adjacencyMatrix.length; i++) {
            for (int j = 0; j < adjacencyMatrix[i].length; j++) {
                newMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }

        adjacencyMatrix = newMatrix;
    }

    
    public void dijkstraAlgorithm() {
        int[] distance = new int[numVertices + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
    
        int[] parent = new int[numVertices + 1];
        Arrays.fill(parent, -1);
    
        Set<Integer> visited = new HashSet<>();
    
        distance[sourceVertex] = 0;
    
        while (visited.size() < vertices.size()) {
            int currentVertex = -1;
            int minDistance = Integer.MAX_VALUE;
    
            for (int vertex : vertices) {
                if (!visited.contains(vertex) && distance[vertex] < minDistance) {
                    currentVertex = vertex;
                    minDistance = distance[vertex];
                }
            }
            
            visited.add(currentVertex);
    
            for (int neighbor : vertices) {
                if (!visited.contains(neighbor) && adjacencyMatrix[currentVertex][neighbor] != 0) {
                    int newDistance = distance[currentVertex] + adjacencyMatrix[currentVertex][neighbor];
                    if (newDistance < distance[neighbor]) {
                        distance[neighbor] = newDistance;
                        parent[neighbor] = currentVertex;
                    }
                }
            }
        }
        
        distance[sourceVertex] = -1;
        System.out.println(numVertices);
        for (int vertex : vertices)
            System.out.println(vertex + " " +distance[vertex]+ " " + parent[vertex]);
    }
    
    public static void main(String[] args) throws IOException {

        String file = "cop3503-asn2-input.txt";

        Scanner sc = new Scanner(new FileReader(file));
        
        List<Integer> input  = new ArrayList<>();
        
        int vertex = -1;
        int destinationVertex = -1;
        int weight = -1;
        
        while(sc.hasNext()){
        if (sc.hasNextInt())
            input.add(sc.nextInt());
        else
            sc.next();
        }        
        
        int i = 0;
        int numVertices = input.get(i++);
        int sourceVertex = input.get(i++);
        int numEdges = input.get(i++);
        
        Dijkstra graph = new Dijkstra(sourceVertex, numVertices);
        
        while (input.size() > i) {
            vertex = input.get(i++);
            destinationVertex = input.get(i++);
            weight = input.get(i++);
            graph.addVertex(vertex);
            graph.addVertex(destinationVertex);
            graph.addEdge(vertex, destinationVertex, weight);
        }

        sc.close();
        graph.dijkstraAlgorithm();

    }
}
