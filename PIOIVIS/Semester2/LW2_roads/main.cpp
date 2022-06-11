#include <iostream>
#include <set>
#include <vector>
#include <fstream>
#include <map>

using namespace std;

class DisjointSet {

    map<int, int> parent;

public:
    void make_set(int x) {
        parent[x] = x;
    }

    int find(int x) {
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    void unite(int x, int y) {
        x = find(x);
        y = find(y);
        if (rand() % 2 == 0) parent[x] = y;
        else parent[y] = x;
    }

};

class Solver {

    int villages_count = 0;
    int roads_count = 0;
    int free_cobblestone_roads = 0;

    vector<pair<int, int>> concrete_roads;
    vector<pair<int, int>> cobblestone_roads;
    vector<tuple<int, int, int>> solution;

    void load_task(const string& path) {
        ifstream fin(path);

        fin >> villages_count
            >> roads_count
            >> free_cobblestone_roads;

        for (int i = 0; i < roads_count; i++) {
            int dep, dest, type;
            fin >> dep >> dest >> type;
            if (type == 0)
                cobblestone_roads.emplace_back(dep, dest);
            else
                concrete_roads.emplace_back(dep, dest);
        }

        fin.close();
    }

    void solve() {
        DisjointSet ds;
        solution.clear();

        for (int i = 1; i <= villages_count; ds.make_set(i), i++);

        for (auto pair : cobblestone_roads) {
            if (ds.find(pair.first) != ds.find(pair.second)) {
                solution.emplace_back(pair.first, pair.second, 0);
                ds.unite(pair.first, pair.second);
            }
            if (solution.size() == free_cobblestone_roads) break;
        }

        for (auto pair : concrete_roads) {
            if (ds.find(pair.first) != ds.find(pair.second)) {
                solution.emplace_back(pair.first, pair.second, 1);
                ds.unite(pair.first, pair.second);
            }
            if (solution.size() == villages_count - 1) break;
        }

        if (solution.size() != villages_count - 1) cout << "FAIL!";
    }

public:
    explicit Solver(const string& in_path) {
        load_task(in_path);
        solve();
    }

    void save_solution(const string& out_path) {
        ofstream out(out_path);
        if (solution.empty()) out << "no solution";
        else {
            for (auto tuple : solution) {
                out << get<0>(tuple) << " " << get<1>(tuple) << " " << get<2>(tuple) << endl;
            }
        }
        out.close();
    }

};

int main() {
    srand(time(nullptr));
    Solver solver("input.in");
    solver.save_solution("out.out");
    return 0;
}
