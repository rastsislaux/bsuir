from system import raw_to_tree, is_dnf


TESTS = [
    ('A', True),
    ('T', True),
    ('.L', True),
    ('(!A)', True),
    ('(A&B)', True),
    ('(A&(!B))', True),
    ('((!A)&(!B))', True),
    ('((A&((!B)&(!C))) | ((!D)&(E&F)))', True),
    ('((A&B) | C)', True),
    ('(A|B)', True),
    ('(A|(B&(C|D)))', False),
    ("((a | b))", True),
    ("((a & b))", True),
    ("(((a | b) | c))", True),
    ("(((a & b) & c))", True),
    ("((a | (b | c)))", True),
    ("((a & (b & c)))", True),
    ('(!(A|B))', False),
    ('(!(A&B))', False),
    ("((a | b) & !(a & b))", False),
    ("((a | b) & (c & d))", False),
    ("(((a | b) & c) | (d & e))", False),
]


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
