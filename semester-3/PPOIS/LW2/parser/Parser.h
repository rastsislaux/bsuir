//
// Created by rostislav on 9/20/22.
//

#ifndef XML_TREE_PARSER_H
#define XML_TREE_PARSER_H

#include "../lexer/Lexer.h"
#include "../model/Document.h"

namespace XmlParser {

    class Parser {

        XmlLexer::Lexer lexer;

        std::map<std::string, std::string> parseInstruction();

        void skipComments();

    public:
        class Error : public std::runtime_error {
        public:
            explicit Error(const std::string &msg, const std::string& location);
        };

        explicit Parser(std::string source);

        Xml::Document parse();

    };

}


#endif //XML_TREE_PARSER_H
