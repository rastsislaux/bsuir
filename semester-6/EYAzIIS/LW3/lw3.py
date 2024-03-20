import spacy
from pprint import pp
import tkinter as tk
from tkinter import filedialog
from tkinter import ttk
from idlelib.tooltip import Hovertip
import webbrowser
import tempfile


nlp = spacy.load('en_core_web_sm')

translations = {
  "ROOT": "Root",
  "nsubj": "Subject",
  "dobj": "Direct object",
  "iobj": "Indirect object",
  "pobj": "Object of preposition",
  "subj": "Nominal subject",
  "csubj": "Clausal subject",
  "attr": "Attribute",
  "agent": "Agent",
  "advcl": "Adverbial clause modifier",
  "advmod": "Adverbial modifier",
  "amod": "Adjectival modifier",
  "appos": "Appositional modifier",
  "aux": "Auxiliary",
  "auxpass": "Passive auxiliary",
  "cc": "Coordinating conjunction",
  "ccomp": "Clausal complement",
  "compound": "Compound word",
  "conj": "Conjunct",
  "dative": "Dative",
  "dep": "Unspecified dependency",
  "det": "Determiner",
  "expl": "Expletive",
  "intj": "Interjection",
  "mark": "Marker",
  "meta": "Meta modifier",
  "neg": "Negation modifier",
  "nmod": "Nominal modifier",
  "npadvmod": "Noun phrase as adverbial modifier",
  "nummod": "Numeric modifier",
  "oprd": "Object predicate",
  "parataxis": "Parataxis",
  "pcomp": "Prepositional complement",
  "poss": "Possession modifier",
  "preconj": "Preconjunct",
  "predet": "Pre-determiner",
  "prep": "Prepositional modifier",
  "prt": "Particle",
  "punct": "Punctuation",
  "quantmod": "Quantifier modifier",
  "relcl": "Relative clause modifier",
  "xcomp": "Open clausal complement"
}

class SyntacticAnalyzer:

    def __init__(self, text) -> None:
        self._text = text
        self._spacy_tokens = nlp(text)
        self._tokens = []

        for token in self._spacy_tokens:
            self._tokens.append({
                "token": token.text,
                "dep": translations.get(token.dep_, token.dep_)
            })

    def get_tokens(self):
        return self._tokens
    
def generate_html(tokens):
    html = """
    <!DOCTYPE html>
    <html>
    <head>
        <style>
            .hovertip {
                position: relative;
                display: inline-block;
            }
            
            .hovertip:hover::after {
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
            }
        </style>
    </head>
    <body>
        <div>
            """
    for token in tokens:
        html += f'<span class="hovertip" data-dep="{token["dep"]}" onclick="changeDep(this)">{token["token"]}</span> '
    html += """
        </div>
        <div>
            <button onclick="saveHtml()">Save</button>
        </div>
    
        <script>
            function changeDep(token) {
                var newDep = prompt("Enter new dep value:");
                if (newDep !== null) {
                    token.dataset.dep = newDep;
                }
            }
            function saveHtml() {
                var html = document.documentElement.outerHTML;
                var blob = new Blob([html], {type: "text/html"});
                var url = URL.createObjectURL(blob);
                var a = document.createElement('a');
                a.href = url;
                a.download = "output.html";
                a.click();
            }
        </script>
    </body>
    </html>
    """

    return html
    
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
            # Call a function to process the selected file and update the table (not implemented in this example)

    def analyze_file(self):
        path = self.file_path.get()
        with open(path) as f:
            text = f.read()
        tokens = SyntacticAnalyzer(text).get_tokens()
        html_content = generate_html(tokens)

        with tempfile.NamedTemporaryFile(mode='w', delete=False, suffix='.html') as temp_file:
            temp_filename = temp_file.name
            temp_file.write(html_content)

        webbrowser.open('file://' + temp_filename)
        
        
if __name__ == "__main__":
    root = tk.Tk()
    app = Interface(root)
    root.mainloop()
