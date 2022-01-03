//
// Created by rostislove on 03.01.2022.
//

#ifndef GRAPHICS_ANIMATIONS2D_H
#define GRAPHICS_ANIMATIONS2D_H

#include "Animation2D.h"
#include "cmath"

class SinusAnimation : public Animation2D {

public:

    char formula(double x, double y, int t) override {
        return fabs(sin(x + t / 1000.) - y) < 0.03 ? '@' : ' ';
    }

    SinusAnimation(int width, int height) : Animation2D(width, height) {}

};

class CosinusAnimation : public Animation2D {

public:

    char formula(double x, double y, int t) override {
        return fabs(cos(x + t / 1000.) - y) < 0.03 ? '@' : ' ';
    }

    CosinusAnimation(int width, int height) : Animation2D(width, height) {}

};

class CosinusAndSinusAnimation : public Animation2D {

public:

    char formula(double x, double y, int t) override {
        return fabs(cos(x + t / 1000.) - y) < 0.03 || fabs(sin(x + t / 1000.) - y) < 0.03 ? '@' : ' ';
    }

    CosinusAndSinusAnimation(int width, int height) : Animation2D(width, height) {}

};

class PumpingCircleAnimation : public Animation2D {

public:

    char formula(double x, double y, int t) override {
        return x*x + y*y < sin(t/1000.) ? '@' : ' ';
    }

    PumpingCircleAnimation(int width, int height) : Animation2D(width, height) {}

};

class PumpingGradientAnimation : public Animation2D {

public:
    char formula(double x, double y, int t) override {

        int index = (x*x + y*y / sin(t/1000.)) * (asciisLength - 1);
        if (index > asciisLength - 1 || index < 0) return ' ';
        return asciis[index];

    }

    PumpingGradientAnimation(int width, int height) : Animation2D(width, height) {}

};

class FallingBallAnimation : public Animation2D {

public:

    constexpr static double acc = 0.0000003;

    double countDistance(double x0, double y0, double x, double y) {
        return sqrt(pow(x0-x, 2) + pow(y0-y, 2));
    }

    char formula(double x, double y, int t) override {

        double xc = -1 + 0.0005*t,
               yc = -1 + (acc * pow(t, 2) / 2);

        if (countDistance(x, y, xc, yc) < 0.2) return '0';
        else return ' ';

    }

    FallingBallAnimation(int width, int height) : Animation2D(width, height) {}

};

class SpinningLine : public Animation2D {
public:

    char formula(double x, double y, int t) override {
        return fabs(y - tan(t/1000.) * x) < 0.03 ? '$' : ' ';
    }

    SpinningLine(int width, int height) : Animation2D(width, height) {}

};


#endif //GRAPHICS_ANIMATIONS2D_H
