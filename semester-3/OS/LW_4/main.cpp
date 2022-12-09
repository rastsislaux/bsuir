#include "Memory.h"

#define PAGE_SIZE 8

int main() {
    auto memory = Memory(PAGE_SIZE);
    int* ptr = (int*) memory.alloc(sizeof(int));
    *ptr = 1;
    auto ptr2 = (int*) memory.alloc(sizeof(int));
    *ptr2 = 2;
    auto ptr3 = (int*) memory.alloc(sizeof(int));
    printf("%d\n", memory.get_page_count());
    *ptr3 = 3;

    //memory.free(ptr2);

    auto ptr4 = (int*) memory.alloc(sizeof(int));
    *ptr4 = 4;

    printf("%d %d %d", *ptr, *ptr2, *ptr3);
    return 0;
}
