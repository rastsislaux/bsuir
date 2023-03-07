from copy import deepcopy

from dnflib.lexer import Token


class Node:

    def __init__(self, value, left=None, right=None):
        self.value = value
        self.left = left
        self.right = right

    def __repr__(self):
        return f"Node({self.value})"


def get_tree_string(node, prefix="", is_left=True):
    tree_str = ""
    if node is None:
        return tree_str
    tree_str += get_tree_string(node.right, prefix + ("│   " if is_left else "    "), False)
    tree_str += prefix + ("└── " if is_left else "┌── ") + str(node.value) + "\n"
    tree_str += get_tree_string(node.left, prefix + ("    " if is_left else "│   "), True)
    return tree_str


def to_tree(prn, root=None):
    prn = deepcopy(prn)

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
