from dataclasses import dataclass
from enum import Enum, auto

_OP_BRACE = "("
_CL_BRACE = ")"
_AND = "/\\"
_OR = "\\/"
_EQU = "~"
_IMP = "->"
_NEG = "!"
_TRUE = "T"
_FALSE = ".L"


class TokenType(Enum):
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


@dataclass
class Token:
    value: str
    type: TokenType

    def __init__(self, atype: TokenType, value=None):
        self.type = atype
        self.value = value


def to_tokens(raw: str):
    raw = raw.replace(" ", "")

    i = -1
    tokens = []
    while i + 1 < len(raw):
        i += 1

        if raw[i] == _OP_BRACE:
            tokens.append(Token(TokenType.OP_BRACE))
        elif raw[i] == _CL_BRACE:
            tokens.append(Token(TokenType.CL_BRACE))
        elif raw[i:i + 2] == _AND:
            tokens.append(Token(TokenType.AND))
            i += 1
        elif raw[i:i + 2] == _OR:
            tokens.append(Token(TokenType.OR))
            i += 1
        elif raw[i] == _NEG:
            tokens.append(Token(TokenType.NEG))
        elif raw[i] == _EQU:
            tokens.append(Token(TokenType.EQU))
        elif raw[i:i + 2] == _IMP:
            tokens.append(Token(TokenType.IMP))
            i += 1
        elif raw[i] == _TRUE:
            tokens.append(Token(TokenType.TRUE))
        elif raw[i:i + 2] == _FALSE:
            tokens.append(Token(TokenType.FALSE))
            i += 1
        elif raw[i].isalpha():
            start = i
            while i + 1 < len(raw) and raw[i + 1].isalnum():
                i += 1
            finish = i
            tokens.append(Token(TokenType.VAR, value=raw[start:finish + 1]))
        else:
            raise RuntimeError(f"Unexpected symbol while lexing: {raw[i]}")

    variables = []
    for token in tokens:
        if token.type == TokenType.VAR and token.value not in variables:
            variables.append(token.value)

    return tokens, variables
