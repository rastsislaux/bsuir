from abc import ABC, abstractmethod


class RecordRepository(ABC):

    @abstractmethod
    def get_all(self): pass

    @abstractmethod
    def add_record(self, record): pass

    @abstractmethod
    def remove_record(self, name): pass

    @abstractmethod
    def get_page(self, page: int, size: int): pass
