from controller.converter import record_to_tuple
from model.record import Record
from repository.filter.record_filter import RecordFilter
from repository.record_repository import RecordRepository


class RecordController:

    def __init__(self, repository: RecordRepository):
        self.repository = repository

    def get_page(self, page: int, size: int, fltr: RecordFilter) -> list[tuple]:
        records = self.repository.get_page(page, size, fltr)
        records = map(record_to_tuple, records)
        return list(records)

    def get_total_count(self, fltr: RecordFilter):
        records = self.repository.get_all(fltr)
        return len(records)

    def add_record(self, name, squad, position, titles: list, sport, rank):
        record = Record(name, squad, position, titles, sport, rank)
        self.repository.add_record(record)

    def remove_record(self, name):
        self.repository.remove_record(name)

    def get_ranks(self):
        return self.repository.get_ranks()

    def get_sports(self):
        return self.repository.get_sports()
