import numpy as np

from .autoencoder import Autoencoder


class NoopAdaptiveLearningRateStrategy(Autoencoder.AdaptiveLearningStrategy):
    def adapt_learning_rate(self, current_learning_rate, current_error, previous_error, **kwargs):
        return current_learning_rate


class SimpleAdaptiveLearningRateStrategy(Autoencoder.AdaptiveLearningStrategy):
    def __init__(self):
        self.min_lr = 0.0001
        self.max_lr = 0.01

    def adapt_learning_rate(self, current_learning_rate, current_error, previous_error):
        if current_error > previous_error:
            current_learning_rate *= 0.5
        else:
            current_learning_rate *= 1.05
        return max(self.min_lr, min(self.max_lr, current_learning_rate))


class GolovkoLearningRateStrategy(Autoencoder.AdaptiveLearningStrategy):
    def adapt_learning_rate(self, current_learning_rate, current_error, previous_error, **kwargs) -> float:
        quad_sum = np.sum(kwargs['inp'] ** 2) + 10
        return 1 / (10 * quad_sum)


class MovingAverageLearningRateStrategy(Autoencoder.AdaptiveLearningStrategy):
    def __init__(self, initial_lr, min_lr=0.0002, max_lr=0.0015):
        self.error_history = []
        self.window_size = 5
        self.lr = initial_lr
        self.min_lr = min_lr
        self.max_lr = max_lr

    def adapt_learning_rate(self, current_learning_rate, current_error, _):
        self.error_history.append(current_error)

        if len(self.error_history) > self.window_size:
            self.error_history.pop(0)

            avg_recent = np.mean(self.error_history[-3:])
            avg_old = np.mean(self.error_history[:-3])

            if avg_recent > avg_old:
                self.lr *= 0.7
            elif avg_recent < avg_old * 0.99:  # Small improvement
                self.lr *= 1.1

            self.lr = max(self.min_lr, min(self.max_lr, self.lr))

        return self.lr
