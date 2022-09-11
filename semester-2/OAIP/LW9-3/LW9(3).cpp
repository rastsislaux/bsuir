/* ln(x) - 5cos(x)
 * interval: 1 - 8
 * answer: ~1.5, ~5, ~7.5
 * MS */

#include <iostream>
#include <cmath>

#define _eps    0.0001
#define _H      0.0001

double function(const double & param) {
    return log(param) - 5 * cos(param);
}

double findRoot(const double & near) {

    if (fabs(function(near)) < _eps)
        return near;

    return findRoot(
            near - ( function(near) * _H / ( function(near) - function(near - _H) ) )
    );

}

double roundToEps(double d) {
    std::cout << round(d/_eps)*_eps << std::endl;
    return round(d/_eps)*_eps;
}

int main() {

    double x0;
    std::cout << "enter x0 (1, 5, 7): ";
    std::cin >> x0;

    std::cout << "Square root: " << roundToEps(findRoot(x0)) << std::endl;

    return 0;

}
