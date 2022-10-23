cmake_minimum_required(VERSION 3.23)
project(PPOIS3)

set(CMAKE_CXX_STANDARD 20)

add_executable(PPOIS3 nstdlib.h tests.cpp)

enable_testing()
add_subdirectory(googletest)
add_executable(test_app tests.cpp)
target_link_libraries(test_app gtest_main)