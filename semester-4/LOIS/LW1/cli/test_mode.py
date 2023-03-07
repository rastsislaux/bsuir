import json
import random


def test_mode():
    try:
        with open("dnf.json") as file:
            dnfs = json.loads(file.read())
        with open("non_dnf.json") as file:
            non_dnfs = json.loads(file.read())
    except FileNotFoundError:
        print("Сгенерируйте наборы ДНФ и не-ДНФ, прежде чем приступать к тестированию!")
        return

    all_c = 0
    right_c = 0
    while True:
        aset = random.choice([dnfs, non_dnfs])
        formula = random.choice(aset)
        print(f"Является ли формула `{formula}` дизъюнктивной нормальной формой? (д/н):", end="")
        command = input()
        all_c += 1
        if command not in ['д', 'н']:
            break
        if (command == 'д') == (formula in dnfs):
            right_c += 1
            print(f"Ответ верный! Результат: {right_c}/{all_c}")
        else:
            print(f"Ответ неверный! Результат: {right_c}/{all_c}")
