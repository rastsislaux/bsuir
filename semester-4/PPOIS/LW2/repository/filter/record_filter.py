from dataclasses import dataclass

from model.record import Record


@dataclass
class RecordFilter:
    name: str = None
    sports: str = None
    rank: str = None
    titles_count: (int, int) = None

    def filter(self, lst: list[Record]):
        result = lst
        if self.name:
            result = filter(lambda x: self.name in x.name, result)
        if self.sports:
            result = filter(lambda x: self.sports in x.sport, result)
        if self.titles_count:
            result = filter(lambda x: len(x.titles) in range(*self.titles_count), result)
        if self.rank:
            result = filter(lambda x: self.rank in x.rank, result)
        return list(result)
