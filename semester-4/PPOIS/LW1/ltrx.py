from copy import copy


class MatrixError(Exception):
    pass


class Matrix:

    def __init__(self, width=0, height=0, default_value=None):
        self._matrix: list[list[object]] = []
        for i in range(width):
            new = []
            for j in range(height):
                new.append(default_value)
            self._matrix.append(new)
        self._width: int = width
        self._height: int = height

    @staticmethod
    def _get_sign(i: int, j: int):
        if i % 2 == 0:
            return j % 2 == 0
        else:
            return j % 2 == 1

    @property
    def size(self) -> (int, int):
        return self._width, self._height

    def set(self, horizontal_index: int, vertical_index: int, value: object):
        self._matrix[horizontal_index][vertical_index] = value

    def get(self, horizontal_index: int, vertical_index: int) -> object:
        return self._matrix[horizontal_index][vertical_index]

    def _check_size_equals(self, matrix: "Matrix"):
        if self.size != matrix.size:
            raise MatrixError("This operation is not supported for matrices of different sizes.")

    def _add_matrix(self, other: "Matrix") -> "Matrix":
        self._check_size_equals(other)
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] + other._matrix[i][j])
        return self

    def _add_value(self, other: object) -> "Matrix":
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] + other)
        return self

    def _sub_matrix(self, other: "Matrix") -> "Matrix":
        self._check_size_equals(other)
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] - other._matrix[i][j])
        return self

    def _sub_value(self, other: object) -> "Matrix":
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] - other)
        return self

    def _mul_matrix(self, other: "Matrix") -> "Matrix":
        self._check_size_equals(other)
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] * other._matrix[i][j])
        return self

    def _mul_value(self, other: object) -> "Matrix":
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] * other)
        return self

    def _div_matrix(self, other: "Matrix") -> "Matrix":
        self._check_size_equals(other)
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] / other._matrix[i][j])
        return self

    def _div_value(self, other: object) -> "Matrix":
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] / other)
        return self

    def _pow_value(self, other: "Matrix"):
        for i in range(self._width):
            for j in range(self._height):
                self.set(i, j, self._matrix[i][j] ** other)
        return self

    def __eq__(self, other: "Matrix") -> bool:
        return self._matrix == other._matrix

    def __ne__(self, other: "Matrix") -> bool:
        return self._matrix != other._matrix

    def __add__(self, other):
        if isinstance(other, Matrix):
            return copy(self)._add_matrix(other)
        return copy(self)._add_value(other)

    def __iadd__(self, other):
        if isinstance(other, Matrix):
            return self._add_matrix(other)
        return self._add_value(other)

    def __sub__(self, other):
        if isinstance(other, Matrix):
            return copy(self)._sub_matrix(other)
        return copy(self)._sub_value(other)

    def __isub__(self, other):
        if isinstance(other, Matrix):
            return self._sub_matrix(other)
        return self._sub_value(other)

    def __mul__(self, other):
        if isinstance(other, Matrix):
            return copy(self)._mul_matrix(other)
        return copy(self)._mul_value(other)

    def __imul__(self, other):
        if isinstance(other, Matrix):
            return self._mul_matrix(other)
        return self._mul_value(other)

    def __truediv__(self, other):
        if isinstance(other, Matrix):
            return copy(self)._div_matrix(other)
        return copy(self)._div_value(other)

    def __itruediv__(self, other):
        if isinstance(other, Matrix):
            return self._div_matrix(other)
        return self._div_value(other)

    def __pow__(self, power, modulo=None):
        return copy(self)._pow_value(power)

    def __ipow__(self, other):
        return self._pow_value(other)

    def __repr__(self):
        return str(self._matrix)

    def minor(self, line: int, row: int) -> "Matrix":
        result: "Matrix" = Matrix(self._width - 1, self._height - 1)
        line_skipped: int = 0
        for i in range(self._width):
            if i == line:
                line_skipped = 1
                continue
            row_skipped: int = 0
            for j in range(self._height):
                if j == row:
                    row_skipped = 1
                    continue
                result.set(i - line_skipped, j - row_skipped, self.get(i, j))
        return result

    def determinator(self):
        if self._height != self._width or self._height < 1:
            raise MatrixError("Determinator is not defined for this matrix")

        if self._height == 2:
            return self.get(1, 1) * self.get(0, 0) - self.get(1, 0) * self.get(0, 1)

        result = 0
        for i in range(self._width):
            a_minor: Matrix = self.minor(i, 0)
            if Matrix._get_sign(i, 0):
                result += self.get(i, 0) * a_minor.determinator()
            else:
                result += -self.get(i, 0) * a_minor.determinator()
        return result

    def norm(self):
        result = 0
        for i in range(self._height):
            line_sum = 0
            for j in range(self._width):
                line_sum += self.get(j, i)
            if result < line_sum:
                result = line_sum
        return result
