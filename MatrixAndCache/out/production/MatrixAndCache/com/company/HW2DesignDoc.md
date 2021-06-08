# Google STEP Homework 2 Design Document
*Carina Samson*
_______
<u>**Q1. Matrix Multiplication**</u>
-------
### a. Matrix Class
*Variables*

- `double [][] a` - input matrix A
- `double [][] b` - input matrix B
- `double [][] c` - resulting matrix C after matrix multiplication
- `sum` - sum of numbers in resulting matrix
- `begin` - start time in milliseconds
- `end` - end time in milliseconds
- `executiontime` - time it takes for the matrix multiplication program to run in milliseconds
- `sum` - sum of the matrix

*Methods*
- `matrixMultiply()`
    - Domain Parameter(s): int n (nxn matrix dimension)
    - Range: returns float value execution time
    - Performs the steps outlined in the algorithm (initializes three matrices, performs matrix multiplication, calculates sum of resulting matrix c and calculates returns execution time)

*Algorithm*
1. Initialize matrices a, b and c to some values (a and b are the matrices being multiplied, while c is the product matrix).
2. Get the start time in milliseconds.
3. Multiply matrix a with matrix b and return result in c
   ``` 
   for i from 0 to n: //Loops through row dimension n
      for j from 0 to n:  //Loops through column dimension n
         set c[i][j] = 0 //Initializes the sum
         for k from 0 to n: //Loops through n dimension again for computation
           set c[i][j] to the sum + a[i][k] × b[k][j] //Performs matrix multiplication
       return c

4. Get the end time in milliseconds.
5. Compute the sum of the resulting matrix c.
6. Print out the input, execution time (calculated by end time - start time) and sum to check results.
7. Returns the execution time.

### b. Main Class (uses PlotPoints Class for Graphing)

*Variables*
- `int n` = input number for the nxn matrix
- `double [] x` = array to store the input number for plotting the graph
- `double [] y` = array to store the execution time for plotting the graph

*Algorithm*
1. Call matrixMultiply() for n = 1 to 500
2. Store the input number(n) and execution time in the arrays
3. Plot the points in the graph using the arrays where x = execution time and y = input number(n)
4. Initialize frame, set frame size width and height, location
5. Initialize mainPane to new PlotPoints class with input x, y arrays
6. Set Content Pane to mainPane
7. Set frame visible = true
9. Paint the background
10. Cast graphics object to Graphics 2D
11. Define the corners of the plot area in pixels
12. Define the scaling between the plot area in x-y and the plot area in pixels
13. Draw the x-axis and y-axis
14. Draw the points for each x,y coordinate in the array
16. Add the labels for both axes
17. Add the labels for xMin, xMax, yMin, yMax to drawing

#### Output Graph Display
https://ibb.co/684cRs9
_______

<u>**Q2. Binary Trees vs. Hash Tables**</u>
-------
*The complexity of searching / adding / removing an element is mostly O(1) with a hash table, whereas the complexity is O(log N) with a tree. This means that a hash table is more efficient than a tree. However, real-world large-scale database systems tend to prefer a tree to a hash table. Why? List as many reasons as possible.*

### Answer:
1. Trees allow us to efficiently access all sorts of values by keeping them ordered, and thus, we can perform a wider variety of operations compared to a hashtable.
2. When it comes to single value lookups, hash tables are much faster, however, as the data set gets larger, the time it takes to perform the occasional O(n) time searches adds up and become much slower compared to trees.
3. When dealing with databases, adding another hashtable index costs O(n) storage and O(n) time to build, which could take up to hours or days. The advantage of having ordered indexes, which we can achieve with a trees, allows us to shorten our run time and optimize storage. One way we can do this is by creating an index with more than one column that we can access instead of having to create separate indexes in a hashtable.
4. Data storage on disk in a way that it won't be corrupt or lost even if the machine crashes requires carefully ordered writes, write-ahead logging, and may even need fine-grained concurrency control. This would require a lot of code if we used various data structures. Thus, many databases only support a single type of persistent index, and in this case, it is best to use trees.
5. Compared to trees, hash tables are not scaleable. B-Tree indexes can be kept at optimal height to minimize the number of I/Os and B-Tree indexes don't really have to worry about the scalability problem that hash indexes have.
-------
<u>**Q3. Cache Implementation**</u>
-------
### a. Cache Class
*Variables*
- `Node sentinel` - each node stores the passed in url
- `int size` - size of the cache
- `int capacity` - maximum capacity of the cache that is set when initializing it
- `HashMap<String, Node> hashMap` - stores the url and webPage contents (allows for 0(1) access)

*Methods*
- `Cache()` - constructor
    - Domain Parameter(s): int capacity (initializes capacity of the cache)
    - Initializes the cache and its hashMap used for access, size (0), and the sentinel node and its "next" and "prev" pointers
- `accessPage()`
    - Domain Parameter(s): String url, String webPage (both passed into the cache)
    - Range: returns void
    - Checks if the url and webPage are already in the cache
        - If they aren't, a new node is created with this website information and put into the hashMap for reference
            - If the size of the cache is still less than the capacity (aka there is still space in the cache), the new node with the website information is inserted into the 'head' of the cache, increasing its size by 1
            - If the size of the cache is greater or equal to the capacity (aka there is no more space), the least recently accessed page is removed from the hashMap and its node is removed from the circular linked list cache, and then the new node is inserted into the 'head' of the cache
        - If they are, the existing node that carries the passed in website information is retrieved from the hashMap, removed from the circular linked list cache and then reinserted at the 'head' of the cache
    - `isInCache()`
        - Domain Parameter(s): String url, String webPage
        - Range: returns boolean true or false depending on whether or not they exist in the cache
        - Checks if the cache contains the passed-in url via the hashMap, and if the url's corresponding value stored in the hashMap is equal to the passed-in webPage contents
    - `insertNode()`
        - Domain Parameter(s): Node node
        - Range: returns void
        - Inserts the node at the 'head' of the cache (node after the sentinel node) by reassigning "next" and "prev" pointers
    - `removeNode()`
        - Domain Parameter(s): Node node
        - Range: returns void
        - Removes the least recently accessed website (node at the 'tail' of the cache and before the sentinel node) by reassigning "next" and "prev" pointers
- `getPages()`
    - Domain Parameter(s): nothing
    - Range: returns a String array list of urls that exist in the cache in order of most recently accessed
    - Loops throught the circular linked list starting at the node after the sentinel and ending at the node before the sentinel, putting each node's url into the array list, ultimately returning the list

*Algorithm*
1. A cache is initialized with a user-inputted size.
2. A website URL and its Web page contents are passed into the cache.
3. Program checks if the URL and Web page contents are already in the cache.
    1. If they already exist in the cache, the page is moved from its original place to the 'head' of the cache as the most recently accessed page, reordering the other pages in the cache accordingly.
    2. If they don't already exist in the cache:
        1. If the cache is at maximum capacity, the least recently accessed page is deleted before the new page is inserted at the 'head' of the cache.
        2. If the cache isn't at maximum capacity yet, the page is inserted into the 'head' of the cache.
4. The contents inside the cache can be printed and displayed from the URL of the most recently accessed page to the URL of the least recently accessed page.

### b. Node Class
*Variables*
- `String url` - each node stores the passed in url
- `String webPage` - each node stores the webPage contents associated with the url
- `Node prev` - references the node that comes before the current node
- `Node next` - references the node that comes after the current node

*Methods*
- `Node()` - constructor
    - Domain Parameter(s): String url and String webPage
    - Creates Node object that stores the url and webPage contents that are passed into the cache