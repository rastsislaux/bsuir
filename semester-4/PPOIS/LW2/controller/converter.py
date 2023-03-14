from model.record import Record


def record_to_tuple(record: Record) -> tuple:
    return record.name, record.squad, record.position, ", ".join(record.titles), record.sport, record.rank
