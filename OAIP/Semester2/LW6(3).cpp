#include <iostream>
using namespace std;

struct dc_queue {

    struct dc_node {
        int info;
        dc_node * prev, * next;
    } * begin, * end;

    void push_front(int info) {
        if (begin == nullptr) {
            begin = end = new dc_node;
            begin->next = nullptr; begin->prev = nullptr;
            begin->info = info;
            return;
        }
        auto * t = new dc_node;
        t->prev = nullptr; t->next = begin; t->info = info;
        begin->prev = t; begin = t;
    }

    void push_back(int info) {
        if (end == nullptr) {
            end = begin = new dc_node;
            end->next = nullptr; end->prev = nullptr;
            end->info = info;
            return;
        }
        auto * t = new dc_node;
        t->next = nullptr; t->prev = end; t->info = info;
        end->next = t; end = t;
    }

    void peek() const {
        dc_node *t = begin;
        while (t != nullptr) {
            cout << t->info << " ";
            t = t->next;
        }
    }

    int size() const {
        int size = 0;
        dc_node *t = begin;
        while (t != nullptr) {
            size++;
            t = t->next;
        }
        return size;
    }

    void peek_back() const {
        dc_node *t = end;
        while (t != nullptr) {
            cout << t->info << " ";
            t = t->prev;
        }
    }

    int remove(int key) {
        dc_node *t = begin; int c = 0;
        while (t != nullptr) {
            if (t->info == key) {
                c++;
                if (t != begin)
                    t->prev->next = t->next;
                else
                    begin = begin->next;
                if (t != end)
                    t->next->prev = t->prev;
                else
                    end = end->prev;
            }
            t = t->next;
        }
        return c;
    }

    int pop_front() {
        int out = begin->info;
        begin->next->prev = nullptr;
        begin = begin->next;
        return out;
    }

    int pop_back() {
        int out = end->info;
        end->prev->next = nullptr;
        end = end->prev;
        return out;
    }

    int task() {

        int i = 0, d = 0;
        while (i < size()) {
            int temp = pop_back();
            if (temp < 0) {
                remove(temp);
                d++;
            } else {
                push_front(temp);
                i++;
            }
        }
        return d;

    }

    void clear() {
        dc_node *t;
        while (begin != nullptr) {
            t = begin;
            begin = begin->next;
            delete t;
        }
    }
};

int main() {

    string command;

    auto * Queue = new dc_queue;

    for (int i = -1000; i < 1001; i++)
        Queue->push_back(i);

    do {
        cout << ">"; getline(cin, command);
        if (command == "pushf") {
            int n; cout << "Введите значение: "; cin >> n; cin.ignore();
            Queue->push_front(n);
        } else if (command == "pushb") {
            int n; cout << "Введите значение: "; cin >> n; cin.ignore();
            Queue->push_back(n);
        } else if (command == "peekf") {
            Queue->peek(); cout << endl;
        } else if (command == "peekb") {
            Queue->peek_back(); cout << endl;
        } else if (command == "popf")
            cout << Queue->pop_front() << endl;
        else if (command == "popb")
            cout << Queue->pop_back() << endl;
        else if (command == "task")
            cout << "Удалено " << Queue->task() << " элементов." << endl;
        else if (command == "remove") {
            int n; cout << "Введите значение: "; cin >> n; cin.ignore();
            Queue->remove(n);
        } else if (command == "clear")
            Queue->clear();
    } while (command != "quit");

    return 0;
}
