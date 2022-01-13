#include <iostream>

using namespace std;

int main() {
    cout << "Введите размер (x=y=n) матрицы: ";
    int n;
    cin >> n;

    int **p;
    p = new int *[n];
    for (int i = 0; i < n; i++) p[i] = new int[n];

    // int p[n][n];

    for(int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            cin >> p[i][j];
    for(int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            if (p[i][j] != p[n-j-1][n-i-1]) {
                cout << "Введённая матрица НЕ ЯВЛЯЕТСЯ симметричной относительно побочной диагонали.";
                return 0;
            }
    cout << "Введённая матрица ЯВЛЯЕТСЯ симметричной относительно побочной диагонали.";
    return 0;
}
