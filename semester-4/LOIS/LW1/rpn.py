from lexer import Token

TOKEN_PRECEDENCE = {
        Token.NEG: 3,
        Token.AND: 2,
        Token.OR: 1,
        Token.IMP: 0,
        Token.EQU: 0,
}


def to_rpn(tokens):
    output = []
    stack = []
    for token in tokens:
        if token in [Token.VAR, Token.TRUE, Token.FALSE]:
            output.append(token)
        elif token in [Token.AND, Token.OR, Token.NEG, Token.IMP, Token.EQU]:
            while stack and TOKEN_PRECEDENCE.get(token, 0) <= TOKEN_PRECEDENCE.get(stack[-1], 0):
                output.append(stack.pop())
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
