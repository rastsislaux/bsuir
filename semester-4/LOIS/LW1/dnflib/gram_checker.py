from dnflib.lexer import Token


def check_grammar(tokens: list[Token]):
    ops_count = 0
    op_br_count = 0
    cl_br_count = 0
    for token in tokens:
        if token in [Token.NEG, Token.OR, Token.AND, Token.IMP, Token.EQU]:
            ops_count += 1
        if token == Token.OP_BRACE:
            op_br_count += 1
        if token == Token.CL_BRACE:
            cl_br_count += 1

    if op_br_count != cl_br_count:
        raise RuntimeError("Expected closing brace.")

    if op_br_count != ops_count:
        raise RuntimeError("Failed grammar.")
