#include <fstream>
#include <iostream>
#include "Graph.h"

int main(int argc, char ** argv) {

    if (argc == 1) {
        Graph myGraph("input.txt");
        std::ofstream fout("output.txt");
        fout << myGraph.getTreeDiameter();
        fout.close();
    } else {
        Graph myGraph(argv[1]);
        std::cout << myGraph.getTreeDiameter() << std::endl;
    }
    return 0;

}
