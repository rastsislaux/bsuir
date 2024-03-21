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
            body {
                font-size: 17px;
            }

            .hovertip {
                position: relative;
                display: inline-block;
                cursor: pointer;
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

            table {
                border-collapse: collapse;
                width: 100vw;
                max-width: 600px;
                margin-bottom: 20px;
                margin: 20px;
            }

            th, td {
                padding: 10px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                background-color: #f2f2f2;
            }

            .tag {
                display: inline-block;
                width: 15px;
                height: 15px;
                margin-right: 5px;
                border-radius: 50%;
                border: 1px solid #ccc;
            }

            #token-container {
                padding: 10px;
                border: 1px solid #ccc;
                line-height: 25px;
            }

            .custom-button {
                padding: 10px 20px;
                font-size: 16px;
                font-weight: bold;
                border: none;
                border-radius: 4px;
                background-color: #4CAF50;
                color: white;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .custom-button:hover {
                background-color: #45a049;
            }
        </style>
    </head>
    <body>
        <div style="text-align: center; padding: 10px;">
            <button class="custom-button" onclick="saveHtml()">Save</button>
        </div>

        <div id="token-container">
            """
    for token in tokens:
        html += f'<span class="hovertip" data-dep="{token["dep"]}" onclick="changeDep(this)">{token["token"]}</span> '
    html += """
        </div>

        <table>
    <tr>
        <th>Tag</th>
        <th>Color</th>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: darkred;"></span>Root</td>
        <td>Dark Red</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: darkgreen;"></span>Subject, Nominal subject, Clausal subject</td>
        <td>Dark Green</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: darkblue;"></span>Direct object, Indirect object, Object of preposition</td>
        <td>Dark Blue</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: purple;"></span>Attribute</td>
        <td>Purple</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: darkorange;"></span>Agent</td>
        <td>Dark Orange</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: teal;"></span>Adverbial clause modifier, Adverbial modifier, Adjectival modifier, Appositional modifier</td>
        <td>Teal</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: darkgoldenrod;"></span>Auxiliary, Passive auxiliary</td>
        <td>Dark Goldenrod</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: maroon;"></span>Coordinating conjunction</td>
        <td>Maroon</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: darkcyan;"></span>Clausal complement, Open clausal complement</td>
        <td>Dark Cyan</td>
    </tr>
    <tr>
        <td><span class="tag" style="background-color: firebrick;"></span>Compound word</td>
        <td>Firebrick</td>
    </tr>
    <!-- Add more rows for other tags -->
</table>
    
        <script>
            renewUnderlining();

            function changeDep(token) {
                var newDep = prompt("Enter new dep value:", token.dataset.dep);
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

            function renewUnderlining() {
                const elements = document.querySelectorAll('[data-dep]');
                elements.forEach((element) => {
                    const dep = element.getAttribute('data-dep');

                    element.style.textDecoration = null;
                    element.style.fontWeight = null;
                    element.style.fontStyle = null;
                    element.style.color = null;
                    
                    switch (dep) {
    case 'Root':
        element.style.color = "darkred";
        break;
    case 'Subject':
    case 'Nominal subject':
    case 'Clausal subject':
        element.style.color = "darkgreen";
        break;
    case 'Direct object':
    case 'Indirect object':
    case 'Object of preposition':
        element.style.color = "darkblue";
        break;
    case 'Attribute':
        element.style.color = "purple";
        break;
    case 'Agent':
        element.style.color = "darkorange";
        break;
    case 'Adverbial clause modifier':
    case 'Adverbial modifier':
    case 'Adjectival modifier':
    case 'Appositional modifier':
        element.style.color = "teal";
        break;
    case 'Auxiliary':
    case 'Passive auxiliary':
        element.style.color = "darkgoldenrod";
        break;
    case 'Coordinating conjunction':
        element.style.color = "maroon";
        break;
    case 'Clausal complement':
    case 'Open clausal complement':
        element.style.color = "darkcyan";
        break;
    case 'Compound word':
        element.style.color = "firebrick";
        break;
    case 'Conjunct':
    case 'Preconjunct':
        element.style.color = "darkolivegreen";
        break;
    case 'Dative':
        element.style.color = "indigo";
        break;
    case 'Unspecified dependency':
        element.style.color = "dimgray";
        break;
    case 'Determiner':
    case 'Pre-determiner':
        element.style.color = "fuchsia";
        break;
    case 'Expletive':
    case 'Particle':
        element.style.color = "navy";
        break;
    case 'Interjection':
        element.style.color = "olive";
        break;
    case 'Marker':
        element.style.color = "darkred";
        break;
    case 'Meta modifier':
        element.style.color = "saddlebrown";
        break;
    case 'Negation modifier':
        element.style.color = "limegreen";
        break;
    case 'Nominal modifier':
        element.style.color = "coral";
        break;
    case 'Noun phrase as adverbial modifier':
        element.style.color = "hotpink";
        break;
    case 'Numeric modifier':
        element.style.color = "silver";
        break;
    case 'Object predicate':
        element.style.color = "darkcyan";
        break;
    case 'Parataxis':
        element.style.color = "darkgray";
        break;
    case 'Prepositional complement':
        element.style.color = "darkorange";
        break;
    case 'Possession modifier':
        element.style.color = "navy";
        break;
    case 'Prepositional modifier':
        element.style.color = "goldenrod";
        break;
    case 'Punctuation':
        // No specific color
        break;
    case 'Quantifier modifier':
        element.style.color = "deepskyblue";
        break;
    case 'Relative clause modifier':
        element.style.color = "deeppink";
        break;
    default:
        // No specific color
        break;
}
                })
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
