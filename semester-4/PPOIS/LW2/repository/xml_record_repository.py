import json
from xml.dom import minidom

from bs4 import BeautifulSoup

from model.record import Record
from repository.filter.record_filter import RecordFilter
from repository.record_repository import RecordRepository


class XmlRecordRepository(RecordRepository):

    def __init__(self, path):
        self.storage = []
        self.path = path

        with open(self.path) as file:
            data = file.read()

        bs = BeautifulSoup(data, "xml")
        records = bs.find_all("record")

        for record in records:
            self.storage.append(Record(
                name=record["name"],
                rank=record["rank"],
                position=record["position"],
                sport=record["sport"],
                squad=record["squad"],
                titles=json.loads(record["titles"].replace("'", '"'))
            ))

    def __save(self):
        doc = minidom.Document()

        xml = doc.createElement("records")
        doc.appendChild(xml)

        for record in self.storage:
            entry = doc.createElement("record")
            entry.setAttribute("name", record.name)
            entry.setAttribute("rank", record.rank)
            entry.setAttribute("sport", record.sport)
            entry.setAttribute("position", record.position)
            entry.setAttribute("squad", record.squad)
            entry.setAttribute("titles", str(record.titles))
            xml.appendChild(entry)

        with open(self.path, 'w') as f:
            f.write(doc.toprettyxml(indent="    "))

    def get_all(self, filt: RecordFilter = None):
        if filt is not None:
            return filt.filter(self.storage)

    def get_page(self, page: int, size: int, fltr: RecordFilter = None):
        return self.get_all(fltr)[page * size:(page + 1) * size]

    def add_record(self, record: Record):
        self.storage.append(record)
        self.__save()

    def remove_record(self, name: str):
        self.storage = list(filter(lambda x: x.name != name, self.storage))
        self.__save()

    def get_ranks(self) -> set[str]:
        result = self.storage
        result = map(lambda x: x.rank, result)
        return set(result)

    def get_sports(self) -> set[str]:
        result = self.storage
        result = map(lambda x: x.sport, result)
        return set(result)
