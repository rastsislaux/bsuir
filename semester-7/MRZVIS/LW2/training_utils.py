import os

import numpy as np

from vectorization import Vectorizer


def load_training_dataset(folder_path, vectorizer: Vectorizer):
    if not os.path.isdir(folder_path):
        raise ValueError(f"The specified path '{folder_path}' is not a directory.")

    png_files = []
    for root, _, files in os.walk(folder_path):
        for path in files:
            if path.lower().endswith('.png'):
                png_files.append(os.path.join(root, path))

    dataset = []
    for path in png_files:
        dataset.append(vectorizer.vectorize(path))

    return np.array(dataset)
