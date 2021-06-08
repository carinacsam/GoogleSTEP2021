package com.company;
import java.io.*;
import java.util.*;


/**Graph class is used to find the shortest path
 */
class DijkstraGraph {
    //Stores page names and their IDs
    static Map<String, String> pages = new TreeMap<>();
    static Map<String, String> pagelookup = new TreeMap<>();
    //Stores vertexMap for the shortest path
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
    //Stores the graph with edges and weights for the shortest path
    static DijkstraGraph graph = new DijkstraGraph();
    //Used to initialize distance for Dijkstra algorithm
    public static final double INFINITY = Double.MAX_VALUE;

    /**Edge class used in the graph
     */
    class Edge {
        //Accesses the next vertex connected by edge for the graph
        public Vertex nextVertex;
        //Accesses weight of the edge for the graph
        public double weight;

        /**Edge constructor
         */
        public Edge(Vertex nextVertex, double edgeWeight) {
            this.nextVertex = nextVertex;
            this.weight = edgeWeight;
        }
    }

    /**Path class used in the priority queue
     */
    class Path implements Comparable<Path> {
        //Accesses the next vertex for the queue
        public Vertex nextVertex;
        //Access the weight of the vertex for the queue
        public double weight;

        /**Path constructor
         */
        public Path(Vertex vertex, double vertexweight) {
            this.nextVertex = vertex;
            this.weight = vertexweight;
        }

        /**Comparator is used to sort the priority queue based on weights
         */
        public int compareTo(Path otherVertex) {
            //Compares current vertex weight to weight of other vertex
            if (weight < otherVertex.weight)
                return -1; //Lower priority
            else
                return 1; //Higher priority
        }
    }

    /**Vertex class used in the shortest path.
     */
    class Vertex {
        public String vertexName; //Name of the vertex
        public List<Edge> adjacent; //List of adjacent vertices (aka links)
        public double distance; //Distance between vertices
        public Vertex previous; //Accesses previous vertex
        public int visited; //Tells us whether or not vertex has been visited

        /**Vertex constructor
         */
        public Vertex(String vertexName) {
            this.vertexName = vertexName;
            this.adjacent = new LinkedList<Edge>();
            reset();
        }

        /**Sets all th vertices to the same distances at the beginning
         */
        public void reset() {
            distance = DijkstraGraph.INFINITY; //Sets all distances to infinity
            previous = null; //Sets previous to null
            visited = 0; //No nodes have been visited yet
        }
    }

    /**Method used to add a new edge to the graph.
     */
    public void addEdge(String sourceName, String destName, double weight) {
        Vertex sourcevertex = getVertex(sourceName);
        Vertex destvertex = getVertex(destName);
        //Adds edge of destination vertex to source vertex's list of adjacent vertices
        sourcevertex.adjacent.add(new Edge(destvertex, weight));
    }

    /**If vertexName is not present, add it to vertexMap.
     */
    private Vertex getVertex(String vertexName) {
        Vertex vertex = vertexMap.get(vertexName);
        //If vertex is not present, create vertex object and add it to vertexMap
        if (vertex == null) {
            vertex = new Vertex(vertexName);
            vertexMap.put(vertexName, vertex);
        }
        return vertex; //Returns the vertex object
    }

    /**Prints shortest path from source to destination after execution of Dijkstra algorithm.
     */
    public void printShortestPath(String destinationName) {
        Vertex destinationVertex = vertexMap.get(destinationName);
        //If the destination doesn't exist, destination isn't found
        if (destinationVertex == null)
            System.out.println("Destination not found.");
            //If the destination hasn't been visited (thus, distance remains infinity), path to it isn't found
        else if (destinationVertex.distance == INFINITY)
            System.out.println("Path not found.");
        else {
            System.out.print("Distance: " + destinationVertex.distance + ", ");
            printPath(destinationVertex);
            System.out.println();
        }
    }

    /**Recursively prints path to destination after running shortest path algorithm.
     * Assumes that the path exists.
     */
    private void printPath(Vertex destinationVertex) {
        if (destinationVertex.previous != null) {
            printPath(destinationVertex.previous);
            System.out.print(" -> ");
        }
        System.out.print(pages.get(destinationVertex.vertexName));
    }

    /**Initializes the output vertex before running the Dijkstra shortest path algorithm.
     */
    private void clearAll() {
        for (Vertex vertex : vertexMap.values())
            vertex.reset();
    }

    /**Runs the Dijkstra single source shortest path algorithm.
     */
    public void dijkstra(String source) {
        PriorityQueue<Path> pq = new PriorityQueue<Path>();
        clearAll(); //Initializes the output vertex
        Vertex start = vertexMap.get(source); //Starting vertex is our source
        start.distance = 0; //Distance is initialized as 0 at first
        pq.offer(new Path(start, start.distance)); //Add the start to the priority queue

        while (!pq.isEmpty()) {
            Path shortestPath = pq.poll();
            Vertex vertex = shortestPath.nextVertex;

            //If the vertex has already been visited, move on to next iteration
            if (vertex.visited != 0)
                continue;

            //Mark vertex as visited
            vertex.visited = 1;

            //For each neighbor vertex in a vertex's adjacent list of vertices
            for (Edge neighbor : vertex.adjacent) {
                Vertex nextvertex = neighbor.nextVertex;

                //newDistance is the sum of the vertex's distance and its neighbor's weight
                double newDistance = vertex.distance + neighbor.weight;

                //If newDistance is less than the next vertex's distance
                if (newDistance < nextvertex.distance) {
                    nextvertex.distance = newDistance; //Next vertex's distance is set to the newDistance
                    nextvertex.previous = vertex; //The next vertex's previous is set to vertex
                    pq.offer(new Path(nextvertex, newDistance)); //New path is put into the priority queue
                }
            }
        }
    }


    //@source: Yuki Yamada-san's file-reading code
    public static void readFile() {
        try {
            //Read the pages.txt file.
            File pageFile = new File("data/pages.txt");
            Scanner pageReader = new Scanner(pageFile);
            while (pageReader.hasNextLine()) {
                String[] page = pageReader.nextLine().split("\t", 0);
                //Put in pages TreeMap key=page[0]:id, value=page[1]:page title
                pages.put(page[0], page[1]);
                //Put in pagelookup TreeMap key=page[1]:page title, value = page[0]:id
                pagelookup.put(page[1], page[0]);
            }
            pageReader.close();

            //Read the links.txt file.
            //Put in graph and add the edges where weight = 1
            File linkFile = new File("data/links.txt");
            Scanner linkReader = new Scanner(linkFile);
            while (linkReader.hasNextLine()) {
                String[] link = linkReader.nextLine().split("\t", 0);
                graph.addEdge(link[0],link[1],1);
            }
            linkReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }
    }

    public static void main(String[] args) {
        readFile();
        String source = pagelookup.get("Google");
        String destination = pagelookup.get("渋谷");
        System.out.printf("Shortest Path from Google to 渋谷: ");
        graph.dijkstra(source);
        graph.printShortestPath(destination);
    }
}