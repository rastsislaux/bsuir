import json
import spacy
import sqlite3
import os

nlp = spacy.load('en_core_web_sm')

os.remove("movies.db")
db = sqlite3.connect("movies.db")
cursor = db.cursor()

pos_tag_translations = {
    'ADJ': 'adjective',
    'ADP': 'adposition',
    'ADV': 'adverb',
    'AUX': 'auxiliary',
    'CCONJ': 'coordinating conjunction',
    'DET': 'determiner',
    'INTJ': 'interjection',
    'NOUN': 'noun',
    'NUM': 'numeral',
    'PART': 'particle',
    'PRON': 'pronoun',
    'PROPN': 'proper noun',
    'PUNCT': 'punctuation',
    'SCONJ': 'subordinating conjunction',
    'SYM': 'symbol',
    'VERB': 'verb',
    'X': 'other',
}

cursor.execute("""
CREATE TABLE IF NOT EXISTS texts (
    file_id INTEGER PRIMARY KEY,
    text_id TEXT,
    num_words TEXT,
    genre TEXT,
    date TEXT,
    country TEXT,
    lang TEXT,
    imdb TEXT,
    title TEXT,
    text TEXT
)
""")

cursor.execute("""
CREATE TABLE IF NOT EXISTS wordforms (
    wordform TEXT,
    lemma TEXT,
    pos TEXT,
    dep TEXT,
    file_id INTEGER,
    FOREIGN KEY (file_id) REFERENCES texts(file_id)
);
""")

with open("sources.json") as file:
    sources = json.load(file)

for source in sources.values():
    cursor.execute("INSERT INTO texts (file_id, text_id, num_words, genre, date, country, lang, imdb, title, text) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                   (source["file_id"], source["text_id"], source["#words"], source["genre"], source["date"], source["country"], source["lang"], source["imdb"], source["title"], source["text"]))

    doc = nlp(source["text"])
    for token in doc:
        if token.text.strip().strip(",").strip(".").strip("'").strip('"').strip("@") == "":
            continue
        cursor.execute('INSERT INTO wordforms (wordform, lemma, pos, dep, file_id) VALUES (?, ?, ?, ?, ?)',
                       (token.text.lower(), token.lemma_, pos_tag_translations[token.pos_], token.dep_, source["file_id"]))

db.commit()
db.close()
