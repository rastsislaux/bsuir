#include <iostream>

using namespace std;

string no_recursion(int number, int system) {
    string result = "";
    while (number >= system) {
        result = to_string(number % system) + result;
        number = number / system;
    }
    result = to_string(number) + result;
    return result;
}

string recursion(int number, int system) {
    if (number < system)
        return to_string(number);
    string result = recursion(number / system, system) + to_string(number % system);
    return result;
}

int main() {
    int n, p;
    cout << "Введите числа N и P (0<P<10): ";
    cin >> n >> p;
    cout << no_recursion(n, p) << " " << recursion(n, p);
}
