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

            // Skip empty lines and comments
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Split the line by whitespace
            String[] parts = line.split("\\s+");

            if (numVertices == 0) {
                numVertices = Integer.parseInt(parts[0]);
            } else if (sourceVertex == 0) {
                sourceVertex = Integer.parseInt(parts[0]);
            } else if (numEdges == 0) {
                numEdges = Integer.parseInt(parts[0]);
            } else {
                // Process the edge information
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

    // Rest of the Dijkstra class implementation...
}