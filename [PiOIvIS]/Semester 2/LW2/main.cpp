#include <fstream>
#include "Graph.h"

int main() {

    Graph myGraph("input.txt");
    std::ofstream fout("output.txt");
    fout << myGraph.getTreeDiameter();
    return 0;

}
