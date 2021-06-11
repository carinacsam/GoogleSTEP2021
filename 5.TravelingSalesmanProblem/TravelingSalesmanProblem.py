#!/usr/bin/env python3

import sys
import math
import random
from random import randint
from common import print_tour, read_input, format_tour

#Calculates the Euclidean distance between two coordinate points
#@source: https://github.com/hayatoito/google-step-tsp
def distance(city1, city2):
    #Distance formula is sqrt((x1-x2)^2 + (y1-y2)^2)
    euclidean_distance = math.sqrt((city1[0] - city2[0]) ** 2 + (city1[1] - city2[1]) ** 2)
    return euclidean_distance

#Calculates the total tour distance
def compute_total(tour, cities):
    total_distance = 0
    for i in range(len(tour) - 1):
        #Adds distance between current tour and next tour to total distance
        total_distance += distance(cities[tour[i]], cities[tour[i + 1]])
    #Adds distance between first tour and the tour before it
    total_distance += distance(cities[tour[-1]], cities[tour[0]])
    return total_distance #Returns the total distance computed

#Solves the TSP using the Greedy Algorithm
#@source: https://github.com/hayatoito/google-step-tsp
def solve_greedy(start_city, cities):
    N = len(cities)
    dist = [[0] * N for i in range(N)]
    #Iterate through the x-coordinates
    for i in range(N):
        #Iterate through the y-coordinates
        for j in range(i, N):
            #Calculate the Euclidean distances between cities
            dist[i][j] = dist[j][i] = distance(cities[i], cities[j])
    current_city = start_city
    #Set is created for us to track all the unvisted cities
    unvisited_cities = set(range(0, N))
    #The first city is removed from the set, indicating that we've visited it
    unvisited_cities.remove(current_city)
    #Tour is then set to list with the first city
    tour = [current_city]

    #Iterate until there are no more unvisited cities
    while unvisited_cities:
        #The next city is set to the nearest city with the minimum distance
        #from the current city
        next_city = min(unvisited_cities,
                        key=lambda city: dist[current_city][city])
        #Remove the next city from the set of unvisited cities
        unvisited_cities.remove(next_city)
        #Append the next city to the tour
        tour.append(next_city)
        #Set the current city to the next city to traverse
        current_city = next_city
    return tour #Tour created using the algorithm is returned

#Swaps the endpoints of two edges by reversing a section of nodes, to optimize for the shortest path
def swap_2opt(tour, city1, city2):
    n = len(tour)
    #Set the new tour as the tour from the start of the passed in tour up to the first city
    new_tour = tour[0:city1]
    #Add the reversed of the tour from the first city to the second city to the new tour
    new_tour.extend(reversed(tour[city1:city2 + 1]))
    #Add the rest of the tour after the swapped cities
    new_tour.extend(tour[city2+1:])
    assert len(new_tour) == n
    return new_tour #New tour after swap is returned

#Solves the tsp using 2-opt algorithm
#Optimizes the route using the 2-opt swap until no improved tour is found
def solve_2opt(tour, cities):
    improvement = True #Initialize the flag to indicate need for improvement
    best_tour = tour 
    best_distance = compute_total(tour, cities)
    while improvement: 
        improvement = False #Improvement will be addressed in iteration
        for i in range(len(best_tour) - 1):
            for j in range(i+1, len(best_tour)):
                #Iterate through tour and swap
                new_tour = swap_2opt(best_tour, i, j) 
                new_distance = compute_total(new_tour, cities) 
                #If new distance is greater than best distance so far, set new best tour and distance
                if new_distance < best_distance: 
                    best_distance = new_distance 
                    best_tour = new_tour
                    #Set improvement as true to indicate that there's more room for improvement
                    improvement = True
                    break #Improvement found, return to the top of the while loop
    return best_tour #Best tour after the algorithm is executed is returned

#Swaps certain endpoints between three edges by reversing sections of nodes to optimize for the shortest path
def swap_3opt(tour, cities, city1, city2, city3):
    #Given a tour with segments 1-2, 3-4, 5-6 
    tour1, tour3, tour5 = tour[city1-1], tour[city2-1], tour[city3-1]
    tour2, tour4, tour6 = tour[city1], tour[city2], tour[city3 % len(tour)]

    #Calculate the total distance for the tour with segments 1-2, 3-4, 5-6
    dist1 = distance(cities[tour1], cities[tour2]) + distance(cities[tour3],
                cities[tour4]) + distance(cities[tour5], cities[tour6])
    #Calculate the total distance for the tour with segments 1-3, 2-4, 5-6
    dist2 = distance(cities[tour1], cities[tour3]) + distance(cities[tour2],
                cities[tour4]) + distance(cities[tour5], cities[tour6])
    #Calculate the total distance for the tour with segments 1-2, 3-5, 4-6
    dist3 = distance(cities[tour1], cities[tour2]) + distance(cities[tour3],
                cities[tour5]) + distance(cities[tour4], cities[tour6])
    #Calculate the total distance for the tour with segments 1-4, 5-2, 3-6
    dist4 = distance(cities[tour1], cities[tour4]) + distance(cities[tour5],
                cities[tour2]) + distance(cities[tour3], cities[tour6])
    #Calculate the total distance for the tour with segments 6-2, 3-4, 5-1
    dist5 = distance(cities[tour6], cities[tour2]) + distance(cities[tour3],
                cities[tour4]) + distance(cities[tour5], cities[tour1])

    #See corresponding diagram in Design Doc for cases
    #(a, b and c are city1, city2, and city3)
    #(Case 1, 2, 3 are completed by the 2-opt swap)
    new_tour = tour
    if dist1 > dist3:
        new_tour[city2:city3] = reversed(tour[city2:city3]) #Swap 2-3 (Case 4)
    elif dist1 > dist2:
        new_tour[city1:city2] = reversed(tour[city1:city2]) #Swap 1-2 (Case 5)
    elif dist1 > dist5:
        new_tour[city1:city3] = reversed(tour[city1:city3]) #Swap 1-3 (Case 6)
    elif dist1 > dist4:
        newPath = tour[city2:city3] + tour[city1:city2] 
        new_tour[city1:city3] = newPath #Two-step swap (Case 7)
    return new_tour #New tour after swap is returned

#Solves the tsp using 3-opt algorithm
def solve_3opt(tour, cities):
    n = len(tour)
    #Iterates between all possible combinations of three edges
    for i in range(n):
        for j in range(i+1, n):
            for k in range(j+1, n + (i>0)):
                best_tour = swap_3opt(tour, cities, i, j, k)
    return best_tour #Best tour so far is returned


#Solve TSP using greedy, 3-opt and 2-opt algorithms, starting from a random city
#Find the shortest distance of the shortest_tour
def solve_tsp_tour(cities):
    n = len(cities)
    shortest_tour = None
    shortest_distance = -1
    #Choose random city indexes
    for start_city in random.sample(range(n), n):
        #Run Greedy first, and then 2opt to improve the tour
        tour = solve_greedy(start_city, cities)
        tour = solve_3opt(tour, cities)
        tour = solve_2opt(tour, cities)
        total_distance = compute_total(tour, cities)
        #If the total distance calculated is the shortest so far
        if shortest_distance < 0 or shortest_distance > total_distance:
            #Set shortest distance to the computed total distance and shortest tour to current tour
            shortest_distance = total_distance
            shortest_tour = tour
    return shortest_tour, shortest_distance #Returns the shortest tour and its distance

#Main program reads the input files, calls solve_tsp_tour for the input_file
#Prints the tour and distance and saves to output file
def main(input_file):
    cities = read_input(input_file)
    shortest_tour, shortest_distance = solve_tsp_tour(cities)
    print(shortest_tour)
    print(shortest_distance)
    with open(f'output_{input_file[6]}.csv', 'w') as f:
        f.write(format_tour(shortest_tour) + '\n')

if __name__ == '__main__':
    assert len(sys.argv) > 1
    main(sys.argv[1])
