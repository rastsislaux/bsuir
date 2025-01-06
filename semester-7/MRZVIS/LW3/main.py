#
# Автор: Липский Р. В.
# Лабораторная работа #3
# Вариант: 8 (Сеть Джордана + ELU)
#
# Источники:
#  - Головко, В. А. Нейросетевые технологии обработки данных : учеб. пособие
#  - Методические материалы к лабораторной работе.
#  - Семченков Н. А. Лабораторная работа #3 (2022)
#

import json

from NeuralNetwork import NeuralNetwork

if __name__ == '__main__':
    neural_network = NeuralNetwork()
    neural_network.start_learning()
    neural_network.generate_predicted_sequence()

