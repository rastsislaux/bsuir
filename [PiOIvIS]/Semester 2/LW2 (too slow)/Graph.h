//
// Created by rostislove on 08.01.2022.
//

#ifndef LW2_GRAPH_H
#define LW2_GRAPH_H

#include "vector"
#include "string"

class Graph {
private:

    std::vector<std::vector<int>> adjacencyMatrix;

    int addVertice();

    void addEdge(int where, int what, int weight);

    int getMaxDistance(int vStart, int vFinish, std::vector<int> visited = std::vector<int>());

public:
    Graph(std::string path);

    int getMaxDistanceOfAll();

    int size();

};


#endif //LW2_GRAPH_H
