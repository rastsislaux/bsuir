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

        std::shared_ptr<Xml::Document> root;

        std::shared_ptr<Xml::Tag> tag;

        void parseInstruction();

        void openTag();

        void skipComments();

        void insertTag(const std::shared_ptr<Xml::Tag>& newTag);

    public:
        class Error : public std::runtime_error {
        public:
            explicit Error(const std::string &msg, const std::string& location);
        };

        explicit Parser(std::string source);

        Xml::Document parse();

        void closeTag();

        void parseContent();
    };

}


#endif //XML_TREE_PARSER_H
