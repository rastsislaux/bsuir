//
// Created by rostislav on 9/21/22.
//

#include "Request.h"

void XmlPath::Request::parsePath(XmlLexer::Lexer& lexer) {
    lexer.next();
    while (lexer.current().getType() != XmlLexer::Token::Type::OP_SQUARE &&
           lexer.current().getType() != XmlLexer::Token::Type::END) {
        lexer.expect({XmlLexer::Token::Type::SLASH});
        lexer.next();
        lexer.expect({XmlLexer::Token::Type::WORD});
        path.emplace_back(lexer.current().getText());
        lexer.next();
    }
}

void XmlPath::Request::parseFilters(XmlLexer::Lexer& lexer) {
    lexer.next();
    while (lexer.current().getType() != XmlLexer::Token::Type::CL_SQUARE) {
        Filter filter{};
        if (lexer.current().getType() == XmlLexer::Token::Type::BANG) {
            filter.negate = true;
            lexer.next();
        }
        lexer.expect({XmlLexer::Token::Type::WORD});
        auto fby = lexer.current().getText();
        if (fby == "attr") filter.by = Filter::By::ATTR;
        else if (fby == "text") filter.by = Filter::By::TEXT;

        lexer.next();
        lexer.expect({XmlLexer::Token::Type::OP_PAREN});
        lexer.next();
        if (filter.by == Filter::By::ATTR) {
            lexer.expect({XmlLexer::Token::Type::STRING});
            filter.attrKey = lexer.current().getText();
            lexer.next();
            lexer.expect({XmlLexer::Token::Type::COMMA});
            lexer.next();
            lexer.expect({XmlLexer::Token::Type::STRING});
            filter.value = lexer.current().getText();
        } else {
            lexer.expect({XmlLexer::Token::Type::STRING});
            filter.value = lexer.current().getText();
        }
        lexer.next();
        lexer.expect({XmlLexer::Token::Type::CL_PAREN});
        lexer.next();
        switch (lexer.current().getType()) {
            case XmlLexer::Token::Type::DBL_AMPERSAND:
                filter.oper = Filter::Operator::AND;
                lexer.next();
                break;
            case XmlLexer::Token::Type::DBL_BAR:
                filter.oper = Filter::Operator::OR;
                lexer.next();
                break;
            default:
                filter.oper = Filter::Operator::NO;
                break;
        }
        filters.emplace_back(filter);
    }
}

XmlPath::Request::Request(std::string requestString) {
    if (requestString == "/") return;
    XmlLexer::Lexer lexer(std::move(requestString));
    parsePath(lexer);

    if (lexer.current().getType() == XmlLexer::Token::Type::OP_SQUARE) {
        parseFilters(lexer);
    }
}

bool XmlPath::Filter::match(const std::shared_ptr<Xml::Tag> &tag) const {
    switch (by) {
        case By::ATTR:
            return tag->getAttribute(attrKey) == value && !negate;
        case By::TEXT:
            return tag->getContent() == value && !negate;
    }
}

std::vector<std::shared_ptr<Xml::Tag>> XmlPath::Filter::filter(std::vector<std::shared_ptr<Xml::Tag>> vect) const {
    std::vector<std::shared_ptr<Xml::Tag>> res;
    for (auto& elem : vect) {
        if (match(elem)) res.emplace_back(elem);
    }
    return res;
}
