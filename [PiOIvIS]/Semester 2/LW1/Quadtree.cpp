//
// Created by rostislove on 07.01.2022.
//

#include "Quadtree.h"
#include "iostream"
#include "vector"
#include "map"

Quadtree::Quadtree(int info) {
    this->info = info;
    this->quadrants = new Quadtree*[4];
}

bool Quadtree::isLeaf() {

    for (int i = 0; i < 4; i++)
        if (quadrants[i] != nullptr)
            return false;
    return true;

}

bool isPowerOfTwo(int x)
{
    return (x & (x - 1)) == 0;
}

int getIntByMaxEntries(int * array, int size) {

    std::map<int, int> counts;
    for (int i = 0; i < 4; i++) {
        counts[array[i]]++;
    }
    int maxn = 0, maxc = 0;
    for (int i = 0; i < 4; i++)
        if (counts[array[i]] > maxc && array[i] != -1) {
            maxc = counts[array[i]];
            maxn = array[i];
        }

    return maxn;

}

void Quadtree::toMatrix(int **target, int size) {

    /* if (size % 2 != 0 && size != 1) {
        printf("Неподходящий порядок матрицы для этого дерева. (попробуйте 2^x)");
        exit(2);
    } */

    for (int i = 0; i < size; i++)
        for (int j = 0; j < size; j++)
            target[i][j] = info;

    if (size == 1)
        return;

    int ** quadrant = new int*[(int)size/2];

    if (quadrants[0]) {
        // Первый квадрант
        for (int i = 0; i < size / 2; i++)
            quadrant[i] = &target[i][size / 2];
        quadrants[0]->toMatrix(quadrant, size / 2);
    }

    if (quadrants[1]) {
        // Второй квадрант
        for (int i = 0; i < size / 2; i++)
            quadrant[i] = &target[i][0];
        quadrants[1]->toMatrix(quadrant, size / 2);
    }

    if (quadrants[2]) {
        // Третий квадрант
        for (int i = size / 2; i < size; i++)
            quadrant[i - size/2] = &target[i][0];
        quadrants[2]->toMatrix(quadrant, size / 2);
    }

    if (quadrants[3]) {
        // Четвертый квадрант
        for (int i = size / 2; i < size; i++)
            quadrant[i - size/2] = &target[i][size / 2];
        quadrants[3]->toMatrix(quadrant, size / 2);
    }

}

void Quadtree::fromMatrix(int **target, int size) {

    int inf = target[0][0];
    bool final = true;
    for (int i = 0; i < size; i++)
        for (int j = 0; j < size; j++)
            if (target[i][j] != inf)
                final = false;

    if (final)
        this->info = inf;
    else {
        this->info = -1;

        int **quadrant = new int *[(int) size / 2];

        // Первый квадрант
        for (int i = 0; i < size / 2; i++)
            quadrant[i] = &target[i][size / 2];
        quadrants[0] = new Quadtree(-1);
        quadrants[0]->fromMatrix(quadrant, size / 2);

        // Второй квадрант
        for (int i = 0; i < size / 2; i++)
            quadrant[i] = &target[i][0];
        quadrants[1] = new Quadtree(-1);
        quadrants[1]->fromMatrix(quadrant, size / 2);

        // Третий квадрант
        for (int i = size / 2; i < size; i++)
            quadrant[i - size / 2] = &target[i][0];
        quadrants[2] = new Quadtree(-1);
        quadrants[2]->fromMatrix(quadrant, size / 2);

        // Четвертый квадрант
        for (int i = size / 2; i < size; i++)
            quadrant[i - size / 2] = &target[i][size / 2];
        quadrants[3] = new Quadtree(-1);
        quadrants[3]->fromMatrix(quadrant, size / 2);

    }

}

void Quadtree::normalize() {

    for (int i = 0; i < 4; i++)
        if (quadrants[i])
            quadrants[i]->normalize();

    int infos[4];
    for (int i = 0; i < 4; i++)
        if (quadrants[i])
            infos[i] = quadrants[i]->info;
        else
            infos[i] = -1;

    int max = getIntByMaxEntries(infos, 4);
    if (this->info == -1)
        this->info = max;

    for (int i = 0; i < 4; i++)
        if (quadrants[i] && quadrants[i]->isLeaf() && quadrants[i]->info == info)
            quadrants[i] = nullptr;

}

void Quadtree::print(std::string indent, bool last)
{
    std::cout << indent << "+- " << info << std::endl;
    indent += last ? "   " : "|  ";

    for (int i = 0; i < 4; i++)
    {
        if (quadrants[i])
            quadrants[i]->print(indent, i == 3);
    }
}

int Quadtree::depth() {

    if (isLeaf())
        return 0;

    int max = 0;
    for (int i = 0; i < 4; i++) {
        if (quadrants[i] != nullptr) {
            int temp = quadrants[i]->depth() + 1;
            if (max < temp)
                max = temp;
        }
    }

    return max;

}