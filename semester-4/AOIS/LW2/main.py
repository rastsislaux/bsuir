# !((!a\/!b)/\(c\/b)/\!(!a/\c))

import json
import textwrap

from form_generators import make_fcnf, make_fdnf, make_numeric_fcnf, make_numeric_fdnf, make_index
from lexer import to_tokens
from reverse_polish_notation import to_rpn
from truth_table import make_truth_table

EDUCATION_FILE = "education.json"

HELP_TEXT = ("What do you want to do?\n"
             "\t/q\t\t\tquit\n"
             "\t/fdnf\t\tget full disjunctive normal form\n"
             "\t/fcnf\t\tget full conjunctive normal form\n"
             "\t/forms\t\tget numeric and index forms\n"
             "\t/tt\t\t\tget truth table\n"
             "> ")
MODE_CHOICE_TEXT = ("Choose mode:\n"
                    "\t1) Normal\n"
                    "\t2) Education\n"
                    "Your choice: ")
ENTER_FORMULA_TEXT = "Enter your formula: "
FAILED_TO_PARSE_TEXT = "Failed to parse formula: "


def print_truth_table(table, variables):
    for values, result in table:
        text = ""
        for variable, value in zip(variables, values):
            text += f"{variable}={1 if value is True else 0}, "
        text = text[:-2]
        text += f": f({','.join(variables)}) = {1 if result is True else 0}"
        print(text)


def normal_mode():
    def actions_with_formula():
        print(HELP_TEXT, end="")

        command = input()

        match command:
            case "/forms":
                tt = make_truth_table(rpn, variables)
                index = make_index(tt)
                nfdnf = make_numeric_fdnf(tt)
                nfcnf = make_numeric_fcnf(tt)
                print(f"Answer: f{index} = {nfdnf} = {nfcnf}")
            case "/rpn":
                print(rpn)
            case "/fdnf":
                fdnf = make_fdnf(make_truth_table(rpn, variables), variables)
                print(f"Answer: {fdnf}")
            case "/fcnf":
                fcnf = make_fcnf(make_truth_table(rpn, variables), variables)
                print(f"Answer: {fcnf}")
            case "/tt":
                truth_table = make_truth_table(rpn, variables)
                print_truth_table(truth_table, variables)
            case "/q":
                return False

        return True

    while True:
        try:
            print(ENTER_FORMULA_TEXT, end="")
            raw_input = input()

            if raw_input == "/q":
                break

            tokens, variables = to_tokens(raw_input)
            variables = list(sorted(variables))
            rpn = to_rpn(tokens)
        except RuntimeError as e:
            print(f"{FAILED_TO_PARSE_TEXT}{e}")
            continue

        while actions_with_formula():
            pass


def education_mode():
    with open(EDUCATION_FILE) as file:
        qa = json.loads(file.read())

    questions = list(map(lambda x: x['q'], qa))
    for i, question in enumerate(questions):
        print(f"{i + 1}. {question}")

    while True:
        qid = input()
        if qid == "/q":
            break
        try:
            qid = int(qid)
        except ValueError as e:
            print(e)
            continue

        try:
            print('\n'.join(textwrap.wrap(qa[qid - 1]['a'], width=150)))
        except IndexError as e:
            print(e)
            continue


def main():
    print(MODE_CHOICE_TEXT, end="")

    match input():
        case "1":
            normal_mode()
        case "2":
            education_mode()


if __name__ == '__main__':
    main()
