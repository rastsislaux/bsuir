from lexer import Token


def is_and(node):
    for child in [node.left, node.right]:
        if child is not None and child.value not in [Token.AND, Token.VAR, Token.TRUE, Token.FALSE, Token.NEG, None]:
            return False
        if child is not None and child.value == Token.AND:
            if not is_and(child):
                return False
    return True


def is_dnf(node):
    if node.value == Token.NEG:
        return node.left.value in [Token.VAR, Token.TRUE, Token.FALSE]

    if node.value in [Token.VAR, Token.TRUE, Token.FALSE]:
        return True

    if node.value == Token.AND:
        return is_and(node)

    if node.value != Token.OR:
        return False

    for child in [node.left, node.right]:
        if child.value not in [Token.AND, Token.VAR, Token.TRUE, Token.FALSE, Token.NEG, Token.OR]:
            return False
        if child.value in [Token.OR, Token.AND]:
            if not is_dnf(child):
                return False

    return True
