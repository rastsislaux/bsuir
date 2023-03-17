from tkinter import ttk
import tkinter as tk
from tkinter.messagebox import showinfo

from controller.record_controller import RecordController
from repository.filter.record_filter import RecordFilter
from view.add_record_dialog import AddRecordDialog

PAGE_SIZE = 10


class MainView:

    # Utils:

    def __clear_table(self):
        for child in self.table.get_children(""):
            self.table.delete(child)

    def __render_table(self, *args, **kwargs):
        self.__clear_table()

        sports = self.controller.get_sports()
        sports.add("")
        self.sport_filter["values"] = tuple(sports)

        ranks = self.controller.get_ranks()
        ranks.add("")
        self.rank_filter["values"] = tuple(ranks)

        titles = self.title_filter.get()
        titles = titles.split("-")
        titles = map(lambda x: x.strip(), titles)
        titles = list(titles)
        titles_filter = None
        if len(titles) == 2:
            try:
                min = int(titles[0])
                max = int(titles[1]) + 1
                titles_filter = (min, max)
            except ValueError:
                pass

        self.fltr = RecordFilter(
            name=self.name_filter.get(),
            sports=self.sport_filter.get(),
            rank=self.rank_filter.get(),
            titles_count=titles_filter
        )

        self.total_counter["text"] = f"Total filtered records: {self.controller.get_total_count(self.fltr)}"
        for record in self.controller.get_page(self.page, PAGE_SIZE, self.fltr):
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
        self.total_counter = ttk.Label(text="Total entries found: 0")
        self.total_counter.pack(side="left")

    def __make_record_controls(self):
        self.remove_all_btn = ttk.Button(self.root, text="Remove all filtered", command=self.__handle_remove_all)
        self.remove_all_btn.pack(side="right")
        self.remove_record_btn = ttk.Button(self.root, text="Remove record", command=self.__handle_remove_record)
        self.remove_record_btn.pack(side="right")
        self.add_record_btn = ttk.Button(self.root, text="Add record", command=self.__handle_add_record)
        self.add_record_btn.pack(side="right")

    def __make_filter_controls(self):
        ttk.Label(self.root, text="Name filter:").pack()
        self.name_filter = ttk.Entry(self.root)
        self.name_filter.bind("<KeyRelease>", self.__render_table)
        self.name_filter.pack()

        ttk.Label(self.root, text="Sport filter:").pack()
        self.sport_filter = ttk.Combobox(self.root)
        self.sport_filter.bind("<<ComboboxSelected>>", self.__render_table)
        self.sport_filter.pack()

        ttk.Label(self.root, text="Rank filter:").pack()
        self.rank_filter = ttk.Combobox(self.root)
        self.rank_filter.bind("<<ComboboxSelected>>", self.__render_table)
        self.rank_filter.pack()

        ttk.Label(self.root, text="Title filter:").pack()
        self.title_filter = ttk.Entry(self.root)
        self.title_filter.bind("<KeyRelease>", self.__render_table)
        self.title_filter.pack()

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

    def __handle_remove_all(self):
        selected_items = self.controller.get_page(self.page, PAGE_SIZE, self.fltr)
        for item in selected_items:
            self.controller.remove_record(item[0])
        self.__render_table()
        showinfo(message=f"Deleted {len(selected_items)} records.")

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

        self.__make_filter_controls()
        self.__make_table()
        self.__make_paging_controls()
        self.__make_record_controls()
        self.__render_table()

    def start(self):
        self.root.mainloop()

