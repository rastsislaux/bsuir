from dnflib.lexer import Token


ATOMIC_EXPRESSIONS = [
    Token.VAR,
    Token.TRUE,
    Token.FALSE
]


ALLOWED_OPS = [
    Token.AND,
    Token.VAR,
    Token.TRUE,
    Token.FALSE,
    Token.NEG,
    Token.OR
]


def is_and(node):
    if node.value in ATOMIC_EXPRESSIONS:
        return True

    if node.value == Token.OR:
        return False

    for child in [node.left, node.right]:
        if child is not None and child.value not in [Token.AND, Token.VAR, Token.TRUE, Token.FALSE, Token.NEG]:
            return False
        if child is not None and child.value == Token.AND:
            if not is_and(child):
                return False
        if child is not None and child.value == Token.NEG:
            if not is_and(child.left):
                return False
    return True


def is_dnf(node):
    if node.value == Token.NEG:
        return is_and(node.left)

    if node.value in ATOMIC_EXPRESSIONS:
        return True

    if node.value == Token.AND:
        return is_and(node)

    if node.value != Token.OR:
        return False

    for child in [node.left, node.right]:
        if child.value not in ALLOWED_OPS:
            return False
        if child.value in [Token.OR, Token.AND]:
            if not is_dnf(child):
                return False

    return True
