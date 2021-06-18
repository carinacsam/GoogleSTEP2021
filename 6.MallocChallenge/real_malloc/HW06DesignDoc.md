# Google STEP Homework 6 Design Document
*Carina Samson*

-------
<u>**Malloc Challenge**</u>
-------
*Summary*

My implementation for this challenge combined both the "best fit" algorithm, as well as "scan and merge." Most of the code was adapted from simple_malloc.c and the changes that I made to it was for the purpose of optimizing efficiency, as well as memory storage. My scan and merge method in particular improved memory utilization by merging free blocks into one block, looking for the best fit for the smallest available memory slot and avoiding memory fragmentation in the process.

*Algorithm (Best Fit)*

1. Traverse the free list and find the best fit slot in the free list 
    1. If the metadata size is greater than or equal to the size
        2. Set the best_fit_metadata equal to the metadata
        3. Set the prev_fit_metadata equal to the prev pointer
    2. Repeat until  we've reached the end of the free list 
     
2. If we're unable to find a memory block on the free list
    1. Request more memory from the system
    2. Add the new memory  to the free list
    3. Call malloc again

4. If pointer is the beginning of the allocated object 
    1. Set pointer equal to best_fit_metadata + 1
    2. Set best_fit_metadata-> size equal to size 
    3. Remove the memory slot from free list
    4. Create a new metadata from the remaining free slot
    5. Add the new metadata to the free list
    6. Return the pointer to the requested memory 

5.  In the my_free implementation
    1. Add the free slot to the free list
    2. Scan the free list for continuous free blocks
    3. Merge the continuous free blocks
    5. Create a new block with the sum of their sizes
    
*Variables*
- `my_heap;` - pointer to the free list


*Functions*

- `my_remove_freelist()`
    - Domain Parameter(s): *block
    - Range: void
    
- `my_add_freelist()`
    - Domain Parameter(s): *block
    - Range: void
    
- `my_scan_and_merge()`
    - Domain Parameter(s): *ptr
    - Range: void

- `my_initialize()`
    - Domain Parameter(s):
    - Range: void

- `my_malloc()`
    - Domain Parameter(s): size
    - Range: my_memory_block
 
- `my_free()`
    - Domain Parameter(s): *ptr
    - Range: void

*Results*

```
Challenge 1: simple malloc => my malloc

Time: 8 ms => 457 ms

Utilization: 70% => 65%

==================================

Challenge 2: simple malloc => my malloc

Time: 5 ms => 125 ms

Utilization: 40% => 31%

==================================

Challenge 3: simple malloc => my malloc

Time: 100 ms => 166 ms

Utilization: 8% => 37%

==================================

Challenge 4: simple malloc => my malloc

Time: 5846 ms => 1775 ms

Utilization: 15% => 73%

==================================

Challenge 5: simple malloc => my malloc

Time: 4347 ms => 1253 ms

Utilization: 15% => 74%

==================================
```
