////////////////////////////////////////////////////////////////////////////////
/*                 (๑＞◡＜๑)  Malloc Challenge!!  (◍＞◡＜◍)                   */
////////////////////////////////////////////////////////////////////////////////

//
// Welcome to Malloc Challenge!! Your job is to invent a smart malloc algorithm.
//
// Rules:
//
// 1. Your job is to implement my_malloc(), my_free() and my_initialize().
//   *  my_initialize() is called only once at the beginning of each challenge.
//      You can initialize the memory allocator.
//   *  my_malloc(size) is called every time an object is allocated. In this
//      challenge, |size| is guaranteed to be a multiple of 8 bytes and meets
//      8 <= size <= 4000.
//   * my_free(ptr) is called every time an object is freed.
//   * Additionally, my_finalize() is called only once at the end of each
//   challenge,
//     so you can use this function for doing some clean ups if you want.
// 2. The only library functions you can use in my_malloc() and my_free() are
//    mmap_from_system() and munmap_to_system().
//   *  mmap_from_system(size) allocates |size| bytes from the system. |size|
//      needs to be a multiple of 4096 bytes. mmap_from_system(size) is a
//      system call and heavy. You are expected to minimize the call of
//      mmap_from_system(size) by reusing the returned
//      memory region as much as possible.
//   *  munmap_to_system(ptr, size) frees the memory region [ptr, ptr + size)
//      to the system. |ptr| and |size| need to be a multiple of 4096 bytes.
//      You are expected to free memory regions that are unused.
//   *  You are NOT allowed to use any other library functions at all, including
//      the default malloc() / free(), std:: libraries etc. This is because you
//      are implementing malloc itself -- if you use something that may use
//      malloc internally, it will result in an infinite recurion.
// 3. simple_malloc(), simple_free() and simple_initialize() in simple_malloc.c
//    are an example of straightforward implementation.
//    Your job is to invent a smarter malloc algorithm than the simple malloc.
// 4. There are five challenges (Challenge 1, 2, 3, 4 and 5). Each challenge
//    allocates and frees many objects with different patterns. Your malloc
//    is evaluated by two criteria.
//   *  [Speed] How faster your malloc finishes the challange compared to
//      the simple malloc.
//   *  [Memory utilization] How much your malloc is memory efficient.
//      This is defined as (S1 / S2), where S1 is the total size of objects
//      allocated at the end of the challange and S2 is the total size of
//      mmap_from_system()ed regions at the end of the challenge. You can
//      improve the memory utilization by decreasing memory fragmentation and
//      reclaiming unused memory regions to the system with munmap_to_system().
// 5. This program works on Linux and Mac but not on Windows. If you don't have
//    Linux or Mac, you can use Google Cloud Shell (See
//    https://docs.google.com/document/d/1TNu8OfoQmiQKy9i2jPeGk1DOOzSVfbt4RoP_wcXgQSs/edit#).
// 6. You need to specify an '-lm' option to compile this program.
//   *  gcc malloc_challenge.c -lm
//   *  clang malloc_challenge.c -lm
//
// Enjoy! :D
//

#include <assert.h>
#include <stdbool.h>
#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void *mmap_from_system(size_t size);
void munmap_to_system(void *ptr, size_t size);

////////////////////////////////////////////////////////////////////////////////

// Each object or free slot has metadata just prior to it:
//
// ... | m | object | m | free slot | m | free slot | m | object | ...
//
// where |m| indicates metadata. The metadata is needed for two purposes:
//
// 1) For an allocated object:
//   *  |size| indicates the size of the object. |size| does not include
//      the size of the metadata.
//   *  |next| pointer set to NULL
//
// 2) For a free slot:
//   *  |size| indicates the size of the free slot. |size| does not include
//      the size of the metadata.
//   *  The free slots are linked with a singly linked list (we call this a
//      free list).
//      |next| points to the next free slot.

typedef struct my_metadata_t {
    size_t size;
    struct my_metadata_t *next;
} my_metadata_t;

// The global information of the my_malloc.
//   *  |free_head| points to the first free slot.
//   *  |dummy| is a dummy free slot (only used to make the free list
//      implementation simpler).
typedef struct my_heap_t {
    my_metadata_t *free_head;
    my_metadata_t dummy;
} my_heap_t;

my_heap_t my_heap;

// Add a free slot to the free list.
void my_add_to_free_list(my_metadata_t *metadata) {
    assert(!metadata->next);
    metadata->next = my_heap.free_head;
    my_heap.free_head = metadata;
}

// Remove a free slot from the free list.
void my_remove_from_free_list(my_metadata_t *metadata,
                             my_metadata_t *prev){
    if (prev) {
        prev->next = metadata->next;
    }
    else {
        my_heap.free_head = metadata->next;
    }
    metadata->next = NULL;
}


// This is called only once at the beginning of each challenge.
void my_initialize() {
    my_heap.free_head = &my_heap.dummy;
    my_heap.dummy.size = 0;
    my_heap.dummy.next = NULL;
}

// This is called every time an object is allocated. |size| is guaranteed
// to be a multiple of 8 bytes and meets 8 <= |size| <= 4000. You are not
// allowed to use any library functions other than mmap_from_system /
// munmap_to_system.
void *my_malloc(size_t size) {
    my_metadata_t *metadata = my_heap.free_head;
    my_metadata_t *prev = NULL;
    my_metadata_t *worst_fit_metadata = NULL;
    my_metadata_t *prev_fit_metadata = NULL;

    // Worst-fit: Find the worst(largest) slot in the free list where the object fits.
    while (metadata) {
        //If the metadata size is greater than or equal to requested size
        if (metadata->size >= size) {
            if (worst_fit_metadata == NULL || worst_fit_metadata->size < metadata->size){
                //Set current best fit to the metadata
                worst_fit_metadata = metadata;
                //Set previous fit to the metadata's prev pointer
                prev_fit_metadata = prev;
            }
        }
        prev = metadata; //prev pointer to the metadata
        metadata = metadata->next ; //move the pointer to the next
    }

    // @source: https://github.com/xharaken/step2/blob/master/malloc_challenge.c
    if (!worst_fit_metadata) {
        // There was no free slot available. We need to request a new memory region
        // from the system by calling mmap_from_system().
        //
        //     | metadata | free slot |
        //     ^
        //     metadata
        //     <---------------------->
        //            buffer_size
        size_t buffer_size = 4096;
        my_metadata_t *metadata =
                (my_metadata_t *)mmap_from_system(buffer_size);
        metadata->size = buffer_size - sizeof(my_metadata_t);
        metadata->next = NULL;
        // Add the memory region to the free list.
        my_add_to_free_list(metadata);
        // Now, try simple_malloc() again. This should succeed.
        return my_malloc(size);
    }

    // |ptr| is the beginning of the allocated object.
    //
    // ... | metadata | object | ...
    //     ^          ^
    //     metadata   ptr
    void *ptr = worst_fit_metadata + 1;
    size_t remaining_size = worst_fit_metadata->size - size;
    worst_fit_metadata->size = size;
    // Remove the free slot from the free list.
    my_remove_from_free_list(worst_fit_metadata,prev_fit_metadata);

    if (remaining_size > sizeof(my_metadata_t)) {
        // Create a new metadata for the remaining free slot.
        //
        // ... | metadata | object | metadata | free slot | ...
        //     ^          ^        ^
        //     metadata   ptr      new_metadata
        //                 <------><---------------------->
        //                   size       remaining size
        my_metadata_t *new_metadata = (my_metadata_t *)((char *)ptr + size);
        new_metadata->size = remaining_size - sizeof(my_metadata_t);
        new_metadata->next = NULL;
        // Add the remaining free slot to the free list.
        my_add_to_free_list(new_metadata);
    }
    return ptr;
}

// This is called every time an object is freed.  You are not allowed to use
// any library functions other than mmap_from_system / munmap_to_system.
void my_free(void *ptr) {
    // Look up the metadata. The metadata is placed just prior to the object.
    //
    // ... | metadata | object | ...
    //     ^          ^
    //     metadata   ptr
    my_metadata_t *metadata = (my_metadata_t *)ptr - 1;
    // Add the free slot to the free list.
    my_add_to_free_list(metadata);
}

void my_finalize() {}

void test() {
    // Implement here!
    assert(1 == 1); /* 1 is 1. That's always true! (You can remove this.) */
}
