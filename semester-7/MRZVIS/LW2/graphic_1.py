import numpy as np

from network import HopfieldNetwork
from vectorization import PlainVectorizer, Vectorizer
import matplotlib.pyplot as plt


def load_training_dataset(size, vectorizer: Vectorizer):
    png_files = [
        f"resized_images/o_{size}.png",
        f"resized_images/x_{size}.png",
        f"resized_images/t_{size}.png"
    ]

    dataset = []
    for path in png_files:
        dataset.append(vectorizer.vectorize(path))

    return np.array(dataset)


def do_recall():
    categories = []
    values = []
    for size in range(21, 160, 10):
        patterns = load_training_dataset(size, vectorizer=PlainVectorizer())

        network = HopfieldNetwork(input_size=patterns.shape[1])
        network.train(patterns, repeats=1)

        noisy, shape = PlainVectorizer().vectorize(f"resized_images_noisy/o_{size}.png", return_original_shape=True)
        recovered_pattern, iterations = network.recall(noisy, return_iterations=True)

        categories.append(size)
        values.append(iterations)

        # PlainVectorizer().devectorize(recovered_pattern, shape).show()

    plt.figure()
    plt.scatter(categories, values, color='blue', marker='o')
    plt.plot(categories, values, color='skyblue')

    plt.title("Зависимость количества итераций от размера образа")
    plt.xlabel('Размер картинки')
    plt.ylabel('Кол-во итераций')

    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    do_recall()
