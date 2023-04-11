from cli.checker import check_dnf
from tests.test_suites import TESTS


def is_formula_dnf(raw):
    check_dnf(raw)
    return True


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
                continue
            print(f"[ ] Test #{i} FAILED: expected {outcome}, but nothing was thrown.")
            continue

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
