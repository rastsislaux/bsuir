#include <iostream>
#include <ranges>

#include "ltrx.h"

template<typename T>
functor(PrintFunctor, T, T, {
    std::cout << elem << " ";
    return elem;
});

int main() {
    auto p = PrintFunctor<int>();

    auto m1 = ltrx::Matrix<int>(1, 5, 5);
    auto m2 = ltrx::Matrix<int>(2, 5, 5);
    auto m3 = m1 * 10;
    for (int i = 0; i < m3.size().first; i++) {
        m3.mapLine(i, p);
        std::cout << std::endl;
    }
}
