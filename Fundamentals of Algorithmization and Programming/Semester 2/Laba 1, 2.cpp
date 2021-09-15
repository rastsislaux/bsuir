#include <iostream>
#include <fstream>
#include <filesystem>

using namespace std;

struct student {
    string name;
    int birth_year, group, physics, math, it, chemistry;
    double average;

    student(string name, int birth_year, int group, int physics, int math, int it, int chemistry) {
        this->name = name;
        this->birth_year = birth_year;
        this->group = group;

        this->physics = physics; this->math = math;
        this->it = it; this->chemistry = chemistry;

        this->average = (physics+math+it+chemistry)/4.;
    }

    student(string path) {
        ifstream fin(path);
        getline(fin, name);
        fin >> birth_year >> group >> physics >> math >> it >> chemistry;
        average = (physics+math+it+chemistry)/4.;
    }

    string text() {
        return "ФИО: " + name + "\nГод рождения: " + to_string(birth_year) + "\nНомер группы: " + to_string(group)
                + "\nОценки по физике, математике, информатике и химии: " + to_string(physics) + " "
                + to_string(math) + " " + to_string(it) + " " + to_string(chemistry) + "\nСредний балл за семестр: "
                + to_string(average) + '\n';
    }

    void save(string path) {
        ofstream fout(path);
        fout << name << endl << birth_year << " " << group << endl << physics << " " << math << " " << it << " " << chemistry;
        fout.close();
    }
};

int main() {

    bool new_file;
    cout << "Вы хотите создать новый файл (1) или работать с существующим (0)? ";
    cin >> new_file; cin.ignore();
    student *new_student;

    if (new_file) {
        string name; int birth_year, group, physics, math, it, chemistry;
        cout << "Введите ФИО студента: "; getline(cin, name);
        cout << "Введите год рождения студента: "; cin >> birth_year;
        cout << "Введите номер группы студента: "; cin >> group;
        cout << "Введите оценки студента по физике, математике, информатике и химии: ";
        cin >> physics >> math >> it >> chemistry; cin.ignore();
        new_student = new student(name, birth_year, group, physics, math, it, chemistry);
        string path;
        cout << "Введите путь, где хотите сохранить файл студента: "; getline(cin, path);
        new_student->save(path);
    }
    else {
        string path;
        int mode;
        cout << "Ввведите путь к файлу студента: "; getline(cin, path);
        new_student = new student(path);
        cout << "Выберите режим работы:\n\t0 - Просмотр\n\t1 - Коррекция\n\t2 - Решение индивидуального задания\nВаш выбор: ";
        cin >> mode; cin.ignore();

        switch (mode) {
            case 0:
                cout << new_student->text();
                break;

            case 1:
                cout << new_student->text();
                int edit_mode;
                cout << "Выберите поле, которое хотите редактировать:\n\t0 - ФИО\n\t1 - Год рождения\n\t2 - Номер группы\n\t3 - Оценки\nВаш выбор: ";
                cin >> edit_mode; cin.ignore();
                cout << "Введите новое значение: ";
                switch (edit_mode) {
                    case 0:
                        getline(cin, new_student->name);
                        break;
                    case 1:
                        cin >> new_student->birth_year;
                        break;
                    case 2:
                        cin >> new_student->group;
                        break;
                    case 3:
                        cin >> new_student->physics >> new_student->math >> new_student->it >> new_student->chemistry;
                        break;
                    default:
                        cout << "Вы выбрали несуществующий вариант." << endl;
                        break;
                }
                new_student->save(path);
                break;

            case 2:
                int group_number;
                cout << "Введите путь к каталогу, где хранятся файлы студентов (.stud): ";
                getline(cin, path);
                cout << "Введите номер интересующей вас группы: "; cin >> group_number;

                for (auto &file : filesystem::directory_iterator(path)) {
                    if (file.path().extension() == ".stud") {
                        new_student = new student(file.path());
                        if (new_student->group == group_number and new_student->average < 4)
                            cout << new_student->text();
                    }
                }
                break;
            default:
                cout << "Вы выбрали несуществующую операцию." << endl;
                break;
        }
    }
    return 0;
}
