from enum import Enum, auto


_OP_BRACE = "("
_CL_BRACE = ")"
_AND = "&"
_OR = "|"
_EQU = "~"
_IMP = "->"
_NEG = "!"
_TRUE = "T"
_FALSE = ".L"


class Token(Enum):
    OP_BRACE = auto()
    CL_BRACE = auto()
    AND = auto()
    OR = auto()
    NEG = auto()
    IMP = auto()
    EQU = auto()
    VAR = auto()
    TRUE = auto()
    FALSE = auto()

    def __repr__(self):
        return self.name

    def __str__(self):
        return self.name


def to_tokens(raw: str):
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
        elif raw[i:i + 2] == _IMP:
            tokens.append(Token.IMP)
            i += 1
        elif raw[i] == _TRUE:
            tokens.append(Token.TRUE)
        elif raw[i:i + 2] == _FALSE:
            tokens.append(Token.FALSE)
            i += 1
        elif raw[i].isalpha():
            tokens.append(Token.VAR)
        else:
            raise RuntimeError(f"Unexpected symbol while lexing: {raw[i]}")

    return tokens
