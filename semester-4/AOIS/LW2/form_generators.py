CONJUNCTION = "/\\"
DISJUNCTION = "\\/"


def make_fcnf(table, variables):
    table = list(filter(lambda x: x[1] is False, table))
    constituents = []
    for values, _ in table:
        used_variables = []
        for variable, value in zip(variables, values):
            if value is True:
                used_variables.append(f"!{variable}")
            else:
                used_variables.append(f"{variable}")
        constituents.append("(" + DISJUNCTION.join(used_variables) + ")")
    return CONJUNCTION.join(constituents)


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
        constituents.append("(" + CONJUNCTION.join(vars2) + ")")
    return DISJUNCTION.join(constituents)


def make_numeric_fdnf(table):
    def tuple_to_int(bool_tuple):
        binary_str = ''.join('1' if b else '0' for b in bool_tuple)
        return int(binary_str, 2)

    table = filter(lambda x: x[1] is True, table)
    table = map(lambda x: x[0], table)
    table = map(lambda x: tuple_to_int(x), table)
    return f"\\/{tuple(table)}"
