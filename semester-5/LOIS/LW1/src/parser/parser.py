################################################################################################
#                                                                                              #
#   Задача: реализовать прямой нечеткий логический вывод                                       #
#   Вариант: 21. Реализовать прямой нечеткий логический вывод, используя импликацию Гёделя     #
#   Авторы: Липский Р. В.   (121701)                                                           #
#           Жолнерчик И. А. (121701)                                                           #
#           Стронгин А. В.  (121701)                                                          #
#                                                                                              #
################################################################################################

from antlr4 import InputStream, CommonTokenStream, ParseTreeWalker

from .antlr.KBListener import KBListener
from .antlr.KBParser import KBParser
from .antlr.KBLexer import KBLexer


class AssignmentListener(KBListener):

    def __init__(self):
        self.assignments = {}

    def enterAssignment(self, ctx: KBParser.AssignmentContext):
        assignment_name = ctx.ID().getText()
        values = [
            (pair.ID().getText(), float(pair.FLOAT().getText())) for pair in ctx.pair()
        ]
        self.assignments[assignment_name] = values


class ImplicationListener(KBListener):

    def __init__(self):
        self.implications = []

    def enterRelation(self, ctx: KBParser.RelationContext):
        self.implications.append((ctx.ID(0).getText(), ctx.ID(1).getText()))


class ANTLRFacade:

    def __init__(self, source_text: str):
        self._walker = ParseTreeWalker()
        self._source_text = source_text

        self._input_stream = InputStream(self._source_text)
        self._lexer = KBLexer(self._input_stream)
        self._stream = CommonTokenStream(self._lexer)
        self._parser = KBParser(self._stream)

        self._tree = self._parser.parse()

    def _get_assignments(self):
        listener = AssignmentListener()
        self._walker.walk(listener, self._tree)
        return listener.assignments

    def _get_impls(self):
        listener = ImplicationListener()
        self._walker.walk(listener, self._tree)
        return listener.implications

    def parse(self):
        return self._get_assignments(), self._get_impls()
