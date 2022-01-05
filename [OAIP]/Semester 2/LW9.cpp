#include <iostream>
#include <cmath>
#include <set>

#define _A      -2.
#define _B      5.
#define _eps    0.001

double function(const double & param) {
    return pow(param, 3.) - 5.*pow(param, 2.) + 12.;
}

double derivative(const double & param) {
    return 3.*pow(param, 2.) - 10.*param;
}

double countXk(const double & xprev) {
    return xprev - ( function(xprev) / derivative(xprev) );
}

double findRoot(const double & near) {
    double x;
    for (x = near; fabs(function(x)) > _eps; x = countXk(x));
    return x;
}

double roundToEps(double d) {
    return round(d/_eps)*_eps;
}

int main() {

    double x = _A - 1;
    std::set<double> roots;

    for (double i = _A; i <= _B; i += _eps)
        roots.emplace(roundToEps(findRoot(i)));

    std::cout << "R =  { ";
    for (auto d : roots) {
        std::cout << d << " ";
    }
    std::cout << "}, где R - множество корней уравнения." << std::endl;

    return 0;
}
