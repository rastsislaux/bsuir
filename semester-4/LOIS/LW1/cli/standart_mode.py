from dnflib.dnf import is_dnf
from dnflib.gram_checker import check_grammar
from dnflib.lexer import to_tokens
from dnflib.rpn import to_rpn
from dnflib.tree import to_tree, get_tree_string


def standart_mode():

    def actions_with_formula():
        print("Что вы хотите сделать?\n"
              "\t1) Проверить на дизъюнктивную нормальную форму\n"
              "\t2) Вывести дерево\n"
              "\t3) Вывести обратную польскую запись\n"
              "\t4) Вернуться назад\n"
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
            case "3":
                print(f"Ответ: {rpn}")
            case "4":
                return False
            case _:
                print("Не знаю такой команды :(")
        return True

    print("Введите формулу: ", end="")
    raw = input()

    try:
        tokens = to_tokens(raw)
        check_grammar(tokens)
        rpn = to_rpn(tokens)
        formula = to_tree(rpn)
    except Exception as e:
        print("Введённая формула не соответствует грамматике.")
        return

    while actions_with_formula():
        pass
