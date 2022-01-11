//
// Created by rostislove on 08.01.2022.
//

#include "Graph.h"

#include "iostream"
#include "fstream"
#include "set"

[[maybe_unused]] Graph::Graph(const std::string & path) {
    std::ifstream fin(path);

    if (!fin.is_open()) {
        std::cout << "Failed to open this file." << std::endl;
        exit(1);
    }

    int count;
    fin >> count;

    for (int i = 0; i < count; i++)
        addVertice();

    do {
        int x, y, z;
        fin >> x >> y >> z;
        addEdge(x-1, y-1, z);
    } while (!fin.eof());

    fin.close();
}

void Graph::addVertice() {   // Добавляем вершину в список
    adjacencyList.emplace_back(std::vector<std::pair<int, int>>());
}

void Graph::addEdge(int where, int what, int weight) {
    adjacencyList[where].emplace_back(std::make_pair(what, weight));
    adjacencyList[what].emplace_back(std::make_pair(where, weight));
}

int Graph::getTreeDiameter() {

    int u = 0, w = 0;

    std::map<int, int> d = getDistance(0);
    for (int i = 0; i < size(); i++)
        if (d[i] > d[u])
            u = i;

    d.clear();
    d = getDistance(u);
    for (int i = 0; i < size(); i++)
        if (d[i] > d[w])
            w = i;

    return d[w];

}

std::map<int, int> Graph::getDistance(int vStart) {

    std::vector<int> d(size(), INT32_MAX);
    d[vStart] = 0;

    auto cmp = [&d](int a, int b) {
        return d[a] > d[b];
    };

    std::set<int, decltype(cmp)> queue(cmp);

    queue.insert(vStart);

    std::map<int, int> result;

    while (!queue.empty()) {

        int v = *queue.begin();
        queue.erase(queue.begin());

        for (size_t j = 0; j < adjacencyList[v].size(); ++j) {

            int to = adjacencyList[v][j].first,
                len = adjacencyList[v][j].second;

            if (d[v] + len < d[to]) {
                queue.erase(to);
                d[to] = d[v] + len;
                result[to] = d[to];
                queue.insert(to);
            }
        }

    }

    return result;

}
size_t Graph::size() {
    return adjacencyList.size();
}
