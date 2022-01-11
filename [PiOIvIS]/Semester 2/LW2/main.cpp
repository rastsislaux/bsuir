#include <iostream>
#include "Graph.h"

int main(int argc, char ** argv) {

    if (argc < 2) {
        std::cout << "Используйте: LW2 [path]" << std::endl;
        return 1;
    }

    Graph myGraph(argv[1]);

    std::cout << "Максимальное расстояние: " << myGraph.getTreeDiameter() << std::endl;

    return 0;

}
