from dnflib.dnf import is_dnf
from dnflib.gram_checker import check_grammar
from dnflib.lexer import to_tokens
from dnflib.rpn import to_rpn
from tests.test_suites import TESTS
from dnflib.tree import to_tree


def raw_to_tree(raw):
    result = to_tokens(raw)
    check_grammar(result)
    result = to_rpn(result)
    result = to_tree(result)
    return result


def is_formula_dnf(raw):
    tree = raw_to_tree(raw)
    return is_dnf(tree)


def tests():

    for i, (formula, outcome) in enumerate(TESTS):

        if isinstance(outcome, type) and issubclass(outcome, Exception):
            try:
                is_formula_dnf(formula)
            except outcome as e:
                print(f"[X] Test #{i} completed successfully.")
                continue
            except Exception as e:
                print(f"[ ] Test #{i} FAILED: expected {outcome}, but {e} was thrown.")
            print(f"[ ] Test #{i} FAILED: expected {outcome}, but nothing was thrown.")

        try:
            if is_formula_dnf(formula) == outcome:
                print(f"[X] Test #{i} completed successfully.")
            else:
                print(f"[ ] Test #{i} FAILED:\n"
                      f"\t- For formula `{formula}` expected outcome is {outcome}, but it was {not outcome}")
        except Exception as e:
            print(f"[ ] Test #{i} FAILED:\n"
                  f"\t- For formula `{formula}` {outcome} expected, but `{e}` was thrown")
