from typing import Callable

import numpy as np


class HopfieldNetwork:
    def __init__(self,
                 input_size,
                 activate: Callable[[np.ndarray], np.ndarray] = np.tanh,
                 max_iterations=1000,
                 tolerance=1e-6):
        self.input_size = input_size
        self.weights = np.zeros((input_size, input_size))
        self.max_iterations = max_iterations
        self.tolerance = tolerance

        self.activation = activate

    def train(self, patterns, repeats=1):
        n_patterns = len(patterns)
        patterns = np.repeat(patterns, repeats, axis=0)

        for i in range(len(patterns)):
            pattern = patterns[i].reshape(-1, 1)
            self.weights += pattern @ pattern.T

        np.fill_diagonal(self.weights, 0)

        self.weights /= n_patterns * repeats

    def recall(self, pattern, return_iterations=False):
        state = pattern.copy()
        iterations = 0

        while iterations < self.max_iterations:
            prev_state = state.copy()

            for i in range(self.input_size):
                net_input = np.dot(self.weights[i], state)
                state[i] = self.activation(net_input)

            iterations += 1

            if np.all(np.abs(state - prev_state) < self.tolerance):
                break

        output = np.where(state > 0, 1, -1)

        if return_iterations:
            return output, iterations
        return output
