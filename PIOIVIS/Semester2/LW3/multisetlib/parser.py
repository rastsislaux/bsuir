"""
THIS FILE IS A PART OF MULTISETLIB (LABORATORY WORK #3)
AUTHOR: ROSTISLAV LIPSKY
DATE:   11.02.2022
"""

from multisetlib.multiset import Multiset


class _Symbol:
    """
    This namespace contains symbols that represent different parts of multiset
    """
    OPENSET = '{'
    CLOSESET = '}'
    OPENTUPLE = '<'
    CLOSETUPLE = '>'
    TERMNAME = ','
    DEFNAME = '='


def _unite_set_token(s: list, open_s: str, close_s: str):
    """
    This method unites tuples and multisets after splitting them by comma
    :param s: list of strings from string splitted by comma
    :param open_s: opening symbol
    :param close_s: closing symbol
    :return: list of strings with united tuples and multisets
    """
    cpy = s.copy()
    i, ignore, concat = 0, 0, False
    while i < len(cpy):

        concatenated = False
        if concat:
            cpy[i - 1] += ',' + cpy[i]
            concatenated = True

        if cpy[i][0] == open_s:
            if concat:
                ignore += 1
            else:
                concat = True

        if cpy[i][-1] == close_s:
            if ignore != 0:
                ignore -= 1
            else:
                concat = False

        if concatenated:
            cpy.pop(i)
            i -= 1

        i += 1

    return cpy


def _parse_str(s: str, first: bool = True):
    """
    This method parses string.
    :param s: string to parse
    :param first: is this the first (non-recursive) call of this method
    :return: int/float/Multiset/tuple depending on the content of the string
    """

    tokens = s
    if first:
        tokens = tokens.replace(" ", "")

    try:
        return int(tokens)
    except ValueError:
        pass

    try:
        return float(tokens)
    except ValueError:
        pass

    if (tokens[0] == _Symbol.OPENSET) ^ (tokens[-1] == _Symbol.CLOSESET):
        raise SyntaxError(f"One of braces is not closed in this token: \"{s}\"")

    elif tokens[0] == _Symbol.OPENSET:
        if tokens == "{}":
            return Multiset()
        inner_tokens = tokens[1:-1].split(",")
        inner_tokens = _unite_set_token(inner_tokens, _Symbol.OPENSET, _Symbol.CLOSESET)
        inner_tokens = _unite_set_token(inner_tokens, _Symbol.OPENTUPLE, _Symbol.CLOSETUPLE)
        return Multiset([_parse_str(token, first=False) for token in inner_tokens])

    elif (tokens[0] == _Symbol.OPENTUPLE) ^ (tokens[-1] == _Symbol.CLOSETUPLE):
        raise SyntaxError(f"One of braces is not closed in this token: \"{s}\"")

    elif tokens[0] == _Symbol.OPENTUPLE:
        if tokens == "<>":
            return ()
        inner_tokens = tokens[1:-1].split(',')
        inner_tokens = _unite_set_token(inner_tokens, _Symbol.OPENSET, _Symbol.CLOSESET)
        inner_tokens = _unite_set_token(inner_tokens, _Symbol.OPENTUPLE, _Symbol.CLOSETUPLE)
        return tuple([_parse_str(token, first=False) for token in inner_tokens])

    return tokens


def parse(s: str) -> (str, Multiset):
    """
    Parse method for end user
    :param s: string with a mathematical form of multiset
    :return: multisetlib.Multiset
    """
    def_index = s.find(_Symbol.DEFNAME)
    return s[0:def_index], _parse_str(s[def_index+1:])
