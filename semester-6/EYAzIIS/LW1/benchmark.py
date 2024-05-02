import spacy
from lorem_text import lorem
import time
import matplotlib.pyplot as plt

nlp = spacy.load('en_core_web_sm')

text_lengths = [i for i in range(1, 300, 3)]

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

def get_morphological_info(word):
    doc = nlp(word)

    morphological_info = None
    for token in doc:
        morphological_info = {
            'text': token.text,
            'lemma': token.lemma_,
            'pos': pos_tag_translations[token.pos_],
        }

    return morphological_info

processing_times = []

for length in text_lengths:
    print(f"Checking {length} words...")
    # Generate dummy text
    text = lorem.words(length)
    text = text.lower().split(' ')

    # Perform your operation on the text
    start_time = time.time()
    for word in text:
        word = word.strip(".").strip(",").strip('"').strip("'").strip("`").strip(":").strip("?").strip("!")
        get_morphological_info(word)
    end_time = time.time()
    processing_time = end_time - start_time
    print(f"Time: {processing_time}")
    processing_times.append(processing_time)

plt.plot(text_lengths, processing_times)
plt.xlabel('Количество слов в тексте')
plt.ylabel('Время обработки (с)')
plt.title('Text Word Count vs Processing Time')
plt.show()
