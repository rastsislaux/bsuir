from lexer import TokenType

TOKEN_PRECEDENCE = {
    TokenType.NEG: 3,
    TokenType.AND: 2,
    TokenType.OR: 1,
    TokenType.IMP: 0,
    TokenType.EQU: 0,
}


def to_rpn(tokens):
    output = []
    stack = []
    for token in tokens:
        if token.type in [TokenType.VAR, TokenType.TRUE, TokenType.FALSE]:
            output.append(token)
        elif token.type in [TokenType.AND, TokenType.OR, TokenType.NEG, TokenType.IMP, TokenType.EQU]:
            while stack and TOKEN_PRECEDENCE.get(token.type, 0) <= TOKEN_PRECEDENCE.get(stack[-1].type, 0):
                output.append(stack.pop())
            stack.append(token)
        elif token.type == TokenType.OP_BRACE:
            stack.append(token)
        elif token.type == TokenType.CL_BRACE:
            while stack[-1].type != TokenType.OP_BRACE:
                output.append(stack.pop())
            stack.pop()
    while stack:
        output.append(stack.pop())

    return output
