#include <gtest/gtest.h>
#include "nstdlib.h"

TEST(QuickSort, mustSortAnIntVector) {
    std::vector<int> vect = {1, 6, 3, 0, -4, 126, 31, -35, 35, 7, 8, -3, 4, -7};
    nstd::qsort(&vect[0], 0, vect.size() - 1, [](int a, int b) { return a < b; });

    std::vector<int> expected = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};
    ASSERT_EQ(vect, expected);
}

TEST(QuickSort, mustSortADoubleVector) {
    std::vector<double> vect = {1, 6, 3, 0, -4, 126, 31, -35, 35, 7, 8, -3, 4, -7};
    nstd::qsort(&vect[0], 0, vect.size() - 1, [](double a, double b) { return a < b; });

    std::vector<double> expected = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};
    ASSERT_EQ(vect, expected);
}

TEST(QuickSort, mustSortAnIntArray) {
    int arr[] = {1, 6, 3, 0, -4, 126, 31, -35, 35, 7, 8, -3, 4, -7};
    nstd::qsort(arr, 0, 13, [](int a, int b) { return a < b; });

    int expected[] = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};

    for (int i = 0; i < 14; i++) {
        ASSERT_EQ(arr[i], expected[i]);
    }
}

TEST(QuickSort, mustSortADoubleArray) {
    double arr[] = {1, 6, 3, 0, -4, 126, 31, -35, 35, 7, 8, -3, 4, -7};
    nstd::qsort(arr, 0, 13, [](double a, double b) { return a < b; });

    double expected[] = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};

    for (int i = 0; i < 14; i++) {
        ASSERT_EQ(arr[i], expected[i]);
    }
}

TEST(QuickSort, mustSortAVectorBackwards) {
    std::vector<int> vect = {1, 6, 3, 0, -4, 126, 31, -35, 35, 7, 8, -3, 4, -7};
    nstd::qsort(&vect[0], 0, vect.size() - 1, [](int a, int b) { return a > b; });

    std::vector<int> expected = { 126, 35, 31, 8, 7, 6, 4, 3, 1, 0, -3, -4, -7, -35 };
    ASSERT_EQ(vect, expected);
}

TEST(QuickSort, mustSortAnArrayBackwards) {
    int arr[] = {1, 6, 3, 0, -4, 126, 31, -35, 35, 7, 8, -3, 4, -7};
    nstd::qsort(arr, 0, 13, [](int a, int b) { return a > b; });

    int expected[] = { 126, 35, 31, 8, 7, 6, 4, 3, 1, 0, -3, -4, -7, -35 };

    for (int i = 0; i < 14; i++) {
        ASSERT_EQ(arr[i], expected[i]);
    }
}

TEST(SortingNetwork, mustSortAnIntVector) {
    nstd::sorting_network sn;
    sn.emplace_back(0, 13);
    sn.emplace_back(1, 12);

    std::vector<int> vect = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};
    sn.apply(&vect[0], [](int a, int b) { return a > b; });

    std::vector<int> expected = {126, 35, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, -7, -35};
    ASSERT_EQ(vect, expected);
}

TEST(SortingNetwork, mustSortADoubleVector) {
    nstd::sorting_network sn;
    sn.emplace_back(0, 13);
    sn.emplace_back(1, 12);

    std::vector<double> vect = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};
    sn.apply(&vect[0], [](double a, double b) { return a > b; });

    std::vector<double> expected = {126, 35, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, -7, -35};
    ASSERT_EQ(vect, expected);
}

TEST(SortingNetwork, mustSortAnIntArray) {
    nstd::sorting_network sn;
    sn.emplace_back(0, 13);
    sn.emplace_back(1, 12);

    int arr[] = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};
    sn.apply(arr, [](int a, int b) { return a > b; });

    int expected[] = {126, 35, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, -7, -35};

    for (int i = 0; i < 14; i++) {
        ASSERT_EQ(arr[i], expected[i]);
    }
}

TEST(SortingNetwork, mustSortADoubleArray) {
    nstd::sorting_network sn;
    sn.emplace_back(0, 13);
    sn.emplace_back(1, 12);

    double arr[] = {-35, -7, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, 35, 126};
    sn.apply(arr, [](double a, double b) { return a > b; });

    double expected[] = {126, 35, -4, -3, 0, 1, 3, 4, 6, 7, 8, 31, -7, -35};

    for (int i = 0; i < 14; i++) {
        ASSERT_EQ(arr[i], expected[i]);
    }
}