from controller.record_controller import RecordController
from repository.xml_record_repository import XmlRecordRepository
from view.main_view import MainView


if __name__ == '__main__':
    # repository = FakerRecordRepository(100)
    repository = XmlRecordRepository("data.xml")
    controller = RecordController(repository)

    MainView(controller).start()
