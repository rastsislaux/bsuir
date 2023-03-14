from controller.converter import record_to_tuple
from model.record import Record
from repository.record_repository import RecordRepository


class RecordController:

    def __init__(self, repository: RecordRepository):
        self.repository = repository

    def get_page(self, page: int, size: int) -> list[tuple]:
        records = self.repository.get_page(page, size)
        records = map(record_to_tuple, records)
        return list(records)

    def add_record(self, name, squad, position, titles: list, sport, rank):
        record = Record(name, squad, position, titles, sport, rank)
        self.repository.add_record(record)

    def remove_record(self, name):
        self.repository.remove_record(name)
