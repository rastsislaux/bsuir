//
// Created by rostislove on 03.01.2022.
//

#ifndef GRAPHICS_VECTOR2D_H
#define GRAPHICS_VECTOR2D_H

#include "cmath"

class vector2d {

public:

    double x, y;

    vector2d(double value) : x(value), y(value) {}
    vector2d(double _x, double _y) : x(_x), y(_y) {}

    vector2d operator+(vector2d const& other) { return vector2d(x + other.x, y + other.y); }
    vector2d operator-(vector2d const& other) { return vector2d(x - other.x, y - other.y); }
    vector2d operator*(vector2d const& other) { return vector2d(x * other.x, y * other.y); }
    vector2d operator/(vector2d const& other) { return vector2d(x / other.x, y / other.y); }

    double length() {
        return sqrt(x*x + y*y);
    }

};


#endif //GRAPHICS_VECTOR2D_H
