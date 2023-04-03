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


def cli_main():

    def check_mode():
        print("Ввевдите /quit для выхода")
        inp = ""
        while inp != "/quit":
            inp = input(" >>> ")
            try:
                check_dnf(inp)
                print("Это дизънюктивная нормальная форма.")
            except DNFException as e:
                print(f"Это не дизъюнктивная нормальная форма: {e}")

    while True:
        print("Выберите режим работы:\n"
              "\t1) Проверить формулу на ДНФ\n"
              "\t2) Тестирование\n"
              "\t3) Сгенерировать набор ДНФ\n"
              "\t4) Сгенерировать набор не-ДНФ\n"
              "\t5) Выход\n"
              "Ваш выбор: ", end="")
        command = input()
        match command:
            case "1":
                check_mode()
            case "5":
                print("До свидания.")
                break
            case _:
                print("Неизвестный режим работы.")
