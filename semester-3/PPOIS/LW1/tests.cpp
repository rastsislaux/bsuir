#include <gtest/gtest.h>
#include "ltrx.h"

TEST(MatrixEquals, MustReturnFalseWhenMatrixesAreNotEqual) {
    auto m1 = ltrx::Matrix<double>(1, 4, 4);
    auto m2 = ltrx::Matrix<double>(2, 4, 4);

    ASSERT_NE(m1, m2);
}

TEST(MatrixEquals, MustReturnTrueWhenMatrixesAreEqual) {
    auto m1 = ltrx::Matrix<double>(1, 4, 4);
    auto m2 = ltrx::Matrix<double>(1, 4, 4);

    ASSERT_EQ(m1, m2);
}

TEST(MatrixPostIncrement, MustIncreaseEveryElementOfMatrixByOne) {
    auto m1 = ltrx::Matrix<double>(0, 5, 5);
    m1++;

    for (int i = 0; i < m1.getWidth(); i++) {
        for (int j = 0; j < m1.getHeight(); j++) {
            ASSERT_EQ(1, m1.get(i, j));
        }
    }
}

TEST(MatrixPreIncrement, MustIncreaseEveryElementOfMatrixByOne) {
    auto m1 = ltrx::Matrix<double>(0, 5, 5);
    ++m1;

    for (int i = 0; i < m1.getWidth(); i++) {
        for (int j = 0; j < m1.getHeight(); j++) {
            ASSERT_EQ(1, m1.get(i, j));
        }
    }
}

TEST(MatrixPostDecrement, MustDecreaseEveryElementOfMatrixByOne) {
    auto m1 = ltrx::Matrix<double>(0, 5, 5);
    m1--;

    for (int i = 0; i < m1.getWidth(); i++) {
        for (int j = 0; j < m1.getHeight(); j++) {
            ASSERT_EQ(-1, m1.get(i, j));
        }
    }
}

TEST(MatrixPreDecrement, MustDecreaseEveryElementOfMatrixByOne) {
    auto m1 = ltrx::Matrix<double>(0, 5, 5);
    --m1;

    for (int i = 0; i < m1.getWidth(); i++) {
        for (int j = 0; j < m1.getHeight(); j++) {
            ASSERT_EQ(-1, m1.get(i, j));
        }
    }
}

TEST(MatrixAdd, MustThrowWhenSizeIsNotEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(1, 5, 5);

    ASSERT_THROW(m1 + m2, ltrx::MatrixError);
}

TEST(MatrixAdd, MustPassWhenSizeIsEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(3, 6, 6);

    auto expected = ltrx::Matrix<double>(4, 6, 6);

    ASSERT_EQ(m1 + m2, expected);
}

TEST(MatrixAdd, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(3, 5, 5);
    auto expected = ltrx::Matrix<double>(8, 5, 5);

    ASSERT_EQ(m1 + 5, expected);
}

TEST(MatrixPlusEquals, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(3, 5, 5);
    auto expected = ltrx::Matrix<double>(8, 5, 5);

    ASSERT_EQ(m1 += 5, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixPlusEquals, MustThrowWhenSizeIsNotEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(1, 5, 5);

    ASSERT_THROW(m1 += m2, ltrx::MatrixError);
}

TEST(MatrixPlusEquals, MustPassWhenSizeIsEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(3, 6, 6);

    auto expected = ltrx::Matrix<double>(4, 6, 6);

    ASSERT_EQ(m1 += m2, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixSub, MustThrowWhenSizeIsNotEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(1, 5, 5);

    ASSERT_THROW(m1 - m2, ltrx::MatrixError);
}

TEST(MatrixSub, MustPassWhenSizeIsEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(3, 6, 6);

    auto expected = ltrx::Matrix<double>(-2, 6, 6);

    ASSERT_EQ(m1 - m2, expected);
}

TEST(MatrixSub, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(3, 5, 5);
    auto expected = ltrx::Matrix<double>(-2, 5, 5);

    ASSERT_EQ(m1 - 5, expected);
}

TEST(MatrixMinusEquals, MustThrowWhenSizeIsNotEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(1, 5, 5);

    ASSERT_THROW(m1 -= m2, ltrx::MatrixError);
}

TEST(MatrixMinusEquals, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(3, 5, 5);
    auto expected = ltrx::Matrix<double>(-2, 5, 5);

    ASSERT_EQ(m1 -= 5, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixMinusEquals, MustPassWhenSizeIsEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(3, 6, 6);

    auto expected = ltrx::Matrix<double>(-2, 6, 6);

    ASSERT_EQ(m1 -= m2, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixMul, MustThrowWhenSizeIsNotEqual) {
    auto m1 = ltrx::Matrix<double>(3, 6, 6);
    auto m2 = ltrx::Matrix<double>(5, 5, 5);

    ASSERT_THROW(m1 * m2, ltrx::MatrixError);
}

TEST(MatrixMul, MustPassWhenSizeIsEqual) {
    auto m1 = ltrx::Matrix<double>(3, 6, 6);
    auto m2 = ltrx::Matrix<double>(5, 6, 6);

    auto expected = ltrx::Matrix<double>(15, 6, 6);

    ASSERT_EQ(m1 * m2, expected);
}

TEST(MatrixMul, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(3, 5, 5);
    auto expected = ltrx::Matrix<double>(15, 5, 5);

    ASSERT_EQ(m1 * 5, expected);
}

TEST(MatrixMulEquals, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(3, 5, 5);
    auto expected = ltrx::Matrix<double>(15, 5, 5);

    ASSERT_EQ(m1 *= 5, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixMulEquals, MustThrowWhenSizeIsNotEqual) {
    auto m1 = ltrx::Matrix<double>(1, 6, 6);
    auto m2 = ltrx::Matrix<double>(1, 5, 5);

    ASSERT_THROW(m1 *= m2, ltrx::MatrixError);
}

TEST(MatrixMulEquals, MustPassWhenSizeIsEqual) {
    auto m1 = ltrx::Matrix<double>(5, 6, 6);
    auto m2 = ltrx::Matrix<double>(3, 6, 6);

    auto expected = ltrx::Matrix<double>(15, 6, 6);

    ASSERT_EQ(m1 *= m2, m1);
    ASSERT_EQ(m1, expected);
}


TEST(MatrixDiv, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(15, 5, 5);
    auto expected = ltrx::Matrix<double>(3, 5, 5);

    ASSERT_EQ(m1 / 5, expected);
}

TEST(MatrixDivEquals, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(15, 5, 5);
    auto expected = ltrx::Matrix<double>(3, 5, 5);

    ASSERT_EQ(m1 /= 5, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixPow, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(2, 5, 5);
    auto expected = ltrx::Matrix<double>(8, 5, 5);

    ASSERT_EQ(m1 ^ 3, expected);
}

TEST(MatrixPowEquals, MustPassWhenByNumber) {
    auto m1 = ltrx::Matrix<double>(2, 5, 5);
    auto expected = ltrx::Matrix<double>(8, 5, 5);

    ASSERT_EQ(m1 ^= 3, m1);
    ASSERT_EQ(m1, expected);
}

TEST(MatrixMinor, MustPass) {
    auto matrix1 = ltrx::Matrix<double>(0, 3, 3);
    auto expected = ltrx::Matrix<double>(0, 2, 2);

    ASSERT_EQ(matrix1.minor(0, 0), expected);
}

TEST(MatrixDeterminator, MustBeEqualToZeroWhenAllElementsAreEqual) {
    auto matrix = ltrx::Matrix<double>(5, 5, 5);

    ASSERT_EQ(matrix.determinator(), 0);
}

TEST(MatrixDeterminator, MustPassWhenAnyMatrixIsPassed) {
    auto matrix = ltrx::Matrix<double>(2, 4, 4);
    matrix.set(0, 0, 9);
    matrix.set(1, 1, 9);
    matrix.set(2, 2, 9);
    matrix.set(3, 3, 9);

    ASSERT_EQ(matrix.determinator(), 5145);
}

TEST(MatrixNorm, mustPass) {
    auto m = ltrx::Matrix<int>(2, 3, 3);
    m.set(0, 0, 1);
    m.set(0, 1, 2);
    m.set(0, 2, 3);

    ASSERT_EQ(m.norm(), 7);
}