import logging

import psycopg2
from psycopg2 import OperationalError


class DAOException(RuntimeError):
    pass


class DAO:

    def __init__(self, db_name, db_user, db_pass, db_host, db_port=5432):
        self._connection = None
        try:
            logging.info(f"Trying to connect to PostgreSQL at {db_host}:{db_port}")
            self._connection = psycopg2.connect(
                database=db_name,
                user=db_user,
                password=db_pass,
                host=db_host,
                port=db_port
            )
            logging.info("Connection to PostgreSQL was successful.")
        except OperationalError as e:
            logging.error("Connection to PostgreSQL failed.")

    @property
    def is_connected(self):
        return self._connection is not None

    def _execute_query(self, query, fetch=True):
        cursor = self._connection.cursor()
        result = None
        try:
            cursor.execute(query)
            if fetch:
                result = cursor.fetchall()
                return result
            else:
                return None
        except OperationalError as e:
            logging.error(f"PSQL Error: {e}")
        return result

    def _get_grade_id_by_name(self, grade):
        return self._execute_query(f"SELECT id FROM grades WHERE name = '{grade}'")[0][0]

    def get_all_workers(self):
        return self._execute_query("SELECT w.id, surname, w.name, patronim, pos, trade_union, g.name FROM workers w "
                                   "JOIN grades g ON w.grade_id = g.id")

    def get_worker(self, worker_id):
        return self._execute_query(f"SELECT w.id, surname, w.name, patronim, pos, trade_union, g.name FROM workers w "
                                   f"JOIN grades g ON w.grade_id = g.id "
                                   f"WHERE w.id = {worker_id}")[0]

    def add_worker(self, surname, name, patronim, position, trade_union, grade):
        grade_id = self._get_grade_id_by_name(grade)
        return self._execute_query(f"INSERT INTO workers(surname, name, patronim, pos, trade_union, grade_id) "
                                   f"VALUES ('{surname}', '{name}', '{patronim}', '{position}', {trade_union}, {grade_id})",
                                   fetch=False)

    def edit_worker(self, worker_id, surname, name, patronim, position, trade_union, grade):
        grade_id = self._get_grade_id_by_name(grade)
        self._execute_query(f"UPDATE workers SET "
                            f"surname = '{surname}', name = '{name}', patronim = '{patronim}', "
                            f"pos = '{position}', trade_union = {trade_union}, grade_id={grade_id} "
                            f"WHERE id = {worker_id}", fetch=False)

    def delete_worker(self, worker_id):
        self._execute_query(f"DELETE FROM workers WHERE id = {worker_id}", fetch=False)

    def get_all_grades(self):
        return self._execute_query("SELECT * FROM grades")

    def get_grade(self, grade_id):
        return self._execute_query(f"SELECT * FROM grades WHERE id = {grade_id}")[0]

    def add_grade(self, name, coefficient):
        self._execute_query(f"INSERT INTO grades(name, coefficient) "
                            f"VALUES ('{name}', {coefficient})", fetch=False)

    def edit_grade(self, grade_id, name, coefficient):
        self._execute_query(f"UPDATE grades SET "
                            f"name = '{name}', coefficient = {coefficient} "
                            f"WHERE id = {grade_id}", fetch=False)

    def delete_grade(self, grade_id):
        self._execute_query(f"DELETE FROM grades WHERE id = {grade_id}", fetch=False)

    def payday(self, month):
        self._execute_query(f"""
            WITH settings AS (
                SELECT
                    (SELECT value FROM settings WHERE name = 'minimal_payment') AS minimal_payment,
                    (SELECT value FROM settings WHERE name = 'surcharge') AS surcharge,
                    (SELECT value FROM settings WHERE name = 'income_tax') AS income_tax,
                    (SELECT value FROM settings WHERE name = 'pension_fund') AS pension_fund,
                    (SELECT value FROM settings WHERE name = 'trade_union') AS trade_union
            )
            INSERT INTO payments (datetime, salary, surcharge, income_tax, pension_fund, trade_union, worker_id)
            SELECT
                '{month}',
                minimal_payment * g.coefficient,
                minimal_payment * g.coefficient * surcharge,
                (minimal_payment * g.coefficient) * income_tax,
                (minimal_payment * g.coefficient) * pension_fund,
                (minimal_payment * g.coefficient) * CASE WHEN w.trade_union THEN settings.trade_union ELSE 0 END,
                w.id
            FROM workers w
            JOIN grades g ON w.grade_id = g.id
            CROSS JOIN settings;
        """, fetch=False)

    def get_worker_payments(self, worker_id):
        return self._execute_query(f"SELECT workers.id, surname, workers.name, patronim, pos, workers.trade_union, "
                                   f"g.name, p.id, datetime, "
                                   f"       salary, surcharge, income_tax, pension_fund, p.trade_union, p.payoff "
                                   f"FROM workers JOIN payments p on workers.id = p.worker_id "
                                   f"JOIN grades g ON workers.grade_id = g.id "
                                   f"WHERE worker_id = {worker_id}")

    def get_workers_with_lowest_payoffs(self, datetime):
        return self._execute_query(f"""
        SELECT w.id, w.surname, w.name, w.patronim, w.pos, w.trade_union, g.name, p.payoff
        FROM workers w
        JOIN payments p ON w.id = p.worker_id
        JOIN public.grades g on w.grade_id = g.id
        WHERE p.datetime LIKE '{datetime}'
          AND p.payoff = (
            SELECT MIN(payoff)
            FROM payments
            WHERE datetime LIKE '{datetime}'
        )
        """)

    def commit(self):
        self._connection.commit()
