#
# Автор: Липский Р. В.
# Лабораторная работа #1
# Вариант: 8
# 
# Источники:
#  - Головко, В. А. Нейросетевые технологии обработки данных : учеб. пособие
#  - Методические материалы к лабораторной работе.
#

import argparse
import os

from PIL import Image

from lib.learning_rate_adoption_strats import GolovkoLearningRateStrategy
from test import do_test_iterations_from_coefficient, do_test_iterations_from_max_error, \
    do_test_iterations_from_learning_rate
from config import SERIALIZATION_STRATEGY, NORMALIZER
from lib.autoencoder import Autoencoder
from lib.learning_rate_adoption_strats import NoopAdaptiveLearningRateStrategy, SimpleAdaptiveLearningRateStrategy, \
    MovingAverageLearningRateStrategy
from lib.normalization import ImageVectorConverter
from lib.serialization import LzmaSerializationStrategy
from lib.serialization import SerializationStrategy
from lib.vectorization import Devectorizer, DevectorizerImpl
from lib.vectorization import VectorizerImpl, Vectorizer


def do_compress(path, output_path,
                compress_coefficient=4, m=4, n=4, max_epochs=2000, required_error=0.00004,
                learning_rate_strategy=MovingAverageLearningRateStrategy(initial_lr=0.001),
                vectorizer: Vectorizer = VectorizerImpl(),
                serialization_strat: SerializationStrategy = LzmaSerializationStrategy(),
                normalizer: Autoencoder.VectorConverter = ImageVectorConverter()):
    arr = vectorizer.open_as_array(path)
    w, h = arr.shape
    rects = vectorizer.divide_into_rectangles(arr, m, n)
    vectors = vectorizer.reshape_to_vectors(rects)
    input_dim = vectors.shape[1]
    output_dim = vectors.shape[1] // compress_coefficient
    ae = Autoencoder(
        input_dim=input_dim,
        compressed_dim=output_dim,
        required_error=required_error,
        max_epochs=max_epochs,
        learning_rate_adaption_strategy=learning_rate_strategy,
        normalizer=normalizer
    )
    ae.train(vectors)
    compressed = ae.compress(vectors)
    print(f"{compressed.shape=}")
    serialization_strat.save(output_path, ae, compressed, w, h, input_dim, output_dim, m, n)

def do_decompress(path,
                  devectorizer: Devectorizer = DevectorizerImpl(),
                  serialization_strat: SerializationStrategy = LzmaSerializationStrategy(),
                  normalizer: Autoencoder.VectorConverter = ImageVectorConverter()):
    w, h, id, od, m, n, model_state, compressed_data = serialization_strat.load(path)

    ae = Autoencoder(
        input_dim=id,
        compressed_dim=od,
        learning_rate_adaption_strategy=NoopAdaptiveLearningRateStrategy(),
        normalizer=normalizer
    )

    ae.W2 = model_state
    decompressed = ae.decompress(compressed_data)
    print(f"{decompressed.shape=}")
    d_rects = devectorizer.reshape_to_sqaures(decompressed, (w, h), m, n)
    d_arr = devectorizer.unite_rectangles(d_rects)
    image = Image.fromarray(d_arr)
    image.show()


def create_argument_parser():
    parser = argparse.ArgumentParser(description="Compress and decompress images.")
    subparsers = parser.add_subparsers(dest="command", required=True)

    test_parser = subparsers.add_parser("test", help="Test the network.")
    test_parser.add_argument(
        "type",
        choices=['i_from_a', 'i_from_e', 'i_from_z', 'i_from_e_images'],
        help="Choose metric to test: "
             "i_from_a (iterations from learning rate), "
             "i_from_e (iterations from max error), "
             "i_from_z (iterations from compress coefficient), "
             "i_from_e_images (iterations from max error with different images)"
    )

    compress_parser = subparsers.add_parser("compress", help="Compress an image.")
    compress_parser.add_argument('-c', type=int, help='Compression coefficient')
    compress_parser.add_argument('-m', type=int, help='Width of rectangle')
    compress_parser.add_argument('-n', type=int, help='Height of rectangle')
    compress_parser.add_argument('-me', type=int, help='Maximum training epochs', default=2000)
    compress_parser.add_argument('-e', type=float, help='Maximal error threshold', default=0.00004)
    compress_parser.add_argument('-lr', type=float, help='Initial learning rate', default=0.0005)
    compress_parser.add_argument(
        '--lr-strat',
        choices=['noop', 'simple', 'moving'],
        help="Choose learning rate adoption strategy: noop, simple, moving.",
        default="moving"
    )
    compress_parser.add_argument("image_path", type=str, help="Path to the input image.")
    compress_parser.add_argument(
        "output_path",
        type=str,
        nargs="?",
        help="Path to save the compressed data. Defaults to the input file name with .rsx extension.",
    )

    decompress_parser = subparsers.add_parser("show", help="Opens image.")
    decompress_parser.add_argument("input_path", type=str, help="Path to the compressed data.")

    return parser


def main():
    def get_learning_rate_strategy():
        # strat = MovingAverageLearningRateStrategy(args.lr)
        strat = GolovkoLearningRateStrategy()
        if args.lr_strat == 'noop':
            strat = NoopAdaptiveLearningRateStrategy()
        elif args.lr_strat == 'simple':
            strat = SimpleAdaptiveLearningRateStrategy()
        return strat

    arg_parser = create_argument_parser()
    args = arg_parser.parse_args()

    if args.command == "compress":
        strategy = get_learning_rate_strategy()

        output_path = args.output_path or f"{os.path.splitext(args.image_path)[0]}.rsx"
        do_compress(args.image_path, output_path,
                    compress_coefficient=args.c,
                    m=args.m, n=args.n,
                    max_epochs=args.me,
                    required_error=args.e,
                    learning_rate_strategy=strategy,
                    serialization_strat=SERIALIZATION_STRATEGY,
                    normalizer=NORMALIZER)
    elif args.command == "show":
        do_decompress(args.input_path, serialization_strat=SERIALIZATION_STRATEGY)
    elif args.command == "test":
        if args.type == "i_from_a":
            do_test_iterations_from_learning_rate()
        elif args.type == "i_from_e":
            do_test_iterations_from_max_error()
        elif args.type == "i_from_z":
            do_test_iterations_from_coefficient()
        elif args.type == 'i_from_e_images':
            do_test_iterations_from_max_error("./test_images/blackbuck.bmp")
            do_test_iterations_from_max_error("./test_images/lena.bmp")
            do_test_iterations_from_max_error("./test_images/snail.bmp")
            do_test_iterations_from_max_error("./test_images/greenland_grid_velo.bmp")


# Command-line interface
if __name__ == "__main__":
    main()
