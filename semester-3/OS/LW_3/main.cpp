#include <iostream>
#include <thread>
#include <vector>
#include <cmath>
#include <fstream>

#include "dinosaur.h"

#define FROM    0
#define TO      2000000

struct Point {
private:
    double _x;
    double _y;
    time_t _calcTime;

public:
    long writtenTime = 0;

    Point(double x, double y, time_t calcTime) {
        this->_x = x;
        this->_y = y;
        this->_calcTime = calcTime;
    }

    [[nodiscard]] double x() const { return _x; }

    [[nodiscard]] double y() const { return _y; }

    [[nodiscard]] time_t calcTime() const { return _calcTime; }

    Point& operator=(const Point&) = delete;
};

template <typename T>
struct ConcurrentQueue {
private:
    std::vector<std::unique_ptr<T>> rawQueue;
    std::mutex mutex;
public:
    void push(std::unique_ptr<T> ptr) {
        mutex.lock();
        rawQueue.emplace_back(std::move(ptr));
        mutex.unlock();
    }

    std::unique_ptr<Point> pop() {
        mutex.lock();
        auto ptr = std::move(rawQueue[rawQueue.size() - 1]);
        rawQueue.pop_back();
        mutex.unlock();
        return ptr;
    }

    bool empty() {
        mutex.lock();
        auto isEmpty = rawQueue.empty();
        mutex.unlock();
        return isEmpty;
    }
};

int main(int argc, char **argv) {

    ConcurrentQueue<Point> calculated, written;

    auto calcFinished = false;
    std::thread calculator([&calculated, &calcFinished]() {
        for (int i = FROM; i < TO; i++) {
            calculated.push(std::make_unique<Point>(i, log(i), time(nullptr)));
        }

        calcFinished = true;
    });

    auto writeFinished = false;
    std::thread writer([&calculated, &written, &calcFinished, &writeFinished]() {
        std::ofstream stream("output.txt");

       while (true) {
           auto isEmpty = calculated.empty();

           if (isEmpty && calcFinished) break;
           if (calculated.empty()) continue;

           auto ptr = calculated.pop();
           stream << "x: " << ptr->x() << ", y: " << ptr->y() << '\n';
           ptr->writtenTime = time(nullptr);
           written.push(std::move(ptr));
       }

       writeFinished = true;
    });

    std::thread logger([&written, &writeFinished]() {
        std::ofstream stream("log.log");

        while (true) {
            auto isEmpty = written.empty();

            if (isEmpty && writeFinished) break;
            if (isEmpty) continue;

            auto ptr = written.pop();
            stream << "x: " << ptr->x() << ", calcTime: " << ptr->calcTime()
                   << ", writtenTime: " << ptr->writtenTime << '\n';
        }
    });

    std::cout << "Wait please. While waiting, have a look at a dinosaur: " << std::endl
              << DINOSAUR << std::endl;

    calculator.join();
    std::cout << "Calculations finished." << std::endl;
    writer.join();
    std::cout << "Writing finished." << std::endl;
    logger.join();
    std::cout << "Logging finished." << std::endl;

    std::cout << "Yay! Done :)" << std::endl;

}
