#include <iostream>
#include <ranges>

#include "ltrx.h"

class print_functor : public ltrx::matrix<int>::const_functor {
    const int& invoke(const int& elem) override {
        std::cout << elem << " ";
    }
};

void print_matrix(const ltrx::matrix<int>& matrix) {
    auto size = matrix.size();
    matrix.for_each(print_functor());
}

int main() {
    ltrx::matrix<int> matrix = ltrx::matrix<int>(0, 10, 10);
    matrix++;
}
