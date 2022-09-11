#include <iostream>
#include <fstream>
#include <vector>
#include <tuple>
#include <sstream>
#include <map>

#define NUMBER_OF_TESTS 11
#define INPUT_EXT       ".in"
#define INPUT_PATH      "input.in"
#define OUTPUT_PATH     "out.out"

#define USAGE_MSG       "Usage: checker.exe [your executable]\n"
#define OK_MSG          "Test %d successful!\n"
#define FAIL_MSG        "Test %d failed...\n"

#define OPEN_FAILED     "Failed to open file.\n"

using namespace std;

class DisjointSet {

    map<int, int> parent;
    size_t number_of_sets = 0;

public:
    void make_set(int x) {
        parent[x] = x;
        number_of_sets++;
    }

    int find(int x) {
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    void unite(int x, int y) {
        x = find(x);
        y = find(y);
        if (x != y) number_of_sets--;
        if (rand() % 2 == 0) parent[x] = y;
        else parent[y] = x;
    }

    size_t size() {
        return number_of_sets;
    }

};

void copy_file(const string& src, const string& dst) {
    ifstream src_file(src, std::ios::binary);
    ofstream dst_file(dst, std::ios::binary);
    dst_file << src_file.rdbuf();
}

vector<tuple<int, int, int>> load_answer(const string& path) {
    vector<tuple<int, int, int>> roads;
    ifstream fin(path);

    if (!fin) {
        printf(OPEN_FAILED);
        return {};
    }

    string str;
    getline(fin, str);
    do {
        stringstream ss(str);
        int f, s, t;
        ss >> f >> s >> t;
        roads.emplace_back(f, s, t);
        getline(fin, str);
    } while (!str.empty());

    return roads;
}

pair<int, int> load_requirements(const string& path) {
    ifstream fin(path);
    if (!fin) {
        printf(OPEN_FAILED);
    }
    int villagesCount, cobbleStoneCount;
    fin >> villagesCount >> cobbleStoneCount >> cobbleStoneCount;
    fin.close();
    return {villagesCount, cobbleStoneCount};
}

bool check_cobblestone(const vector<tuple<int, int, int>>& roads, int maxCobbleStone) {
    int counter = 0;
    for (auto & [first, second, type] : roads) {
        if (type == 0) {
            counter++;
        }
    }
    printf("Number of cobblestone roads: %d/%d\n", counter, maxCobbleStone);
    if (counter != maxCobbleStone) return false;
    return true;
}

bool check_coherence(const vector<tuple<int, int, int>>& roads, int villagesCount) {
    DisjointSet ds;
    for (int i = 1; i <= villagesCount; ds.make_set(i), i++);
    for (auto [first, second, type] : roads) {
        ds.unite(first, second);
    }
    return ds.size() == 1;
}

bool check_answer(const string& path) {
    auto roads = load_answer(path);
    auto [villagesCount, cobbleStoneCount] = load_requirements(INPUT_PATH);
    printf("Cobblestone count check: %d\n", check_cobblestone(roads, cobbleStoneCount));
    printf("Coherence check check: %d\n", check_coherence(roads, villagesCount));
    return (check_cobblestone(roads, cobbleStoneCount) && check_coherence(roads, villagesCount));
}

int main(int argc, char ** argv) {

    if (argc < 2) {
        printf(USAGE_MSG);
        return 1;
    }

    for (int i = 1; i <= NUMBER_OF_TESTS; i++) {
        copy_file(to_string(i) + INPUT_EXT, INPUT_PATH);
        system(argv[1]);
        if (check_answer(OUTPUT_PATH)) {
            printf(OK_MSG, i);
        } else {
            printf(FAIL_MSG, i);
        }
        printf("-----------------------------------------\n");
    }

    return 0;
}
