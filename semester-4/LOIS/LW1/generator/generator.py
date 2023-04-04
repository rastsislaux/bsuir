import json
from copy import deepcopy
import random

from cli.checker import check_dnf, DNFException
from generator.lexer import Token
from generator.tree import Node

VARIABLES = [chr(i) for i in reversed(range(ord('A'), ord('Z')))]
print(VARIABLES)


def generate_tree(depth):
    if depth == 0:
        return Node(Token.VAR)
    else:
        op = random.choice([Token.AND, Token.OR])
        node = Node(op)
        node.left = generate_tree(depth - 1)
        node.right = generate_tree(depth - 1)
        return node


def tree_to_string(node, variables: list, first=True):
    if first:
        variables = deepcopy(variables)

    if node is None:
        return ''
    elif node.value == Token.VAR:
        return variables.pop()
    elif node.value == Token.AND:
        left_str = tree_to_string(node.left, variables, False)
        right_str = tree_to_string(node.right, variables, False)
        return f'({left_str}/\\{right_str})'
    elif node.value == Token.OR:
        left_str = tree_to_string(node.left, variables, False)
        right_str = tree_to_string(node.right, variables, False)
        return f'({left_str}\\/{right_str})'
    else:
        return f'!{tree_to_string(node.left, variables, False)}'


def is_dnf(formula):
    try:
        check_dnf(formula)
        return True
    except DNFException:
        return False


def generate_test_set(out_file, gen_dnf=True):
    dnf = set()
    while len(dnf) < (50 if not gen_dnf else 5):
        formula = generate_tree(random.randint(1, 4))
        formula = tree_to_string(formula, VARIABLES)
        if is_dnf(formula) == gen_dnf:
            dnf.add(formula)
    with open(out_file, "w") as file:
        file.write(json.dumps(list(dnf), indent=4))
