import abc

import numpy as np
from PIL import Image


class Vectorizer(abc.ABC):
    def open_as_array(self, file_path):
        pass

    def divide_into_rectangles(self, arr, m, n):
        pass

    def reshape_to_vectors(self, rects):
        pass


class VectorizerImpl(Vectorizer):
    def open_as_array(self, file_path):
        image = Image.open(file_path)
        image = image.convert('L')
        return np.array(image)

    def divide_into_rectangles(self, arr, m, n):
        w, h = arr.shape

        squares = np.empty((w // m, h // n, m, n), dtype=arr.dtype)
        for i in range(w // m):
            for j in range(h // n):
                squares[i, j] = arr[i * m:(i + 1) * m, j * n:(j + 1) * n]

        return squares

    def reshape_to_vectors(self, rects):
        return rects.reshape(rects.shape[0] * rects.shape[1], rects.shape[2] * rects.shape[3])


class Devectorizer(abc.ABC):
    def reshape_to_sqaures(self, vectors, shape, m, n):
        pass

    def unite_rectangles(self, rects):
        pass


class DevectorizerImpl(Devectorizer):

    def reshape_to_sqaures(self, vectors, shape, m, n):
        return vectors.reshape(shape[0] // m, shape[1] // n, m, n)

    def unite_rectangles(self, rects):
        horizontal_concat = np.concatenate(rects, axis=1)
        return np.concatenate(horizontal_concat, axis=1)
