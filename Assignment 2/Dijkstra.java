// An adjacency matrix method using a Set for vertices,and a two-dimensional array of Booleans to store the edges.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Dijkstra {
    private Set<Integer> vertices;
    private int[][] adjacencyMatrix;
    
    public Dijkstra() {
        vertices = new HashSet<>();
        adjacencyMatrix = new int[0][0];
    }

    public void addVertex(int vertex) {
        vertices.add(vertex);
        resizeMatrix();
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

    public void printGraph() {
        for (int vertex : vertices) {
            System.out.print("Vertex " + vertex + " is connected to: ");
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                if (adjacencyMatrix[vertex][i] != -1) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        Dijkstra graph = new Dijkstra();

        String file = "cop3503-asn2-input.txt";

        Scanner sc = new Scanner(new FileReader(file));

        String line;
        int numVertices = 0;
        int sourceVertex = 0;
        int numEdges = 0;
        
        if(parse
        numVertices = sc.;
        
        sourceVertex = sc.nextInt();
        
        numEdges = sc.nextInt();
            } 
            else {
                int vertex = Integer.parseInt(parts[0]);
                int destinationVertex = Integer.parseInt(parts[1]);
                int weight = Integer.parseInt(parts[2]);

                graph.addVertex(vertex);
                graph.addVertex(destinationVertex);
                graph.addEdge(vertex, destinationVertex, weight);
            }
        }
        reader.close();
        graph.printGraph();
    }
}
