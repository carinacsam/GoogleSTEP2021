# Google STEP Homework 4 Design Document
*Carina Samson*
_______
<u>**How To Run My Java Program**</u>

*From IDE (specifically IntelliJ IDEA CE):*
1. Open Settings/Preferences Ctrl+Alt+S , select Build, Execution, Deployment | Compiler, and specify the necessary amount of memory = 5000 in the Build process heap size field.
2. From the main menu, select Help | Change Memory Settings set to 5000.
3. Download wikipedia_data.zip and extract data files to working directory.
4. Select Run WikipediaBFS or DijkstraGraph to run program.

*From Terminal:*

BFS program:

`javac WikipediaBFS.java`

`java -Xmx5000M WikipediaBFS.java`

Dijkstra program:

`javac DijkstraGraph.java`

`java -Xmx5000M DijkstraGraph.java`

-------
<u>**Wikipedia BFS**</u>
-------
*Algorithm*
1. Read large input files (also works for small input files pages_small.txt and links_small.txt)
    1. Read pages.txt and put key = ID and value = page name in the TreeMap called "pages"
    2. Read links.txt and put key = ID and value = page's links (or adjacent connected nodes) in the TreeMap called "links"
2. Get source ID and destination IDs from the pages TreeMap
3. Call bfsPath() on source ID to destination ID to find the shortest path between the two
    1. Store the visited parent of each node in the "visited" TreeMap. When we add nodes a and b via visited.put(a,b), we indicate that in order to get to vertex a, you must have visited vertex b right before.
    2. Create a queue for the BFS traversal
4. Initialize the source as the first visited node and enqueue it
5. While queue is not empty, iterate as follows:
    1. Remove the item from queue and assign it to nextVertex
    2. If nextVertex = destination then path is found. Set connected = true and then break from loop
    3. Iterate for all neighbors of nextVertex in the links TreeMap
        1. If neighbor has not been visited yet, add neighbor to queue
        2. Add the neighbor and nextVertex in the visited TreeMap (visited.put(neighbor, nextVertex))
4. If source and destination are connected, then print shortest path between the two
    1. Create a new ArrayList to store the path
        1. From the destination backtrack towards the source using the visited TreeMap by adding nodes to the path list.
    2. Add destination to path first
    3. Set previousVertex = destination and then iterate until source is reached
        1. Traverse by setting previousVertex to it's parent node and then add it to the path
    4. Print the path backwards since it was stored in order from most recent to least recently visited.
        1. For each item in path, get the corresponding key value using get(ID) from the pages TreeMap
        2. Print the name of the page with an arrow in between them

*Variables*
- `Map<String, String> pages` - stores node labels (page) and their IDs
- `Map<String, String> links` - stores node's ID and the IDs of their neighbor nodes
- `Map<String, String> visited` - stores the visited parent of each page node
- `Queue<String> queue`- used to keep track of BFS traversal

*Methods*
- `readFile()`
    - Domain Parameter(s): Nothing
    - Range: Return void
    - Reads file and put pages and their IDs, as well as page IDs and the IDs of neighboring pages into two different TreeMaps
- `bfsPath()`
    - Domain Parameter(s): source (starting page), destinaion (ending page)
    - Range: Return void
    - Uses BFS to find the shortest path from the source to its destination
    - `getID()`
        - Domain Parameter(s): pageName (name of the page)
        - Range: Return page's ID
        - Returns any page's ID (used to get the source and destination pages)
- `printPath()`
    - Domain Parameter(s): visited (TreeMap of nodes that were visited from most recent to least recent), source (starting page), destinaion (ending page)
    - Range: Return void
    - Prints the path from the start to the destination, as well as the number of links in the shortest path
-------
<u>**DijkstraGraph**</u>
-------
*Algorithm*
1. Read large input files (also works for small input files pages_small.txt and links_small.txt)
    1. Read pages.txt and put key = ID and value = page name in the TreeMap called "pages"
    2. Create another TreeMap for page lookup and put key = page name and value = ID
    3. Read links.txt and add edges in the graph, marked by the sourceVertex and destVertex, all with a weight of 1.
2. Get source ID and destination IDs from the pagelookup TreeMap
3. Call djikstra() on the source ID to find the shortest path
    1. Create vertex priority queue
    2. Call clearall() to initialize distance from source to destination = infinity(unknown) and previous vertex = null()
    3. Initialize, set start vertex = source and distance = 0
    4. Add the start vertex to priority queue
    5. While priority queue is not empty, iterate as follows:
        1. Remove the vertex from priority queue
        2. Iterate for all unvisited neighbors of the vertex
            1. Get all the unvisited neighbours and calculate their distances to current vertex.
            2. Compare the newly calculated distance to the current assigned value and assign the smaller one.
            3. Search for the  best vertex with the shortest distance and add to priority queue
4. Call printShortPath() on the destination ID to print the shortest path
    1. Print the total distance from source to destination
    2. Call recursive function printPath to print the shortest path from source to destination

