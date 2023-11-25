################################################################################################
#                                                                                              #
#   Задача: реализовать прямой нечеткий логический вывод                                       #
#   Вариант: 21. Реализовать прямой нечеткий логический вывод, используя импликацию Гёделя     #
#   Авторы: Липский Р. В.   (121701)                                                           #
#           Жолнерчик И. А. (121701)                                                           #
#           Стронгин А. В.   (121701)                                                          #
#                                                                                              #
################################################################################################

import argparse
from fuzzy_logic import FuzzyLogic

from parser import ANTLRFacade


def parse_file(path: str):
    with open(path) as file:
        return ANTLRFacade(file.read()).parse()


def parse_args():
    parser = argparse.ArgumentParser(description="Direct fuzzy inference")
    parser.add_argument("--kb", help="Specify the filename", required=True)
    parser.add_argument("--show-duplicates", "-sd", help="Add duplicate result predicates to comments", action="store_true")
    parser.add_argument("--output", "-o", help="Write output to file")
    return parser.parse_args()


def main():
    args = parse_args()

    logic = FuzzyLogic()
    sets, rules = parse_file(args.kb)

    #res = logic._infer(sets['B'], sets['D'], sets['A'])
    #print(res)

    inferences = logic.run_inference(sets, rules, show_duplicates=args.show_duplicates)

    if not args.output:
        for inference in inferences:
            print(inference)
    else:
        with open(args.output, "w") as file:
            file.write("\n".join(inferences))


if __name__ == '__main__':
    main()
