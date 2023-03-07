from cli.standart_mode import standart_mode
from cli.test_mode import test_mode
from dnflib.generator import generate_test_set


def cli_main():
    while True:
        print("Выберите режим работы:\n"
              "\t1) Стандартный\n"
              "\t2) Тестирование\n"
              "\t3) Сгенерировать набор ДНФ\n"
              "\t4) Сгенерировать набор не-ДНФ\n"
              "\t5) Выход\n"
              "Ваш выбор: ", end="")
        command = input()
        match command:
            case "1":
                standart_mode()
            case "2":
                test_mode()
            case "3":
                generate_test_set("dnf.json", gen_dnf=True)
            case "4":
                generate_test_set("non_dnf.json", gen_dnf=False)
            case "5":
                print("До свидания.")
                break
            case _:
                print("Неизвестный режим работы.")
