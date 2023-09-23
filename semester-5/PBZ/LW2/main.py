import pprint

from DAO import DAO

import logging
import tabulate
from datetime import datetime


DB_NAME = "pbz"
DB_USER = "pbz"
DB_PASS = "pbz"
DB_HOST = "localhost"


def preinput(prompt, default):
    user_input = input(f"({default}) {prompt}")
    if user_input:
        return user_input
    else:
        return default


def current_day_month():
    current_date = datetime.now()
    return current_date.strftime("%B %Y")


def configure_logging():
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    )


class SalariesApp:

    def __init__(self):
        self._dao = DAO(
            db_name=DB_NAME,
            db_user=DB_USER,
            db_pass=DB_PASS,
            db_host=DB_HOST
        )

        if not self._dao.is_connected:
            raise RuntimeError(f"Failed to connect.")

    def run(self):
        cmd = ""
        while cmd != "/quit":
            cmd = input(" > ")
            self.handle_cmd(cmd)

    def handle_cmd(self, cmd):
        match cmd.split()[0]:
            case "/workers":
                self.handle_workers()
            case "/addworker":
                self.handle_add_worker()
            case "/editworker":
                self.handle_edit_worker()
            case "/delworker":
                self.handle_delete_worker()
            case "/grades":
                self.handle_grades()
            case "/addgrade":
                self.handle_add_grade()
            case "/editgrade":
                self.handle_edit_grade()
            case "/delgrade":
                self.handle_delete_grade()
            case "/payday":
                self.handle_payday()
            case "/payments":
                self.handle_get_payments()
            case "/lowest":
                self.handle_get_lowest()
            case "/quit":
                self._dao.commit()
            case "/help":
                self.help()

    def handle_workers(self):
        workers = self._dao.get_all_workers()
        print(tabulate.tabulate(workers, headers=[
            "ID", "Surname", "Name", "Patronim", "Position", "Trade Union", "Grade"
        ]))

    def handle_grades(self):
        grades = self._dao.get_all_grades()
        print(tabulate.tabulate(grades, headers=[
            "ID", "Name", "Coefficient"
        ]))

    def handle_add_worker(self):
        self._dao.add_worker(
            surname=input("Surname: "),
            name=input("Name: "),
            patronim=input("Patronim: "),
            position=input("Position: "),
            trade_union=input("Trade union (true/false): "),
            grade=input("Grade: ")
        )

    def handle_edit_worker(self):
        worker_id = input("ID: ")
        worker = self._dao.get_worker(worker_id)
        self._dao.edit_worker(
            worker_id=worker_id,
            surname=preinput("Surname: ", worker[1]),
            name=preinput("Name: ", worker[2]),
            patronim=preinput("Patronim: ", worker[3]),
            position=preinput("Position: ", worker[4]),
            trade_union=preinput("Trade union (true/false): ", worker[5]),
            grade=preinput("Grade: ", worker[6])
        )

    def handle_delete_worker(self):
        worker_id = input("ID: ")
        self._dao.delete_worker(worker_id)

    def handle_add_grade(self):
        self._dao.add_grade(
            name=input("Name: "),
            coefficient=input("Coefficient: ")
        )

    def handle_edit_grade(self):
        grade_id = input("ID: ")
        grade = self._dao.get_grade(grade_id)
        self._dao.edit_grade(
            grade_id=grade_id,
            name=preinput("Name: ", grade[1]),
            coefficient=preinput("Coefficient: ", grade[2])
        )

    def handle_delete_grade(self):
        print("WARNING! This action will delete all workers with that grade.")
        grade_id = input("ID: ")
        self._dao.delete_grade(grade_id)

    def handle_payday(self):
        self._dao.payday(preinput("Month: ", current_day_month()))

    def handle_get_payments(self):
        payments = self._dao.get_worker_payments(
            worker_id=input("ID: ")
        )
        print(tabulate.tabulate(payments, headers=[
            "ID", "Surname", "Name", "Position", "Trade Union", "Grade", "Payment ID", "Datetime", "Salary", "Surcharge",
            "Income Tax", "Pension Fund", "Trade Union", "Payoff"
        ]))

    def handle_get_lowest(self):
        table = self._dao.get_workers_with_lowest_payoffs(
            datetime=preinput("Month: ", current_day_month())
        )
        print(tabulate.tabulate(table, headers=[
            "ID", "Surname", "Name", "Patronim", "Position", "Trade Union", "Grade", "Payoff"
        ]))

    def help(self):
        print("""Available commands:
- /workers: Get information about all workers.
- /addworker: Add a new worker to the database.
- /editworker: Edit the details of an existing worker.
- /delworker: Delete a worker from the database.
- /grades: Get information about all grades.
- /addgrade: Add a new grade to the database.
- /editgrade: Edit the details of an existing grade.
- /delgrade: Delete a grade from the database.
- /payday: Generate payments for the current month.
- /payments: Get information about all payments.
- /lowest: Get workers with the lowest salary for a specific month.
- /quit: Save changes and exit the program.
- /help: Show this message""")


if __name__ == '__main__':
    configure_logging()
    SalariesApp().run()
