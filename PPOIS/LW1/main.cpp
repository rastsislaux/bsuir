#include <iostream>
#include <ranges>

#include "ltrx.h"

template<typename T>
class PrintFunctor : public ltrx::Matrix<T>::AbstractFunctor {
    T& invoke(T& elem) override {
        std::cout << elem << " ";
        return elem;
    }
};

int main() {
    auto matrix = ltrx::Matrix<int>(2, 5, 5);
    auto matrix2 = ltrx::Matrix<int>(3, 5, 5);
    auto printFunctor = PrintFunctor<int>();

    auto res = matrix + matrix2;
    res.mapLine(0, printFunctor);
}
