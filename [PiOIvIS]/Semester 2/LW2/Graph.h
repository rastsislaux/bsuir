//
// Created by rostislove on 08.01.2022.
//

#ifndef LW2_GRAPH_H
#define LW2_GRAPH_H

#include "vector"
#include "map"
#include "string"

class Graph {
private:

    std::vector<std::vector<std::pair<int, int>>> adjacencyList;

    void addVertice();

    void addEdge(int where, int what, int weight);

public:
    [[maybe_unused]] explicit Graph(const std::string & path);

    int getTreeDiameter();

    std::pair<int, int> getDistance(int vStart);

    size_t size();

};


#endif //LW2_GRAPH_H
