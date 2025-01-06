import abc

import numpy as np
from PIL import Image


class Vectorizer(abc.ABC):
    def vectorize(self, path, return_original_shape=False):
        pass

    def devectorize(self, array, shape):
        pass

class PlainVectorizer(Vectorizer):
    def vectorize(self, path, return_original_shape=False):
        letter_image = Image.open(path).convert('L')
        array_2d = np.array(letter_image)
        array_1d = array_2d.reshape(array_2d.shape[0] * array_2d.shape[1]) // 255
        b_array_1d = np.where(array_1d >= 0.5, 1, -1)
        if return_original_shape:
            return b_array_1d, array_2d.shape
        else:
            return b_array_1d

    def devectorize(self, array, shape):
        array_2d = (((array + 1) // 2) * 255).reshape(shape)
        return Image.fromarray(array_2d.astype(np.uint8))
