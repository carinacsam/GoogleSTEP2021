package com.company;
import java.io.*;
import java.util.*;

public class WikipediaBFS {

    //@source: Yuki Yamada-san's code
    static Map<String, String> pages = new TreeMap<>(); //Stores node labels (page) and their IDs
    static Map<String, Set<String>> links = new TreeMap<>(); //Stores node's ID and the IDs of their neighbor nodes

    //@source: Yuki Yamada-san's file-reading code
    public static void readFile() {
        try {
            //Read the pages.txt file.
            //Put it in tree map with key = ID and value = page name
            File pageFile = new File("data/pages.txt");
            Scanner pageReader = new Scanner(pageFile);
            while (pageReader.hasNextLine()) {
                String[] page = pageReader.nextLine().split("\t", 0);
                // page[0]: id, page[1]: title
                pages.put(page[0], page[1]);
            }
            pageReader.close();

            //Read the links.txt file.
            //Put it in tree map with key = ID and value = page's adjacent connected nodes
            File linkFile = new File("data/links.txt");
            Scanner linkReader = new Scanner(linkFile);
            while (linkReader.hasNextLine()) {
                String[] link = linkReader.nextLine().split("\t", 0);
                // link[0]: id (from), links[1]: id (to)
                if (!links.containsKey(link[0])) {
                    links.put(link[0], new TreeSet<String>());
                }
                links.get(link[0]).add(link[1]);
            }
            linkReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }
    }

    /**Returns any page's ID (used to get the source and destination pages)
     *
     * @param pageName - name of the page
     * @return page's ID
     */
    public static String getpageID(String pageName) {
        String ID = "Does not exist.";
        for (Map.Entry<String, String> page : pages.entrySet()) {
            if (page.getValue().equals(pageName)) {
                ID = page.getKey();
                break;
            }
        }
        return ID; //Return pageID
    }

    /**Uses BFS to find the shortest path from the source to its destination
     *
     * @param source - starting page
     * @param destination - ending page
     * @return
     */
    public static void bfsPath(String source, String destination) {
        //Stores the visited parent of each page node
        // e.g. visited.put(a, b) means that to get to vertex a, you must visit vertex b beforehand.
        Map<String, String> visited = new TreeMap<String, String>();

        //Used to keep track of BFS traversal
        Queue<String> queue = new LinkedList<>();
        boolean connected = false;

        //Initialize the source and set it to -1 to indicate that it has no visited parents.
        queue.offer(source);
        visited.put(source, "-1");

        while (!queue.isEmpty()) {

            //Remove the last item in the queue (should be source in the first step) and
            String nextVertex = queue.poll();

            //If the nextVertex is equal to destination, path is found and break from loop
            if (nextVertex.equals(destination)) {
                connected = true;
                break;
            }

            //If the nextVertex doesn't have neighbors, jump to the next iteration of the loop
            if (links.get(nextVertex) == null) {
                continue;
            }

            //Search through neighbors indicated by links TreeMap
            for (String neighbor : links.get(nextVertex)) {
                //If neighbor hasn't been visited yet, add the neighbor to the queue and visited TreeMap
                if (!visited.containsKey(neighbor)) {
                    queue.offer(neighbor);
                    visited.put(neighbor, nextVertex);
                }
            }
        }
        //If the source and destination have the path, print it
        if (connected) {
            printPath(visited, source, destination);
        }
        //Otherwise, indicate that the path wasn't found.
        else {
            System.out.println("No Path Found.");
        }
    }

    /**Prints the path from the start to the destination, as well as the number of links in the shortest path
     *
     * @param visited - nodes that were visited from most recent to least recent
     * @param source - starting page
     * @param destination - ending page
     * @return
     */
    public static void printPath(Map <String, String> visited, String source, String destination) {

        //First add the destination to the path.
        List<String> path = new ArrayList<>();
        path.add(destination);

        //From the destination backtrack towards the source using the visited TreeMap by adding nodes to the path list.
        String previousVertex = destination;
        while (previousVertex != source) {
            previousVertex = visited.get(previousVertex);
            path.add(previousVertex);
        }

        //Print the path backwards since it was stored in order from most recent to least recently visited.
        for (int i = path.size() - 1; i >= 0; --i) {
            String ID = path.get(i);
            System.out.print(pages.get(ID));
            if (i > 0) {
                System.out.print(" -> ");
            }
        }
        System.out.printf(", Distance: %d %n", path.size()-1);
    }

    public static void main(String[] args) {
        readFile();
        //Testing BFS shortest path algorithm.
        System.out.print("Shortest Path from Google to 渋谷: ");
        bfsPath(getpageID("Google"),getpageID("渋谷"));
        System.out.print("Shortest Path from Google to ラーメン: ");
        bfsPath(getpageID("Google"),getpageID("ラーメン"));
        System.out.print("Shortest Path from Google to Android: ");
        bfsPath(getpageID("Google"),getpageID("Android"));
        System.out.print("Shortest Path from Google to キットカット: ");
        bfsPath(getpageID("Google"),getpageID("キットカット"));
        System.out.print("Shortest Path from Google to アイスクリーム: ");
        bfsPath(getpageID("Google"),getpageID("アイスクリーム"));
        System.out.print("Shortest Path from Google to 東京: ");
        bfsPath(getpageID("Google"),getpageID("東京"));
    }
}