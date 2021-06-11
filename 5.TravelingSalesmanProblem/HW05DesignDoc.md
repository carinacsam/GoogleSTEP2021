# Google STEP Homework 5 Design Document
*Carina Samson*

-------
<u>**Traveling Salesman Problem**</u>
-------
*Algorithm*
1. Read the input file from command line terminal
   1. Store the x,y coordinates from the input file to cities
2. Call solve_tsp() to get a random start city.
   1. Iterate until the best_tour with the shortest distance is found
3. Execute the Greedy Algorithm using the method solve_greedy()
   1. Calculate the Euclidean distances between cities
   2. Set current_city equal to start_city
   3. Create a set called unvisited_cities to track all the unvisited cities, starting with start city
   4. Remove the first city from unvisited_cities, indicating that we've visited it
   5. Add current_city to the tour
   6. Iterate until there are no more unvisited cities
        1. Find the nearest city with the minimum distance from the current city and store it as next_city
        2. Add next_city to the tour
        3. Set current_city = next_city to iterate
    7. Return the resulting tour
4. Execute the 3-Opt algorithm using the method solve_3opt() to improve tour
    1. Refer to the following diagram, which shows the different 3-Opt cases (a, b and c refer to city1, city2 and city3 in my code): [3-Opt Diagram](https://ibb.co/1nBSMPZ)
    2. Iterate for all possible combinations of three edges in the given tour
        1. Call swap_3opt() for all combination of the three segments 
        2. Create a subset of tour with segments 1-2, 3-4 and 5-6
        3. Calculate the distances for the subtours 
        4. Swap the segments by comparing the distances of the subtours 
        5. Reverse the segments for cases 4-7 only
        6. Case 1-3 will be completed by the 2-opt swap function
5. Execute the 2-Opt algorithm using the method solve_2opt() to further improve tour
    1. Refer to the following diagram, showing how 2-Opt works: [2-Opt Diagram](https://ibb.co/8Mk4rJD)
    2. Set best_tour as our previously created tour and best_distance as the computed total distance of our tour
        1. Find a segment that crosses over itself
        2. Reorder the segment so that it does not cross
        3. Calculate the new distance of the tour after the swap
    3. Repeat until no improvements can be made
    4. If new tour distance after swap is less than the distance of the given tour then return new tour
6. Return the newly optimized tour
7. Print the shortest tour and its distance
8. Save the shortest tour as an ordered list of the index numbers of cities to the corresponding output file

*Variables*
- `input_file` - name of the input file
- `cities` - tuple consisting of x, y coordinate points
- `tour` - used to keep track of the TSP tour
- `shortest_tour` - the resulting shortest TSP tour
- `shortest_distance`-  the shortest_distance of the TSP tour

*Methods*
- `solve_tsp_tour()`
  - Domain Parameter(s): cities
  - Range: shortest_tour, shortest_distance

- `solve_greedy()`
  - Domain Parameter(s): start_city, cities
  - Range: tour

- `solve_3opt()`
  - Domain Parameter(s): tour, cities
  - Range: best_tour

- `swap_3opt()`
  - Domain Parameter(s): tour, cities, city1, city2, city3
  - Range: new_tour

- `solve_2opt()`
  - Domain Parameter(s): tour, cities
  - Range: best_tour

- `swap_2opt()`
  - Domain Parameter(s): tour, city1, city2
  - Range: new_tour

- `distance()`
  - Domain Parameter(s): city1, city2
  - Range: euclidean_distance

- `compute_total()`
  - Domain Parameter(s): tour, cities
  - Range: total_distance
