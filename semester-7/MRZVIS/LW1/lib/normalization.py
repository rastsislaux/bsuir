import numpy as np
from numpy import ndarray

from .autoencoder import Autoencoder


class ImageVectorConverter(Autoencoder.VectorConverter):
    def normalize_data(self, data):
        return data / 255.0

    def denormalize_data(self, data):
        return np.clip(data * 255, 0, 255).astype(np.uint8)

class NoopNormalizer(Autoencoder.VectorConverter):
    def normalize_data(self, data) -> ndarray:
        return data

    def denormalize_data(self, data) -> ndarray:
        return data
