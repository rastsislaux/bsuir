def make_fcnf(table, variables):
    table = list(filter(lambda x: x[1] is False, table))
    constituents = []
    for values, _ in table:
        vars2 = []
        for variable, value in zip(variables, values):
            if value is True:
                vars2.append(f"!{variable}")
            else:
                vars2.append(f"{variable}")
        constituents.append("(" + "\\/".join(vars2) + ")")
    return "/\\".join(constituents)


def make_numeric_fcnf(table):
    def tuple_to_int(bool_tuple):
        binary_str = ''.join('1' if b else '0' for b in bool_tuple)
        return int(binary_str, 2)

    table = filter(lambda x: x[1] is False, table)
    table = map(lambda x: x[0], table)
    table = map(lambda x: tuple_to_int(x), table)
    return f"/\\{tuple(table)}"


def make_index(table):

    def bool_tuple_to_int(bool_tuple):
        binary_str = ''.join('1' if not b else '0' for b in bool_tuple)
        return int(binary_str, 2)

    index = 0
    for values, result in table:
        #match binstr(values):
        #    case "000": weight = 128
        #    case "001": weight = 64
        #    case "010": weight = 32
        #    case "011": weight = 16
        #    case "100": weight = 8
        #    case "101": weight = 4
        #    case "110": weight = 2
        #    case "111": weight = 1
        #    case _: raise RuntimeError("Unreachable")
        weight = pow(2, bool_tuple_to_int(values))
        if result:
            index += weight
    return index


def make_fdnf(table, variables):
    table = list(filter(lambda x: x[1] is True, table))
    constituents = []
    for values, _ in table:
        vars2 = []
        for variable, value in zip(variables, values):
            if value is False:
                vars2.append(f"!{variable}")
            else:
                vars2.append(f"{variable}")
        constituents.append("(" + "/\\".join(vars2) + ")")
    return "\\/".join(constituents)


def make_numeric_fdnf(table):
    def tuple_to_int(bool_tuple):
        binary_str = ''.join('1' if b else '0' for b in bool_tuple)
        return int(binary_str, 2)

    table = filter(lambda x: x[1] is True, table)
    table = map(lambda x: x[0], table)
    table = map(lambda x: tuple_to_int(x), table)
    return f"\\/{tuple(table)}"
