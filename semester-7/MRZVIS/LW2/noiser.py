import numpy as np
import matplotlib.pyplot as plt

from network import HopfieldNetwork
from vectorization import PlainVectorizer, Vectorizer


def switch_values(arr: np.ndarray, percent: float) -> np.ndarray:
    """
    Switch a given percentage of values in a {-1, 1} ndarray of any dimension.

    Parameters:
    arr (np.ndarray): Input array containing values of -1 and 1.
    percent (float): Percentage of values to switch (0 to 100).

    Returns:
    np.ndarray: New array with switched values.
    """
    if not (0 <= percent <= 100):
        raise ValueError("Percent must be between 0 and 100")

    # Calculate how many values to switch
    total_elements = arr.size
    num_to_switch = int((percent / 100) * total_elements)

    # Get indices of the elements to switch
    indices_to_switch = np.random.choice(total_elements, num_to_switch, replace=False)

    # Create a copy of the array to modify
    arr_flipped = arr.copy()

    # Switch the values at the selected indices
    arr_flipped[np.unravel_index(indices_to_switch, arr.shape)] *= -1  # Flip -1 to 1 and 1 to -1

    return arr_flipped

def load_training_dataset(size, vectorizer: Vectorizer):
    png_files = [
        f"letters/o.png",
        f"letters/x.png",
        f"letters/t.png"
    ]

    dataset = []
    for path in png_files:
        dataset.append(vectorizer.vectorize(path))

    return np.array(dataset)


def do_recall():
    categories = []
    values = []

    first_recovered_pattern = None

    for percent in range(1, 100):
        patterns = load_training_dataset(percent, vectorizer=PlainVectorizer())

        network = HopfieldNetwork(input_size=patterns.shape[1])
        network.train(patterns, repeats=1)

        noisy, shape = PlainVectorizer().vectorize("letters/o.png", return_original_shape=True)
        noisy = switch_values(noisy, percent)
        recovered_pattern, iterations = network.recall(noisy, return_iterations=True)

        if first_recovered_pattern is None:
            first_recovered_pattern = recovered_pattern
        else:
            if not np.array_equal(first_recovered_pattern, recovered_pattern):
                break

        categories.append(percent)
        values.append(iterations)

        # PlainVectorizer().devectorize(recovered_pattern, shape).show()

    plt.figure()
    plt.scatter(categories, values, color='blue', marker='o')
    plt.plot(categories, values, color='skyblue')

    plt.title("Зависимость количества итераций от зашумленности образа")
    plt.xlabel('Заушмленность образа (%)')
    plt.ylabel('Кол-во итераций')

    plt.tight_layout()
    plt.show()


if __name__ == "__main__":
    do_recall()
