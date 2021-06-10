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
def compute_total(tour,cities):
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

#Swaps the endpoints of two edges by reversing a section of nodes,
#to eliminate crossovers
def swap_2opt(tour, city1, city2):
    n = len(tour)
    #Assert that the first city passed in is inside the tour
    assert city1 >= 0 and city1 < (n - 1)
    #Asserts that second city comes after the first city, and that it is inside the tour
    assert city2 > city1 and city2 < n
    #Set the new tour as the tour from the start of the passed in tour up to the first city
    new_tour = tour[0:city1]
    #Add the reversed of the tour from the first city to the second city to the new tour
    new_tour.extend(reversed(tour[city1:city2 + 1]))
    #Add the 
    new_tour.extend(tour[city2+1:])
    assert len(new_tour) == n
    return new_tour #New tour after swap was made is returned

#Solves the tsp using 2-opt algorithm
#Optimizes the route using the 2-opt swap until no improved tour is found
#@source: https://en.wikipedia.org/wiki/2-opt
def solve_2opt(tour, cities):
    improvement = True
    best_tour = tour
    best_distance = compute_total(tour, cities)
    while improvement:
        improvement = False
        for i in range(len(best_tour) - 1):
            for j in range(i+1, len(best_tour)):
                new_tour = swap_2opt(best_tour, i, j)
                new_distance = compute_total(new_tour, cities)
                if new_distance < best_distance:
                    best_distance = new_distance
                    best_tour = new_tour
                    improvement = True
                    break #Improvement found, return to the top of the while loop
    return best_tour #Best tour after algorithm is executed is returned


#Solve TSP using greedy and 2-opt algorithms
#Start tour with a random city
#Find the shortest distance of the shortest_tour
def solve_tsp_tour(cities):
    n = len(cities)
    shortest_tour = None
    shortest_distance = -1
    for start_city in random.sample(range(n),5):
        tour = solve_greedy(start_city,cities)
        tour = solve_2opt(tour,cities)
        total_distance = compute_total(tour,cities)
        if shortest_distance < 0 or shortest_distance > total_distance:
            shortest_distance = total_distance
            shortest_tour = tour
    return shortest_tour,shortest_distance

#   Main program reads the input files, calls solve_tsp_tour for the input_file
#   Prints the tour and distance and saves to output file
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