from itertools import product

from calculator import calculate


def generate_combinations(length):
    combs = product([True, False], repeat=length)
    return list(combs)


def make_truth_table(rpn, variables):
    solutions = []
    for i in generate_combinations(len(variables)):
        values = dict(zip(variables, i))
        solutions.append((i, calculate(rpn, **values)))
    return solutions
