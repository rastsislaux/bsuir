from flask import Flask, request, Response
from flask_cors import CORS

from DAO import DAO

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})

dao = DAO(
    db_name="pbz",
    db_user="pbz",
    db_pass="pbz",
    db_host="localhost"
)


@app.route("/workers", methods=["POST"])
def get_workers():
    search = request.args.get("search")
    print(search)
    workers = dao.get_all_workers(search=search)
    workers = map(lambda x: {
        "id": x[0],
        "surname": x[1],
        "name": x[2],
        "patronim": x[3],
        "position": x[4],
        "trade_union": x[5],
        "grade": x[6]
    }, workers)
    return list(workers)


@app.route("/add_worker", methods=["POST"])
def add_worker():
    data = request.get_json()
    dao.add_worker(**data)
    return {}, 200


@app.route("/get_worker", methods=["POST"])
def get_worker():
    data = request.get_json()
    worker = dao.get_worker(data["worker_id"])
    return {
        "id": worker[0],
        "surname": worker[1],
        "name": worker[2],
        "patronim": worker[3],
        "position": worker[4],
        "trade_union": worker[5],
        "grade": worker[6]
    }

@app.route("/edit_worker", methods=["POST"])
def put_worker():
    data = request.get_json()
    worker = dao.get_worker(data["id"])

    dao.edit_worker(
        worker_id=data.get("id"),
        surname=worker[1] if data.get("surname") is None else data["surname"],
        name=worker[2] if data.get("name") is None else data["name"],
        patronim=worker[3] if data.get("patronim") is None else data["patronim"],
        position=worker[4] if data.get("position") is None else data["position"],
        trade_union=worker[5] if data.get("trade_union") is None else data["trade_union"],
        grade=worker[6] if data.get("grade") is None else data["grade"]
    )
    return {}, 200


@app.route("/delete_worker", methods=["POST"])
def delete_worker():
    dao.delete_worker(**request.get_json())
    return {}, 200


@app.route("/grades", methods=["POST"])
def get_grades():
    grades = dao.get_all_grades()
    grades = map(lambda x: {
        "id": x[0],
        "name": x[1],
        "coefficient": x[2]
    }, grades)
    return list(grades)

@app.route("/add_grade", methods=["POST"])
def add_grade():
    dao.add_grade(**request.get_json())
    return {}, 200


@app.route("/edit_grade", methods=["POST"])
def edit_grade():
    data = request.get_json()
    grade = dao.get_grade(data["id"])

    dao.edit_grade(
        grade_id=data.get("id"),
        name=grade[1] if data.get("name") is None else data["name"],
        coefficient=grade[2] if data.get("coefficient") is None else data["coefficient"],
    )
    return {}, 200


@app.route("/delete_grade", methods=["POST"])
def delete_grade():
    dao.delete_worker(**request.get_json())
    return {}, 200


@app.route("/payments", methods=["POST"])
def get_payments():
    payments = dao.get_worker_payments(**request.get_json())
    print(payments)
    payments = map(lambda x: {
        "id": x[0],
        "surname": x[1],
        "name": x[2],
        "patronim": x[3],
        "position": x[4],
        "trade_union": x[5],
        "grade": x[6],
        "payment_id": x[7],
        "datetime": x[8],
        "salary": x[9],
        "surcharge": x[10],
        "income_tax": x[11],
        "pension_fund": x[12],
        "trade_union_tax": x[13],
        "payoff": x[14]
    }, payments)
    return list(payments)


@app.route("/payday", methods=["POST"])
def payday():
    data = request.get_json()
    dao.payday(data["month"])
    return {}, 201


@app.route("/lowest", methods=["POST"])
def lowest_pay():
    month = request.get_json()["month"]
    data = dao.get_workers_with_lowest_payoffs(month)
    data = map(lambda x: {
        "id": x[0],
        "surname": x[1],
        "name": x[2],
        "patronim": x[3],
        "position": x[4],
        "trade_union": x[5],
        "grade": x[6],
        "payoff": x[7]
    }, data)
    return list(data)


@app.route("/get_settings", methods=["POST"])
def get_settings():
    data = dao.get_settings()
    obj = {}
    for row in data:
        obj[row[0]] = row[1]
    return obj


@app.route("/edit_settings", methods=["POST"])
def edit_settings():
    data = request.get_json()
    dao.edit_settings(
        minimal_payment=data["minimal_payment"],
        surcharge=data["surcharge"],
        income_tax=data["income_tax"],
        pension_fund=data["pension_fund"],
        trade_union=data["trade_union"]
    )
    return {}, 200


if __name__ == '__main__':
    app.run(host="0.0.0.0")
