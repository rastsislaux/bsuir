from controller.record_controller import RecordController
from repository.faker_record_repository import FakerRecordRepository
from view.main_view import MainView


if __name__ == '__main__':
    repository = FakerRecordRepository(5)
    controller = RecordController(repository)

    MainView(controller).start()
