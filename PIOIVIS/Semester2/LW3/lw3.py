"""
    Название:       Set union
    Разработчик:    Липский Ростислав Владимирович
    Дата:           13.01.2022
    Описание:       программа получает в качестве параметра путь к JSON-файлу,
                    содержащему информацию о множествах и формирует множество,
                    равное объединению произвольного количества исходных мно-
                    жеств (с учётом кратных вхождений элементов.)
"""

import json
import sys


def check_input(inp, level = 0) -> bool:

    if level == 0 or level == 1:
        if type(inp) != list:
            raise TypeError("JSON file must contain a list of lists.")

    if type(inp) == list:
        for element in inp:
            check_input(element, level + 1)

    if type(inp) not in [list, str, int, float, bool]:
        raise TypeError("Only lists, string, integer, float, boolean types are allowed.")



def print_set(given_set: list, given_end: str = "") -> None:
    """
    Function that prints a list as a mathematical set
    :param given_set: the set you want to print
    :param given_end: the string appended in the end
    :return: nothing
    """

    print("{ ", end="")

    for i, element in enumerate(given_set):
        if i == len(given_set) - 1:
            print(element, end=" ")
        else:
            print(element, end=", ")

    print("}", end=given_end)


def sets_union(sets) -> list:
    """
    Function to create a list that represents the mathematical union of multisets
    :param sets: a list of lists (sets)
    :return: a list (multiset union)
    """

    result = []

    for gset in sets:
        for element in gset:
            result.append(element)

    return result


def main(argv: list):
    """
    Main function
    :param argv: arguements from console as a list of strings
    :return: nothing
    """

    if len(argv) != 1:
        print("Usage: python3 lw3.py {path to json}")
        return

    # Opening a file. 'with' keywords prevents memory leaks
    with open(argv[0]) as input_file:
        json_text = input_file.read()

    # Using a JSON parser to get the sets to unite
    try:
        sets = json.loads(json_text)
    except json.decoder.JSONDecodeError as e:
        print(f"Failed to load JSON-file: {e.msg}.")
        return

    try:
        check_input(sets)
    except TypeError as e:
        print(f"Wrong format of input file: {e}")
        return

    # Printing given sets before uniting
    print("Given sets:")
    for i, gset in enumerate(sets):
        print(f"A{i+1} = ", end="")
        print_set(gset, "\n")

    # Printing the result
    print("Result:")
    for i in range(len(sets)):
        if i != len(sets) - 1:
            print(f"A{i+1} ⋃ ", end="")
        else:
            print(f"A{i+1} = ", end="")
    print_set(sets_union(sets), given_end="\n")
    

if __name__ == "__main__":
    main(sys.argv[1:])
