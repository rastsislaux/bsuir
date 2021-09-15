#include <iostream>
#include <cstring>
using namespace std;
int main() {
    // Ввод
    char numbers_chars[256];
    cout << "Введите строку (не более 256 символов): ";
    cin.getline(numbers_chars, 256);
    string ns = (string)numbers_chars;

    // char[]
    cout << "Обработка массива типа char: ";
    char * numbers = strtok(numbers_chars, " ");
    while (numbers != NULL) {
        if (atoi(numbers)%2==0) cout << numbers << " ";
        numbers = strtok(NULL, " "); }
    cout << endl;

    // string
    cout << "Обработка объекта string: ";
    string buff = "";
    for (int i = 0; i <= ns.length(); i++) {
        if (ns[i] == NULL or ns[i] == ' ') {
            if ((int)ns[i-1]%2==0) cout << buff << " ";
            buff = "";
        } else buff.push_back(ns[i]);
    }
    return 0;
}
