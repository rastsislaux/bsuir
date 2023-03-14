from dataclasses import dataclass


@dataclass
class Record:
    name: str
    squad: str
    position: str
    titles: list[str]
    sport: str
    rank: str
