from old.dnflib import is_dnf
from old.dnflib import check_tokens, check_rpn
from old.dnflib import to_tokens
from old.dnflib import to_rpn
from tests.test_suites import TESTS
from old.dnflib import to_tree


def raw_to_tree(raw):
    result = to_tokens(raw)
    check_tokens(result)
    result = to_rpn(result)
    check_rpn(result)
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

if __name__ == '__main__':
    tests()