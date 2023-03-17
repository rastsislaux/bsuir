from abc import ABC, abstractmethod

from model.record import Record
from repository.filter.record_filter import RecordFilter


class RecordRepository(ABC):

    @abstractmethod
    def get_all(self, filt: RecordFilter = None): pass

    @abstractmethod
    def get_page(self, page: int, size: int, fltr: RecordFilter = None): pass

    @abstractmethod
    def add_record(self, record: Record): pass

    @abstractmethod
    def remove_record(self, name: str): pass

    @abstractmethod
    def get_sports(self) -> set[str]: pass

    @abstractmethod
    def get_ranks(self) -> set[str]: pass
