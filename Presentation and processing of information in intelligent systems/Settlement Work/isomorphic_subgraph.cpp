#include <iostream>
#include <vector>
#include <map>
#include <fstream>

using namespace std;

// Комбинаторика
void swap(int *a, int i, int j)
{
    int s = a[i];
    a[i] = a[j];
    a[j] = s;
}
bool next_permutation(int *a, int n)
{
    int j = n - 2;
    while (j != -1 && a[j] >= a[j + 1]) j--;

    if (j == -1)
        return false;

    int k = n - 1;
    while (a[j] >= a[k]) k--;
    swap(a, j, k);
    int l = j + 1, r = n - 1;
    while (l<r)
        swap(a, l++, r--);
    return true;
}
map<int, int> reverse(map<int, int> a, int size) {
    map<int, int> result;
    for (int i = 0; i < size; i++)
        result[i] = a[size-i-1];
    return result;
}

// Функция факториала
double factorial(int number) {
    if (!number or number == 1) return 1;
    return number* factorial(number-1);
}

// Поиск изоморфных подграфов
vector<map<int, int>> isomorphic_subgraph_vertex_matches(int ** matrix1, int ** matrix2, int vc1, int vc2) {

    vector<map<int, int>> possible_matches;
    int * temp = new int[vc1];
    for (int i = 0; i < vc1; i++)
        temp[i] = i;

    int permutation_counter = 1;
    int fact = (int)factorial(vc1);

    do {
        cout << "\rИщем перестановки: " << permutation_counter << "/" << fact << " (" << (int)((double)permutation_counter/fact*100) << "%)";
        permutation_counter++;
        map<int, int> temp_matches;
        for (int i = 0; i < vc2; i++)
            temp_matches[i] = temp[i];
        if (find(possible_matches.begin(), possible_matches.end(), temp_matches) == possible_matches.end())
            possible_matches.push_back(temp_matches);
    } while (next_permutation(temp, vc1));

    cout << endl;

    vector<map<int, int>> result;
    permutation_counter = 1;
    for (auto matches : possible_matches) {
        cout << "\rПроверяем возможные соответствия: " << permutation_counter << "/" << possible_matches.size()
            <<" (" << (int)(permutation_counter/(double)possible_matches.size()*100) << "%)";
        permutation_counter++;
        bool isomorphic_subgraph = true;
        for (int i = 0; i < vc2 and isomorphic_subgraph; i++)
            for (int j = 0; j < vc2 and isomorphic_subgraph; j++) {
                if (matrix2[i][j] != matrix1[matches[i]][matches[j]])
                    isomorphic_subgraph = false;
            }
        if (isomorphic_subgraph and find(result.begin(), result.end(), reverse(matches, vc2)) == result.end()) result.push_back(matches);
    }
    cout << endl;
    return result;
}

// Визуализация
bool is_in_match(int vertex, map<int, int> match) {
    for (int i = 0; i < match.size(); i++)
        if (match[i] == vertex) return true;
    return false;
}

string translate_matrix_to_dot(int ** matrix, map<int, int> matches, int size) {
    string result = "graph new_graph {\n\t";
    for (int i = 0; i < size; i++) {
        result += to_string(i) + (is_in_match(i, matches) ? "[fillcolor=blue]" : "[fillcolor=white]") + ";\n\t";
    }
    for (int i = 0; i < size; i++)
        for (int j = i; j < size; j++)
            if (matrix[i][j] == 1)
                result += to_string(i) + "--" + to_string(j) + ";\n\t";

    result += "}\n";
    return result;
}

// Основная функция
int main(int argc, char * argv[]) {

    if(argc > 1) {
        for (int i = 1; i < argc; i++) {
            ifstream fin;
            fin.open(argv[i]);

            int size1, size2;
            fin >> size1;
            int **matrix1 = new int *[size1];
            for (int j = 0; j < size1; j++) {
                matrix1[j] = new int[size1];
                for (int k = 0; k < size1; k++)
                    fin >> matrix1[j][k];
            }

            fin >> size2;
            int **matrix2 = new int *[size2];
            for (int j = 0; j < size2; j++) {
                matrix2[j] = new int[size2];
                for (int k = 0; k < size2; k++)
                    fin >> matrix2[j][k];
            }

            ofstream fout; fout.open("orig/orig_graph" + to_string(i) + ".dot");
            fout << translate_matrix_to_dot(matrix1, *(new map<int, int>), size1); fout.close();
            fout.open("orig/sample_graph" + to_string(i) + ".dot");
            fout << translate_matrix_to_dot(matrix2, *(new map<int, int>), size2); fout.close();

            vector<map<int, int>> matches = isomorphic_subgraph_vertex_matches(matrix1, matrix2, size1, size2);

            int mc = 0;
            vector<string> saved;
            for (auto match: matches) {
                string temp_s = translate_matrix_to_dot(matrix1, match, size1);
                if (std::find(saved.begin(), saved.end(), temp_s) == saved.end()) {
                    ofstream fout("results/graph" + to_string(i) + "_" + to_string(mc) + ".dot");
                    fout << temp_s; fout.close(); mc++;
                    saved.push_back(temp_s);
                }
            }

            cout << endl;
            fin.close();
        }
        cout << "Работа программы завершена." << endl;
    } else
        cout << "Введите файлы заданий в качестве аргументов." << endl;
    return 0;
}
