from antlr4 import InputStream, CommonTokenStream
from antlr4.error.ErrorListener import ErrorListener

from dist.dnfLexer import dnfLexer
from dist.dnfParser import dnfParser


class DNFException(RuntimeError):
    pass


class ExceptionErrorListener(ErrorListener):
    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        raise DNFException(f"{line}:{column}: {msg}")


def check_dnf(formula: str):
    data = InputStream(formula)
    lexer = dnfLexer(data)
    lexer.removeErrorListeners()
    lexer.addErrorListener(ExceptionErrorListener())

    stream = CommonTokenStream(lexer)
    parser = dnfParser(stream)
    parser.removeErrorListeners()
    parser.addErrorListener(ExceptionErrorListener())
    parser.dnf()
