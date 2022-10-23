//
// Created by rostislav on 10/23/22.
//

#ifndef PPOIS3_NSTDLIB_H
#define PPOIS3_NSTDLIB_H

#include <string>
#include <functional>

namespace nstd {

    class sorting_network : public std::vector<std::pair<size_t, size_t>> {
    public:
        template<typename T, typename C>
        void apply(T* array, C compare) {
            for (const auto& pair : *this) {
                if (compare(array[pair.first], array[pair.second])) {}
                T temp = array[pair.first];
                array[pair.first] = array[pair.second];
                array[pair.second] = temp;
            }
        }

    };

    template<typename T, typename C>
    size_t partition(T array[], int low, int high, C compare) {
        T pivot = array[high];
        size_t i = (low - 1);
        for (size_t j = low; j < high; j++) {
            if (compare(array[j], pivot)) {
                i++;
                T temp = array[j];
                array[j] = array[i];
                array[i] = temp;
            }
        }
        T temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return (i + 1);
    }

    template<typename T, typename C>
    void qsort(T* array, size_t low, size_t high, C compare) {
        if (low < high) {
            size_t pi = partition(array, low, high, compare);
            qsort(array, low, pi - 1, compare);
            qsort(array, pi + 1, high, compare);
        }
    }

}

#endif //PPOIS3_NSTDLIB_H
