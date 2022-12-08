#include <iostream>
#include <map>
#include <unistd.h>

#define PAGE_SIZE 8

class Memory {

    pthread_mutex_t lock;

    const intptr_t page_size;

    long page_count = 0;

    void** pages = (void**) calloc(0, sizeof(void*));

    std::map<intptr_t , intptr_t> allocated;

    static void error(std::string text) {
        printf("[MEM_MAN] Error: %s\n Exiting with exit code 1.", text.c_str());
        exit(1);
    }

    bool is_free(void* addr) {
        auto addr_int = (intptr_t) addr;
        for (auto pair : allocated) {
             if (addr_int >= pair.first && addr_int <= pair.second) return false;
        }
        return true;
    }

    bool is_free(intptr_t addr) {
        return is_free((void*) addr);
    }

    bool find_free_space(intptr_t size, intptr_t& pg, intptr_t& ofst) {
        if (page_count == 0) {
            return false;
        }

        intptr_t page;
        intptr_t offset = -1;
        for (intptr_t i = 0; i < page_count; i++) {
            for (intptr_t j = 0; j < page_size - size + 1; j++) {
                bool found = true;
                for (intptr_t k = 0; k < size; k++) {
                    if (!is_free((intptr_t)pages[i] + j + k)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    offset = j;
                    break;
                }
            }
            if (offset != -1) {
                page = i;
                break;
            }
        }

        if (offset == -1) {
            return false;
        }

        pg = page;
        ofst = offset;
        return true;
    }

    void* alloc_page() {
        pages = (void**) realloc(pages, ++page_count);
        return pages[page_count - 1] = sbrk(page_size);
    }

public:
    Memory(intptr_t page_size) : page_size(page_size) {
        if (pthread_mutex_init(&lock, nullptr) != 0) error("Mutex init failed");
    }

    int get_page_count() const {
        return page_count;
    }

    void* alloc(intptr_t size) {
        pthread_mutex_lock(&lock);
        intptr_t page = -1, offset = -1;
        bool succ = find_free_space(size, page, offset);
        if (!succ) {
            alloc_page();
            find_free_space(size, page, offset);
        }

        void* ptr = (void*)((intptr_t)pages[page] + offset);
        allocated[(intptr_t)pages[page] + offset] = (intptr_t)pages[page] + size + offset - 1;
        pthread_mutex_unlock(&lock);
        return ptr;
    }

    void free(void* ptr) {
        auto it = allocated.find((intptr_t) ptr);
        if (it != allocated.end()) {
            allocated.erase(it);
        }
    }

};

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
