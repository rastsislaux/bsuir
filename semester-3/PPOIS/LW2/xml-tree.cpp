#include "lexer/Lexer.h"

#include <iostream>

int main(int argc, char** argv) {
    auto lexer = XmlLexer::Lexer("word");
    while (lexer.hasNext()) {
        std::cout << lexer.next().str() << std::endl;
    }
}