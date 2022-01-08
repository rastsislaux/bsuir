//
// Created by rostislove on 07.01.2022.
//

#ifndef QUADTREE_QUADTREE_H
#define QUADTREE_QUADTREE_H

#include "string"

struct Quadtree {

    int info;

    Quadtree ** quadrants;

    Quadtree(int info);

    int depth();

    bool isLeaf();

    void toMatrix(int ** target, int size);

    void fromMatrix(int ** target, int size);

    void normalize();

    void print(std::string indent, bool last);

};


#endif //QUADTREE_QUADTREE_H
