# Google STEP Homework 6 Design Document
*Carina Samson*

-------
<u>**Malloc Challenge**</u>
-------
*Summary*
My implementation for this challenge combined both the "best fit" algorithm, as well as "merge." Most of the code was adapted from simple_malloc.c and the changes that I made to it was for the purpose of optimizing efficiency, as well as memory storage.  My scan and merge method in particular improved memory utilization by merging free blocks into one block, looking for the best fit for the smallest available memory slot and avoiding memory fragmentation in the process. The my_add_to_freelist function was also updated to sort the free list by address for faster merging.

*Analysis*
I compared the results of my improved best fit with merge algorithm, with the first fit, worst fit and naive best fit algorithms and the results are displayed in the table below. The first fit algorithm finds the first free slot where the object can fit. As you can see from the results, the execution time is fast for the challenge 1-3 but it became so slower in challenge 4 and 5. The memory utilization also decreased and it was not efficient because of memory fragmentation. The worst fit algorithm finds the largest slot where the object fits. It was the slowest algorithm as you can see from the results and the memory utilization was also the worst compared to the others. My improved best fit and merge algorithm had better memory utilization compared to other algorithms. The execution time was also fast in challenge 4 and 5. 


*Results*
| -------------- | ---------------- | ---------------- | ---------------- | --------------- | --------------- |
| Algorithm      | Challenge 1      | Challenge 2      | Challenge 3      |Challenge 4      |Challenge 5      |
| -------------- | ---------------- | ---------------- | ---------------- |---------------- |---------------- |
| Best Fit       | Time: 392 ms     | Time: 110 ms     | Time: 170 ms     | Time: 1916 ms   | Time: 1371 ms   |
| and merge      | Utilization: 70% | Utilization: 40% | Utilization: 44% | Utilization: 75 | Utilization: 76%|
| -------------- | ---------------- | ---------------- | ---------------- | --------------- | --------------- |
| First Fit      | Time: 7 ms       | Time: 6 ms       | Time: 106 ms     | Time: 6644 ms   | Time: 4961 ms   |
|                | Utilization: 70% | Utilization: 40% | Utilization: 7%  | Utilization: 15%| Utilization: 15%|
| -------------- | ---------------- | ---------------- | ---------------- | --------------- | --------------- |
| Worst Fit      | Time: 1233 ms    | Time: 428 ms     | Time: 44751 ms   | Time: 247198 ms | Time: 197912ms  |
|                | Utilization: 70% | Utilization: 40% | Utilization: 4%  | Utilization: 7% | Utilization: 7% |
| -------------- | ---------------- | ---------------- | ---------------- | --------------- | --------------- |



*Algorithm: Best fit with merge*
1. Traverse the free list and find the best fit slot in the free list
    1. If the metadata size is greater than or equal to the size
    2. Set the best_fit_metadata equal to the metadata
    3. Set the prev_fit_metadata equal to the prev pointer
    4. Find the smallest free slot(best_fit_metadata) that can be allocated
    5. Repeat until  we've reached the end of the free list

2. If we're unable to find a memory block on the free list
    1. Request more memory from the system
    2. Add the new memory to the free list, sorted by address
    3. Call malloc again

4. After finding the best fit slot
    1. Set pointer equal to best_fit_metadata + 1
    2. Set best_fit_metadata-> size equal to size
    3. Remove the memory slot from free list
    4. Create a new metadata from the remaining free slot
    5. Add the new metadata to the free list, sorted by address
    6. Return the pointer to the requested memory

5.  In the my_free implementation
    1. Add the free slot to the free list, sorted by address
    2. Scan the free list for continuous free blocks of memory
    3. Merge the two continuous free blocks
    4. Create a new block with the sum of their sizes


*Data Structures*
1. my_medata_t

```
typedef struct my_metadata_t {
    size_t size;
    struct my_metadata_t *next;
} my_metadata_t;

```
- Each object or free slot has metadata just prior to it:
... | m | object | m | free slot | m | free slot | m | object | ...

where |m| indicates metadata. The metadata is needed for two purposes:
- For an allocated object:
    - |size| indicates the size of the object. |size| does not include the size of the metadata.
    - |next| pointer set to NULL
- For a free slot:
    - |size| indicates the size of the free slot. |size| does not include the size of the metadata.
    - The free slots are linked with a singly linked list (we call this a free list).
    - |next| points to the next free slot.`


2. my_heap_t

```
typedef struct my_heap_t {
    my_metadata_t *free_head;
    my_metadata_t dummy;
} my_heap_t;
```
- The global information of the my_malloc.
- |free_head| points to the first free slot.
- |dummy| is a dummy free slot (only used to make the free list implementation simpler).


*Variables*
- `my_heap_t my_heap` - free list

*Functions*

- `my_remove_from_freelist()`
    - Domain Parameter(s):  metadata, *prev
    - Range: void
    - Remove a free slot from the free list.

- `my_add_to_freelist()`
    - Domain Parameter(s): *metadata
    - Range: void
    - Add a free slot to the free list sorted by address for faster merging

- `my_scan_and_merge()`
    - Domain Parameter(s):
    - Range: void
    - Scan continuous addressed free blocks. Merge two blocks and create a new block with the sum of their sizes.

- `my_initialize()`
    - Domain Parameter(s):
    - Range: void
    - This is called only once at the beginning of each challenge.

- `my_malloc()`
    - Domain Parameter(s): size
    - Range: *ptr
    - This is called every time an object is allocated..

- `my_free()`
    - Domain Parameter(s): *ptr
    - Range: void
    - Add the free slot to the free list.
    - Call scan_and_merge to merge two free continuous blocks and create a new block.