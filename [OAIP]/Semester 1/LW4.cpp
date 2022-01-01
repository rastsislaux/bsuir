#include <iostream>
#include <fstream>
#include <vector>
#include <filesystem>
#include <cstring>

using namespace std;

struct employee {
    string name, position, start_date, path;
    int department;

    employee(string name, string position, string start_date, int department) {
        this->name = name; this->position = position;
        this->start_date = start_date; this->department = department;
    }

    employee(string path) {
        this->path = path;
        ifstream fin(path);
        getline(fin, name);
        getline(fin, position);
        getline(fin, start_date);
        fin >> department; fin.close();
    }

    void save(string path) {
        ofstream fout(path);
        fout << name << endl << position << endl << start_date << endl << department;
        fout.close();
    }

    void save() {
        save(path);
    }

    string text() {
        return "Имя: " + name + "\nДолжность: " + position + "\nДата начала работы: " + start_date + "\nНомер отдела: "
            + to_string(department) + '\n';
    }
};

vector<employee> linear_search(vector<employee> where, int department) {
    vector <employee> result;
    for (employee item : where)
        if (item.department == department)
            result.push_back(item);
    return result;
}

bool in(vector<int> where, int department) {
    vector <employee> result;
    for (int item : where)
        if (item == department)
            return 1;
    return 0;
}

vector<employee> bubble_sort(vector<employee> target) {
    vector <employee> result = target;
    for (int i = 0; i < result.size(); i++)
        for (int j = 0; j < result.size(); j++)
            if (strcmp(result[i].start_date.c_str(), result[j].start_date.c_str()) > 0) {
                employee temp = result[i];
                result[i] = result[j];
                result[j] = temp;
            }
    return result;
}

void quick_sort(employee *target, int first, int last) {
    if (first < last) {
        int left = first, right = last;
        string middle = target[(left+right) / 2].start_date;
        do {
            while (strcmp(target[left].start_date.c_str(), middle.c_str()) > 0) left++;
            while (strcmp(target[right].start_date.c_str(), middle.c_str()) < 0) right--;
            if (left <= right) {
                employee temp = target[left];
                target[left] = target[right];
                target[right] = temp;
                left++; right--;
            }

        } while (left < right);
        quick_sort(target, first, right);
        quick_sort(target, left, last);
    }
}

employee binary_search(vector<employee> target, string start_date, int first, int last) {
    if (first < last) {
        int left = first, right = last;
        int middle = (left + right) / 2;
        if (strcmp(target[middle].start_date.c_str(), start_date.c_str()) == 0) return target[middle];
        else if (strcmp(target[middle].start_date.c_str(), start_date.c_str()) > 0)
            return binary_search(target, start_date, middle + 1, right);
        else return binary_search(target, start_date, left, middle - 1);
    }
    if (start_date == target[last].start_date) return target[last];
    else return employee("", "", "", -1);
}

int main() {
    vector<employee> employees;
    string path;
    cout << "Введите путь к папке с файлами сотрудников: ";
    getline(cin, path);

    for (auto &file : filesystem::directory_iterator(path))
        if (file.path().extension() == ".emp")
            employees.push_back(employee(file.path()));

    int mode;
    cout << "Выберите желаемое действие:\n\t0 - Просмотреть список работников\n\t";
    cout << "1 - Просмотреть информацию о работнике\n\t2 - Редактировать работника\n\t";
    cout << "3 - Создать нового работника\n\t4 - Линейный поиск\n\t5 - Пузырьковая сортировка\n\t";
    cout << "6 - Быстрая сортировка\n\t7 - Индивидуальное задание\n\t8 - Двоичный поиск\nВаш выбор: ";
    cin >> mode; cin.ignore();

    int number = 0, edit_mode, department;
    string start_date;
    employee n("", "", "", 0);
    vector<employee> results; vector<int> checked;

    switch (mode) {
        case 0:
            cout << "Список работников:" << endl;
            for (employee item : employees) {
                cout << '\t' << number << ". " << item.name << " - " << item.start_date << endl;
                number++;
            }
            break;
        case 1:
            cout << "Введите номер работника: "; cin >> number;
            cout << employees[number].text();
            break;
        case 2:
            cout << "Введите номер работника: "; cin >> number;
            cout << employees[number].text();
            cout << "Что вы хотите изменить?\n\t0 - ФИО\n\t1 - Должность\n\t2 - Дата начала работы\n\t3 - Номер отдела";
            cin >> edit_mode; cin.ignore(); cout << "Введите новое значение: ";
            switch (edit_mode) {
                case 0:
                    getline(cin, employees[number].name); break;
                case 1:
                    getline(cin, employees[number].position); break;
                case 2:
                    getline(cin, employees[number].start_date); break;
                case 3:
                    cin >> employees[number].department; break;
                default:
                    cout << "Выбранного вами поля не существует." << endl; break;
            }
            employees[number].save();
            break;
        case 3:
            cout << "Введите ФИО: "; getline(cin, n.name);
            cout << "Введите должность: "; getline(cin, n.position);
            cout << "Введите дату начала работы: "; getline(cin, n.start_date);
            cout << "Введите номер отдела: "; cin >> n.department; cin.ignore();
            cout << "Введите путь к новому файлу: "; getline(cin, n.path);
            n.save();
            break;
        case 4:
            cout << "Введите искомый отдел: ";
            cin >> department;
            results = linear_search(employees, department);
            for (employee item : results)
                cout << item.name << " - " << item.start_date << endl;
            break;
        case 5:
            results = bubble_sort(employees);
            for (employee item : results)
                cout << item.name << " - " << item.start_date << endl;
            break;
        case 6:
            quick_sort(&employees[0], 0, employees.size()-1);
            for (employee item : employees)
                cout << item.name << " - " << item.start_date << endl;
            break;
        case 7:
            quick_sort(&employees[0], 0, employees.size()-1);
            for (int i = 0; i < employees.size(); i++) {
                if (in(checked, employees[i].department)) continue;
                cout << "Отдел " << employees[i].department << ":" << endl;
                results = linear_search(employees, employees[i].department);
                for (employee item : results)
                    cout << item.name << " - " << item.start_date << endl;
                checked.push_back(employees[i].department);
            }
            break;
        case 8:
            quick_sort(&employees[0], 0, employees.size()-1);
            cout << "Введите искомую дату начала работы: ";
            getline(cin, start_date);
            n = binary_search(employees, start_date, 0, employees.size()-1);
            if (n.department != -1) cout << n.text();
            else cout << "Работник с такой датой начала работы не найден." << endl;
            break;
        default:
            cout << "Вы выбрали несуществующее действие." << endl;
            break;
    }
    return 0;
}
