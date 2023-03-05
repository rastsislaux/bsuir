#! /bin/python3

from dnf import is_dnf
from lexer import to_tokens
from rpn import to_rpn
from tree import to_tree, get_tree_string


def raw_to_tree(raw):
    result = to_tokens(raw)
    result = to_rpn(result)
    result = to_tree(result)
    return result


def main():
    print("Введите формулу: ", end="")
    raw = input()
    formula = raw_to_tree(raw)

    print("Что вы хотите сделать?\n"
          "\t1) Проверить на дизъюнктивную нормальную форму\n"
          "\t2) Вывести дерево\n"
          "Ваш выбор: ", end="")
    command = input()

    match command:
        case "1":
            if is_dnf(formula):
                print(f"Ответ: Формула {raw} является дизъюнктивной нормальной формой.")
            else:
                print(f"Ответ: Формула {raw} НЕ является дизъюнктивной нормальной формой.")
        case "2":
            print("Дерево")
            print(get_tree_string(formula))
        case _:
            print("Не знаю такой команды :(")


if __name__ == "__main__":
    main()
