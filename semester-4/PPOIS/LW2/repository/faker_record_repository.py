import random

from model.record import Record
from faker import Faker

from repository.filter.record_filter import RecordFilter
from repository.record_repository import RecordRepository


FAKER_LOCALE = "en_US"


class FakerRecordRepository(RecordRepository):

    fake = Faker(FAKER_LOCALE)

    @classmethod
    def _make_fake_record(cls):
        titles = [cls.fake.location_on_land() for _ in range(random.randint(0, 3))]
        return Record(
            name=cls.fake.name(),
            squad=random.choice(["", "Goalkeeper", "Attacker", "Light forward", "Strong forward", "Center"]),
            position=random.choice(["Goalkeeper", "Defender", "Midfielder", "Forward"]),
            titles=[f"{x[2]} Cup" for x in titles],
            sport=random.choice(["Soccer", "Basketball", "American Football", "Baseball", "Volleyball", "Tennis"]),
            rank=random.choice(["Beginner", "Amateur", "Professional", "Junior", "Senior", "Elite", "All-Star"])
        )

    def __init__(self, records_count: int):
        self.storage: list[Record] = []

        for i in range(records_count):
            self.storage.append(self._make_fake_record())

    def get_all(self, fltr: RecordFilter = None):
        result = self.storage
        if fltr is not None:
            result = fltr.filter(result)
        return list(result)

    def get_page(self, page: int, size: int, fltr: RecordFilter = None):
        return self.get_all(fltr)[page * size:(page + 1) * size]

    def add_record(self, record):
        self.storage.append(record)

    def remove_record(self, name):
        self.storage = list(filter(lambda x: x.name != name, self.storage))

    def get_ranks(self) -> set[str]:
        result = self.storage
        result = map(lambda x: x.rank, result)
        return set(result)

    def get_sports(self) -> set[str]:
        result = self.storage
        result = map(lambda x: x.sport, result)
        return set(result)
