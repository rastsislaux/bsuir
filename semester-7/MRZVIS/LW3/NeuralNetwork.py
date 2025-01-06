from math import sqrt

import numpy as np


class NeuralNetwork:
    sequence = []  # исходная последовательность
    resSequence = []  # выходная последовательность
    expSequence = []  # ожидаемая выходная последовательность
    k = None  # размерность обучаемой последовательности
    p = None  # количество входных нейронов
    m = None  # количество нейроной на скрытом слое
    e = None  # максимально допустимая ошибка
    alfa = None  # коэффициент альфа
    N = None  # максимальное количество шагов обучения
    r = None  # количество предсказываемых элементов

    input = []  # входной вектор (p)
    hidden = []  # выходной вектор из скрытого слоя
    output = None  # выходной вектор из выходного слоя
    context_output = None  # контекстный слой для выходного слоя
    X = None  # матрица обучения m x p
    W = None  # матрица весов W на скрытом слое p x m
    W_ = None  # матрица весов W_ на выходном слое m x 1
    W_C = None  # матрица весов W_C для контекстного слоя
    T = None  # пороговые значения для скрытого слоя
    T_ = None  # пороговые значения для выходного слоя

    expValues = []  # значения, которые необходимо получить при обучении для каждого входного вектора

    stat_iteration = []
    stat_error = []

    def __init__(self):

        print(" 1. Fibonacci series: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, ...\n",
              "2. Power function(x^2): 1, 4, 9, 16, 25, 36, 49, 64, 81, ...\n",
              "3. Sequence of natural numbers: 1, 2, 3, 4, 5, 6, 7, 8, ...\n",
              "4. Enter your own sequence")

        key = input("\n Choose sequence: ")

        if key == '1':
            num = int(input("Input number of elements: (7<=n<=15)\n"))

            if num < 3 or num > 15:
                print("Invalid parameters!")
                exit()

            NeuralNetwork.k = num
            NeuralNetwork.sequence = NeuralNetwork.calc_fibonacci_series(num)
            NeuralNetwork.print_sequence()

            key_two = input("Use standard params(1) or enter your own(2)?:\n")

            if key_two == '1':
                NeuralNetwork.p = 2
                NeuralNetwork.m = NeuralNetwork.k - NeuralNetwork.p
                NeuralNetwork.alfa = 0.001
                NeuralNetwork.e = 0.001
                NeuralNetwork.N = 100
                NeuralNetwork.show_input_parameters()
            elif key_two == '2':
                NeuralNetwork.enter_input_parameters()
            else:
                print("Invalid parameters!")
                exit()

            num = int(input("Input number of elements to predict: (1<=n<=10)\n"))
            if num < 1 or num > 10:
                print("Invalid parameters!")
                exit()

            NeuralNetwork.r = num
            NeuralNetwork.expSequence = NeuralNetwork.calc_fibonacci_series(NeuralNetwork.k + NeuralNetwork.r)
        elif key == '2':
            num = int(input("Input number of elements: (4<=n<=8)\n"))

            if num < 4 or num > 8:
                print("Invalid parameters!")
                return

            NeuralNetwork.k = num
            NeuralNetwork.sequence = NeuralNetwork.calc_power_function(num)
            NeuralNetwork.print_sequence()

            key_two = input("Use standard params(1) or enter your own(2)?:\n")

            if key_two == '1':
                NeuralNetwork.p = 2
                NeuralNetwork.m = NeuralNetwork.k - NeuralNetwork.p
                NeuralNetwork.alfa = 0.001
                NeuralNetwork.e = 0.000001
                NeuralNetwork.N = 100
                NeuralNetwork.show_input_parameters()
            elif key_two == '2':
                NeuralNetwork.enter_input_parameters()
            else:
                print("Invalid parameters!")
                return

            num = int(input("Input number of elements to predict: (1<=n<=10)\n"))
            if num < 1 or num > 10:
                print("Invalid parameters!")
                return

            NeuralNetwork.r = num
            NeuralNetwork.expSequence = NeuralNetwork.calc_power_function(NeuralNetwork.k + NeuralNetwork.r)
        elif key == '3':
            num = int(input("Input number of elements: (3<=n<=15)\n"))

            if num < 3 or num > 15:
                print("Invalid parameters!")
                return

            NeuralNetwork.k = num
            NeuralNetwork.sequence = NeuralNetwork.calc_sequence_of_natural_numbers(num)
            NeuralNetwork.print_sequence()

            key_two = input("Use standard params(1) or enter your own(2)?:\n")

            if key_two == '1':
                NeuralNetwork.p = 2
                NeuralNetwork.m = NeuralNetwork.k - NeuralNetwork.p
                NeuralNetwork.alfa = 0.001
                NeuralNetwork.e = 0.001
                NeuralNetwork.N = 100
                NeuralNetwork.show_input_parameters()
            elif key_two == '2':
                NeuralNetwork.enter_input_parameters()
            else:
                print("Invalid parameters!")
                return

            num = int(input("Input number of elements to predict: (1<=n<=10)\n"))
            if num < 1 or num > 10:
                print("Invalid parameters!")
                return

            NeuralNetwork.r = num
            NeuralNetwork.expSequence = NeuralNetwork.calc_sequence_of_natural_numbers(
                NeuralNetwork.k + NeuralNetwork.r)
        elif key == '4':
            num = int(input("Input number of elements: (2=n<=10)\n"))

            if num < 2 or num > 10:
                print("Invalid parameters!")
                exit()

            NeuralNetwork.k = num
            NeuralNetwork.sequence = []

            for i in range(num):
                user_input = input("Input element\n")
                try:
                    val = int(user_input)
                except ValueError:
                    print("Invalid parameters!")
                    return
                NeuralNetwork.sequence.append(val)

            NeuralNetwork.print_sequence()
            NeuralNetwork.enter_input_parameters()

            num = int(input("Input number of elements to predict: (1<=n<=10)\n"))
            if num < 1 or num > 10:
                print("Invalid parameters!")
                return

            NeuralNetwork.r = num
            NeuralNetwork.expSequence = NeuralNetwork.sequence

            for i in range(NeuralNetwork.r):
                user_input = input("Input element\n")
                try:
                    val = int(user_input)
                except ValueError:
                    print("Invalid parameters!")
                    return
                NeuralNetwork.expSequence.append(val)

        if NeuralNetwork.check_input_parameters():
            pass
        else:
            print("Invalid input parameters!")
            exit()

        NeuralNetwork.create_matrices()

    @staticmethod
    def calc_fibonacci_series(number):
        seq = [0, 1]

        for size in range(1, number - 1):
            seq.append(seq[size] + seq[size - 1])
        return seq

    @staticmethod
    def calc_power_function(number):
        seq = []

        for i in range(1, number + 1):
            seq.append(i ** 2)
        return seq

    @staticmethod
    def calc_sequence_of_natural_numbers(number):
        seq = []

        for i in range(1, number + 1):
            seq.append(i)
        return seq

    @staticmethod
    def print_sequence():
        print("Learning sequence:")
        print(np.array(NeuralNetwork.sequence).reshape(NeuralNetwork.k, 1))

    @staticmethod
    def show_input_parameters():
        print("Number of columns in learning matrix p=", NeuralNetwork.p)
        print("Number of hidden layer neurons m=", NeuralNetwork.m)
        print("Max error e =", NeuralNetwork.e)
        print("Step learning alfa =", NeuralNetwork.alfa)
        print("Max number of learning steps N =", NeuralNetwork.N)

    @staticmethod
    def enter_input_parameters():
        NeuralNetwork.p = int(input("Number of columns in learning matrix(p) (p>=1 & p<k)\n"))
        NeuralNetwork.m = NeuralNetwork.k - NeuralNetwork.p
        NeuralNetwork.e = float(input("Enter max error(e) (0<e<=0.1)\n"))
        NeuralNetwork.alfa = float(input("Enter step learning(alfa) (0<alfa<=0.1 & alfa<=e)\n"))
        NeuralNetwork.N = int(input("Enter max number of learning steps(N) (1<=N<=1000000)\n"))

    @staticmethod
    def check_input_parameters():
        if NeuralNetwork.p <= 0 or NeuralNetwork.p >= NeuralNetwork.k or \
                NeuralNetwork.e <= 0 or NeuralNetwork.e > 0.1 or NeuralNetwork.alfa <= 0 \
                or NeuralNetwork.alfa > 0.1 or NeuralNetwork.N < 1 or NeuralNetwork.N > 1000000:
            return False
        return True

    # ---------------------------------------------------------------------------------------------------------------------
    @staticmethod
    def create_matrices():
        NeuralNetwork.input = [0 for _ in range(NeuralNetwork.p)]
        NeuralNetwork.hidden = [0 for _ in range(NeuralNetwork.m)]
        NeuralNetwork.output = 0
        NeuralNetwork.context_output = np.array([0])
        NeuralNetwork.T = [0 for _ in range(NeuralNetwork.m)]
        NeuralNetwork.T_ = 0

        NeuralNetwork.X = [[NeuralNetwork.sequence[i + j] for j in range(NeuralNetwork.p)] for i in
                           range(NeuralNetwork.m)]

        NeuralNetwork.X = np.array(NeuralNetwork.X).reshape(NeuralNetwork.m, NeuralNetwork.p)

        for i in range(NeuralNetwork.m):
            NeuralNetwork.expValues.append(NeuralNetwork.expSequence[NeuralNetwork.p + i])

        NeuralNetwork.W = np.random.randn(NeuralNetwork.p, NeuralNetwork.m)

        NeuralNetwork.W_ = np.random.randn(NeuralNetwork.m, 1)

        NeuralNetwork.W_C = np.random.randn(1, NeuralNetwork.m)

        # print(NeuralNetwork.X)
        # print(NeuralNetwork.expSequence)
        # print(NeuralNetwork.expValues)
        # print(NeuralNetwork.W)
        # print(NeuralNetwork.W_)
        # print(NeuralNetwork.W_C)

    @staticmethod
    def start_learning():

        error = NeuralNetwork.e + 1
        iteration = 0

        while iteration <= NeuralNetwork.N and error > NeuralNetwork.e:
            iteration += 1
            error = 0.0
            for row in range(NeuralNetwork.m):
                NeuralNetwork.input = np.array(NeuralNetwork.X[row])

                NeuralNetwork.forward()

                error += pow(NeuralNetwork.output - NeuralNetwork.expValues[row], 2)  # / 2

                NeuralNetwork.back_error_prop(NeuralNetwork.expValues[row])
            error = error / NeuralNetwork.m
            if iteration <= 1000 or iteration % 10000 == 0:
                NeuralNetwork.stat_iteration.append(iteration)
                NeuralNetwork.stat_error.append(error[0])
                print("Iteration: ", iteration, " Error: ", error)

        print("Finish learning")
        print("Iterations = ", iteration, " \nError = ", error)

    @staticmethod
    def back_error_prop(val):
        diff = NeuralNetwork.alfa * (NeuralNetwork.output - val)

        de_dh1 = NeuralNetwork.multiplying_matrix_transpose_by_vector((NeuralNetwork.output - val),
                                                                      np.transpose(NeuralNetwork.W_))
        de_dt1 = NeuralNetwork.derivative_elu(NeuralNetwork.hidden) * de_dh1

        de_dw1 = NeuralNetwork.multiplying_matrix_by_transpose_matrix(de_dt1, NeuralNetwork.input)

        NeuralNetwork.W = NeuralNetwork.W - NeuralNetwork.alfa * de_dw1
        NeuralNetwork.W_ -= diff * NeuralNetwork.transpose_vector(NeuralNetwork.hidden)

        de_dw1 = NeuralNetwork.multiplying_matrix_by_transpose_matrix(de_dt1, NeuralNetwork.context_output)
        NeuralNetwork.W_C = NeuralNetwork.W_C - NeuralNetwork.alfa * de_dw1

        NeuralNetwork.T = diff @ np.transpose(NeuralNetwork.W_) @ NeuralNetwork.derivative_elu(
            NeuralNetwork.hidden)

        NeuralNetwork.T_ = diff

        NeuralNetwork.W = NeuralNetwork.normalize(NeuralNetwork.W)
        NeuralNetwork.W_ = NeuralNetwork.normalize(NeuralNetwork.W_)
        NeuralNetwork.W_C = NeuralNetwork.normalize(NeuralNetwork.W_C)

    @staticmethod
    def forward():
        in_w = NeuralNetwork.input @ NeuralNetwork.W
        cont_w = NeuralNetwork.context_output @ NeuralNetwork.W_C
        in_plus_con = in_w + cont_w
        with_por = in_plus_con - NeuralNetwork.T
        NeuralNetwork.hidden = NeuralNetwork.elu(with_por)

        hid_w = NeuralNetwork.hidden @ NeuralNetwork.W_
        without_por = hid_w - NeuralNetwork.T_
        NeuralNetwork.output = NeuralNetwork.elu(without_por)
        NeuralNetwork.context_output = NeuralNetwork.output

        # NeuralNetwork.hidden = NeuralNetwork.leaky_relu(
        #     NeuralNetwork.input @ NeuralNetwork.W) + NeuralNetwork.leaky_relu(
        #     NeuralNetwork.context_output @ NeuralNetwork.W_C)
        #
        # # s -= NeuralNetwork.T[j]
        #
        # NeuralNetwork.output = NeuralNetwork.leaky_relu(NeuralNetwork.hidden @ NeuralNetwork.W_)
        # NeuralNetwork.context_output = NeuralNetwork.output
        #
        # # s -= NeuralNetwork.T_

    @staticmethod
    def elu_1(vector, alpha=0.2):
        for x in range(len(vector)):
            number = vector[x]
            if number > 0:
                pass
            else:
                vector[x] = alpha * number

        return np.array(vector)
        # if x > 0:
        #     return x
        # else:
        #     return alpha * x

    @staticmethod
    def derivative_elu_1(vector, alpha=0.2):
        for x in range(len(vector)):
            number = vector[x]
            if number > 0:
                vector[x] = 1.0
            else:
                vector[x] = alpha

        return np.array(vector)
        # if x > 0:
        #     return 1
        # else:
        #     return alpha

    @staticmethod
    def elu(vector, alpha=1.0):
        """
        Apply the ELU activation function to a vector.

        Parameters:
        vector (list or np.array): Input vector.
        alpha (float): Scaling factor for negative inputs. Default is 1.0.

        Returns:
        np.array: The vector after applying the ELU function.
        """
        for x in range(len(vector)):
            number = vector[x]
            if number > 0:
                pass
            else:
                vector[x] = alpha * (np.exp(number) - 1)

        return np.array(vector)

    @staticmethod
    def derivative_elu(vector, alpha=1.0):
        """
        Compute the derivative of the ELU activation function.

        Parameters:
        vector (list or np.array): Input vector (pre-activation values).
        alpha (float): Scaling factor for negative inputs. Default is 1.0.

        Returns:
        np.array: The vector after computing the derivative of the ELU function.
        """
        for x in range(len(vector)):
            number = vector[x]
            if number > 0:
                vector[x] = 1.0
            else:
                vector[x] = alpha * np.exp(number)

        return np.array(vector)

    # ---------------------------------------------------------------------------------------------------------------------
    @staticmethod
    def generate_predicted_sequence():
        NeuralNetwork.resSequence = []

        j = NeuralNetwork.k - NeuralNetwork.p
        for i in range(NeuralNetwork.p):
            NeuralNetwork.input[i] = NeuralNetwork.sequence[j]
            j += 1

        for i in range(NeuralNetwork.r):

            if i > 0:
                for j in range(NeuralNetwork.p - 1):
                    NeuralNetwork.input[j] = NeuralNetwork.input[j + 1]

                NeuralNetwork.input[NeuralNetwork.p - 1] = NeuralNetwork.output

            print(NeuralNetwork.input)

            NeuralNetwork.forward()
            print(NeuralNetwork.output)
            NeuralNetwork.output[0] = round(NeuralNetwork.output[0])
            NeuralNetwork.resSequence.append(NeuralNetwork.output)

        print("Result sequence: ")
        for i in range(NeuralNetwork.r):
            print("Result: ", NeuralNetwork.resSequence[i], "  Expected value: ",
                  NeuralNetwork.expSequence[NeuralNetwork.k + i], "  Line error: ",
                  NeuralNetwork.expSequence[NeuralNetwork.k + i] - NeuralNetwork.resSequence[i])

    @staticmethod
    def transpose_vector(vector):
        vector_new = []
        for size in vector:
            vector_new.append([size])
        return np.array(vector_new)

    @staticmethod
    def multiplying_matrix_transpose_by_vector(vector, matrix):
        new_matrix = []

        for col in range(len(matrix[0])):
            value = 0
            for row in range(len(matrix)):
                value += vector[row] * matrix[row][col]
                if row == len(matrix) - 1:
                    new_matrix.append(value)

        return np.array(new_matrix)

    @staticmethod
    def multiplying_matrix_by_transpose_matrix(matrix, matrix_transpose):
        size_1 = len(matrix_transpose)
        size_2 = len(matrix)

        matrix_new = [[0 for _ in range(size_2)] for _ in range(size_1)]

        for row in range(size_1):
            for col in range(size_2):
                matrix_new[row][col] = matrix_transpose[row] * matrix[col]

        return np.array(matrix_new)

    @staticmethod
    def normalize(matrix):
        for col in range(len(matrix[0])):
            value = 0

            for row in range(len(matrix)):
                value += matrix[row][col] * matrix[row][col]
            value = sqrt(value)

            for row in range(len(matrix)):
                matrix[row][col] = matrix[row][col] / value

        return np.array(matrix)
