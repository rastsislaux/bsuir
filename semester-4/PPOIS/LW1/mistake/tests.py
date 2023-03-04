#! /bin/python
import pytest

from ltrx import Matrix, MatrixError


def test_matrix_equals_must_return_false_when_matrices_are_not_equal():
    m1 = Matrix(default_value=1, width=2, height=2)
    m2 = Matrix(default_value=2, width=2, height=2)

    assert m1 != m2


def test_matrix_equals_must_return_true_when_matrices_are_equal():
    m1 = Matrix(default_value=1, width=2, height=2)
    m2 = Matrix(default_value=1, width=2, height=2)

    assert m1 == m2


def test_matrix_add_must_raise_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 + m2


def test_matrix_add_must_pass_when_size_equals():
    m1 = Matrix(default_value=1, width=1, height=1)
    m2 = Matrix(default_value=1, width=1, height=1)

    assert m1 + m2 == Matrix(default_value=2, width=1, height=1)


def test_matrix_add_must_pass_with_number():
    m1 = Matrix(default_value=1, width=2, height=2)

    assert m1 + 1 == Matrix(default_value=2, width=2, height=2)


def test_matrix_iadd_must_throw_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 += m2


def test_matrix_iadd_must_pass_when_size_equals():
    m1 = Matrix(default_value=1, width=1, height=3)
    m2 = Matrix(default_value=2, width=1, height=3)
    m1 += m2

    assert m1 == Matrix(default_value=3, width=1, height=3)


def test_matrix_iadd_must_pass_when_number():
    m1 = Matrix(default_value=1, width=1, height=3)
    v2 = 2
    m1 += v2

    assert m1 == Matrix(default_value=3, width=1, height=3)


def test_matrix_sub_must_raise_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 - m2


def test_matrix_sub_must_pass_when_size_equals():
    m1 = Matrix(default_value=1, width=1, height=1)
    m2 = Matrix(default_value=1, width=1, height=1)

    assert m1 - m2 == Matrix(default_value=0, width=1, height=1)


def test_matrix_sub_must_pass_with_number():
    m1 = Matrix(default_value=1, width=2, height=2)

    assert m1 - 1 == Matrix(default_value=0, width=2, height=2)


def test_matrix_isub_must_throw_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 -= m2


def test_matrix_isub_must_pass_when_size_equals():
    m1 = Matrix(default_value=1, width=1, height=3)
    m2 = Matrix(default_value=2, width=1, height=3)
    m1 -= m2

    assert m1 == Matrix(default_value=-1, width=1, height=3)


def test_matrix_isub_must_pass_when_number():
    m1 = Matrix(default_value=1, width=1, height=3)
    v2 = 2
    m1 -= v2

    assert m1 == Matrix(default_value=-1, width=1, height=3)


def test_matrix_mul_must_raise_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 * m2


def test_matrix_mul_must_pass_when_size_equals():
    m1 = Matrix(default_value=1, width=1, height=1)
    m2 = Matrix(default_value=1, width=1, height=1)

    assert m1 * m2 == Matrix(default_value=1, width=1, height=1)


def test_matrix_mul_must_pass_with_number():
    m1 = Matrix(default_value=1, width=2, height=2)

    assert m1 * 1 == Matrix(default_value=1, width=2, height=2)


def test_matrix_imul_must_throw_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 *= m2


def test_matrix_imul_must_pass_when_size_equals():
    m1 = Matrix(default_value=1, width=1, height=3)
    m2 = Matrix(default_value=2, width=1, height=3)
    m1 *= m2

    assert m1 == Matrix(default_value=2, width=1, height=3)


def test_matrix_imul_must_pass_when_number():
    m1 = Matrix(default_value=1, width=1, height=3)
    v2 = 2
    m1 *= v2

    assert m1 == Matrix(default_value=2, width=1, height=3)


def test_matrix_div_must_raise_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 / m2


def test_matrix_div_must_pass_when_size_equals():
    m1 = Matrix(default_value=4, width=1, height=1)
    m2 = Matrix(default_value=2, width=1, height=1)

    assert m1 / m2 == Matrix(default_value=2, width=1, height=1)


def test_matrix_div_must_pass_with_number():
    m1 = Matrix(default_value=4, width=2, height=2)

    assert m1 / 2 == Matrix(default_value=2, width=2, height=2)


def test_matrix_idiv_must_throw_when_size_not_equals():
    m1 = Matrix(width=1, height=3)
    m2 = Matrix(width=4, height=2)

    with pytest.raises(MatrixError):
        m1 /= m2


def test_matrix_idiv_must_pass_when_size_equals():
    m1 = Matrix(default_value=4, width=1, height=3)
    m2 = Matrix(default_value=2, width=1, height=3)
    m1 /= m2

    assert m1 == Matrix(default_value=2, width=1, height=3)


def test_matrix_idiv_must_pass_when_number():
    m1 = Matrix(default_value=4, width=1, height=3)
    v2 = 2
    m1 /= v2

    assert m1 == Matrix(default_value=2, width=1, height=3)


def test_matrix_pow_must_pass_with_number():
    m1 = Matrix(default_value=2, width=2, height=2)

    assert m1 ** 3 == Matrix(default_value=8, width=2, height=2)


def test_matrix_ipow_must_pass_when_number():
    m1 = Matrix(default_value=2, width=1, height=3)
    v2 = 3
    m1 **= v2

    assert m1 == Matrix(default_value=8, width=1, height=3)


def test_matrix_minor():
    m1 = Matrix(default_value=0, width=3, height=3)
    expected = Matrix(default_value=0, width=2, height=2)

    assert expected == m1.minor(0, 0)


def test_matrix_determinator_must_be_zero_when_all_equal():
    m1 = Matrix(default_value=0, width=3, height=3)

    assert 0 == m1.determinator()


def test_matrix_determinator_must_pass():
    m1 = Matrix(default_value=2, width=4, height=4)
    m1.set(0, 0, 9)
    m1.set(1, 1, 9)
    m1.set(2, 2, 9)
    m1.set(3, 3, 9)

    assert m1.determinator() == 5145


def test_matrix_norm_must_pass():
    m1 = Matrix(default_value=2, width=3, height=3)
    m1.set(0, 0, 1)
    m1.set(0, 1, 2)
    m1.set(0, 2, 3)

    assert m1.norm() == 7
