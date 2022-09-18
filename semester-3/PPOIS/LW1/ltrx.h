/**
 * @file ltrx.h
 * @author R. Lipski (rostislav.lipsky@gmail.com)
 * @brief This file is main file of LTRX library, which was created to work with numeric matrices.
 * @version 0.1
 * @date 2022-09-18
 * 
 * @copyright Copyright (c) 2022
 * 
 * @warning Everything is contained in a header file, as templates do not support sharding.
 * 
 */

//
// Created by rostislav on 9/6/22.
//

#ifndef LTRX_LTRX_H
#define LTRX_LTRX_H

#include <vector>
#include <exception>
#include <cmath>

namespace ltrx {

    /**
     * @brief Base exception class for LTRX errors
     * @details Base exception class for LTRX errors. Inherits from std::runtime_error
     * @author R. Lipski
     */
    class MatrixError : public std::runtime_error {
    public:
       /**
        * @brief Construct a new Matrix Error object
        * @details Basic constructor that consumes a string which stands for exception message, calls for std::runtime_error::runtime_error(std::string)
        * 
        * @param msg - message about what went wrong
        * 
        * @author R. Lipski
        */
        explicit MatrixError(std::string msg) : std::runtime_error(msg) { }
    };

    /**
     * @brief Matrix class
     * @details Class that represents matrix of any numeric type, supports some features of functional-style programming, all of basic math operators.
     * 
     * @tparam T - type of matrix elements
     * 
     * @warning supports only numeric types (int, long, float, double, etc.)
     * 
     * @author R. Lipski
     */
    template<typename T>
    class Matrix {
    private:
        std::vector<std::vector<T>> matrix;
        size_t width;
        size_t height;

    public:
       /**
        * @brief Abstract class to represent functors.
        * @details Abstract class to represent functors. Functors are ltrx-specific objects, that have invoke() method, which must consume element of a matrix and return new element to be stored in a new matrix.
        * 
        * @tparam K is a type that functor returns.
        * 
        * @author R. Lipski
        */
        template <typename K>
        class AbstractFunctor {
        public:
            virtual K invoke(T& elem) = 0;
        };

    private:
        /**
         * @brief Macro that generates a basic functor constructor.
         * 
         * @author R. Lipski
         */
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

        /**
         * @brief Get the sign for determinatnor calculation
         * 
         * @param i is a number of line
         * @param j is a number of row
         * @return true if the sign is plus
         * @return false if the sign is minus
         * 
         * @author R. Lipski
         */
        static bool getSign(size_t i, size_t j) {
                return (i % 2 == 0)
                    ? (j % 2 == 0)
                    : (j % 2 == 1);
        }

        /**
         * @brief Check if other matrix is of the same size. Throws an exception if not.
         * 
         * @param s other matrix
         * 
         * @author R. Lipski
         */
        void checkSizeEquals(const Matrix& s) {
            auto size = this->size();
            if (s.size() != size) {
                throw MatrixError("This operation is not supported for matrixes of different size.");
            }
        }

        /**
         * @brief Find the sum of matrixes.
         * @details Sums all correspondings elements of matrix, changing this object.
         * 
         * @param other is other matrix
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& add(const Matrix& other) {
            checkSizeEquals(other);
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] + other.matrix[i][j]);
                }
            }
            return *this;
        }

        /**
         * @brief Find the sum of a matrix and a number.
         * @details Sums all elements of matrix with a number, changing this object.
         * 
         * @param value is the value to sum with
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& add(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] + value);
                }
            }
            return *this;
        }

        /**
         * @brief Find the difference of two matrices.
         * @details Subtracts corresponding elements of other matrix from current matrix, modifying this object.
         * 
         * @param other is matrix to subtract
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& sub(const Matrix& other) {
            checkSizeEquals(other);
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] - other.matrix[i][j]);
                }
            }
            return *this;
        }

        /**
         * @brief Find the difference of matrix and a number.
         * @details Subtract the number from each element of current matrix, modifying this object.
         * 
         * @param value is the number to subtract
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& sub(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] - value);
                }
            }
            return *this;
        }

        /**
         * @brief Find the product of matrices.
         * @details Multiply each element of current matrix on corresponding elemnt of other matrix, modifying this object.
         * 
         * @param other is matrix ti multiply by
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& mul(const Matrix& other) {
            checkSizeEquals(other);
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] * other.matrix[i][j]);
                }
            }
            return *this;
        }

        /**
         * @brief Find the product of matrix and a number.
         * @details Multiply each element of current matrix on corresponding element of other matrix, modifying this object.
         * 
         * @param value number to multiply by
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& mul(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] * value);
                }
            }
            return *this;
        }

        /**
         * @brief Find the quotient of a matrix and a number.
         * @details Divide each element of current matrix on a number, modifying this object.
         * 
         * @param value number to divide by
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& div(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, this->matrix[i][j] / value);
                }
            }
            return *this;
        }

        /**
         * @brief Find the power of a matrix.
         * @details Exponentianets each element of current matrix in the power of a number, modifying this object.
         * 
         * @param value power to exponentiate in the power of
         * @return Matrix& reference to this object
         * 
         * @author R. Lipski
         */
        Matrix& mpow(const T& value) {
            for (size_t i = 0; i < width; i++) {
                for (size_t j = 0; j < height; j++) {
                    this->set(i, j, pow(this->matrix[i][j], value));
                }
            }
            return *this;
        }

    public:
        /**
         * @brief Construct a new Matrix object
         * 
         * @param defaultValue value to initialize elements with
         * @param width width of a matrix
         * @param height height of a matrix
         * 
         * @author R. Lipski
         */
        Matrix(T defaultValue, size_t width, size_t height) {
            this->width = width;
            this->height = height;
            matrix.resize(width);
            for (auto& line : matrix) {
                line = std::vector<T>();
                line.resize(height, defaultValue);
            }
        }

        /**
         * @brief Construct a new Matrix object
         * 
         * @param width width of a matrix
         * @param height height of a matrix
         * 
         * @warning elements of matrix remain uninitialized
         * 
         * @author R. Lipski
         */
        Matrix(size_t width, size_t height) {
            this->width = width;
            this->height = height;
            matrix.resize(width);
            for (auto& line : matrix) {
                line = std::vector<T>();
                line.resize(height);
            }
        }

        /**
         * @brief Get the width of a Matrix
         * 
         * @return size_t width of a matrix
         * 
         * @author R. Lipski
         */
        size_t getWidth() {
            return width;
        }

        /**
         * @brief Get the height of a matrix
         * 
         * @return size_t height of a matrix
         * 
         * @author R. Lipski
         */
        size_t getHeight() {
            return height;
        }

        /**
         * @brief Get an element of a matrix
         * 
         * @param horizontalIndex location of an element horizontally
         * @param verticalIndex location of an element vertically
         * @return T element
         * 
         * @author R. Lipski
         */
        T get(size_t horizontalIndex, size_t verticalIndex) const {
            return matrix[horizontalIndex][verticalIndex];
        }

        /**
         * @brief Set an element of a matrix
         * 
         * @param horizontalIndex location of an element horizontally
         * @param verticalIndex location of an element vertically
         * @param value value to set
         * 
         * @author R. Lipski
         */
        void set(size_t horizontalIndex, size_t verticalIndex, T value) {
            matrix[horizontalIndex][verticalIndex] = value;
        }

        /**
         * @brief map each element of a matrix to a functor
         * 
         * @tparam K type of new elements of a matrix
         * @param f functor
         * @return Matrix<K> new matrix consisting of mapped values of current matrix
         * 
         * @author R. Lipski 
         */
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

        /**
         * @brief map each element of a certain line of a matrix to a functor
         * 
         * @tparam K type of new elements of a matrix
         * @param lineIndex number of line
         * @param f functor
         * @return Matrix<K>& reference to this object
         * 
         * @author R. Lipski
         * 
         * @warning trying to use a functor with return type different from current will lead to undefined behaviour.
         */
        template<typename K>
        Matrix<K>& mapLine(size_t lineIndex, AbstractFunctor<K>& f) {
            for (auto& elem : matrix[lineIndex]) {
                elem = f.invoke(elem);
            }
            return *this;
        }

        /**
         * @brief preincrement operator, see also sum()
         * 
         * @return Matrix& reference to this object
         */
        Matrix& operator++() {
            this->map(s_incFunctor);
            return *this;
        }

        /**
         * @brief postincrement operator, see alse sum()
         * 
         * @return Matrix new object
         */
        Matrix operator++(int) {
            Matrix<T> temp = *this;
            this->map(s_incFunctor);
            return temp;
        }

        /**
         * @brief predecrement operator, see also sub()
         * 
         * @return Matrix& reference to this object
         */
        Matrix& operator--() {
            this->map(s_decFunctor);
            return *this;
        }

        /**
         * @brief postdecrement operator, see alse sub()
         * 
         * @return Matrix new object
         */
        Matrix operator--(int) {
            Matrix<T> temp = *this;
            this->map(s_decFunctor);
            return temp;
        }

        /**
         * @brief sum operator, see alse sum()
         * 
         * @param other other matrix to sum with
         * @return Matrix new object
         */
        Matrix operator+(const Matrix& other) const {
            auto result = Matrix(*this);
            return result.add(other);
        }

        /**
         * @brief += operator, see alse sum()
         * 
         * @param other matrix to sum with
         * @return Matrix& reference to this object
         */
        Matrix& operator+=(const Matrix& other) {
            return add(other);
        }

        /**
         * @brief sum operator, see also sum()
         * 
         * @param other number to sum with
         * @return Matrix new object
         */
        Matrix operator+(const T& other) const {
            auto result = Matrix(*this);
            return result.add(other);
        }

        /**
         * @brief =+ operator, see alse sum()
         * 
         * @param other number to sum with
         * @return Matrix& reference to this object
         */
        Matrix& operator+=(const T& other) {
            return add(other);
        }

        /**
         * @brief subtract operator, see also sub()
         * 
         * @param other matrix to subract
         * @return Matrix new object
         */
        Matrix operator-(const Matrix& other) const {
            auto result = Matrix(*this);
            return result.sub(other);
        }

        /**
         * @brief -= operator, see also sub()
         * 
         * @param other matrix to subtract
         * @return Matrix& reference to this object
         */
        Matrix& operator-=(const Matrix& other) {
            return sub(other);
        }

        /**
         * @brief subtract operator, see also sub()
         * 
         * @param other number to subract
         * @return Matrix new object
         */
        Matrix operator-(const T& other) const {
            auto result = Matrix(*this);
            return result.sub(other);
        }

        /**
         * @brief -= operator, see also sub()
         * 
         * @param other number to subtract
         * @return Matrix& reference to this object
         */
        Matrix& operator-=(const T& other) {
            return sub(other);
        }

        /**
         * @brief multiply operator, see also mul()
         * 
         * @param other matrix to multiply by
         * @return Matrix new object
         */
        Matrix operator*(const Matrix& other) {
            auto result = Matrix(*this);
            return result.mul(other);
        }

        /**
         * @brief *= operator, see also mul()
         * 
         * @param other matrix to multiply by
         * @return Matrix& reference to this object
         */
        Matrix& operator*=(const Matrix& other) {
            return mul(other);
        }

        /**
         * @brief *= operator, see alse mul()
         * 
         * @param value value to multiply by
         * @return Matrix new object
         */
        Matrix operator*(const T& value) {
            auto result = Matrix(*this);
            return result.mul(value);
        }

        /**
         * @brief *= operator, see alse mul()
         * 
         * @param value value to multiply by
         * @return Matrix& reference to this object
         */
        Matrix& operator*= (const T& value) {
            return mul(value);
        }

        /**
         * @brief divide operator, see also div(const T& value)
         * 
         * @param value value to divide by
         * @return Matrix new object
         */
        Matrix operator/(const T& value) {
            auto result = Matrix(*this);
            return result.div(value);
        }

        /**
         * @brief /= operator, see alse div(const T& value)
         * 
         * @param value value to divide by
         * @return Matrix& reference to this object
         */
        Matrix& operator/=(const T& value) {
            return div(value);
        }

        /**
         * @brief power operator, see alse ltrx::Matrix::pow(const T& value)
         * 
         * @param value value to exponentiate in the power of
         * @return Matrix new object
         */
        Matrix operator^(const T& value) {
            auto result = Matrix(*this);
            return result.mpow(value);
        }

        /**
         * @brief ^= operator, see also pow()
         * 
         * @param value value to exponentiate in the power of
         * @return Matrix& reference to this object
         */
        Matrix& operator^=(const T& value) {
            return mpow(value);
        }

        /**
         * @brief equals operator
         * 
         * @param other other matrix
         * @return true is equal
         * @return false is NOT equal
         */
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

        /**
         * @brief find the minor of an element of a Matrix
         * 
         * @param line line of an element
         * @param row row of an element
         * @return Matrix new Matrix object, minor of an element in current Matrix
         */
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

        /**
         * @brief see ltrx::Matrix<T>::minor(size_t& line, size_t& row)
         * 
         * @param line line of an element
         * @param row row of an element
         * @return Matrix new Matrix object, minor of an element in current Matrix
         */
        Matrix minor(int line, int row) {
            size_t l = line;
            size_t r = row;
            return minor(l, r);
        }

        /**
         * @brief find the determinator of current Matrix
         * 
         * @return T determinator
         */
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

        /**
         * @brief find the norm a current Matrix
         * 
         * @return T norm
         */
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

        /**
         * @brief get size of current Matrix
         * 
         * @return std::pair<int, int> first is width, second is length
         */
        [[nodiscard]] std::pair<int, int> size() const {
            return {width, height};
        }
    };

}

/**
 * @brief easily generate basic functor
 * 
 */
#define functor(NAME, RET_TYPE, IN_TYPE, BODY) \
    class NAME : public ltrx::Matrix<IN_TYPE>::AbstractFunctor<RET_TYPE> { \
        virtual RET_TYPE invoke(IN_TYPE& elem) BODY \
    }

#endif //LTRX_LTRX_H
