from tkinter import ttk
import tkinter as tk

from controller.record_controller import RecordController
from view.add_record_dialog import AddRecordDialog

PAGE_SIZE = 10


class MainView:

    # Utils:

    def __clear_table(self):
        for child in self.table.get_children(""):
            self.table.delete(child)

    def __render_table(self):
        self.__clear_table()
        for record in self.controller.get_page(self.page, PAGE_SIZE):
            self.table.insert("", "end", text=record[0], values=record[1:])

    # UI:

    def __make_table(self):
        self.table = ttk.Treeview(self.root, columns=("Состав", "Позиция", "Титулы", "Вид спорта", "Разряд"))
        self.table.heading("#0", text="ФИО")
        self.table.heading("Состав", text="Состав")
        self.table.heading("Позиция", text="Позиция")
        self.table.heading("Титулы", text="Титулы")
        self.table.heading("Вид спорта", text="Вид спорта")
        self.table.heading("Разряд", text="Разряд")
        self.table.pack()

    def __make_paging_controls(self):
        self.prev_page_btn = ttk.Button(self.root, text="Previous page", command=self.__handle_prev_page)
        self.prev_page_btn.pack(side="left")
        self.next_page_btn = ttk.Button(self.root, text="Next page", command=self.__handle_next_page)
        self.next_page_btn.pack(side="left")

    def __make_record_controls(self):
        self.remove_record_btn = ttk.Button(self.root, text="Remove record", command=self.__handle_remove_record)
        self.remove_record_btn.pack(side="right")
        self.add_record_btn = ttk.Button(self.root, text="Add record", command=self.__handle_add_record)
        self.add_record_btn.pack(side="right")

    # Handlers:

    def __handle_next_page(self):
        self.page += 1
        self.__render_table()

    def __handle_prev_page(self):
        if self.page > 0:
            self.page -= 1
        self.__render_table()

    def __handle_add_record(self):
        AddRecordDialog(self.root, self.__handle_add_record_submit)

    def __handle_remove_record(self):
        selected_items = self.table.selection()
        for selection in selected_items:
            item_id = selection
            value = self.table.item(item_id)['text']
            self.controller.remove_record(value)
        self.__render_table()

    def __handle_add_record_submit(self, dialog):
        name = dialog.name.get()
        squad = dialog.squad.get()
        position = dialog.position.get()
        titles = [x.strip() for x in dialog.title.get().split(",")]
        sport = dialog.sport.get()
        rank = dialog.rank.get()
        self.controller.add_record(name, squad, position, titles, sport, rank)
        self.__render_table()

    def __init__(self, controller: RecordController):
        self.page = 0
        self.controller = controller
        self.root = tk.Tk()

        self.__make_table()
        self.__make_paging_controls()
        self.__make_record_controls()
        self.__render_table()

    def start(self):
        self.root.mainloop()

