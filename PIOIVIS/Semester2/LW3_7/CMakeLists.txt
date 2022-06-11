cmake_minimum_required(VERSION 3.22)
project(wannclang)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_COMPILER /bin/clang++)

add_executable(wannclang main.cpp setlib.cpp setlib.h)
