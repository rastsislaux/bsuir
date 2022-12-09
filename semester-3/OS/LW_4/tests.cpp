//
// Created by rostislav on 12/10/22.
//

#include <gtest/gtest.h>
#include "Memory.h"

TEST(alloc, MustNotHavePagesWhenInit) {
    auto m = Memory(8);
    ASSERT_EQ(0, m.get_page_count());
}

TEST(alloc, MustAllocateFirstPage) {
    auto m = Memory(8);
    m.alloc(4);
    ASSERT_EQ(1, m.get_page_count());
}

TEST(alloc, MustAllocateMorePages) {
    auto m = Memory(8);
    m.alloc(8);
    m.alloc(8);
    ASSERT_EQ(2, m.get_page_count());
}

TEST(alloc, MustReturnValidPtr) {
    auto m = Memory(8);
    int* ptr = (int*) m.alloc(sizeof(int));
    *ptr = 123;
    ASSERT_EQ(123, *ptr);
}

TEST(alloc, MustPlacePointersCorrectly) {
    auto m = Memory(8);
    intptr_t p1 = (intptr_t) m.alloc(2);
    intptr_t p2 = (intptr_t) m.alloc(3);
    intptr_t p3 = (intptr_t) m.alloc(1);

    ASSERT_EQ(2, p2 - p1);
    ASSERT_EQ(3, p3 - p2);
}

TEST(alloc, mustFitLongIn8Page) {
    auto m = Memory(8);
    ASSERT_NO_THROW(m.alloc(8));
}

TEST(free, mustPreventAllocatingNewPage) {
    auto m = Memory(8);
    void* ptr = m.alloc(8);
    m.free(ptr);
    m.alloc(8);
    ASSERT_EQ(1, m.get_page_count());
}

TEST(free, mustDoNothingWhenMemoryIsNotAllocated) {
    auto m = Memory(8);
    void* ptr = m.alloc(4);
    m.free(ptr);
    ASSERT_NO_THROW(m.free(ptr));
}

TEST(integration, allocMustPlacePtrInFreedSpace) {
    auto m = Memory(8);
    int* ptr1 = (int*) m.alloc(4);
    m.free(ptr1);
    int* ptr2 = (int*) m.alloc(4);
    ASSERT_EQ(ptr1, ptr2);
}