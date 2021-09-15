    #include <iostream>
    #include <cstring>
    using namespace std;
    int main() {
        char line[256];
        cout << "Введите строку (не более 256 символов): ";
        cin.getline(line, 256);
        char * groups = strtok(line, " ");
        while (groups != NULL) {
            if (strlen(groups) % 2 == 0) cout << groups << " ";
            groups = strtok(NULL, " "); }
        cout << endl;
        return 0;
    }

