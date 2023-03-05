import tkinter as tk

from cli import *


class Application(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.result_text = None
        self.tree_button = None
        self.check_button = None
        self.formula_entry = None
        self.formula_label = None
        self.master = master
        self.pack()
        self.create_widgets()

    def create_widgets(self):
        self.formula_label = tk.Label(self, text="Enter Formula:")
        self.formula_label.pack()
        self.formula_entry = tk.Entry(self)
        self.formula_entry.pack()

        self.check_button = tk.Button(self, text="Check DNF", command=self.check_dnf)
        self.check_button.pack()

        self.tree_button = tk.Button(self, text="Display Tree", command=self.display_tree)
        self.tree_button.pack()

        self.result_text = tk.Text(self, height=10, width=50)
        self.result_text.pack()

    def check_dnf(self):
        raw = self.formula_entry.get()

        formula = raw_to_tree(raw)
        is_formula_dnf = is_dnf(formula)

        if is_formula_dnf:
            self.result_text.insert(tk.END, "The formula is in disjunctive normal form.\n")
        else:
            self.result_text.insert(tk.END, "The formula is not in disjunctive normal form.\n")

    def display_tree(self):
        raw = self.formula_entry.get()
        formula = raw_to_tree(raw)
        tree_str = get_tree_string(formula)
        self.result_text.insert(tk.END, tree_str)


root = tk.Tk()
app = Application(master=root)
app.mainloop()
