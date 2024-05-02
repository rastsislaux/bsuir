import tkinter as tk
from collections import Counter
from tkinter import filedialog
from tkinter import ttk
import tkinter.messagebox as messagebox
import spacy
import webbrowser   
import os
import json
from idlelib.tooltip import Hovertip
import re

nlp = spacy.load('en_core_web_sm')

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


def validate_numeric_input(input):
    # Regular expression to match numeric input
    pattern = r'^\d*$'
    return re.match(pattern, input) is not None


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


def beautiful(data: dict):
    return f"Text: {data['text']}, Lemma: {data['lemma']}, Pos: {data['pos']}"


class MyApp:
    def __init__(self, root, db):
        self.db = db
        self.show = db
        self.root = root
        self.columns = ("Word", "Morphologic Info", "Occurrences")
        self.root.title("Text Analyzer")
        self.root.geometry("1600x1600")
        style = ttk.Style()
        style.theme_use("clam")
        style.configure("Treeview", rowheight=40)
        
        self.apply_button = None

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

        container = tk.Frame(root)
        container.pack()

        # Create labels and entry fields
        self.word_var = tk.StringVar()
        word_label = tk.Label(container, text="Word:")
        word_label.pack(side="left")
        word_entry = tk.Entry(container, textvariable=self.word_var)
        word_entry.pack(side="left")
        Hovertip(word_entry, "In this entry you can put a word or a part of a word, \n\
and only rows where the word matches your input will be shown in the table below. \n\
You can leave this field empty, then no filtering will be performed.")

        self.info_var = tk.StringVar()
        morphologic_label = tk.Label(container, text="Morphologic Info:")
        morphologic_label.pack(side="left")
        morphologic_entry = tk.Entry(container, textvariable=self.info_var)
        morphologic_entry.pack(side="left")
        Hovertip(morphologic_entry, "In this entry you can put some text that contains morphological info, \n\
and only rows where morphological info matches your input will be show in the table below. \n\
You can leave this field empty, then no filtering will be performed.")

        validate_numeric = root.register(validate_numeric_input)

        occurences_label = tk.Label(container, text="Occurences:")
        occurences_label.pack(side="left")

        self.occurences_lower_var = tk.StringVar()
        occurences_lower_entry = tk.Entry(container, width=5, validate="key", validatecommand=(validate_numeric, "%P"), textvariable=self.occurences_lower_var)
        occurences_lower_entry.pack(side="left")
        Hovertip(occurences_lower_entry, "In this entry you can put a number, so only word with number of occurences \n\
higher than your input will be shown in the table below. \n\
You can leave this field empty, then no filtering will be performed.")

        tk.Label(container, text=" <= x <= ").pack(side="left")

        self.occurences_higher_var = tk.StringVar()
        occurences_higher_entry = tk.Entry(container, width=5, validate="key", validatecommand=(validate_numeric, "%P"), textvariable=self.occurences_higher_var)
        occurences_higher_entry.pack(side="left")
        Hovertip(occurences_higher_entry, "In this entry you can put a number, so only words with number of occurences \n\
lower than your input will be shown in the table below. \n\
You can leave this field empty, then no filtering will be performed.")

        self.word_var.trace_add("write", self.on_entry_change)
        self.info_var.trace_add("write", self.on_entry_change)
        self.occurences_lower_var.trace_add("write", self.on_entry_change)
        self.occurences_higher_var.trace_add("write", self.on_entry_change)

        # Table
        self.table_frame = ttk.Frame(root)
        self.table_frame.pack(pady=10)

        self.tree = ttk.Treeview(self.table_frame, columns=self.columns, show="headings", height=20)
        Hovertip(self.tree, "This table represents the database of the application.\n\
The first row shows the word for which all information is shown.\n\
The second row shows morphologic information about the word.\n\
The third row shows number of occurences of this word in analyzed texts.")

        for col in self.columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=500)

        self.tree.pack(side="left")

        # Contact with developers button
        self.contact_button = ttk.Button(root, text="Contact with developers", command=self.contact_developers)
        Hovertip(self.contact_button, "If you have some problems with application that you can't resolve or encountered some kind of error\n\
                 you can press this button to contact the developers using mail. We'll try to help you as soon as possible.")
        self.contact_button.pack(pady=10)

        # Edit button to trigger editing
        self.edit_button = ttk.Button(root, text="Edit Selected", command=self.edit_selected)
        Hovertip(self.edit_button, "Before pressing this button, choose a row from the table above. Pressing this button will allow you to change\n\
                 morphological information for the word, if it is incorrect or not full.")
        self.edit_button.pack(pady=30)

        # Entry widgets for editing
        self.edit_entries = []
        for col in ("Word", "Morphologic Info"):
            entry = ttk.Entry(root)
            entry.pack()
            if col == "Word":
                entry["state"] = "disabled"
                Hovertip(entry, "This field shows the word, that you are currently editing.")
            else:
                Hovertip(entry, "This field shows morphologic information that you're editing right now.")
            self.edit_entries.append(entry)

        # Clear existing data
        for item in self.tree.get_children():
            self.tree.delete(item)

        # Insert new data
        for word, info in self.show.items():
            self.tree.insert("", "end", values=(word, beautiful(info[1]), info[0]))

        if len(self.db) == 0:
            messagebox.showinfo("Your first time", "Congratulations on the first time using our application.\n\
This application will help you analyze natural-language texts,\n\
If you're not familiar with application, check out tooltips that appear\n\
when you hover on different parts of the application.\n\
If that won't help, feel free to contact the developers using\n\
'Contact developers' button!")
            

    def on_entry_change(self, *args):
        word_filter = self.word_var.get()
        info_filter = self.info_var.get()
        low_occ = self.occurences_lower_var.get()
        low_occ = None if low_occ == "" else int(low_occ)
        high_occ = self.occurences_higher_var.get()
        high_occ = None if high_occ == "" else int(high_occ)
        
        to_show = {}
        for key, value in self.db.items():
            check_word_filter = word_filter in key
            check_info_filter = info_filter in beautiful(value[1])
            check_low_occ_filter = True
            if low_occ is not None:
                check_low_occ_filter = value[0] >= low_occ
            check_high_occ_filter = True
            if high_occ is not None:
                check_high_occ_filter = value[0] <= high_occ
            
            if check_word_filter and check_info_filter and check_low_occ_filter and check_high_occ_filter:
                to_show[key] = value

        self.show = to_show

        # Clear existing data
        for item in self.tree.get_children():
            self.tree.delete(item)

        # Insert new data
        for word, info in self.show.items():
            self.tree.insert("", "end", values=(word, beautiful(info[1]), info[0]))


    def edit_selected(self):
        selected_item = self.tree.selection()

        if not selected_item:
            return  # No item selected

        # Get values of the selected row
        values = self.tree.item(selected_item, 'values')

        self.edit_entries[0]["state"] = "enabled"

        self.edit_entries[0].delete(0, tk.END)
        self.edit_entries[0].insert(0, values[0])

        self.edit_entries[1].delete(0, tk.END)
        self.edit_entries[1].insert(0, values[1])

        self.edit_entries[0]["state"] = "disabled"

        # Fill Entry widgets with existing values
        #for entry, value in zip(self.edit_entries, values):
        #    entry.delete(0, tk.END)
        #    entry.insert(0, value)

        # Button to apply changes
        if not self.apply_button:
            self.apply_button = ttk.Button(self.root, text="Apply Changes", command=lambda: self.apply_changes(selected_item))
            Hovertip(self.apply_button, "Press this button, when you're done editing morphologic info for the word and it will be saved in the database.")
            self.apply_button.pack(pady=5)

    def apply_changes(self, selected_item):
        # Get values from Entry widgets
        new_values = [entry.get() for entry in self.edit_entries]

        # Update the table with the edited information
        self.tree.item(selected_item, values=new_values)

        # Remove the Apply Changes button
        for entry in self.edit_entries:
            entry.delete(0, tk.END)

    def select_file(self):
        file_path = filedialog.askopenfilename(filetypes=[("Text Files", "*.txt *.rtf"), ("All Files", "*.*")])

        if file_path:
            self.file_path.set(file_path)
            # Call a function to process the selected file and update the table (not implemented in this example)

    def analyze_file(self):
        path = self.file_path.get()
        with open(path) as f:
            text = f.read().replace("\n", " ")
        tokens = text.split(" ")

        counter = Counter(tokens)
        occurrences_map = dict(counter.items())
        occurrences_map.pop("")

        for word, occurrences in occurrences_map.items():
            word = word.strip(".").strip(",").strip('"').strip("'").strip("`").strip(":").strip("?").strip("!")
            if word in self.db:
                self.db[word][0] += occurrences
            else:
                self.db[word] = [occurrences, get_morphological_info(word)]

        # Clear existing data
        for item in self.tree.get_children():
            self.tree.delete(item)

        # Insert new data
        for word, info in self.show.items():
            self.tree.insert("", "end", values=(word, beautiful(info[1]), info[0]))

    def contact_developers(self):
        webbrowser.open_new("mailto:andrewvs0707@gmail.com")


if __name__ == "__main__":
    root = tk.Tk()

    db = {}
    if os.path.exists("database.json"):
        with open("database.json") as file:
            db = json.loads(file.read())

    app = MyApp(root, db)
    root.mainloop()

    with open("database.json", "w") as file:
        json.dump(app.db, file, indent=2, ensure_ascii=False)
