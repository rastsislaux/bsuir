//
// Created by rostislav on 9/6/22.
//

#ifndef LTRX_LTRX_H
#define LTRX_LTRX_H


#include <vector>
#include <exception>

namespace ltrx {

    class MatrixError : public std::runtime_error {
    public:
        explicit MatrixError(std::string msg) : std::runtime_error(msg) { }
    };

    template<typename T>
    class Matrix {
    private:
        std::vector<std::vector<T>> matrix;
        size_t width;
        size_t height;

    public:
        class AbstractFunctor {
        public:
            virtual T& invoke(T& elem) = 0;
        };

    private:
        class IncFunctor : public AbstractFunctor {
            T& invoke(T& elem) override {
                elem++;
                return elem;
            }
        } s_incFunctor;

        class DecFunctor : public AbstractFunctor {
            T& invoke(T& elem) override {
                elem--;
                return elem;
            }
        } s_decFunctor;

    public:
        Matrix(T defaultValue, size_t width, size_t height) {
            this->width = width;
            this->height = height;
            matrix.resize(width);
            for (auto& line : matrix) {
                line = std::vector<T>();
                line.resize(height, defaultValue);
            }
        }

        size_t getWidth() {
            return width;
        }

        size_t getHeight() {
            return height;
        }

        T get(size_t horizontalIndex, size_t verticalIndex) {
            return matrix[horizontalIndex][verticalIndex];
        }

        Matrix& map(AbstractFunctor& f) {
            for (auto& line : matrix) {
                for (auto& elem : line) {
                    elem = f.invoke(elem);
                }
            }
            return *this;
        }

        Matrix& mapLine(size_t lineIndex, AbstractFunctor& f) {
            for (auto& elem : matrix[lineIndex]) {
                elem = f.invoke(elem);
            }
            return *this;
        }

        Matrix& operator++() {
            this->map(s_incFunctor);
            return *this;
        }

        Matrix operator++(int) {
            Matrix<T> temp = *this;
            this->map(s_incFunctor);
            return temp;
        }

        Matrix& operator--() {
            this->map(s_decFunctor);
            return *this;
        }

        Matrix operator--(int) {
            Matrix<T> temp = *this;
            this->map(s_decFunctor);
            return temp;
        }

        Matrix operator+(const Matrix& other) const {
            auto size = this->size();
            if (other.size() != size) {
                throw MatrixError("Matrix of different size cant be added.");
            }
            auto result = Matrix(0, size.first, size.second);
            for (int i = 0; i < size.first; i++) {
                for (int j = 0; j < size.second; j++) {
                    result.matrix[i][j] = this->matrix[i][j] + other.matrix[i][j];
                }
            }
            return result;
        }

        [[nodiscard]] std::pair<int, int> size() const {
            return {width, height};
        }
    };

}


#endif //LTRX_LTRX_H
