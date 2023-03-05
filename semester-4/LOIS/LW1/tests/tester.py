from cli import raw_to_tree, is_dnf
from tests.test_suites import TESTS


def is_formula_dnf(raw):
    tree = raw_to_tree(raw)
    return is_dnf(tree)


def tests():

    for i, (formula, outcome) in enumerate(TESTS):
        if is_formula_dnf(formula) == outcome:
            print(f"[X] Test #{i} completed successfully.")
        else:
            print(f"[ ] Test #{i} FAILED:\n"
                  f"\t- For formula `{formula}` expected outcome is {outcome}, but it was {not outcome}")


if __name__ == '__main__':
    tests()
