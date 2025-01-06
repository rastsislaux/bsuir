#
# Автор: Липский Р. В.
# Лабораторная работа #2
# Вариант: 8 (Сеть Хопфилда с непрерывным состоянием)
#
# Источники:
#  - Головко, В. А. Нейросетевые технологии обработки данных : учеб. пособие
#  - Методические материалы к лабораторной работе.
#

import argparse

from network import HopfieldNetwork
from training_utils import load_training_dataset
from vectorization import PlainVectorizer

def do_recall(path):
    patterns = load_training_dataset("letters", vectorizer=PlainVectorizer())

    network = HopfieldNetwork(input_size=patterns.shape[1])
    network.train(patterns, repeats=3)

    noisy, shape = PlainVectorizer().vectorize(path, return_original_shape=True)
    recovered_pattern, iterations = network.recall(noisy, return_iterations=True)

    PlainVectorizer().devectorize(recovered_pattern, shape).show()
    print(f"\nКоличество итераций до релаксации: {iterations}")


def main():
    parser = argparse.ArgumentParser(description="Process a path for recall operation.")
    parser.add_argument(
        "path",
        type=str,
        help="The path to process in the recall operation."
    )

    args = parser.parse_args()
    do_recall(args.path)


if __name__ == "__main__":
    main()
