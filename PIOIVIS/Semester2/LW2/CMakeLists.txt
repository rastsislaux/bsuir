cmake_minimum_required(VERSION 3.21)
project(LW2)

set(CMAKE_CXX_STANDARD 20)

add_executable(LW2 main.cpp Graph.cpp Graph.h)

include_directories(/home/rostislove/etc/boost_1_78_0/)