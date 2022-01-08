#include <iostream>
#include "Quadtree.h"
#include "map"

#define MATRIX_SIZE 16

void printMatrix(int **& matrix, int size) {

    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            std::cout << matrix[i][j] << " ";
        }
        std::cout << '\n';
    }

}

int main() {

    auto myTree = new Quadtree(0);
    Quadtree * ptr = myTree->quadrants[0] = new Quadtree(1);
    myTree->quadrants[1] = new Quadtree(8);
    myTree->quadrants[2] = new Quadtree(9);
    myTree->quadrants[3] = new Quadtree(7);
    for (int i = 2; i < 5; i++) {
        ptr = ptr->quadrants[0] = new Quadtree(i);
    }

    std::cout << "Original tree:" << std::endl;
    myTree->print("", true);

    int ** matrix = new int*[MATRIX_SIZE];
    for (int i = 0; i < MATRIX_SIZE; i++)
        matrix[i] = new int[MATRIX_SIZE];

    myTree->toMatrix(matrix, MATRIX_SIZE);
    std::cout << "Matrix from tree:" << std::endl;
    printMatrix(matrix, MATRIX_SIZE);

    auto myNewTree = new Quadtree(-1);
    myNewTree->fromMatrix(matrix, MATRIX_SIZE);
    myNewTree->normalize();
    std::cout << "Tree from matrix:" << std::endl;
    myNewTree->print("", true);

    return 0;
}
