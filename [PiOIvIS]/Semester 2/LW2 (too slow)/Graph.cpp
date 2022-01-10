//
// Created by rostislove on 08.01.2022.
//

#include "Graph.h"
#include "cstdint"
#include "iostream"
#include "fstream"

Graph::Graph(std::string path) {
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

int Graph::addVertice() {   // Добавляем вершину в список

    // Расширяем предыдущие строки матрицы
    for (int i = 0; i < adjacencyMatrix.size(); i++)
        adjacencyMatrix[i].emplace_back(0);

    // Добавляем ещё одну строку и заполняем её нулями
    adjacencyMatrix.emplace_back(std::vector<int>());
    int index = adjacencyMatrix.size() - 1;
    for (int i = 0; i <= index; i++)
        adjacencyMatrix[index].emplace_back(0);

    return adjacencyMatrix.size() - 1;
}

void Graph::addEdge(int where, int what, int weight) {
    adjacencyMatrix[where][what] = weight;
    adjacencyMatrix[what][where] = weight;
}

int Graph::getMaxDistance(int vStart, int vFinish, std::vector<int> visited) {

    if (vStart == vFinish) return 0;

    for (auto vertice : visited)
        if (vertice == vStart) return INT16_MAX;

    visited.push_back(vStart);

    std::vector<int> distances;

    for (int i = 0; i < adjacencyMatrix[vStart].size(); i++)
        if (adjacencyMatrix[vStart][i] != 0)
            distances.push_back(adjacencyMatrix[vStart][i] + getMaxDistance(i, vFinish, visited));

    int max = INT16_MAX;
    for (int i = 0; i < distances.size(); i++)
        if (distances[i] < max) max = distances[i];

    return max;
}

int Graph::getMaxDistanceOfAll() {

    std::vector<int> distances;
    for (int i = 0; i < size(); i++)
        for (int j = 0; j < size(); j++)
            distances.emplace_back(
                    getMaxDistance(i, j)
            );

    int max = 0;
    for (int i = 0; i < distances.size(); i++)
        if (distances[i] > max)
            max = distances[i];

    return max;

}

int Graph::size() {
    return adjacencyMatrix.size();
}
