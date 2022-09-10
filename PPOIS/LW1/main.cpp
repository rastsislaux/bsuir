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
    auto m = ltrx::Matrix<int>(2, 3, 3);
    m.set(0, 0, 1);
    m.set(0, 1, 2);
    m.set(0, 2, 3);
    std::cout << m.norm() << std::endl;
}
