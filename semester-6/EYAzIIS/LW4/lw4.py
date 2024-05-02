import spacy
from spacytextblob.spacytextblob import SpacyTextBlob

from string import punctuation
from spacy.lang.en.stop_words import STOP_WORDS
from collections import Counter
from heapq import nlargest

import tkinter as tk
from tkinter import filedialog
from tkinter import ttk
from idlelib.tooltip import Hovertip
import webbrowser
import tempfile
from nltk.corpus import wordnet as wn

nlp = spacy.load('en_core_web_sm')
nlp.add_pipe("spacytextblob")

class SemanticAnalysis:

    def __init__(self, text) -> None:
        self.text = text
        self._doc = nlp(text)
        self.text_annotated = []
        for token in self._doc:
            if (len(token) < 2):
                continue

            try:
                self.text_annotated.append({
                    "text": token.text,
                    "def": wn.synsets(token.text)[0].definition()
                })
            except IndexError:
                self.text_annotated.append({
                    "text": token.text,
                    "def": None
                })

        for ent in self._doc.ents:
            print(ent.text, ent.start_char, ent.end_char, ent.label_)
        
        self.summary = SemanticAnalysis._make_summary(self._doc)
        self.polarity = SemanticAnalysis._get_polarity_description(self._doc._.blob.polarity)
        self.subjectivity = SemanticAnalysis._get_subjectivity_description(self._doc._.blob.subjectivity)
        self.keywords = SemanticAnalysis._extract_keywords(self._doc)
        self.entities = SemanticAnalysis._extract_entites(self._doc)


    @staticmethod
    def _make_summary(doc):
        stop_words = list(STOP_WORDS)
        pos_tags = ["PROPN", "ADJ", "NOUN", "VERB"]
        keywords = []

        # Фильтруем текст от пунктуации, stop words
        for token in doc:
            if token.text in punctuation or token.text in stop_words:
                continue
            if token.pos_ in pos_tags:
                keywords.append(token.text)

        freq_word = Counter(keywords)
        freq_word.most_common(5)

        # Вычисляем частоту слов и нормализуем её
        max_freq = Counter(keywords).most_common(1)[0][1]
        for word in freq_word.keys():
            freq_word[word] = freq_word[word] / max_freq

        # Исходя из частоты слов, вычисляем важность предложения
        sent_strength = { }
        for sent in doc.sents:
            for word in sent:
                if word.text in freq_word.keys():
                    if sent in sent_strength.keys():
                        sent_strength[sent] += freq_word[word.text]
                    else:
                        sent_strength[sent] = freq_word[word.text]
        
        # Собираем 3 предложения с самым высоким весом и составляем из них summary
        summarized_sentences = nlargest(3, sent_strength, key=sent_strength.get)
        final_sentences = [w.text for w in summarized_sentences]
        summary = ' '.join(final_sentences)

        return summary
    
    @staticmethod
    def _get_polarity_description(polarity):
        if polarity <= -0.5:
            return "Very Negative"
        elif polarity <= -0.2:
            return "Negative"
        elif polarity <= 0.2:
            return "Neutral"
        elif polarity <= 0.5:
            return "Positive"
        else:
            return "Very Positive"

    @staticmethod
    def _get_subjectivity_description(subjectivity):
        if subjectivity <= 0.2:
            return "Objective"
        elif subjectivity <= 0.4:
            return "Somewhat Objective"
        elif subjectivity <= 0.6:
            return "Neutral"
        elif subjectivity <= 0.8:
            return "Somewhat Subjective"
        else:
            return "Subjective"

    @staticmethod
    def _extract_keywords(doc):
        stop_words = list(STOP_WORDS)
        pos_tags = ["NOUN"]
        keywords = []

        # Фильтруем текст от пунктуации, stop words
        for token in doc:
            if token.text in punctuation or token.text in stop_words:
                continue
            if token.pos_ in pos_tags:
                keywords.append(token.text)

        freq_word = Counter(keywords)

        # Вычисляем частоту слов и нормализуем её
        max_freq = Counter(keywords).most_common(1)[0][1]
        for word in freq_word.keys():
            freq_word[word] = freq_word[word] / max_freq

        return freq_word.most_common(10)
    
    @staticmethod
    def _extract_entites(doc):
        entities = {}
        
        # Iterate over the entities in the document
        for entity in doc.ents:
            entity_info = {
                'text': entity.text,
                'label': entity.label_
            }
            entities[entity_info["text"]] = entity_info
        
        return entities
                

class Interface:

    def __init__(self, root) -> None:
        self.root = root
        self.root.title("Syntactic Analyzer")
        self.root.geometry("600x300")
        self.token_buttons = []

        style = ttk.Style()
        style.theme_use("clam")

        # File Selector
        self.file_path = tk.StringVar()
        self.file_label = ttk.Label(root, text="Selected File:")
        self.file_label.pack(pady=10)
        self.file_entry = ttk.Entry(root, textvariable=self.file_path, state='disabled', width=40)
        Hovertip(self.file_entry, "This field shows the path to the file that is currently selected. To change it press `Select File` button below.")
        self.file_entry.pack(pady=10)
        self.file_button = ttk.Button(root, text="Select File", command=self.select_file)
        Hovertip(self.file_button, "Press this button in order to choose a file to analyze.\nThe application supports .txt and .rtf formats.")
        self.file_button.pack(pady=10)

        self.analyze_button = ttk.Button(root, text="Analyze File", command=self.analyze_file)
        self.analyze_button.pack(pady=10)
        Hovertip(self.analyze_button, "Press this button in order to start the process of analyzing the text file specified higher.\nYou will see the results lower in the table.")


    def select_file(self):
        file_path = filedialog.askopenfilename(filetypes=[("Text Files", "*.txt *.rtf"), ("All Files", "*.*")])

        if file_path:
            self.file_path.set(file_path)

    def analyze_file(self):
        path = self.file_path.get()
        with open(path) as f:
            text = f.read()

        analysis = SemanticAnalysis(text)
        html_content = Interface._generate_html(analysis)

        with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.html') as temp_file:
            temp_filename = temp_file.name
            temp_file.write(html_content)

        webbrowser.open('file://' + temp_filename)

    @staticmethod
    def _generate_html(analysis):
        html_template = '''
        <html>
        <head>
            <script>
            function changeDep(token) {{
                var newDep = prompt("Enter new dep value:", token.dataset.dep);
                if (newDep !== null) {{
                    token.dataset.dep = newDep;
                }}
                renewUnderlining();
            }}
            function saveHtml() {{
                var html = document.documentElement.outerHTML;
                var blob = new Blob([html], {{type: "text/html"}});
                var url = URL.createObjectURL(blob);
                var a = document.createElement('a');
                a.href = url;
                a.download = "output.html";
                a.click();
            }}
            </script>
            <style>
                body {{
                    font-family: Arial, sans-serif;
                    margin: 20px;
                }}
                
                h1 {{
                    color: #333;
                }}
                
                .section {{
                    margin-bottom: 30px;
                }}
                
                .field-name {{
                    font-weight: bold;
                }}
                
                .text-field {{
                    border: 1px solid #ccc;
                    padding: 10px;
                    width: 80%;
                    margin-bottom: 10px;
                }}
                
                .list-field {{
                    list-style-type: none;
                    padding: 0;
                    margin: 0;
                }}
                
                .list-field li {{
                    margin-bottom: 5px;
                }}
                
                .keyword {{
                    font-weight: bold;
                }}
                
                .entity {{
                    font-style: italic;
                }}

                 .hovertip {{
                    position: relative;
                    display: inline-block;
                    cursor: pointer;
                }}
                
                .hovertip:hover::after {{
                    content: attr(data-dep);
                    position: absolute;
                    bottom: 125%;
                    left: 50%;
                    transform: translateX(-50%);
                    padding: 5px;
                    background-color: #000;
                    color: #fff;
                    border-radius: 4px;
                    white-space: nowrap;
                }}
            </style>
        </head>
        <body>
            <div style="text-align: center; padding: 10px;">
                <button class="custom-button" onclick="saveHtml()">Save</button>
            </div>

            <h1>Analysis Report</h1>
            
            <div id="token-container">
            '''
        for token in analysis.text_annotated:
            html_template += f'<span class="hovertip" data-dep="{token["def"]}" onclick="changeDep(this)">{token["text"]}</span> '
        html_template += '''
            </div>
            
            <!--
            <div class="section">
                <h2>Summary</h2>
                <textarea class="text-field">{}</textarea>
            </div>
            
            <div class="section">
                <h2>Polarity</h2>
                <textarea class="text-field">{}</p>
            </div>
            
            <div class="section">
                <h2>Subjectivity</h2>
                <textarea class="text-field">{}</p>
            </div>
            
            <div class="section">
                <h2>Keywords</h2>
                <ul class="list-field">
                    {}
                </ul>
            </div>
            
            <div class="section">
                <h2>Entities</h2>
                <ul class="list-field">
                    {}
                </ul>
            </div>
            -->
        </body>
        </html>
        '''

        # Format the keyword and entity lists
        keyword_list = '\n'.join('<li><span class="keyword">{}</span> ({})</li>'.format(k[0], k[1]) for k in analysis.keywords)
        entity_list = '\n'.join('<li><span class="entity">{}</span> ({})</li>'.format(e["text"], e["label"]) for _, e in analysis.entities.items())

        # Format the HTML template with the provided data
        html_content = html_template.format(
            # analysis.text,
            analysis.summary,
            analysis.polarity,
            analysis.subjectivity,
            keyword_list,
            entity_list
        )

        return html_content
        
        
if __name__ == "__main__":
    root = tk.Tk()
    app = Interface(root)
    root.mainloop()
