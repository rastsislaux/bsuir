import sqlite3
from dataclasses import dataclass
from pprint import pprint
import tkinter as tk
from tkinter import ttk
from idlelib.tooltip import Hovertip
import webbrowser

@dataclass
class SearchResult:
    wordform: str
    lemma: str
    pos: str
    link: str
    examples: list[str]


class DBConnection:

    def __init__(self, path) -> None:
        self.db = sqlite3.connect(path)
        self.cursor = self.db.cursor()

    def find_info_by_word(self, word):
        self.cursor.execute("""
            SELECT DISTINCT
                wf.wordform, wf.lemma, wf.pos,
                ts.title, ts.country, ts.date
            FROM wordforms wf
            JOIN texts ts ON wf.file_id = ts.file_id
            WHERE wf.wordform LIKE '%' || ? || '%' OR wf.lemma LIKE '%' || ? || '%'
        """, (word, word))
        results = self.cursor.fetchall()

        self.cursor.execute("""
            SELECT count(*) FROM wordforms wf WHERE wf.wordform LIKE '%' || ? || '%' OR wf.lemma LIKE '%' || ? || '%'
        """, (word, word))
        occurences = self.cursor.fetchone()

        self.cursor.execute("""
            SELECT DISTINCT ts.text, ts.title, ts.country, ts.date
            FROM texts ts
            JOIN wordforms wf ON wf.file_id = ts.file_id
            WHERE wf.wordform LIKE '%' || ? || '%' OR wf.lemma LIKE '%' || ? || '%'
        """, (word, word))
        raw_examples = self.cursor.fetchall()

        examples = []
        for example in raw_examples:
            #if len(examples) == 5:
            #    break

            txt: str = example[0]

            start = txt.find(word)
            end = start

            while txt[start] not in ['.', '!', '?', ':']:
                start -= 1

            while txt[end] not in ['.', '!', '?', ':']:
                end += 1

            if (txt[start:end].strip() == ""):
                continue

            examples.append({
                "text": txt[start + 2:end + 1],
                "link": f"{example[1]} ({example[2]}, {example[3]})"
            })


        results = map(lambda x: SearchResult(x[0], x[1], x[2], f"{x[3]} ({x[4]}, {x[5]})", []), results)
        return {
            "occurences": str(occurences[0]),
            "search_results": list(results),
            "examples": examples
        }

    def close(self):
        self.db.close()


class ManagerApp:

    def __init__(self, root) -> None:
        self.conn = DBConnection("movies.db")
        self.root = root

        self.root.title("Corpus Manager")
        self.root.geometry("2300x1800")

        style = ttk.Style()
        style.theme_use("clam")
        style.configure("Treeview", rowheight=40)

        self.contact_button = ttk.Button(root, text="Contact with developers", command=self.contact_developers)
        Hovertip(self.contact_button, "If you have some problems with application that you can't resolve or encountered some kind of error\n\
you can press this button to contact the developers using mail. We'll try to help you as soon as possible.")
        self.contact_button.pack(pady=10)

        ttk.Label(root, text="Query:").pack(pady=10)

        self.entry_var = tk.StringVar()
        entry1 = ttk.Entry(root, textvariable=self.entry_var)
        entry1.pack()
        Hovertip(entry1, "Type your query here to get results. Query must be a word wordforms of which you would like to find.")

        srch = ttk.Button(root, text="Search", command=self.search)
        srch.pack(pady=10)
        Hovertip(srch, "Press this button to get results from the corpus. Before pressing this button you should type something in the input above.")

        ttk.Label(root, text="Number of occurences: ").pack(pady=10)

        self.occ_numb = tk.StringVar()
        entry1 = ttk.Entry(root, textvariable=self.occ_numb, state="disabled")
        entry1.pack(pady=10)
        Hovertip(entry1, "This field shows number of occurences of the word that you used as a query in the corpus.")

        ttk.Label(root, text="Search results:").pack(pady=10)

        # Table 1
        self.table_frame = ttk.Frame(root)
        self.table_frame.pack(pady=10)
        
        self.tree = ttk.Treeview(self.table_frame, columns=("Wordform", "Lemma", "POS", "Link"), show="headings", height=20)
        for col in ("Wordform", "Lemma", "POS", "Link"):
            self.tree.heading(col, text=col)
            self.tree.column(col, width=500)
        self.tree.pack(side="left")
        Hovertip(self.tree, "In this table you can see the results of a search. Wordform is a specific wordform entry from the table, lemma is lemma\n \
of that specific wordform, POS stands for part of speech and link is a link to the movie where this specific wordform was used.")

        # Table 2
        self.table_frame_2 = ttk.Frame(root)
        self.table_frame_2.pack(pady=10)
        
        self.tree_2 = ttk.Treeview(self.table_frame_2, columns=("Example", "Link"), show="headings", height=20)
        for col in ("Example", "Link"):
            self.tree_2.heading(col, text=col)
            self.tree_2.column(col, width=1000)
        self.tree_2.pack(side="left")

        Hovertip(self.tree_2, "In this table you can see examples of usage of the word you used\n\
in a query and a link to the movie where that specific example originated.")

    def contact_developers(self):
        webbrowser.open_new("mailto:andrewvs0707@gmail.com")

    def search(self):
        word = self.entry_var.get()
        res = self.conn.find_info_by_word(word)
        self.occ_numb.set(res["occurences"])

        # Clear existing data
        for item in self.tree.get_children():
            self.tree.delete(item)

        # Insert new data
        for result in res["search_results"]:
            result: SearchResult = result
            self.tree.insert("", "end", values=(
                result.wordform,
                result.lemma,
                result.pos,
                result.link
            ))

        for item in self.tree_2.get_children():
            self.tree_2.delete(item)

        for result in res["examples"]:
            self.tree_2.insert("", "end", values=(
                result["text"],
                result["link"]
            ))
        


if __name__ == "__main__":
    root = tk.Tk()
    app = ManagerApp(root)
    root.mainloop()
