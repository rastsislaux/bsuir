cmake_minimum_required(VERSION 3.22)
project(ltrx)

set(CMAKE_CXX_STANDARD 20)

add_executable(ltrx main.cpp ltrx.h)

enable_testing()
add_subdirectory(googletest)
add_executable(test_app tests.cpp)
target_link_libraries(test_app gtest_main)
