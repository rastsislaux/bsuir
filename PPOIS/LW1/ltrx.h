//
// Created by rostislav on 9/6/22.
//

#ifndef LTRX_LTRX_H
#define LTRX_LTRX_H

#include <vector>
#include <exception>
#include <cmath>

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
        template <typename K>
        class AbstractFunctor {
        public:
            virtual K invoke(T& elem) = 0;
        };

    private:
        #define functor(NAME, RET_TYPE, IN_TYPE, BODY) \
            class NAME : public AbstractFunctor<RET_TYPE> { \
                virtual RET_TYPE invoke(IN_TYPE& elem) BODY \
            }

        functor(IncFunctor, T, T, {
            elem++;
            return elem;
        }) s_incFunctor;

        functor(DecFunctor, T, T, {
            elem--;
            return elem;
        }) s_decFunctor;

        static bool getSign(size_t i, size_t j) {
                return (i % 2 == 0)
                    ? (j % 2 == 0)
                    : (j % 2 == 1);
        }

        void checkSizeEquals(const Matrix& s) {
            auto size = this->size();
            if (s.size() != size) {
                throw MatrixError("This operation is not supported for matrixes of different size.");
            }
        }

        Matrix& add(const Matrix& other) {
            checkSizeEquals(other);
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] + other.matrix[i][j]);
                }
            }
            return *this;
        }

        Matrix& add(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] + value);
                }
            }
            return *this;
        }

        Matrix& sub(const Matrix& other) {
            checkSizeEquals(other);
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] - other.matrix[i][j]);
                }
            }
            return *this;
        }

        Matrix& sub(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] - value);
                }
            }
            return *this;
        }

        Matrix& mul(const Matrix& other) {
            checkSizeEquals(other);
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] * other.matrix[i][j]);
                }
            }
            return *this;
        }

        Matrix& mul(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] * value);
                }
            }
            return *this;
        }

        Matrix& div(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] / value);
                }
            }
            return *this;
        }

        Matrix& mpow(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, pow(this->matrix[i][j], value));
                }
            }
            return *this;
        }

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

        Matrix(size_t width, size_t height) {
            this->width = width;
            this->height = height;
            matrix.resize(width);
            for (auto& line : matrix) {
                line = std::vector<T>();
                line.resize(height);
            }
        }

        size_t getWidth() {
            return width;
        }

        size_t getHeight() {
            return height;
        }

        T get(size_t horizontalIndex, size_t verticalIndex) const {
            return matrix[horizontalIndex][verticalIndex];
        }

        void set(size_t horizontalIndex, size_t verticalIndex, T value) {
            matrix[horizontalIndex][verticalIndex] = value;
        }

        template<typename K>
        Matrix<K> map(AbstractFunctor<K>& f) {
            Matrix<K> result = Matrix<K>(width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    std::cout << matrix[i][j] << " ";
                    result.set(i, j, f.invoke(matrix[i][j]));
                }
            }
            return result;
        }

        template<typename K>
        Matrix<K>& mapLine(size_t lineIndex, AbstractFunctor<K>& f) {
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
            auto result = Matrix(*this);
            return result.add(other);
        }

        Matrix& operator+=(const Matrix& other) {
            return add(other);
        }

        Matrix operator+(const T& other) const {
            auto result = Matrix(*this);
            return result.add(other);
        }

        Matrix& operator+=(const T& other) {
            return add(other);
        }

        Matrix operator-(const Matrix& other) const {
            auto result = Matrix(*this);
            return result.sub(other);
        }

        Matrix& operator-=(const Matrix& other) {
            return sub(other);
        }

        Matrix operator-(const T& other) const {
            auto result = Matrix(*this);
            return result.sub(other);
        }

        Matrix& operator-=(const T& other) {
            return sub(other);
        }

        Matrix operator*(const Matrix& other) {
            auto result = Matrix(*this);
            return result.mul(other);
        }

        Matrix& operator*=(const Matrix& other) {
            return mul(other);
        }

        Matrix operator*(const T& value) {
            auto result = Matrix(*this);
            return result.mul(value);
        }

        Matrix& operator*= (const T& value) {
            return mul(value);
        }

        Matrix operator/(const T& value) {
            auto result = Matrix(*this);
            return result.div(value);
        }

        Matrix& operator/=(const T& value) {
            return div(value);
        }

        Matrix operator^(const T& value) {
            auto result = Matrix(*this);
            return result.mpow(value);
        }

        Matrix& operator^=(const T& value) {
            return mpow(value);
        }

        bool operator==(const Matrix& other) const {
            if (size() != other.size()) {
                return false;
            }
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (get(i, j) != other.get(i, j)) {
                        return false;
                    }
                }
            }
            return true;
        }

        Matrix minor(size_t& line, size_t& row) {
            Matrix result = Matrix(width - 1, height - 1);
            T lineSkipped = 0;
            for (int i = 0; i < width; i++) {
                if (i == line) {
                    lineSkipped = 1;
                    continue;
                }
                T rowSkipped = 0;
                for (int j = 0; j < height; j++) {
                    if (j == row) {
                        rowSkipped = 1;
                        continue;
                    }
                    result.set(i - lineSkipped, j - rowSkipped, get(i, j));
                }
            }
            return result;
        }

        Matrix minor(int line, int row) {
            size_t l = line;
            size_t r = row;
            return minor(l, r);
        }

        T determinator() {
            if (height != width || height < 1) {
                throw new MatrixError("Determinator is not defined for this matrix.");
            }

            if (height == 2) {
                return get(1, 1) * get(0, 0) - get(1, 0) * get(0, 1);
            }

            T result = 0;
            size_t zero = 0;
            for (size_t i = 0; i < width; i++) {
                Matrix aMinor = minor(i, zero);
                result += (getSign(i, 0) ? 1 : -1) * get(i, 0) * aMinor.determinator();
            }

            return result;
        }

        T norm() {
            T result = 0;
            for (int i = 0; i < height; i++) {
                T lineSum = 0;
                for (int j = 0; j < width; j++) {
                    lineSum += get(j, i);
                }
                if (result < lineSum) {
                    result = lineSum;
                }
            }
            return result;
        }

        [[nodiscard]] std::pair<int, int> size() const {
            return {width, height};
        }
    };

}

#define functor(NAME, RET_TYPE, IN_TYPE, BODY) \
    class NAME : public ltrx::Matrix<IN_TYPE>::AbstractFunctor<RET_TYPE> { \
        virtual RET_TYPE invoke(IN_TYPE& elem) BODY \
    }

#endif //LTRX_LTRX_H
