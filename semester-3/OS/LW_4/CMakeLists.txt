cmake_minimum_required(VERSION 3.24)
project(OS_LW_4)

set(CMAKE_CXX_STANDARD 20)

add_executable(OS_LW_4 main.cpp Memory.h)

enable_testing()
add_subdirectory(googletest)
add_executable(test_app tests.cpp Memory.h)
target_link_libraries(test_app gtest_main)
