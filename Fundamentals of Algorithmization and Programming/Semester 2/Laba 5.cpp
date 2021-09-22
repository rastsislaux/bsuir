#include <iostream>

using namespace std;

struct stack {
    int info;
    stack *next;

    static stack * push(stack *target, int info) {
        auto *t = new stack;
        t->info = info;
        t->next = target;
        return t;
    }

    static void peek(stack *target) {
        stack *t = target;
        while (t != nullptr) {
            cout << t->info << " ";
            t = t->next;
        }
    }

    static stack * pop(stack *target, int *out) {
        stack *t = target;
        *out = target->info;
        target = target->next;
        delete t;
        return target;
    }

    static void clear(stack **target) {
        stack *t;
        while (*target != nullptr) {
            t = *target;
            *target = (*target)->next;
            delete t;
        }
    }

    static void address_swap(stack **target) {
        *target = stack::push(*target, 0);
        stack *t = nullptr, *t1, *r;
        if((*target)->next->next == nullptr) return;
        do {
            for (t1 = *target; t1->next->next != t; t1 = t1->next)
                if (t1->next->info > t1->next->next->info) {
                    r = t1->next->next;
                    t1->next->next = r->next;
                    r->next = t1->next;
                    t1->next=r;
                }
            t = t1->next;
        } while ((*target)->next->next != t);
        int i; *target = stack::pop(*target, &i);
    }

    static void info_swap(stack *target) {
        stack *t = nullptr, *t1;
        int r;
        do {
            for (t1 = target; t1->next != t; t1 = t1->next)
                if (t1->info > t1->next->info) {
                    r = t1->info;
                    t1->info = t1->next->info;
                    t1->next->info = r;
                }
            t = t1;
        } while (target->next != t);
    }

    static int task(stack **target, int n) {
        stack::info_swap(*target);
        int counter = 0, v;
        while ((*target) != nullptr and (*target)->info < n) {
            counter++;
            *target = stack::pop(*target, &v);
        }
        return counter;
    }
};

int main() {
    stack *st = nullptr;
    string command;
    int info;
    while (command != "quit") {
        cout << "> ";
        getline(cin, command);
        if (command == "push") {
            cout << "Введите информацию для ввода: ";
            cin >> info; cin.ignore();
            st = stack::push(st, info);
        } else if (command == "pop") {
            st = stack::pop(st, &info);
            cout << info << endl;
        } else if (command == "peek") {
            stack::peek(st);
            cout << endl;
        } else if (command == "clear") {
            stack::clear(&st);
        } else if (command == "aswap") {
            stack::address_swap(&st);
        } else if (command == "iswap") {
            stack::info_swap(st);
        } else if (command == "task") {
            cout << "Введите число для сравнения: ";
            cin >> info; cin.ignore();
            cout << stack::task(&st, info) << endl;
        }
    }
    return 0;
}
