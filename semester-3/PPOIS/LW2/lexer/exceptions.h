//
// Created by rostislav on 9/20/22.
//

#ifndef XML_TREE_EXCEPTIONS_H
#define XML_TREE_EXCEPTIONS_H

#include <string>
#include <exception>
#include <stdexcept>

namespace XmlLexer {
    class UnreachableError : std::runtime_error {
    public:
        explicit UnreachableError() : std::runtime_error("This place in code must be unreachable.") { }
    };

    class NotImplemented : std::runtime_error {
    public:
        explicit NotImplemented(std::string file, int line)
        : std::runtime_error("This functionality is not yet implemented: file: " + file + ", line: " + std::to_string(line)) { }
    };
}

#endif //XML_TREE_EXCEPTIONS_H
