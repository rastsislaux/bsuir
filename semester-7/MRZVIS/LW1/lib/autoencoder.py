import abc

import numpy as np
from numpy import ndarray


class Autoencoder:
    class AdaptiveLearningStrategy(abc.ABC):
        def adapt_learning_rate(self, current_learning_rate, current_error, previous_error, **kwargs) -> float:
            pass

    class VectorConverter(abc.ABC):
        def normalize_data(self, data) -> ndarray:
            pass

        def denormalize_data(self, data) -> ndarray:
            pass

    class NetworkIntrospector(abc.ABC):
        def reset(self):
            pass

        def set_epochs(self, epochs):
            pass

    class NoopNetworkIntrospector(NetworkIntrospector):
        pass

    class SimpleNetworkIntrospector(NetworkIntrospector):
        def __init__(self):
            self.epochs = 0

        def reset(self):
            self.epochs = 0

        def set_epochs(self, epochs):
            self.epochs = epochs

    class WeightInitializer(abc.ABC):
        def init_w1(self, input_dim, compressed_dim) -> ndarray:
            pass
        def init_w2(self, input_dim, compressed_dim) -> ndarray:
            pass

    class OrthogonalWeightInitializer(WeightInitializer):
        def init_w1(self, input_dim, compressed_dim) -> ndarray:
            return np.random.uniform(0, 1, size=(input_dim, compressed_dim))
        def init_w2(self, input_dim, compressed_dim) -> ndarray:
            return np.random.uniform(0, 1, size=(compressed_dim, input_dim))

    def __init__(self, input_dim, compressed_dim,
                 learning_rate_adaption_strategy: AdaptiveLearningStrategy,
                 normalizer: VectorConverter,
                 weight_initializer: WeightInitializer = OrthogonalWeightInitializer(),
                 learning_rate=0.001, required_error=1e-4, max_epochs=1000, batch_size=1,
                 introspector=NoopNetworkIntrospector()):
        self.input_dim = input_dim
        self.compressed_dim = compressed_dim
        self.learning_rate = learning_rate
        self.required_error = required_error
        self.max_epochs = max_epochs
        self.batch_size = batch_size
        self.learning_rate_adoption_strategy = learning_rate_adaption_strategy
        self.normalizer = normalizer
        self.introspector=introspector

        self.W1 = weight_initializer.init_w1(input_dim, compressed_dim)
        self.W2 = weight_initializer.init_w2(input_dim, compressed_dim)

    def forward(self, x):
        self.encoded = x @ self.W1
        self.decoded = self.encoded @ self.W2
        return self.decoded

    def backward(self, output, batch, y):
        error = output - batch
        grad_W2 = y.T @ error

        error_y = error @ self.W2.T
        grad_W1 = output.T @ error_y


        self.learning_rate = self.learning_rate_adoption_strategy.adapt_learning_rate(
            self.learning_rate, float('inf'), float('inf'), inp=y, w=self.W2
        )

        self.W2 -= self.learning_rate * grad_W2

        for i in range(self.W2.shape[0]):
            norm = np.linalg.norm(self.W2[i])
            if norm > 0:
                self.W2[i] /= norm

        self.learning_rate = self.learning_rate_adoption_strategy.adapt_learning_rate(
            self.learning_rate, float('inf'), float('inf'), inp=output, w=self.W1
        )
        self.W1 -= self.learning_rate * grad_W1
        for i in range(self.W1.shape[0]):
            norm = np.linalg.norm(self.W1[i])
            if norm > 0:
                self.W1[i] /= norm

    def train(self, data):
        data = self.normalizer.normalize_data(data)
        previous_error = float('inf')
        current_error = float('inf')
        epoch = 0
        n_samples = data.shape[0]

        while current_error > self.required_error and epoch < self.max_epochs:
            epoch += 1

            indices = range(n_samples)
            for i in range(0, n_samples, self.batch_size):
                batch_indices = indices[i:min(i + self.batch_size, n_samples)]
                batch = data[batch_indices]
                output = self.forward(batch)
                self.backward(output, batch, self.encoded)

            x1 = (data @ self.W1) @ self.W2
            current_error = 0.5 * np.sum((x1 - data) ** 2)
            print(f"Epoch [{epoch}], Error: {current_error:.6f}, Learning rate: {self.learning_rate:.6f}")

            previous_error = current_error


        self.introspector.set_epochs(epoch)
        print(f"Training completed in {epoch} epochs with final error: {current_error:.6f}")

    def compress(self, data):
        data = self.normalizer.normalize_data(data)
        return data @ self.W1

    def decompress(self, compressed_data):
        reconstructed = compressed_data @ self.W2
        return self.normalizer.denormalize_data(reconstructed)
