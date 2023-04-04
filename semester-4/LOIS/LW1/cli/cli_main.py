from cli.checker import check_dnf, DNFException
from cli.test_mode import test_mode
from generator.generator import generate_test_set


def cli_main():

    def check_mode():
        print("Ввевдите /quit для выхода")
        inp = ""
        while inp != "/quit":
            inp = input(" >>> ")
            try:
                check_dnf(inp)
                print("Это дизънюктивная нормальная форма.")
            except DNFException as e:
                print(f"Это не дизъюнктивная нормальная форма: {e}")

    while True:
        print("Выберите режим работы:\n"
              "\t1) Проверить формулу на ДНФ\n"
              "\t2) Тестирование\n"
              "\t3) Сгенерировать набор ДНФ\n"
              "\t4) Сгенерировать набор не-ДНФ\n"
              "\t5) Выход\n"
              "Ваш выбор: ", end="")
        command = input()
        match command:
            case "1":
                check_mode()
            case "2":
                test_mode()
            case "3":
                generate_test_set("dnf.json", True)
            case "4":
                generate_test_set("non_dnf.json", False)
            case "5":
                print("До свидания.")
                break
            case _:
                print("Неизвестный режим работы.")
