from lexer import Token, TokenType


OPERATION_NOT_SUPPORTED_TEXT = "Operation not supported: "


def calculate(expr: list[Token], **kwargs):
    stack = []
    for token in expr:
        match token.type:
            case TokenType.VAR:
                stack.append(kwargs[token.value])
            case TokenType.AND:
                op1 = stack.pop()
                op2 = stack.pop()
                stack.append(op1 and op2)
            case TokenType.OR:
                op1 = stack.pop()
                op2 = stack.pop()
                stack.append(op1 or op2)
            case TokenType.IMP:
                op1 = stack.pop()
                op2 = stack.pop()
                stack.append(op1 if op2 else True)
            case TokenType.EQU:
                op1 = stack.pop()
                op2 = stack.pop()
                stack.append(op1 == op2)
            case TokenType.NEG:
                op1 = stack.pop()
                stack.append(not op1)
            case _:
                raise RuntimeError(f"{OPERATION_NOT_SUPPORTED_TEXT}{token.type}")
    return stack[0]
