//
// Created by rostislav on 9/21/22.
//

#ifndef XML_TREE_REQUEST_H
#define XML_TREE_REQUEST_H

#include <string>
#include <utility>
#include <vector>

#include "../lexer/Lexer.h"
#include "../model/Tag.h"

namespace XmlPath {

    struct Filter {
        enum class By {
            ATTR,
            TEXT
        };

        enum class Operator {
            AND,
            OR,
            NO,
        };

        By by;
        Operator oper;
        bool negate;

        std::string attrKey;
        std::string value;

        [[nodiscard]] bool match(const std::shared_ptr<Xml::Tag>& tag) const;;

        [[nodiscard]] std::vector<std::shared_ptr<Xml::Tag>> filter(std::vector<std::shared_ptr<Xml::Tag>> vect) const;

    };

    class Request {
    public:
        std::vector<std::string> path;

        std::vector<Filter> filters;

        void parsePath(XmlLexer::Lexer& lexer);

    public:
        explicit Request(std::string requestString);

        void parseFilters(XmlLexer::Lexer &lexer);
    };

}


#endif //XML_TREE_REQUEST_H
