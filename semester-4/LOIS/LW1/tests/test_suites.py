# --------------------------------------------------------------------------------
# Лабораторная работа №1 по дисциплине "Логические основы интеллектуальных систем"
# Выполнена студентами группы 121701 БГУИР:
#  - Липский Ростислав Владимирович
#  - Олешкевич Алексей Сергеевич
#  - Смольник Владислав Андреевич
#
#


TESTS = [
    (r"A", True),
    (r"(!A)", True),
    (r"((A\/B)\/C)", True),
    (r"(A\/(B\/C))", True),
    (r"(A\/B)", True),
    (r"((A/\B)\/C)", True),
    (r"((A/\B)\/(A/\C))", True),
    (r"((A/\B)\/((A/\B)\/((A/\B)/\C)))", True),
    (r"((A/\((!B)/\(!C)))\/((!D)/\(E/\F)))", True),

    (r"(A\/(B/\(C\/D)))", RuntimeError),
    (r"(!(A\/B))", RuntimeError),
    (r"(((A\/B)\/C)/\D)", RuntimeError),
    (r"(((A/\B)\/(C\/D))\/((E\/F)\/(G/\H)))", RuntimeError),

    (r"a", RuntimeError),
    (r"A12", RuntimeError),
    (r"(A!B)", RuntimeError),
    (r"((A\/B\/C))", RuntimeError),
    (r"A/\B/\C", RuntimeError),
    (r"(A \/ B)", RuntimeError),
    (r"(A)", RuntimeError),
    (r"A\/B", RuntimeError),
]
