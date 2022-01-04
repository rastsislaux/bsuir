#ifndef GRAPHICS_ANIMATION2D_H
#define GRAPHICS_ANIMATION2D_H

#include "vector2d.h"
#include "cstdio"

class Animation2D {

protected:

    int width, height;
    double aspect;

    virtual double countX(int i) final { return ((double)i / width * 2. - 1.) * aspect; }

    virtual double countY(int j) final { return -((double)j / height * 2. - 1.); }

public:

    constexpr static char asciis[] = "@%MkdmOCUcujt){?_>!:^ ";
    constexpr static int asciisLength = 24;

    virtual char formula(double x, double y, int t) {
        return ' ';
    }

    Animation2D(int width, int height) {
        this->width = width;
        this->height = height;
        aspect = (double)width/height*0.5;
    }

    virtual char * generateFrame(int t) {

        char * frame = new char[width * height + 1];
        frame[width * height] = '\0';

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                vector2d uv = vector2d(i, j) / vector2d(width, height) * 2. - 1.;
                uv.x *= aspect;

                char pixel = formula(uv.x, uv.y, t);
                frame[i + j * width] = pixel;

            }
        }

        return frame;

    }

};


#endif //GRAPHICS_ANIMATION2D_H
