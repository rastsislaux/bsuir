import tkinter as tk


class AddRecordDialog:
    def __init__(self, parent, on_submit):
        # Create a new window
        self.top = tk.Toplevel(parent)
        self.top.title("Dialog")

        # Create 6 Entry widgets for string input
        self.name = tk.Entry(self.top)
        self.squad = tk.Entry(self.top)
        self.position = tk.Entry(self.top)
        self.title = tk.Entry(self.top)
        self.sport = tk.Entry(self.top)
        self.rank = tk.Entry(self.top)

        # Add the Entry widgets to the window using grid layout
        self.name.grid(row=0, column=1)
        self.squad.grid(row=1, column=1)
        self.position.grid(row=2, column=1)
        self.title.grid(row=3, column=1)
        self.sport.grid(row=4, column=1)
        self.rank.grid(row=5, column=1)

        # Add labels to describe each Entry widget
        tk.Label(self.top, text="ФИО").grid(row=0, column=0)
        tk.Label(self.top, text="Состав").grid(row=1, column=0)
        tk.Label(self.top, text="Позиция").grid(row=2, column=0)
        tk.Label(self.top, text="Титулы (через зпт)").grid(row=3, column=0)
        tk.Label(self.top, text="Вид спорта").grid(row=4, column=0)
        tk.Label(self.top, text="Разряд").grid(row=5, column=0)

        # Add a button to submit the inputs
        tk.Button(self.top, text="Submit", command=lambda: on_submit(self)).grid(row=6, column=1)
