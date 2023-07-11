
import java.util.*;
import java.lang.*; 
import java.io.*; 
public class Dijkstra {
    private Set<Integer> vertices;
    private Map<VertexInfo, Boolean> adjacencyMatrix;

    public Dijkstra() {
        vertices = new HashSet<>();
        adjacencyMatrix = new HashMap<>();
    }

    public void addVertex(int vertex) {
        vertices.add(vertex);
    }

    public void addEdge(int source, int destination, int weight) {
        if (!vertices.contains(source) || !vertices.contains(destination))
            throw new IllegalArgumentException("One or more vertices do not exist.");

        VertexInfo edge = new VertexInfo(source, destination, weight);
        adjacencyMatrix.put(edge, true);
    }

    public boolean hasEdge(int source, int destination, int weight) {
        VertexInfo edge = new VertexInfo(source, destination, weight);
        return adjacencyMatrix.getOrDefault(edge, false);
    }

    public void printGraph() {
        for (int vertex : vertices) {
            for (VertexInfo edge : adjacencyMatrix.keySet()) {
                if (edge.source == vertex) {
                    System.out.println(vertex + " " + edge.destination + " " + edge.weight + "");
                }
            }
        }
    }

    private class VertexInfo {
        private int source;
        private int destination;
        private int weight;
        private int parent = -1;
        private int defaultWeight = 999999;
        
        public VertexInfo(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;

            VertexInfo other = (VertexInfo) obj;
            return (source == other.source && destination == other.destination && weight == other.weight)
                    || (source == other.destination && destination == other.source);
        }

        @Override
        public int hashCode() {
            return Objects.hash(source, destination, weight) + Objects.hash(destination, source, weight);
        }
    }

    public static void main(String[] args) throws IOException {
        Dijkstra graph = new Dijkstra();

        String file = "cop3503-asn2-input.txt";

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        int numVertices = 0;
        int sourceVertex = 0;
        int numEdges = 0;

        // Read the file line by line
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            String[] parts = line.split(" ");

            if (numVertices == 0) {
                numVertices = Integer.parseInt(parts[0]);
            } 
            else if (sourceVertex == 0) {
                sourceVertex = Integer.parseInt(parts[0]);
            } 
            else if (numEdges == 0) {
                numEdges = Integer.parseInt(parts[0]);
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
