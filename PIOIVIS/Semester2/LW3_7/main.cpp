#include <iostream>

#include "setlib.h"

using namespace std;

int main() {
    cout << "Enter set: ";
    string setString;
    getline(cin, setString);

    vector<string> parsed;
    try { parsed = setl::parseString(setString); }
    catch (const std::runtime_error& e) {
        cout << e.what() << endl;
        return 0;
    }

    cout << "All possible permutations without repeats:" << endl;
    int i = 0;

    sort(parsed.begin(), parsed.end());
    do {
        cout << ++i << ". " << setl::vectorToString(parsed) << endl;
    } while (std::next_permutation(parsed.begin(), parsed.end()));

    return 0;
}
