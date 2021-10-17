#include <iostream>
using namespace std;

struct queue {

    struct node {
        int info;
        node *next;
    } * begin, * end;

    void push(int info) {
        node * t = new node;
        t->info = info;
        t->next = nullptr;
        if (begin == nullptr)
            begin = end = t;
        else {
            end->next = t;
            end = t;
        }
    }

    void peek() const {
        node *t = begin;
        while (t != nullptr) {
            cout << t->info << " ";
            t = t->next;
        }
    }

    int pop() {
        int out = begin->info;
        begin = begin->next;
        return out;
    }

    void clear() {
        node *t;
        while (begin != nullptr) {
            t = begin;
            begin = begin->next;
            delete t;
        }
    }
};

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
        int sum = 0, count = 0;
        for(dc_node * t = begin; t != nullptr; t = t->next) {
            sum += t->info;
            count++;
        }
        double average = (double)sum/count;
        int out = 0;
        for (dc_node * t = begin; t != nullptr; t = t->next)
            if (t->info < average)
                out += remove(t->info);
        return out;
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
    bool mode;
    cout << "С каким типом очереди вы хотите работать?\n\t0 - односвязный список\n\t1 - двусвязный список\nВаш выбор: ";
    cin >> mode; cin.ignore();

    string command;

    if (!mode) {
        auto * Queue = new queue;
        do {
            cout << ">"; getline(cin, command);
            if (command == "push") {
                int n;
                cout << "Введите значение: "; cin >> n; cin.ignore();
                Queue->push(n);
            } else if (command == "peek") {
                Queue->peek();
                cout << endl;
            } else if (command == "pop")
                cout << Queue->pop() << endl;
            else if (command == "clear")
                Queue->clear();
        } while (command != "quit");
    }
    else {
        auto * Queue = new dc_queue;
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
                cout << Queue->task() << endl;
            else if (command == "remove") {
                int n; cout << "Введите значение: "; cin >> n; cin.ignore();
                Queue->remove(n);
            } else if (command == "clear")
                Queue->clear();
        } while (command != "quit");
    }

    return 0;
}
