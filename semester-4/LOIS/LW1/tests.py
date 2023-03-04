from system import raw_to_tree, is_dnf


def formula_is_dnf(raw):
    tree = raw_to_tree(raw)
    return is_dnf(tree)


assert formula_is_dnf("( (A*(!B)*(!C)) + ((!D)*E*F) )")
assert formula_is_dnf("( (A*B) + C )")
assert formula_is_dnf("( A + B )")

assert not formula_is_dnf("(!( A + B ))")
assert not formula_is_dnf("(!( A * B ))")
assert not formula_is_dnf("( A + (B * (C + D)) )")
