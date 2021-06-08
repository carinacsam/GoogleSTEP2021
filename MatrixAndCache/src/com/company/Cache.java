package com.company;
import java.util.ArrayList;
import java.util.HashMap;

/** Cache was implemented using a combination of a circular linked list and a hashmap */
public class Cache {

    /** Node class creates node objects that are stored inside the circular linked list */
    private class Node {
        String url;
        String webPage;
        Node prev; //References the node that comes before the current node
        Node next; //References the node that comes after the current node

        //A Node object stores the website's url (key) and the webPage contents (value)
        public Node(String url, String webPage) {
            this.url = url;
            this.webPage = webPage;
        }
    }

    private Node sentinel; //A node
    private int size; //Size of the cache
    private int capacity; //Maximum capacity of the cache (set when initializing)
    private HashMap<String, Node> hashMap; //Stores url and webPage contents for O(1) access

    /** Initializes the cache */
    public Cache(int capacity) {
        this.capacity = capacity; //Capacity of the cache is set
        hashMap = new HashMap<>(); //Used to reference url and webpage in O(1) time
        sentinel = new Node(null, null); //Sentinel node is set
        sentinel.next = sentinel; //Initializes pointer that will point to the node with the most recently accessed page
        sentinel.prev = sentinel; //Initializes pointer that will point to the node with the least recently accessed page
        size = 0;
    }

    /** Accesses pages by inserting and removing nodes (website information) from the cache in the following manners:
     * If the url and its webPage don't exist in the cache:
        * If the size of the cache is less than the capacity, a new node is created with the website information
          and inserted at the "head" of the cache as the most recently accessed website
        * If the size of the cache is greater than the capacity, the least recently accessed website is removed from
          the cache and the new node is inserted into the "head" of the cache as the most recently accessed website
     * If the url and its webPage are already exists in the cache, then the node that corresponds to the website
       information, removes it from its place in the cache and then reinserts it at the "head" of the cache.
     * */
    public void accessPage(String url, String webPage) {
        if (!isInCache(url, webPage)) { //If the url and webPage aren't already in the cache
            Node newNode = new Node(url, webPage); //New node is created with url and webPage contents
            hashMap.put(url, newNode); //Url and node are put into a hashMap for reference as its key and value respectively
            if (size < capacity) { //If size is less than capacity (aka there is still space in cache)
                insertNode(newNode); //New node is inserted into the "head" of the cache
                size += 1; //Size is increased by 1
            } else { //If size is greater or equal to capacity (aka there is no more space)
                hashMap.remove(sentinel.prev.url); //Remove least recently accessed page from hashMap reference
                removeNode(sentinel.prev); //Least recently accessed page's node (node behind the sentinel) is removed
                insertNode(newNode); //New node is inserted into "head" of the cache
            }
        } else { //If the url and webPage are in the cache
            Node node = hashMap.get(url); //Retrieve the node from the hashMap via its url
            removeNode(node); //Remove the node from where it is in the linked list
            insertNode(node); //Reinsert the node at the "head" of the cache
        }
    }

    /** Checks if url and webPage are in the cache */
    private boolean isInCache(String url, String webPage) {
        //Checks if the cache has the url via the hashMap, and if its corresponding value equals to its webPage content.
        return hashMap.containsKey(url) && (hashMap.get(url).webPage).equals(webPage);
    }

    /** Inserts node to the "head" of the cache as most recently accessed page by resetting pointers accordingly */
    private void insertNode(Node node) {
        node.next = sentinel.next; //Node's "next" pointer is set to the node that's after the sentinel
        node.prev = sentinel; //Node's "prev" pointer is set to the sentinel node
        sentinel.next.prev = node; //"Prev" pointer of the node that's currently after the sentinel is set to the node
        sentinel.next = node; //The sentinel's "next" pointer is set to the node that's passed in
    }

    /** Removes the least recently accessed page's node from the linked list by resetting pointers accordingly */
    private void removeNode(Node node) {
        if (size != 0) { //Remove only works if the size isn't 0 (to avoid null pointer exceptions)
            node.prev.next = node.next; //Set the node before the current node's "next" pointer to the node after the current node
            node.next.prev = node.prev; //Set the node after the current node's "prev" pointer to the node before the current node
        }
    }

    /** Creates an array list of all the urls that exist in the cache
     * Ordered by most recently accessed to least recently accessed
     * */
    public ArrayList<String> getPages() {
        Node currNode = sentinel.next; //Starts at the node after the sentinel (most recently accessed)
        ArrayList<String> urls = new ArrayList<>();
        while (currNode != sentinel) { //Ends at the node before the sentinel (least recently accessed)
            urls.add(currNode.url); //Adds url to the array list "urls"
            currNode = currNode.next;
        }
        return urls;
    }

    /** Testing using Haraken's test cases */
    public static void main(String[] args) {
        Cache cache = new Cache(4);
        System.out.println(cache.getPages()); //Should print []
        cache.accessPage("a.com", "AAA");
        System.out.println(cache.getPages()); //Should print ["a.com"]
        cache.accessPage("b.com", "BBB");
        System.out.println(cache.getPages()); //Should print ["b.com", "a.com"]
        cache.accessPage("c.com", "CCC");
        System.out.println(cache.getPages()); //Should print ["c.com", "b.com", "a.com"]
        cache.accessPage("d.com", "DDD");
        System.out.println(cache.getPages()); //Should print ["d.com", "c.com", "b.com", "a.com"]
        cache.accessPage("d.com", "DDD");
        System.out.println(cache.getPages()); //Should print ["d.com", "c.com", "b.com", "a.com"]
        cache.accessPage("a.com", "AAA");
        System.out.println(cache.getPages()); //Should print ["a.com", "d.com", "c.com", "b.com"]
        cache.accessPage("c.com", "CCC");
        System.out.println(cache.getPages()); //Should print ["c.com", "a.com", "d.com", "b.com"]
        cache.accessPage("a.com", "AAA");
        System.out.println(cache.getPages()); //Should print ["a.com", "c.com", "d.com", "b.com"]
        cache.accessPage("a.com", "AAA");
        System.out.println(cache.getPages()); //Should print ["a.com", "c.com", "d.com", "b.com"]
        cache.accessPage("e.com", "EEE");
        System.out.println(cache.getPages()); //Should print ["e.com", "a.com", "c.com", "d.com"]
        cache.accessPage("f.com", "FFF");
        System.out.println(cache.getPages()); //Should print ["f.com", "e.com", "a.com", "c.com"]
        cache.accessPage("e.com", "EEE");
        System.out.println(cache.getPages()); //Should print ["e.com", "f.com", "a.com", "c.com"]
        cache.accessPage("a.com", "AAA");
        System.out.println(cache.getPages()); //Should print ["a.com", "e.com", "f.com", "c.com"]
        System.out.println("OK!");
    }
}
