#! /bin/python3

from enum import Enum, auto


_OP_BRACE = "("
_CL_BRACE = ")"
_AND = "*"
_OR = "+"
_EQU = "~"
_IMP = "->"
_NEG = "!"


class Node:

    def __init__(self, value, left=None, right=None):
        self.value = value
        self.left = left
        self.right = right

    def __repr__(self):
        return f"Node({self.value})"


def print_tree(node, prefix="", is_left=True):
    if node is None:
        return
    print_tree(node.right, prefix + ("│   " if is_left else "    "), False)
    print(prefix + ("└── " if is_left else "┌── ") + str(node.value))
    print_tree(node.left, prefix + ("    " if is_left else "│   "), True)


class Token(Enum):
    OP_BRACE = auto()
    CL_BRACE = auto()
    AND = auto()
    OR = auto()
    NEG = auto()
    IMP = auto()
    EQU = auto()
    VAR = auto()

    def __repr__(self):
        return self.name

    def __str__(self):
        return self.name


def get_tokens(raw: str):
    raw = raw.replace(" ", "")

    i = -1
    tokens = []
    while i + 1 < len(raw):
        i += 1

        if raw[i] == _OP_BRACE:
            tokens.append(Token.OP_BRACE)
        elif raw[i] == _CL_BRACE:
            tokens.append(Token.CL_BRACE)
        elif raw[i] == _AND:
            tokens.append(Token.AND)
        elif raw[i] == _OR:
            tokens.append(Token.OR)
        elif raw[i] == _NEG:
            tokens.append(Token.NEG)
        elif raw[i] == _EQU:
            tokens.append(Token.EQU)
        elif raw[i:i + 1] == _IMP:
            tokens.append(Token.IMP)
        elif raw[i].isalpha():
            tokens.append(Token.VAR)
        else:
            raise RuntimeError(f"Unexpected symbol while lexing: {raw[i]}")

    return tokens


def to_rpn(tokens):
    output = []
    stack = []
    for token in tokens:
        if token == Token.VAR:
            output.append(token)
        elif token in [Token.AND, Token.OR, Token.NEG, Token.IMP, Token.EQU]:
            stack.append(token)
        elif token == Token.OP_BRACE:
            stack.append(token)
        elif token == Token.CL_BRACE:
            while stack[-1] != Token.OP_BRACE:
                output.append(stack.pop())
            stack.pop()
    while stack:
        output.append(stack.pop())
    return output


def to_tree(prn, root=None):
    if root is None:
        root = Node(prn.pop())

    if root.value in [Token.AND, Token.EQU, Token.IMP, Token.OR]:
        root.left = Node(prn.pop())
        to_tree(prn, root.left)
        root.right = Node(prn.pop())
        to_tree(prn, root.right)
    elif root.value == Token.NEG:
        root.left = Node(prn.pop())
        to_tree(prn, root.left)

    return root


def is_and(node):
    for child in [node.left, node.right]:
        if child is not None and child.value not in [Token.AND, Token.VAR, Token.NEG, None]:
            return False
        if child is not None and child.value == Token.AND:
            if not is_and(child):
                return False
    return True


def is_dnf(node):
    if node.value != Token.OR:
        return False

    for child in [node.left, node.right]:
        if child.value not in [Token.AND, Token.VAR, Token.NEG, Token.OR]:
            return False
        if child.value == Token.OR:
            if not is_dnf(child):
                return False
        if child.value == Token.AND:
            if not is_and(child):
                return False

    return True


def raw_to_tree(raw):
    tokens = get_tokens(raw)
    rpn = to_rpn(tokens)
    tree = to_tree(rpn)
    return tree


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
            print_tree(formula)
        case _:
            print("Не знаю такой команды :(")


if __name__ == "__main__":
    main()
