from enum import Enum, auto


class Token(Enum):
    AND = auto()
    OR = auto()
    NEG = auto()
    VAR = auto()

    def __repr__(self):
        return self.name

    def __str__(self):
        return self.name
