cmake_minimum_required(VERSION 3.22)
project(xml-tree)

set(CMAKE_CXX_STANDARD 20)

add_executable(xml-tree xml-tree.cpp lexer/Lexer.h lexer/Lexer.cpp lexer/Token.cpp lexer/Token.h lexer/consts.h lexer/exceptions.h parser/Parser.cpp parser/Parser.h model/Document.cpp model/Document.h model/Tag.cpp model/Tag.h)