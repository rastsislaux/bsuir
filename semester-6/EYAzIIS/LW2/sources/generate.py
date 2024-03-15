MOVIES_SOURCES_PATH = "movies_sources.txt"
MOVIES_TEXTS_PATH = "movies_text.txt"

import json

def generate_sources_list():
    with open(MOVIES_SOURCES_PATH, encoding="utf-8", errors="ignore") as file:
        raw = file.readlines()
    raw = map(lambda x: x.strip("\n"), raw)
    raw = list(raw)[3:]
    
    sources = {}
    for line in raw:
        splitted = line.split("\t")
        source = {
            "text_id": splitted[0],
            "file_id": splitted[1],
            "#words": splitted[2],
            "genre": splitted[3],
            "date": splitted[4],
            "country": splitted[5],
            "lang": splitted[6],
            "imdb": splitted[7],
            "title": splitted[8]
        }
        sources[source["file_id"]] = source
    return sources


def add_texts_to_sources(sources):
    with open(MOVIES_TEXTS_PATH, encoding="utf-8", errors="ignore") as file:
        raw = file.readlines()
    raw = map(lambda x: x.strip("\n"), raw)
    raw = list(raw)[1:]

    for line in raw:
        file_id = line[2:line.find(" ")]
        sources[file_id]["text"] = line[line.find(" "):]

sources = generate_sources_list()
print("===========================")
add_texts_to_sources(sources)

with open("sources.json", "w") as file:
    json.dump(sources, file, ensure_ascii=False, indent=2)
