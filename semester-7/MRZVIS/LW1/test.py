import numpy as np

from lib.learning_rate_adoption_strats import NoopAdaptiveLearningRateStrategy
from lib.autoencoder import Autoencoder
from lib.learning_rate_adoption_strats import MovingAverageLearningRateStrategy, GolovkoLearningRateStrategy
from lib.normalization import ImageVectorConverter
from lib.vectorization import VectorizerImpl
import matplotlib.pyplot as plt


def do_test_iterations_from_learning_rate():
    path = "./test_images/blackbuck.bmp"
    vectorizer = VectorizerImpl()
    m, n = 4, 4
    compress_coefficient = 2
    required_error = 20
    max_epochs = 15
    normalizer = ImageVectorConverter()

    arr = vectorizer.open_as_array(path)
    rects = vectorizer.divide_into_rectangles(arr, m, n)
    vectors = vectorizer.reshape_to_vectors(rects)
    input_dim = vectors.shape[1]
    output_dim = vectors.shape[1] // compress_coefficient

    alphas = np.linspace(0.01, 0.07, 10)
    result = {}
    for initial_lr in alphas:
        epochs = []
        for i in range(1):
            introspector = Autoencoder.SimpleNetworkIntrospector()
            ae = Autoencoder(
                input_dim=input_dim,
                compressed_dim=output_dim,
                required_error=required_error,
                max_epochs=max_epochs,
                learning_rate=initial_lr,
                learning_rate_adaption_strategy=NoopAdaptiveLearningRateStrategy(),
                normalizer=normalizer,
                introspector=introspector
            )
            ae.train(vectors)
            epochs.append(introspector.epochs)
        result[initial_lr] = np.mean(epochs)

    categories = [float(value) for value in result.keys()]
    values = [float(value) for value in result.values()]

    plt.figure()
    plt.scatter(categories, values, color='blue', marker='o')
    plt.plot(categories, values, color='skyblue')

    plt.title("Зависимость количества итераций от изначального коэффициента обучения")
    plt.xlabel('Изначальный коэффициент обучения')
    plt.ylabel('Кол-во итераций')

    plt.tight_layout()
    plt.show()


def do_test_iterations_from_max_error(path = "./test_images/blackbuck.bmp"):
    vectorizer = VectorizerImpl()
    m, n = 8, 8
    compress_coefficient = 2
    max_epochs = 200
    normalizer = ImageVectorConverter()

    arr = vectorizer.open_as_array(path)
    rects = vectorizer.divide_into_rectangles(arr, m, n)
    vectors = vectorizer.reshape_to_vectors(rects)
    input_dim = vectors.shape[1]
    output_dim = vectors.shape[1] // compress_coefficient

    errors = np.linspace(100,  300, 10)
    result = {}
    for error in errors:
        epochs = []
        for i in range(1):
            introspector = Autoencoder.SimpleNetworkIntrospector()
            ae = Autoencoder(
                input_dim=input_dim,
                compressed_dim=output_dim,
                required_error=error,
                max_epochs=max_epochs,
                learning_rate_adaption_strategy=GolovkoLearningRateStrategy(),
                normalizer=normalizer,
                introspector=introspector
            )
            ae.train(vectors)
            epochs.append(introspector.epochs)
        result[error] = np.mean(epochs)

    categories = [float(value) for value in result.keys()]
    values = [float(value) for value in result.values()]

    fig, ax = plt.subplots(figsize=(10, 5))
    ax.plot(categories, values, color='skyblue', label='Average Iterations')
    ax.scatter(categories, values, color='blue', marker='o')  # Show actual data points
    plt.scatter(categories, values, color='blue', marker='o')
    plt.plot(categories, values, color='skyblue')
    image = plt.imread(path)

    ax.set_title("Зависимость количества итераций от заданного предела ошибки")
    ax.set_xlabel('Предел ошибки')
    ax.set_ylabel('Кол-во итераций')

    # Create a new axis for the image
    image_ax = fig.add_axes((0.85, 0.2, 0.1, 0.6))  # Adjust the position and size [left, bottom, width, height]
    image_ax.imshow(image)
    image_ax.axis('off')  # Hide the axes for the image

    plt.tight_layout()
    plt.show()


def do_test_iterations_from_coefficient():
    path = "./test_images/blackbuck.bmp"
    vectorizer = VectorizerImpl()
    m, n = 8, 8
    required_error = 300
    max_epochs = 30
    normalizer = ImageVectorConverter()

    arr = vectorizer.open_as_array(path)
    rects = vectorizer.divide_into_rectangles(arr, m, n)
    vectors = vectorizer.reshape_to_vectors(rects)
    input_dim = vectors.shape[1]

    errors = [2,3,4,5,6,7,8]
    result = {}
    for compress_coefficient in errors:
        epochs = []
        for i in range(1):
            introspector = Autoencoder.SimpleNetworkIntrospector()
            output_dim = vectors.shape[1] // compress_coefficient
            ae = Autoencoder(
                input_dim=input_dim,
                compressed_dim=output_dim,
                required_error=required_error,
                max_epochs=max_epochs,
                learning_rate_adaption_strategy=GolovkoLearningRateStrategy(),
                normalizer=normalizer,
                introspector=introspector
            )
            ae.train(vectors)
            epochs.append(introspector.epochs)
        result[compress_coefficient] = np.mean(epochs)

    categories = [float(value) for value in result.keys()]
    values = [float(value) for value in result.values()]

    plt.figure()
    plt.scatter(categories, values, color='blue', marker='o')
    plt.plot(categories, values, color='skyblue')

    plt.title("Зависимость количества итераций от коэффициента сжатия")
    plt.xlabel('Коэффициент сжатия')
    plt.ylabel('Кол-во итераций')

    plt.tight_layout()
    plt.show()


